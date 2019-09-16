FROM ubuntu:18.04

RUN apt-get update && apt-get install -y build-essential unzip wget && \
	apt-get install -y software-properties-common && \
	apt-get install libboost-iostreams1.62.0

RUN apt-get update && \
	apt-get install -y openjdk-8-jre && \
	rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

RUN wget static.sing-group.org/software/SEDA/dev_resources/sapp.tar.gz -O /tmp/sapp.tar.gz \
	&& mkdir /opt/sapp \
	&& tar zxvf /tmp/sapp.tar.gz -C /opt/sapp && rm /tmp/sapp.tar.gz
