# Creation of SEDA distributables

Run `create-dist.sh -c -z -w -d --deb --snap --rpm` to:

1. Compile SEDA and plugins (`-c`) and generates man documentation.
2. Create Windows 64b and 32b, Linux 64b and Mac OS X 64b portable distributions (`-z`).
3. Create the Windows 64b installer (`-w`). When used alone, this flag also activates the previous one and creates the portable distributions.
4. Build the SEDA documentation (`-d`).
5. Create the debian (`--deb`), snap (`--snap`), and rpm (`--rpm`) packages.

Run `create-dist.sh --clean` to clean all builds (Maven and the `target/dist` folder), ignoring all other flags in the command.

# Requirements

## Java

Java 11 is required to run some commands and the script assumes `java` provides it. If not installed, a `jdk-11.0.18` can be downloaded [here](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html). Then, set the `JAVA_CMD` variable to the path of the `java` binary:

```shell
export JAVA_CMD="/path/to/jdk-11.0.18/bin/java"
```

## Java tools

To create Linux installers and packages, the script makes use of `yacli-tools` and `java-to-distributable`.

By default, their jars must be at `/opt/java-dev-tools` (as specified in the `JAVA_DEV_TOOLS` environment variable).

## Windows installers

To create Windows installers, `wine` and `mingw-w64` must be installed.

## Snap distributable

To create the snap packages, `snap`, `snapcraft` and `lxd` must be installed:

```shell
sudo snap install snapcraft --classic
sudo snap install lxd
lxd init --auto
```

If `lxd init` fails, try [this procedure](https://stackoverflow.com/a/54505693/1821422):

```shell
sudo usermod -a -G lxd $(whoami)
newgrp lxd
```

And check that it works with: `/snap/bin/lxc query --wait -X GET /1.0`.

## Rpm distributable

To create the rpm packages, `rpm` must be installed:

```shell
sudo apt install rpm
```
