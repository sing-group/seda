#include <windows.h>

int main()
{
        WinExec("jre1.8.0_111\\bin\\javaw.exe -Xmx4G -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -Dseda.java.path=\"jre1.8.0_111\\bin\" -classpath \"jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar;jars/seda-plugin-blast-${SEDA_VERSION}.jar;jars/seda-plugin-clustalomega-${SEDA_VERSION}.jar;jars/seda-plugin-bedtools-${SEDA_VERSION}.jar;jars/seda-plugin-prosplign-procompart-${SEDA_VERSION}.jar;jars/seda-plugin-splign-compart-${SEDA_VERSION}.jar;jars/seda-plugin-emboss-${SEDA_VERSION}.jar;jars/seda-plugin-sapp-${SEDA_VERSION}.jar\" -Dseda.local.execution.enabled.bedtools=false -Dseda.local.execution.enabled.spligncompart=false -Dseda.local.execution.enabled.prosplignprocompart=false -Dseda.local.execution.enabled.emboss=false org.sing_group.seda.gui.SedaPanel", SW_HIDE);
        return 0;
}
