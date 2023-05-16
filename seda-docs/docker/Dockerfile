FROM 3.6.9-slim

RUN apt-get update \
 && apt-get install --no-install-recommends -y graphviz imagemagick make \
 && apt-get autoremove \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

RUN python3 -m pip install --no-cache-dir Jinja2==2.3 setuptools==40.2.0 Sphinx==1.7.0 sphinx-rtd-theme==0.4.1
