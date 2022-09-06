FROM ubuntu:18.04

RUN apt-get update && \
	apt-get install -y libfontconfig1 libxrender1 libxtst6 libxi6 libgconf-2-4 wget && \
	rm -rf /var/lib/apt/lists/*

# Install Docker
RUN apt-get update && apt-get install -y \
	apt-transport-https ca-certificates curl gnupg-agent software-properties-common && \
	curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - && \
	apt-key fingerprint 0EBFCD88 && \
	add-apt-repository \
		"deb [arch=amd64] https://download.docker.com/linux/ubuntu \
		$(lsb_release -cs) \
		stable" && \
	apt-get update && apt-get install -y docker-ce docker-ce-cli containerd.io

RUN wget https://www.sing-group.org/seda/downloads/releases/seda-linux-64b-1.6.0.tar.gz -O /tmp/SEDA.tar.gz && \
	mkdir /opt/SEDA && tar zxvf /tmp/SEDA.tar.gz -C /opt/SEDA && rm /tmp/SEDA.tar.gz

RUN sed -i -e "s#DIR=#if [ -d \"/data\" ]; then D_ARGS='-Dseda.output.directory=/data -Dseda.input.directory=/data'; else D_ARGS='-Dseda.output.directory=/ -Dseda.input.directory=/'; fi\n\nDIR=#g" /opt/SEDA/run-gui.sh

RUN sed -i -e "s#java\"#java\" \$D_ARGS -Dseda.local.execution.enabled=false#g" /opt/SEDA/run-gui.sh && \
	sed -i -e "s#java\"#java\" \$D_ARGS -Dseda.local.execution.enabled=false#g" /opt/SEDA/run-cli.sh && \
	chmod u+x /opt/SEDA/run-*.sh

CMD /opt/SEDA/run-gui.sh
