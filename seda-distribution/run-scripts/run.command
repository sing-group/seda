#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

"$DIR/mac/jre1.8.0_111/bin/java" -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath "$DIR/jars/seda-0.2.0-SNAPSHOT-jar-with-dependencies.jar:$DIR/jars/seda-plugin-blast-0.2.0-SNAPSHOT.jar" org.sing_group.seda.gui.SedaPanel
