Installation and configuration
******************************
SEDA installers (available at https://www.sing-group.org/seda/download.html) are self-contained and do not require the installation or configuration of additional dependencies to run the SEDA core functionalities. Nevertheless, some operations (e.g. Blast or Clustal Omega alignment) require users to: (i) provide the software binaries so that SEDA can invoke them at runtime, (ii) a Docker installation so that SEDA can run the software dependencies using Docker images.

Docker
=======

Linux
-------

Follow the official Docker CE installation instructions for your distribution:
- CentOS (https://docs.docker.com/install/linux/docker-ce/centos/).
- Debian (https://docs.docker.com/install/linux/docker-ce/debian/).
- Fedora (https://docs.docker.com/install/linux/docker-ce/fedora/).
- Ubuntu (https://docs.docker.com/install/linux/docker-ce/ubuntu/).

It is recommended to follow the post-installation steps (https://docs.docker.com/install/linux/linux-postinstall/) in order to manage Docker as non-root user. Otherwise, SEDA should be executed as superuser in order to be able to run Docker.

Windows
-------

Follow the official Docker for Windows installation instructions (https://docs.docker.com/docker-for-windows/). Please, note that SEDA requires Docker for Windows, Docker Toolbox is not supported.

Regarding the Docker for Windows configuration, access must be explicitely granted to the drive where the user temporary folder is located (usually *C*), as the following image shows.

.. figure:: images/installation/windows/1.png
   :align: center

Mac OS
------

Follow the official Docker for Mac installation instructions (https://docs.docker.com/docker-for-mac/). Please, note that SEDA requires Docker for Mac, Docker Toolbox is not supported.
