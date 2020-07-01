#include <windows.h>

int main()
{
        WinExec("jre1.8.0_111\\bin\\javaw.exe -Xmx4G -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -Dseda.java.path=\"jre1.8.0_111\\bin\" -classpath \"jars/*;lib/*\" -Dseda.local.execution.enabled.bedtools=false -Dseda.local.execution.enabled.spligncompart=false -Dseda.local.execution.enabled.prosplignprocompart=false -Dseda.local.execution.enabled.emboss=false org.sing_group.seda.gui.SedaPanel", SW_HIDE);
        return 0;
}
