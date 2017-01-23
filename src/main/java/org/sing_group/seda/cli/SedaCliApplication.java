package org.sing_group.seda.cli;

import static java.util.Arrays.asList;

import java.util.List;

import org.sing_group.seda.cli.command.GenerateCommand;

import es.uvigo.ei.sing.yaacli.CLIApplication;
import es.uvigo.ei.sing.yaacli.command.Command;

public class SedaCliApplication extends CLIApplication {

	@Override
	protected List<Command> buildCommands() {
		return asList(new GenerateCommand());
	}

	@Override
	protected String getApplicationName() {
		return "Sequence Datasets";
	}

	@Override
	protected String getApplicationCommand() {
		return "seda";
	}

}
