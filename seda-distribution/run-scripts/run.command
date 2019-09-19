#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

"$DIR/mac/jre1.8.0_111/bin/java" -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -Dseda.java.path="$DIR/mac/jre1.8.0_111/bin" -classpath "$DIR/jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar:$DIR/jars/seda-plugin-blast-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-clustalomega-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-bedtools-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-prosplign-procompart-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-splign-compart-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-emboss-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-sapp-${SEDA_VERSION}.jar" -Dseda.local.execution.enabled.spligncompart=false -Dseda.local.execution.enabled.prosplignprocompart=false org.sing_group.seda.gui.SedaPanel
