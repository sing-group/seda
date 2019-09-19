@echo off
start windows\32b\jre1.8.0_111\bin\java -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath "jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar" org.sing_group.seda.gui.SedaPanel
