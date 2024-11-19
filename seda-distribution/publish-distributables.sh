#!/bin/bash

set -o nounset
set -o errexit

TARGET_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

WORKING_DIR=$TARGET_DIR/target
BUILDS_DIR=$WORKING_DIR/builds
SRC_SEDA=$TARGET_DIR/..

SEDA_VERSION=`cat ../pom.xml | grep "<version>[0-9]" | head -1 | sed -e 's/\t<version>//g' -e 's/<\/version>//g'`
SEDA_VERSION_SHORT=`echo $SEDA_VERSION | sed 's/\(^[0-9]*\.[0-9]*\).*/\1/'`

# This is where java-to-distributable and yacli-tools jars are.
JAVA_CMD=${JAVA_CMD:-"java"}
JAVA_DEV_TOOLS=${JAVA_DEV_TOOLS:-"/opt/java-dev-tools"}

# Check script parameters.

RPM=false
SNAPCRAFT=false
PPA=false

for key in $@; do
  case $key in
    -r|--rpm)
    RPM=true
    ;;
    -s|--snap)
    SNAPCRAFT=true
    ;;
    -pp|--ppa)
    PPA=true
    ;;
    	*)
    echo "Unknown argument: $key"
    ;;
  esac
done

# Maven coordinates for SEDA and its plugins

SEDA_COORDINATE="org.sing_group:seda:"$SEDA_VERSION
SEDA_BLAST_COORDINATE="org.sing_group:seda-plugin-blast:"$SEDA_VERSION
SEDA_CGA_COORDINATE="org.sing_group:seda-plugin-cga:"$SEDA_VERSION
SEDA_CLUSTALOMEGA_COORDINATE="org.sing_group:seda-plugin-clustalomega:"$SEDA_VERSION
SEDA_BEDTOOLS_COORDINATE="org.sing_group:seda-plugin-bedtools:"$SEDA_VERSION
SEDA_SPLIGN_COMPART_COORDINATE="org.sing_group:seda-plugin-splign-compart:"$SEDA_VERSION
SEDA_PROSPLIGN_PROCOMPART_COORDINATE="org.sing_group:seda-plugin-prosplign-procompart:"$SEDA_VERSION
SEDA_EMBOSS_COORDINATE="org.sing_group:seda-plugin-emboss:"$SEDA_VERSION
SEDA_SAPP_COORDINATE="org.sing_group:seda-plugin-sapp:"$SEDA_VERSION
SEDA_PFAM_COORDINATE="org.sing_group:seda-plugin-pfam:"$SEDA_VERSION

cd $WORKING_DIR

if [ "$RPM" = "true" ]; then

	# SEDA Command Line Interface (CLI) and Graphic User Interface (GUI) RPM distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-rpm-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLIGN_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLIGN_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $BUILDS_DIR/seda/rpm \
		--man-page $WORKING_DIR/seda-cli.1.gz \
		--autocompletion $WORKING_DIR/seda-cli-completion.bash \
		--choices-file $TARGET_DIR/resources/seda-distributable-choices.xml \
		--sign \
		--publish \
		--verbose

	mv $BUILDS_DIR/seda/rpm/rpmbuild/RPMS/noarch/*rpm $BUILDS_DIR/ && rm -rf $BUILDS_DIR/seda/rpm

fi

if [ "$SNAPCRAFT" = "true" ]; then

	# SEDA Command Line Interface (CLI) and Graphic User Interface (GUI) Snapcraft distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-snap-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLIGN_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLIGN_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $BUILDS_DIR/seda/snap \
		--man-page $WORKING_DIR/seda-cli.1.gz \
		--autocompletion $WORKING_DIR/seda-cli-completion.bash \
		--choices-file $TARGET_DIR/resources/seda-snap-distributable-choices.xml \
		--sign \
		--publish \
		--verbose

	mv $BUILDS_DIR/seda/snap/*.snap $BUILDS_DIR/ && rm -rf $BUILDS_DIR/seda/snap

fi

if [ "$PPA" = "true" ]; then

	# SEDA Command Line Interface (CLI) and Graphic User Interface (GUI) PPA distributable

	${JAVA_CMD} -jar $JAVA_DEV_TOOLS/java-to-distributable-0.1.0-SNAPSHOT-jar-with-dependencies-and-services.jar \
		generate-ppa-distributable \
		--maven --from-maven-local \
		--maven-coordinates $SEDA_COORDINATE \
		--maven-coordinates $SEDA_BLAST_COORDINATE \
		--maven-coordinates $SEDA_CGA_COORDINATE \
		--maven-coordinates $SEDA_CLUSTALOMEGA_COORDINATE \
		--maven-coordinates $SEDA_BEDTOOLS_COORDINATE \
		--maven-coordinates $SEDA_SPLIGN_COMPART_COORDINATE \
		--maven-coordinates $SEDA_PROSPLIGN_PROCOMPART_COORDINATE \
		--maven-coordinates $SEDA_EMBOSS_COORDINATE \
		--maven-coordinates $SEDA_SAPP_COORDINATE \
		--maven-coordinates $SEDA_PFAM_COORDINATE \
		--output-directory $BUILDS_DIR/seda/ppa \
		--man-page $WORKING_DIR/seda-cli.1.gz \
		--autocompletion $WORKING_DIR/seda-cli-completion.bash \
		--choices-file $TARGET_DIR/resources/seda-distributable-choices.xml \
		--sign \
		--publish \
		--verbose

	mv $BUILDS_DIR/seda/ppa/*.dsc $BUILDS_DIR/ && mv $BUILDS_DIR/seda/ppa/*.tar.xz $BUILDS_DIR/ \
	&& mv $BUILDS_DIR/seda/ppa/*.build $BUILDS_DIR/ && mv $BUILDS_DIR/seda/ppa/*.buildinfo $BUILDS_DIR/ \
	&& mv $BUILDS_DIR/seda/ppa/*.changes $BUILDS_DIR/ && rm -rf $BUILDS_DIR/seda/ppa

fi

rm -rf $BUILDS_DIR/seda
