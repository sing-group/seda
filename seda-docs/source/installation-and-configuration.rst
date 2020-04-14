Installation and configuration
******************************

SEDA installers (available at https://www.sing-group.org/seda/download.html) are self-contained and do not require the installation or configuration of additional dependencies to run the SEDA core functionalities.

Nevertheless, some operations (e.g. BLAST or Clustal Omega alignment) make use of external software dependencies and require users: (i) to have a Docker installation so that SEDA can run the software dependencies using Docker images, or (ii) to provide the location of the software binaries so that SEDA can invoke them at runtime.

We recommend using Docker since it can be installed on the main operating systems easily and allows to run these dependencies using the official images provided and maintained by us, avoiding you to have them installed and configured locally (sometimes that is not always possible since some of them are not available for Windows or Mac OS).

SEDA Installation
=================

Linux
-----

Portable version
^^^^^^^^^^^^^^^^

Download and uncompress the 64 bit distribution for Linux (*seda-linux-64b-X.Y.Z.tar.gz*). The following files will appear.

.. figure:: images/installation/linux/1.png
   :align: center

Now, make double click on the *run.sh* file (or execute ```./run.sh```) in order to run SEDA.

See the :ref:`Linux Docker<linux_docker>` section to see how to install Docker in Linux and be able to use the operations requiring external software dependencies.

.. Note::
   The first time an operation requiring external software dependencies is opened and Docker is available, the waiting cursor appears and it may seem that the GUI is frozen. This is normal because SEDA should download the Docker image from the Docker Hub registry and this may take a while depending on the speed of your internet connection.

Dockerized version
^^^^^^^^^^^^^^^^^^

A dockerized version of SEDA for Linux systems is available at https://hub.docker.com/r/pegi3s/seda/. Using this Docker image, SEDA can be run with the following commands:

.. code-block:: console

 xhost +
 docker run --rm -ti -e USERID=$UID -e USER=$USER -e DISPLAY=$DISPLAY -v /var/db:/var/db:Z -v /tmp/.X11-unix:/tmp/.X11-unix -v $HOME/.Xauthority:/home/developer/.Xauthority -v "/your/data/dir:/data" -v /var/run/docker.sock:/var/run/docker.sock -v /tmp:/tmp pegi3s/seda

Where ```/your/data/dir``` must be replaced with the data directory that SEDA will have access to and that will be available as ```/data```.

Windows
-------

Portable version
^^^^^^^^^^^^^^^^

Download and uncompress the 64 bit distribution for Windows (*seda-windows-64b-X.Y.Z.zip*). The following files will appear.

.. figure:: images/installation/windows/portable/1.png
   :align: center

Now, make double click on the *run.bat* file in order to run SEDA. In some cases, a message like the one below may appear. If so, just click on *More info* and then you will be able to click the *Run anyway* button to continue opening SEDA.

.. figure:: images/installation/windows/portable/2.png
   :align: center

See the :ref:`Windows Docker<windows_docker>` section to see how to install Docker for Windows and be able to use the operations requiring external software dependencies.

.. Note::
   The first time an operation requiring external software dependencies is opened and Docker is available, the waiting cursor appears and it may seem that the GUI is frozen. This is normal because SEDA should download the Docker image from the Docker Hub registry and this may take a while depending on the speed of your internet connection.

Executable installer
^^^^^^^^^^^^^^^^^^^^

First, make double click on the *seda-windows-64b-X.Y.Z.exe* file in order to run the SEDA installer. In some cases, a message like the one below may appear. If so, just click on *More info* and then you will be able to click the *Run anyway* button to continue the SEDA installation.

.. figure:: images/installation/windows/installer/1.png
   :align: center

Then, you just only need to follow the installation wizard steps until the instalation is completed.

.. figure:: images/installation/windows/installer/2.png
   :align: center

.. figure:: images/installation/windows/installer/3.png
   :align: center

.. figure:: images/installation/windows/installer/4.png
   :align: center

.. figure:: images/installation/windows/installer/5.png
   :align: center

.. figure:: images/installation/windows/installer/6.png
   :align: center

.. figure:: images/installation/windows/installer/7.png
   :align: center

And finally, just check the *Run SEDA* option to check that the installation has succeeded.

.. figure:: images/installation/windows/installer/8.png
   :align: center

See the :ref:`Windows Docker<windows_docker>` section to see how to install Docker for Windows and be able to use the operations requiring external software dependencies.

.. Note::
   The first time an operation requiring external software dependencies is opened and Docker is available, the waiting cursor appears and it may seem that the GUI is frozen. This is normal because SEDA should download the Docker image from the Docker Hub registry and this may take a while depending on the speed of your internet connection.

Mac OS
------

Download and uncompress the Mac OS distribution (*seda-mac-X.Y.Z.zip*). The following files will appear.

.. figure:: images/installation/mac/1.png
   :align: center

Now, make double click on the *run.command* file in order to run SEDA. In some cases, the system will prompt the following dialog saying that the application can't be opened due to the security settings.

.. figure:: images/installation/mac/2.png
   :align: center

To fix this, go to *System Preferences* and find the *Security & Privacy* settings.

.. figure:: images/installation/mac/3.png
   :align: center

In the *General* tab, enable the *App Store and identified developers* option under the *Allow apps downloaded from* text.

.. figure:: images/installation/mac/4.png
   :align: center

Finally, make double click again on the *run.command* file in order to run SEDA. When prompted, click the *Open* button to open SEDA.

.. figure:: images/installation/mac/5.png
   :align: center

See the :ref:`Mac OS Docker<mac_docker>` section to see how to install Docker for Mac and be able to use the operations requiring external software dependencies.

.. Note::
   The first time an operation requiring external software dependencies is opened and Docker is available, the waiting cursor appears and it may seem that the GUI is frozen. This is normal because SEDA should download the Docker image from the Docker Hub registry and this may take a while depending on the speed of your internet connection.

Docker installation
===================

.. _linux_docker:

Linux
-------

Follow the official Docker CE installation instructions for your distribution:

- CentOS (https://docs.docker.com/install/linux/docker-ce/centos/).
- Debian (https://docs.docker.com/install/linux/docker-ce/debian/).
- Fedora (https://docs.docker.com/install/linux/docker-ce/fedora/).
- Ubuntu (https://docs.docker.com/install/linux/docker-ce/ubuntu/).

It is recommended to follow the post-installation steps (https://docs.docker.com/install/linux/linux-postinstall/) in order to manage Docker as non-root user. Otherwise, SEDA should be executed as superuser in order to be able to run Docker.

.. _windows_docker:

Windows
-------

Follow the official Docker for Windows installation instructions (https://docs.docker.com/docker-for-windows/). Please, note that SEDA requires Docker for Windows, Docker Toolbox is not supported.

Regarding the Docker for Windows configuration, access must be explicitely granted to the drive where the user temporary folder is located (usually *C*), as the following image shows.

.. figure:: images/installation/windows/docker/1.png
   :align: center

.. _mac_docker:

Mac OS
------

Follow the official Docker for Mac installation instructions (https://docs.docker.com/docker-for-mac/). Please, note that SEDA requires Docker for Mac, Docker Toolbox is not supported.

.. _dependencies:

Dependencies
============

As explained before, some operations require third-party software (e.g. BLAST) in order to work. This section describes the dependencies required by SEDA. If Docker is available, then SEDA can run these software dependencies using Docker images (we recommend using the official iamges provided and maintained by us, although custom images can be used).

+----------------------+--------+-----+-----+--------------------------+
| BLAST                | 2.6.0  | Yes | Yes | Yes                      |
+======================+========+=====+=====+==========================+
| Clustal Omega        | 1.2.4  | Yes | Yes | Yes                      |
+----------------------+--------+-----+-----+--------------------------+
| bedtools             | 2.25.0 | Yes | No  | Yes (MacPorts, Homebrew) |
+----------------------+--------+-----+-----+--------------------------+
| EMBOSS               | 6.6.0  | Yes | No  | Yes (Native, Homebrew)   |
+----------------------+--------+-----+-----+--------------------------+
| Splign/Compart       | N/A    | Yes | No  | No                       |
+----------------------+--------+-----+-----+--------------------------+
| ProSplign/ProCompart | N/A    | Yes | No  | No                       |
+----------------------+--------+-----+-----+--------------------------+

BLAST
-----

BLAST operations in SEDA make use of the following commands: makeblastdb, blastdb_aliastool, blastdbcmd, blastp, blastn, blastx, tblastn, and tblastx. BLAST binaries are available here: https://blast.ncbi.nlm.nih.gov/Blast.cgi?CMD=Web&PAGE_TYPE=BlastDocs&DOC_TYPE=Download. SEDA was developed and tested using the version **2.6.0-1**, which is the version included in the official Docker image (https://hub.docker.com/r/singgroup/seda-blast).

Clustal Omega
-------------

Clustal Omega binaries are available here: http://www.clustal.org/omega/#Download. SEDA was developed and tested using the version **1.2.4**, which is the version included in the official Docker image (https://hub.docker.com/r/singgroup/seda-clustalomega/dockerfile).

bedtools
--------

Bedtools binaries are available here: https://bedtools.readthedocs.io/en/latest/content/installation.html. SEDA was developed and tested using the version **2.25.0**, which is the version included in the official Docker image (https://hub.docker.com/r/singgroup/seda-clustalomega/dockerfile).

EMBOSS
------

EMBOSS-based operations in SEDA make use of the getorf command. EMBOSS binaries are available here: http://emboss.sourceforge.net/download/. EMBOSS was developed and tested using the version **6.6.0**, which is the version included in the official Docker image (https://hub.docker.com/r/singgroup/seda-emboss).

Splign/Compart
--------------

The original Splign and Compart binaries are available here: https://www.ncbi.nlm.nih.gov/sutils/splign/splign.cgi?textpage=downloads. Nevertheless, it is recommended to use the following binaries: http://static.sing-group.org/software/SEDA/dev_resources/splign-compart.tar.gz. This version is the one included in the official Docker image (https://hub.docker.com/r/singgroup/seda-splign-compart).

ProSplign/ProCompart
--------------------

The original ProSplign and ProCompart binaries are available here: https://www.ncbi.nlm.nih.gov/sutils/static/prosplign/prosplign.html. Nevertheless, SEDA requires the following version in order to work properly: http://static.sing-group.org/software/SEDA/dev_resources/pro-ncbi.tar.gz. This version is the one included in the official Docker image (https://hub.docker.com/r/singgroup/seda-prosplign-procompart).

SAPP
----

The original SAPP binaries are available here: http://sapp.gitlab.io/installation/. Nevertheless, it is recommended to use the following binaries: http://static.sing-group.org/software/SEDA/dev_resources/sapp.tar.gz. This version is the one included in the official Docker image (https://hub.docker.com/r/singgroup/seda-sapp).
