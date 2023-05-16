# SEDA docs

Documentation is written using [reStructuredText](http://docutils.sourceforge.net/rst.html). In the following links you can find information about the syntax:
- http://docutils.sourceforge.net/docs/user/rst/quickref.html
- http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html

To build the documentation run:

    make html

This makefile uses a Docker image (`singgroup/sphinx:1.7.8`) that can be built using the files in the `docker` directory or pull from Docker Hub. This way it is guaranteed that stable versions of `sphinx` and the `sphinx_rtd_theme` modules are used.

Documentation will be generated in `./build/html` directory.

## Requirements

You need `sphinx` installed to generate the documentation:

	sudo apt install python-sphinx

You also need to install the ReadTheDocs theme

	pip install sphinx_rtd_theme

