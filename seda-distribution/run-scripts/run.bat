@echo off
start windows\64b\jre1.8.0_111\bin\java -Xmx4G -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath "jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar;jars/seda-plugin-blast-${SEDA_VERSION}.jar" org.sing_group.seda.gui.SedaPanel
