package org.sing_group.seda.cli;

import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.yaacli.command.AbstractCommand;
import es.uvigo.ei.sing.yaacli.command.option.Option;

public abstract class SedaCommand extends AbstractCommand {
  //TODO Define the options for file input and output
  
  @Override
  protected List<Option<?>> createOptions() {
    final List<Option<?>> options = new ArrayList<>();
    
    //TODO: Add this options to "options".
    
    options.addAll(this.createSedaOptions());
    
    return options;
  }
  
  protected abstract List<Option<?>> createSedaOptions();
}
