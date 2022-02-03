package org.sing_group.seda.plugin.core.cli;

import org.sing_group.seda.cli.command.DisambiguateSequenceNamesCommand;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;

import es.uvigo.ei.sing.yacli.command.Command;

public class DisambiguateSequenceNamesSedaCliPlugin implements SedaCliPlugin {
	private final DisambiguateSequenceNamesCommand command;

	public DisambiguateSequenceNamesSedaCliPlugin() {
		this.command = new DisambiguateSequenceNamesCommand();
	}

	@Override
	public Command getCommand() {
		return this.command;
	}

}
