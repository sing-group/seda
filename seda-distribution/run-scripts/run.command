#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Uncomment the following line to set the maximum amount of RAM memory that SEDA can use permanently:
# SEDA_JAVA_MEMORY="-Xmx6G"

"$DIR/mac/jre1.8.0_111/bin/java" ${SEDA_JAVA_MEMORY} -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -Dseda.java.path="$DIR/mac/jre1.8.0_111/bin" -classpath "$DIR/jars/*:$DIR/lib/*" -Dseda.local.execution.enabled.spligncompart=false -Dseda.local.execution.enabled.prosplignprocompart=false org.sing_group.seda.gui.SedaPanel
