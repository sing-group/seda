@echo off
start windows\32b\jre1.8.0_111\bin\java -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath "jars/seda-0.2.0-SNAPSHOT-jar-with-dependencies.jar;jars/seda-plugin-blast-0.2.0-SNAPSHOT.jar" org.sing_group.seda.gui.SedaPanel
