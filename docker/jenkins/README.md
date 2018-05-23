# Jenkins

Jenkins docker image

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java8 - [jdk-8u171-linux-x64.tar.gz](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- Maven 3.5.0 - [apache-maven-3.5.0-bin.tar.gz](https://archive.apache.org/dist/maven/maven-3/3.5.0/binaries/)

You have to install Docker and Docker Compose:

- [Docker](https://docs.docker.com/installation/#installation)
- [Docker Compose](https://docs.docker.com/compose/install)

### Installing

* Clone source code from git

```
$ git clone git@github.com:ivangrod/devbunch.git
```

* Build the image with Docker

```
$ cd docker/jenkins
$ docker build -t myUsername/jenkins .
```

* Run the application from the command line using:

```
$  docker run  \
	 -v /var/run/docker.sock:/var/run/docker.sock \
     -v $(which docker):/usr/bin/docker:ro \
     -v /lib64/libdevmapper.so.1.02:/usr/lib/x86_64-linux-gnu/libdevmapper.so.1.02 \
     -v /lib64/libudev.so.0:/usr/lib/x86_64-linux-gnu/libudev.so.0 \
     -e JENKINS_USER=jenkinsAdminUser \
     -e JENKINS_PASS=$EcR€tP@ssw0rd \
     -p 8080:8080 \
     --name jenkins \
     --privileged=true -t -i \
	myUsername/jenkins
```
