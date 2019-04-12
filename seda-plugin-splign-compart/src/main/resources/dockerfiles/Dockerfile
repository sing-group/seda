FROM ubuntu:16.04

RUN apt-get update \
	&& apt-get install -y wget libkrb5-3 libsqlite3-0 libcurl3

RUN wget static.sing-group.org/software/SEDA/dev_resources/splign-compart.tar.gz -O /tmp/splign-compart.tar.gz \
	&& mkdir /opt/splign-compart \
	&& tar zxvf /tmp/splign-compart.tar.gz -C /opt/splign-compart && rm /tmp/splign-compart.tar.gz

RUN ln -s -T /lib/x86_64-linux-gnu/libpcre.so.3 /lib/x86_64-linux-gnu/libpcre.so.0

ENV PATH="/opt/splign-compart:${PATH}"

RUN apt-get remove -y wget
