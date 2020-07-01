@echo off

rem Uncomment the following line to set the maximum amount of RAM memory that SEDA can use:
rem set SEDA_JAVA_MEMORY=-Xmx2G

start windows\32b\jre1.8.0_111\bin\java %SEDA_JAVA_MEMORY% -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath "jars-with-dependencies/seda-${SEDA_VERSION}-jar-with-dependencies.jar" org.sing_group.seda.gui.SedaPanel
