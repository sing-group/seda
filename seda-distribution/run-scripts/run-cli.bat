@echo off

rem Edit the following line to set the maximum amount of RAM memory that SEDA can use:
set SEDA_JAVA_MEMORY=-Xmx4G

windows\64b\jre1.8.0_111\bin\java %SEDA_JAVA_MEMORY% -Dseda.java.path="windows\64b\jre1.8.0_111\bin" -classpath "jars/*;lib/*" -Dseda.local.execution.enabled.bedtools=false -Dseda.local.execution.enabled.spligncompart=false -Dseda.local.execution.enabled.prosplignprocompart=false -Dseda.local.execution.enabled.emboss=false org.sing_group.seda.cli.SedaCliApplication %*
