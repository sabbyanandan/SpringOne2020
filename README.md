# Getting Started with Spring Cloud Data Flow
A Spring Cloud Data Flow workshop on Kubernetes. 

1. The workshop is primarily geared towards the students who attend the [SCDF workshop at SpringOne 2020](https://springone.io/2020/workshops/spring-cloud-data-flow).
2. Students will use the [Strigo](https://strigo.io/) platform to learn how to prepare the environment and exercise SCDF's feature capabilities on Kubernetes.
3. Instructions: [![HackMD documents](https://hackmd.io/badge.svg)](https://hackmd.io/@sabbyanandan/B1bDf74fv)

> Tip: It is possible to follow the instructions to repeat this workshop in an EC2 instance (image: `ami-03e97315b2269f290`;
> region: `us-west-2`).

> Tip: Alternatively, you can repeat using a Minikube/Kind cluster in your computer or against an external Kubernetes cluster.

## Applications
1. [`trucks`](https://github.com/sabbyanandan/SpringOne2020/tree/master/thumbinator) — generates trucks in random interval
2. [`brake-temperture`](https://github.com/sabbyanandan/SpringOne2020/tree/master/brake-temperature) — computes moving average of truck's brake temperature in 10s interval
3. [`brake-logs`](https://github.com/sabbyanandan/SpringOne2020/tree/master/brake-logs) — prints the truck data
4. [`thumbinator`](https://github.com/sabbyanandan/SpringOne2020/tree/master/thumbinator) — a task/batch-job that can create thumbnails from images

## Presentation
[Getting Started with Spring Cloud Data Flow](https://speakerdeck.com/sabbyanandan/getting-started-with-spring-cloud-data-flow)
