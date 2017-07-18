package org.sing_group.seda.cli;

import es.uvigo.ei.sing.yaacli.CLIApplication;
import es.uvigo.ei.sing.yaacli.CommandLine;

public class SedaCli extends CommandLine {
	@Override
	protected Class<? extends CLIApplication> getCLIApplication() {
		return SedaCliApplication.class;
	}
	
	public static void main(String[] args) {
		new SedaCli().run();
	}

}
