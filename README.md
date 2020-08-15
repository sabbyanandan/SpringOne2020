---
title: 'Getting Started with Spring Cloud Data Flow'
disqus: hackmd
---

Getting Started with Spring Cloud Data Flow
===

[TOC]

## What is this?

A step-by-step guide and walk through to get started with Spring Cloud Data Flow (SCDF) on Kubernetes.

1. The guide is created for students who attend the [SCDF workshop at SpringOne 2020](https://springone.io/2020/workshops/spring-cloud-data-flow).
2. Students will learn how to prepare the environment to run SCDF on Kubernetes in the Strigo platform. Alternatively, you can follow the instructions to repeat this in your laptop or in an EC2 instance.

## Prerequisite

1. [Strigo](https://strigo.io/) account 
2. Registered for the SCDF workshop with an access code

> Ignore this if you're following this guide to repeat it in your laptop.

### Machine Requirements

We will be using Minikube as the Kubernetes environment with `--vm-driver=docker` as the driver and at least 4CPUs and 10GB RAM is required to run the lab in entireity.

```
minikube start --vm-driver=docker --kubernetes-version v1.17.0 --memory=10240 --cpus=4
```

> If you are repeating it in EC2, you will have to launch the AMI with `t2.xlarge`  instance type.

> Kubernetes 1.17.x is used in the lab but you can pick a version that is [compatible](https://dataflow.spring.io/docs/installation/kubernetes/compatibility/) with SCDF.

### Spring Cloud Data Flow

To demonstrate stream processing, batch processing, analytics and the observability capabilities in SCDF, the following components will be provisioned in Kubernetes.

1. Spring Cloud Data Flow
2. Spring Cloud Skipper
3. Prometheus
4. Grafana
5. Apache Kafka
6. MariaDB 

To provision all this in a single command, the lab relies on the Spring Cloud Data Flow's Bitnami chart. Students will have to run the `sh scripts/deploy-scdf.sh` script that is available in the repo.

### Tools / Programs

The AMI is prepared with the following tools.

1. Java
2. Maven
3. Docker
4. Minikube
5. K9s
6. Helm
7. kubectl
8. VS Code

> Once when you login to the VM, test the environment by running the `sh scripts/test-env.sh` script.

>[ec2-user@ip SpringOne2020]$ sh scripts/test-env.sh 
docker is ready
minikube is ready
helm is ready
kubectl is ready
k9s is ready
java is ready
mvn is ready

What will be covered in the workshop?
---
```sequence
Strigo->Lab: Start the Lab in Strigo

Note left of Prepare: You'll do this once
Lab->Prepare: Start minikube
Lab->Prepare: Deploy SCDF stack
Lab->Prepare: Pull latest code from Git and build the applications
Lab->Prepare: Generate docker images

Prepare-->Lab: Stuck? Cleanup and repeat

Prepare->Streaming Lab: Build an IoT streaming use-case
Prepare->Streaming Lab: Deploy streaming use-case from SCDF to K8s; verify results
Prepare->Streaming Lab: Monitor and observe performance using Prometheus & Grafana

Streaming Lab-->Prepare: Stuck? Cleanup and repeat; ask questions

Prepare->Batch Lab: Build and design a batch-job use-case
Prepare->Batch Lab: Launch the batch-job from SCDF to K8s; verify results
Prepare->Batch Lab: Schedule the batch-job in SCDF to K8s; verify results
Prepare->Batch Lab: Monitor and observe performance using Prometheus & Grafana

Batch Lab-->Prepare: Stuck? Cleanup and repeat; ask questions

```

## Appendix and FAQ

:::info
Spring Cloud Data Flow for Kubernetes
Spring Cloud Data Flow Documentation
Spring Cloud Data Flow Samples
:::

###### tags: `event streaming` `batch processing` `stateful streams` `predictive analytics` `cloud-native` `microservices` 
