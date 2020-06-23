#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Uncomment the following line to set the maximum amount of RAM memory that SEDA can use permanently:
# SEDA_JAVA_MEMORY="-Xmx6G"

"$DIR/linux/64b/jre1.8.0_111/bin/java" ${SEDA_JAVA_MEMORY} -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -Dseda.java.path="$DIR/linux/64b/jre1.8.0_111/bin/" -classpath "$DIR/jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar:$DIR/jars/seda-plugin-blast-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-clustalomega-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-bedtools-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-prosplign-procompart-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-splign-compart-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-emboss-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-sapp-${SEDA_VERSION}.jar" org.sing_group.seda.gui.SedaPanel
