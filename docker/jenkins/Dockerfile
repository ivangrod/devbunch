#this is the base image we use to create our image from
FROM jenkins/jenkins

MAINTAINER David Romero (davromalc@gmail.com)
MAINTAINER Iván Gutiérrez (ivangutrod@gmail.com)

USER root
RUN apt-get update \
      && apt-get install -y sudo \
      && apt-get install -y libltdl7 \
      && rm -rf /var/lib/apt/lists/*
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers

COPY jdk-8u171-linux-x64.tar.gz /var/jenkins_home/downloads/
COPY apache-maven-3.5.0-bin.tar.gz /var/jenkins_home/downloads/

COPY plugins.txt /usr/share/jenkins/plugins.txt
#plugins.sh is deprecated
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/plugins.txt

# copy custom built plugins
COPY plugins/*.hpi /usr/share/jenkins/ref/plugins/

# so we can use jenkins cli
COPY config/jenkins.properties /usr/share/jenkins/ref/

# remove executors in master
COPY config/*.groovy /usr/share/jenkins/ref/init.groovy.d/

# lets configure Jenkins with some defaults
COPY config/*.xml /usr/share/jenkins/ref/


