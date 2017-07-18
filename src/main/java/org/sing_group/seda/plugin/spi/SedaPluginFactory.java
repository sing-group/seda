package org.sing_group.seda.plugin.spi;

import java.util.stream.Stream;

public interface SedaPluginFactory {
	public Stream<SedaGuiPlugin> getGuiPlugins();
	
	public Stream<SedaCliPlugin> getCliPlugins();
}
