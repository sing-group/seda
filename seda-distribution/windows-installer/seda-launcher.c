#include <windows.h>

int main()
{
        WinExec("jre1.8.0_111\\bin\\javaw.exe -Xmx4G -Dswing.defaultlaf=javax.swing.plaf.nimbus.NimbusLookAndFeel -classpath \"jars/seda-${SEDA_VERSION}-jar-with-dependencies.jar;jars/seda-blast-plugin-${SEDA_VERSION}.jar\" org.sing_group.seda.gui.SedaPanel", SW_HIDE);
        return 0;
}
