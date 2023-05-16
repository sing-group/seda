#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Uncomment the following line to set the maximum amount of RAM memory that SEDA can use permanently:
# SEDA_JAVA_MEMORY="-Xmx6G"

"$DIR/linux/64b/jre1.8.0_111/bin/java" ${SEDA_JAVA_MEMORY} -Dseda.java.path="$DIR/linux/64b/jre1.8.0_111/bin/" -classpath "$DIR/jars/*:$DIR/lib/*" org.sing_group.seda.cli.SedaCliApplication "$@"
