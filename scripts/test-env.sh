#!/bin/bash

# Test if docker exist
if which docker >/dev/null; then
    echo "docker is ready"
else
    echo "docker is not ready"
fi

# Test if minikube exist
if which minikube >/dev/null; then
    echo "minikube is ready"
else
    echo "minikube is not ready"
fi

# Test if helm exist
if which helm >/dev/null; then
    echo "helm is ready"
else
    echo "helm is not ready"
fi

# Test if kubectl exist
if which kubectl >/dev/null; then
    echo "kubectl is ready"
else
    echo "kubectl is not ready"
fi

# Test if k9s exist
if which k9s >/dev/null; then
    echo "k9s is ready"
else
    echo "k9s is not ready"
fi

# Test if java exist
if which java >/dev/null; then
    echo "java is ready"
else
    echo "java is not ready"
fi

# Test if mvn exist
if which mvn >/dev/null; then
    echo "mvn is ready"
else
    echo "mvn is not ready"
fi


