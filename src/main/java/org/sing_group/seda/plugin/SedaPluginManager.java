package org.sing_group.seda.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import org.sing_group.seda.plugin.spi.SedaPluginFactory;

public class SedaPluginManager {
  private final static Collection<SedaPluginFactory> PLUGIN_FACTORIES;
  
  static {
    final ServiceLoader<SedaPluginFactory> loader = ServiceLoader.load(SedaPluginFactory.class);

    PLUGIN_FACTORIES = new ArrayList<>();
    loader.forEach(PLUGIN_FACTORIES::add);
  }
  
  public Stream<SedaPluginFactory> getFactories() {
    return PLUGIN_FACTORIES.stream();
  }
}
