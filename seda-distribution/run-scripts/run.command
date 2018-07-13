#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

"$DIR/mac/jre1.8.0_111/bin/java" -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath "$DIR/jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar:$DIR/jars/seda-plugin-blast-${SEDA_VERSION}.jar:$DIR/jars/seda-plugin-clustalomega-${SEDA_VERSION}.jar" org.sing_group.seda.gui.SedaPanel
