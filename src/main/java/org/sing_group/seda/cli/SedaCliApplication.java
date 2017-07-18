package org.sing_group.seda.cli;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.sing_group.seda.plugin.SedaPluginManager;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;

import es.uvigo.ei.sing.yaacli.CLIApplication;
import es.uvigo.ei.sing.yaacli.command.Command;

public class SedaCliApplication extends CLIApplication {
  
	@Override
	protected List<Command> buildCommands() {
	  return new SedaPluginManager().getFactories()
		  .flatMap(SedaPluginFactory::getCliPlugins)
		  .map(SedaCliPlugin::getCommand)
		.collect(toList());
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
