#!/bin/bash

TARGET_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

WORKING_DIR=$TARGET_DIR/target
BUILDS_DIR=$WORKING_DIR/builds
SRC_SEDA=$TARGET_DIR/..

SEDA_VERSION=`cat ../pom.xml | grep "<version>[0-9]" | sed -e 's/\t<version>//g' -e 's/<\/version>//g'`
SEDA_VERSION_SHORT=`echo $SEDA_VERSION | sed 's/\(^[0-9]*\.[0-9]*\).*/\1/'`

# Check script parameters.

ZIPS=false
WINDOWS=false
COMPILE=false
DOCS=false
CLEAN=false

for key in $@; do
  case $key in
	-cl|--clean)
    CLEAN=true
    ;;
    -z|--zips)
    ZIPS=true
    ;;
    -w|--windows)
    WINDOWS=true
    ZIPS=true
    ;;
    -c|--compile)
    COMPILE=true
    ;;
    -d|--docs)
    DOCS=true
    ;;
  esac
done

if [ "$CLEAN" = "true" ]; then
	cd $SRC_SEDA && mvn clean
	cd $WORKING_DIR && rm -rf $BUILDS_DIR && mkdir -p $BUILDS_DIR
	exit 0
fi

# Compile SEDA if needed and copy the neccessary jars to the TARGET_DIR.

if [ "$COMPILE" = "true" ]; then
	cd $SRC_SEDA && mvn clean install -Dmaven.test.skip=true
fi

mkdir -p $WORKING_DIR && cd $WORKING_DIR

rm -rf jars && mkdir jars

cp $SRC_SEDA/seda-plugin-blast/target/seda-plugin-blast-$SEDA_VERSION.jar jars/seda-plugin-blast-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-clustalomega/target/seda-plugin-clustalomega-$SEDA_VERSION.jar jars/seda-plugin-clustalomega-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-bedtools/target/seda-plugin-bedtools-$SEDA_VERSION.jar jars/seda-plugin-bedtools-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-splign-compart/target/seda-plugin-splign-compart-$SEDA_VERSION.jar jars/seda-plugin-splign-compart-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-prosplign-procompart/target/seda-plugin-prosplign-procompart-$SEDA_VERSION.jar jars/seda-plugin-prosplign-procompart-$SEDA_VERSION.jar
cp $SRC_SEDA/seda/target/seda-$SEDA_VERSION-jar-with-dependencies.jar jars/seda-$SEDA_VERSION-jar-with-dependencies.jar

# Copy the run scripts to the TARGET_DIR and put the actual SEDA version

for RUN_SCRIPT in "run.sh" "run.bat" "run-32b.bat" "run.command"
do
	rm -f $WORKING_DIR/$RUN_SCRIPT
	cp $TARGET_DIR/run-scripts/$RUN_SCRIPT $WORKING_DIR/$RUN_SCRIPT
	sed -i "s/\${SEDA_VERSION}/$SEDA_VERSION/g" $RUN_SCRIPT
done

# Create the SEDA ZIPS in the BUILDS_DIR.

WINDOWS_RESOURCES="java-installer-resources-windows-1.8.0_111"
LINUX_RESOURCES="java-installer-resources-linux-1.8.0_111"
MAC_RESOURCES="java-installer-resources-mac-1.8.0_111"

DIST_LINUX=$BUILDS_DIR/"seda-linux-64b-$SEDA_VERSION.tar.gz"
DIST_WINDOWS=$BUILDS_DIR/"seda-windows-64b-$SEDA_VERSION.zip"
DIST_WINDOWS_32B=$BUILDS_DIR/"seda-windows-32b-$SEDA_VERSION.zip"
DIST_MAC=$BUILDS_DIR/"seda-mac-$SEDA_VERSION.zip"

cd $WORKING_DIR && rm -rf $BUILDS_DIR && mkdir -p $BUILDS_DIR

if [ "$ZIPS" = "true" ]; then

	# Create the Linux ZIP.

	if [ ! -d "linux" ]; then
		wget http://static.sing-group.org/software/dev-resources/$LINUX_RESOURCES.zip
		unzip $LINUX_RESOURCES.zip 'linux/64b/*'
	fi

	echo "tar -cvzf $DIST_LINUX run.sh jars linux/64b/jre1.8.0_111"

	rm -f $DIST_LINUX && tar -cvzf $DIST_LINUX run.sh jars linux/64b/jre1.8.0_111

	# Create the Windows 64b and 32b ZIPs.

	if [ ! -d "windows" ]; then
		wget http://static.sing-group.org/software/dev-resources/$WINDOWS_RESOURCES.zip
		unzip $WINDOWS_RESOURCES.zip 'windows/64b/*'
		unzip $WINDOWS_RESOURCES.zip 'windows/32b/*'
	fi

	rm -f $DIST_WINDOWS && zip -r $DIST_WINDOWS run.bat jars windows/64b/jre1.8.0_111
	rm -f $DIST_WINDOWS_32B && zip -r $DIST_WINDOWS_32B run-32b.bat jars windows/32b/jre1.8.0_111
	printf "@ run-32b.bat\n@=run.bat\n" | zipnote -w $DIST_WINDOWS_32B

	# Create the Mac OS X ZIP.

	if [ ! -d "mac" ]; then
		wget http://static.sing-group.org/software/dev-resources/$MAC_RESOURCES.zip
		unzip $MAC_RESOURCES.zip 'mac/jre1.8.0_111/*'
	fi

	rm -f $DIST_MAC && zip -r $DIST_MAC run.command jars mac/jre1.8.0_111
fi

# Create the Windows 64b installer.

if [ "$WINDOWS" = "true" ]; then
	WINDOWS_INSTALLER_RESOURCES=$WORKING_DIR/windows-installer

	rm -rf $WINDOWS_INSTALLER_RESOURCES
	cp -R $TARGET_DIR/windows-installer $WINDOWS_INSTALLER_RESOURCES/

	sed -i "s/\${SEDA_VERSION}/$SEDA_VERSION/g" $WINDOWS_INSTALLER_RESOURCES/"installer-script.nsi"
	sed -i "s/\${SEDA_VERSION}/$SEDA_VERSION/g" $WINDOWS_INSTALLER_RESOURCES/"seda-launcher.c"

	cd $WORKING_DIR

	if [ ! -d "NSIS" ]; then
		unzip $WINDOWS_RESOURCES.zip 'NSIS/*'
	fi

	MINGW32_WINDRES="i686-w64-mingw32-windres"
	$MINGW32_WINDRES $WINDOWS_INSTALLER_RESOURCES/resources.rc -O coff -o $WINDOWS_INSTALLER_RESOURCES/resources.res

	MINGW32_GCC="i686-w64-mingw32-gcc"
	$MINGW32_GCC -Wl,-subsystem,windows $WINDOWS_INSTALLER_RESOURCES/seda-launcher.c -o $WINDOWS_INSTALLER_RESOURCES/seda.exe


	cd $WINDOWS_INSTALLER_RESOURCES

	MAKE_NSIS="wine ../NSIS/makensis.exe"
	$MAKE_NSIS installer-script.nsi

	mv seda-windows-64b-$SEDA_VERSION.exe $BUILDS_DIR/"seda-windows-64b-$SEDA_VERSION.exe"

	cd ..
	rm -rf $WINDOWS_INSTALLER_RESOURCES
fi

# Build the documentation.

if [ "$DOCS" = "true" ]; then
	cd $SRC_SEDA/seda-docs
	sed -i "s/^version.*/version = u'$SEDA_VERSION'/g" source/conf.py
	sed -i "s/^release.*/release = u'$SEDA_VERSION_SHORT'/g" source/conf.py
	rm -rf build && make html
	cd ..
fi
