FROM ubuntu:16.04

RUN apt-get update \
	&& apt-get install -y wget libkrb5-3 libsqlite3-0 libcurl3

RUN wget static.sing-group.org/software/SEDA/dev_resources/pro-ncbi.tar.gz -O /tmp/pro-ncbi.tar.gz \
	&& mkdir /opt/pro-ncbi \
	&& tar zxvf /tmp/pro-ncbi.tar.gz -C /opt/pro-ncbi && rm /tmp/pro-ncbi.tar.gz

RUN ln -s -T /lib/x86_64-linux-gnu/libpcre.so.3 /lib/x86_64-linux-gnu/libpcre.so.0 \
	&& ln -s /usr/lib/x86_64-linux-gnu/libdb-5.3.so /usr/lib/libdb-4.6.so

ENV PATH="/opt/pro-ncbi:${PATH}"

RUN apt-get remove -y wget
