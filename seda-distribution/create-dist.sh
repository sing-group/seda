#!/bin/bash

TARGET_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

WORKING_DIR=$TARGET_DIR/target
BUILDS_DIR=$WORKING_DIR/builds
SRC_SEDA=$TARGET_DIR/..

SEDA_VERSION=`cat ../pom.xml | grep "<version>[0-9]" | head -1 | sed -e 's/\t<version>//g' -e 's/<\/version>//g'`
SEDA_VERSION_SHORT=`echo $SEDA_VERSION | sed 's/\(^[0-9]*\.[0-9]*\).*/\1/'`

echo "SEDA VERSION: "$SEDA_VERSION
echo "SEDA VERSION SHORT: "$SEDA_VERSION_SHORT

# This is where java-to-distributable and yacli-tools jars are.
JAVA_CMD=${JAVA_CMD:-"java"}
JAVA_DEV_TOOLS=${JAVA_DEV_TOOLS:-"/opt/java-dev-tools"}

# Check script parameters.

ZIPS=false
WINDOWS=false
COMPILE=false
DOCS=false
CLEAN=false
DEBIAN=false
RPM=false
SNAPCRAFT=false

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
    -de|--deb)
    DEBIAN=true
    ;;
    -r|--rpm)
    RPM=true
    ;;
    -s|--snap)
    SNAPCRAFT=true
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
	if [ $? -ne 0 ]; then
		tput setaf 1
		echo -e "\n[SEDA build] There was an error running mvn clean install -Dmaven.test.skip=true\n"
		exit -1
	fi
fi

mkdir -p $WORKING_DIR && cd $WORKING_DIR

rm -rf jars && mkdir jars
rm -rf lib && mkdir lib
rm -rf jars-with-dependencies && mkdir jars-with-dependencies

cp $SRC_SEDA/seda-plugin-blast/target/seda-plugin-blast-$SEDA_VERSION.jar jars/seda-plugin-blast-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-cga/target/seda-plugin-cga-$SEDA_VERSION.jar jars/seda-plugin-cga-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-clustalomega/target/seda-plugin-clustalomega-$SEDA_VERSION.jar jars/seda-plugin-clustalomega-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-bedtools/target/seda-plugin-bedtools-$SEDA_VERSION.jar jars/seda-plugin-bedtools-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-splign-compart/target/seda-plugin-splign-compart-$SEDA_VERSION.jar jars/seda-plugin-splign-compart-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-prosplign-procompart/target/seda-plugin-prosplign-procompart-$SEDA_VERSION.jar jars/seda-plugin-prosplign-procompart-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-emboss/target/seda-plugin-emboss-$SEDA_VERSION.jar jars/seda-plugin-emboss-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-sapp/target/seda-plugin-sapp-$SEDA_VERSION.jar jars/seda-plugin-sapp-$SEDA_VERSION.jar
cp $SRC_SEDA/seda-plugin-pfam/target/seda-plugin-pfam-$SEDA_VERSION.jar jars/seda-plugin-pfam-$SEDA_VERSION.jar
cp $SRC_SEDA/seda/target/seda-$SEDA_VERSION.jar jars/seda-$SEDA_VERSION.jar

cp $SRC_SEDA/seda/target/seda-$SEDA_VERSION-jar-with-dependencies.jar jars-with-dependencies/seda-$SEDA_VERSION-jar-with-dependencies.jar

cp -R $SRC_SEDA/seda-plugin-blast/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-cga/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-clustalomega/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-bedtools/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-splign-compart/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-prosplign-procompart/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-emboss/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-sapp/target/lib/* lib
cp -R $SRC_SEDA/seda-plugin-pfam/target/lib/* lib
cp -R $SRC_SEDA/seda/target/lib/* lib

rm lib/seda-*$SEDA_VERSION.jar

# Generate the SEDA man page

SEDA_COORDINATE="org.sing_group:seda:"$SEDA_VERSION
SEDA_BLAST_COORDINATE="org.sing_group:seda-plugin-blast:"$SEDA_VERSION
SEDA_CGA_COORDINATE="org.sing_group:seda-plugin-cga:"$SEDA_VERSION
SEDA_CLUSTALOMEGA_COORDINATE="org.sing_group:seda-plugin-clustalomega:"$SEDA_VERSION
SEDA_BEDTOOLS_COORDINATE="org.sing_group:seda-plugin-bedtools:"$SEDA_VERSION
SEDA_SPLING_COMPART_COORDINATE="org.sing_group:seda-plugin-splign-compart:"$SEDA_VERSION
SEDA_PROSPLING_PROCOMPART_COORDINATE="org.sing_group:seda-plugin-prosplign-procompart:"$SEDA_VERSION
SEDA_EMBOSS_COORDINATE="org.sing_group:seda-plugin-emboss:"$SEDA_VERSION
SEDA_SAPP_COORDINATE="org.sing_group:seda-plugin-sapp:"$SEDA_VERSION
SEDA_PFAM_COORDINATE="org.sing_group:seda-plugin-pfam:"$SEDA_VERSION

# Man for SEDA Command Line Interface (CLI)

${JAVA_CMD} -jar $JAVA_DEV_TOOLS/yacli-tools-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
	generate-man \
		--from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR \
		--yacli-class-name org.sing_group.seda.cli.SedaCliApplication \
		--choices-file $TARGET_DIR/resources/seda-cli-man-choices.xml \
		--man-layout-file $TARGET_DIR/resources/seda-cli-man.vm \
		--compress --verbose

# Copy the run scripts to the TARGET_DIR and put the actual SEDA version

for RUN_SCRIPT in $(ls $TARGET_DIR/run-scripts/)
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
	
	rm -f $DIST_LINUX && tar -cvzf $DIST_LINUX run-gui.sh run-cli.sh seda-cli.1.gz jars lib linux/64b/jre1.8.0_111

	# Create the Windows 64b and 32b ZIPs.

	if [ ! -d "windows" ]; then
		wget http://static.sing-group.org/software/dev-resources/$WINDOWS_RESOURCES.zip
		unzip $WINDOWS_RESOURCES.zip 'windows/64b/*'
		unzip $WINDOWS_RESOURCES.zip 'windows/32b/*'
	fi

	rm -f $DIST_WINDOWS && zip -r $DIST_WINDOWS run-gui.bat run-cli.bat jars lib windows/64b/jre1.8.0_111
	rm -f $DIST_WINDOWS_32B && zip -r $DIST_WINDOWS_32B run-gui-32b.bat run-cli-32b.bat jars-with-dependencies/seda-$SEDA_VERSION-jar-with-dependencies.jar windows/32b/jre1.8.0_111
	printf "@ run-gui-32b.bat\n@=run-gui.bat\n" | zipnote -w $DIST_WINDOWS_32B
	printf "@ run-cli-32b.bat\n@=run-cli.bat\n" | zipnote -w $DIST_WINDOWS_32B

	# Create the Mac OS X ZIP.

	if [ ! -d "mac" ]; then
		wget http://static.sing-group.org/software/dev-resources/$MAC_RESOURCES.zip
		unzip $MAC_RESOURCES.zip 'mac/jre1.8.0_111/*'
	fi

	rm -f $DIST_MAC && zip -r $DIST_MAC run-gui.command run-cli.command jars lib mac/jre1.8.0_111
fi

# Create the Windows 64b installer.

if [ "$WINDOWS" = "true" ]; then
	WINDOWS_INSTALLER_RESOURCES=$WORKING_DIR/windows-installer

	rm -rf $WINDOWS_INSTALLER_RESOURCES
	cp -R $TARGET_DIR/windows-installer $WINDOWS_INSTALLER_RESOURCES/

	sed -i "s/\${SEDA_VERSION}/$SEDA_VERSION/g" $WINDOWS_INSTALLER_RESOURCES/"installer-script.nsi"
	sed -i "s/\${SEDA_VERSION}/$SEDA_VERSION/g" $WINDOWS_INSTALLER_RESOURCES/"seda-launcher.c"
	sed -i "s/\${SEDA_VERSION}/$SEDA_VERSION/g" $WINDOWS_INSTALLER_RESOURCES/"license.txt"

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
	sed -i "s/^version.*/version = u'$SEDA_VERSION_SHORT'/g" source/conf.py
	sed -i "s/^release.*/release = u'$SEDA_VERSION'/g" source/conf.py
	rm -rf build && make html
	cd ..
fi

# Generate SEDA GNU/Linux distributables.

if [ "$DEBIAN" = "true" ]; then

	# SEDA Command Line Interface (CLI) Debian distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-deb-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR/seda-cli/deb \
		--man-page $WORKING_DIR/seda-cli.1.gz \
		--choices-file $TARGET_DIR/resources/seda-cli-distributable-choices.xml \
		--verbose
	
	# SEDA Graphic User Interface (GUI) Debian distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-deb-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR/seda-gui/deb \
		--choices-file $TARGET_DIR/resources/seda-gui-distributable-choices.xml \
		--verbose
fi

if [ "$RPM" = "true" ]; then

	# SEDA Command Line Interface (CLI) RPM distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-rpm-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR/seda-cli/rpm \
		--man-page $WORKING_DIR/seda-cli.1.gz \
		--choices-file $TARGET_DIR/resources/seda-cli-distributable-choices.xml \
		--verbose
	
	# SEDA Graphic User Interface (GUI) RPM distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-rpm-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR/seda-gui/rpm \
		--choices-file $TARGET_DIR/resources/seda-gui-distributable-choices.xml \
		--verbose
	
fi

if [ "$SNAPCRAFT" = "true" ]; then

	# SEDA Command Line Interface (CLI) Snapcraft distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-snap-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR/seda-cli/snap \
		--man-page $WORKING_DIR/seda-cli.1.gz \
		--choices-file $TARGET_DIR/resources/seda-cli-distributable-choices.xml \
		--verbose
	
	# SEDA Graphic User Interface (GUI) Snapcraft distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-snap-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLING_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLING_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $WORKING_DIR/seda-gui/snap \
		--choices-file $TARGET_DIR/resources/seda-gui-distributable-choices.xml \
		--verbose
	
fi
