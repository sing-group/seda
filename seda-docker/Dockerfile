FROM ubuntu:18.04

RUN apt-get update && \
	apt-get install -y libfontconfig1 libxrender1 libxtst6 libxi6 libgconf-2-4 wget make g++ libargtable2-dev && \
	rm -rf /var/lib/apt/lists/*

RUN wget http://www.clustal.org/omega/clustal-omega-1.2.4.tar.gz -O /tmp/clustalomega.tar.gz \
	&& tar zxvf /tmp/clustalomega.tar.gz -C /opt/ && rm /tmp/clustalomega.tar.gz \
	&& cd /opt/clustal-omega-1.2.4/ \
	&& ./configure && make && make install

RUN wget ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/2.2.30/ncbi-blast-2.2.30+-x64-linux.tar.gz -O /tmp/blast.tar.gz && \
	mkdir /opt/blast && tar zxvf /tmp/blast.tar.gz -C /opt/blast && rm /tmp/blast.tar.gz

ENV PATH /opt/blast/ncbi-blast-2.2.30+/bin:$PATH

RUN wget https://www.sing-group.org/seda/downloads/releases/seda-linux-64b-1.0.0.tar.gz -O /tmp/SEDA.tar.gz && \
	mkdir /opt/SEDA && tar zxvf /tmp/SEDA.tar.gz -C /opt/SEDA && rm /tmp/SEDA.tar.gz

RUN sed -i -e "s#DIR=#if [ -d \"/data\" ]; then D_ARGS='-Dseda.output.directory=/data -Dseda.input.directory=/data'; else D_ARGS='-Dseda.output.directory=/ -Dseda.input.directory=/'; fi\n\nDIR=#g" /opt/SEDA/run.sh
RUN sed -i -e "s#java\"#java\" \$D_ARGS -Dseda.docker.execution.enabled=false#g" /opt/SEDA/run.sh

CMD /opt/SEDA/run.sh
