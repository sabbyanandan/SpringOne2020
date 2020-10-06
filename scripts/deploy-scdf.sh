#!/bin/bash

## Add Bitnami Repo and update it
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

## Installing and configuring prometheus
prometheus_namespace="monitoring"
prometheus_release_name="prom"
prometheus_host="prometheus-operated"
prometheus_port="9090"

if ! kubectl get namespace "${prometheus_namespace}" >/dev/null; then
  echo "A namespace called ${prometheus_namespace} for prometheus should exist, creating it"
  kubectl create namespace "${prometheus_namespace}" >/dev/null
fi
echo "A namespace called ${prometheus_namespace} exists in the cluster"

if ! helm status -n "${prometheus_namespace}" "${prometheus_release_name}" >/dev/null; then
  echo "Install bitnami/prometheus-operator prometheus_release_name=${prometheus_release_name} prometheus_namespace=${prometheus_namespace}"

  helm upgrade --wait -n "${prometheus_namespace}" --install "${prometheus_release_name}" \
    bitnami/prometheus-operator >/dev/null
fi
echo "A release of bitnami/prometheus-operator, ${prometheus_release_name}, is running on ${prometheus_namespace} namespace"

## Installing and configuring grafana
grafana_namespace="monitoring"
grafana_release_name="graf"

if ! kubectl get namespace "${grafana_namespace}" >/dev/null; then
  echo "A namespace called ${grafana_namespace} for grafana should exist, creating it"
  kubectl create namespace "${grafana_namespace}" >/dev/null
fi
echo "A namespace called ${grafana_namespace} exists in the cluster"

download_dasboard() {
  local -r dashboard=${1:?dashboard url is required}
  local -r destination=${2:?destination path is required}

  echo "Downloading dashboard form: ${dashboard}"
  if ! curl -sSL -o "${destination}" "${dashboard}"; then
    echo "dashboard does not exist"
  fi
}

if ! helm status -n "${grafana_namespace}" "${grafana_release_name}" >/dev/null; then
  echo "Install bitnami/grafana grafana_release_name=${grafana_release_name} grafana_namespace=${grafana_namespace}"

  grafana_tmp_folder="/tmp/graf.$(date +%s)"
  mkdir -p "${grafana_tmp_folder}"

  grafana_dashboard_root_url="https://raw.githubusercontent.com/spring-cloud/spring-cloud-dataflow/master/src/grafana/prometheus/docker/grafana/dashboards"

  for dashboard in "scdf-applications" "scdf-streams" "scdf-task-batch"; do
    ! download_dasboard "${grafana_dashboard_root_url}/${dashboard}.json" "${grafana_tmp_folder}/${dashboard}.json" exit 1

    kubectl -n "${grafana_namespace}" delete configmap "grafana-dashboards-${dashboard}" || true &&
      kubectl -n "${grafana_namespace}" create configmap "grafana-dashboards-${dashboard}" --from-file=${dashboard}.json=${grafana_tmp_folder}/${dashboard}.json
  done

  cat >"${grafana_tmp_folder}/datasources.yaml" <<EOF
apiVersion: 1
datasources:
- name: ScdfPrometheus
  type: prometheus
  access: proxy
  org_id: 1
  url: http://${prometheus_host}:${prometheus_port}
  is_default: true
  version: 5
  editable: true
  read_only: false
EOF

  kubectl -n "${grafana_namespace}" delete secret "grafana-datasources" || true &&
    kubectl -n "${grafana_namespace}" create secret generic "grafana-datasources" --from-file=datasources.yaml=${grafana_tmp_folder}/datasources.yaml

  helm upgrade --wait -n "${grafana_namespace}" --install "${grafana_release_name}" \
    bitnami/grafana \
    --set dashboardsProvider.enabled=true \
    --set dashboardsConfigMaps[0].configMapName=grafana-dashboards-scdf-applications \
    --set dashboardsConfigMaps[0].fileName=scdf-applications.json \
    --set dashboardsConfigMaps[1].configMapName=grafana-dashboards-scdf-streams \
    --set dashboardsConfigMaps[1].fileName=scdf-streams.json \
    --set dashboardsConfigMaps[2].configMapName=grafana-dashboards-scdf-task-batch \
    --set dashboardsConfigMaps[2].fileName=scdf-task-batch.json \
    --set datasources.secretName=grafana-datasources >/dev/null
fi
echo "A release of bitnami/prometheus-operator, ${grafana_release_name}, is running on ${grafana_namespace} namespace"

## Installing and configuring spring-cloud-dataflow
scdf_namespace="default"
scdf_release_name="scdf"

if ! helm status "${scdf_release_name}" >/dev/null; then
  echo "Install bitnami/spring-cloud-dataflow scdf_release_name=${scdf_release_name} scdf_namespace=${scdf_namespace}"

  helm upgrade --wait -n "${scdf_namespace}" --install "${scdf_release_name}" bitnami/spring-cloud-dataflow \
    --set kafka.enabled=true \
    --set rabbitmq.enabled=false \
    --set metrics.enabled=true \
    --set metrics.serviceMonitor.enabled=true \
    --set metrics.serviceMonitor.namespace="${prometheus_namespace}" \
    --set server.configuration.grafanaInfo="http://localhost:3000" >/dev/null
fi
echo "A release of bitnami/spring-cloud-dataflow, ${scdf_release_name}, is running on ${scdf_namespace} namespace"

MARIADB_ROOT_PASSWORD=$(kubectl get secret --namespace "${scdf_namespace}" "${scdf_release_name}-mariadb" -o jsonpath="{.data.mariadb-root-password}" | base64 --decode)

helm upgrade --wait -n "${scdf_namespace}" --install "${scdf_release_name}" bitnami/spring-cloud-dataflow \
  --set mariadb.rootUser.password=$MARIADB_ROOT_PASSWORD \
  --set kafka.enabled=true \
  --set rabbitmq.enabled=false \
  --set metrics.enabled=true \
  --set metrics.serviceMonitor.enabled=true \
  --set metrics.serviceMonitor.namespace="${prometheus_namespace}" \
  --set server.configuration.grafanaInfo="http://localhost:3000" >/dev/null

echo ""
echo "### Stack succesfully deployed ###"
echo ""
echo "Connect to Data Flow"
echo "    $ helm status ${scdf_release_name}"

echo "Grafana password"
echo "    $ kubectl -n ${grafana_namespace} get secret ${grafana_release_name}-grafana-admin -o jsonpath={.data.GF_SECURITY_ADMIN_PASSWORD} | base64 --decode"

echo "Forward grafana"
echo "    $ kubectl port-forward -n ${grafana_namespace} svc/${grafana_release_name}-grafana 3000:3000"
