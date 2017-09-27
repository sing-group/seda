package org.sing_group.seda.plugin.core;

import java.util.stream.Stream;

import org.sing_group.seda.plugin.core.cli.TransformationsSedaCliPlugin;
import org.sing_group.seda.plugin.core.gui.PatternFilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReformatFastaSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveRedundantSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SortSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SplitSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.TransformationsSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;

public class CoreSedaPluginFactory implements SedaPluginFactory {

  @Override
  public Stream<SedaGuiPlugin> getGuiPlugins() {
    return Stream.of(
      new TransformationsSedaGuiPlugin(),
      new PatternFilteringSedaGuiPlugin(),
      new RemoveRedundantSequencesSedaGuiPlugin(),
      new SortSedaGuiPlugin(),
      new SplitSedaGuiPlugin()
    );
  }

  @Override
  public Stream<SedaCliPlugin> getCliPlugins() {
    return Stream.of(
      new TransformationsSedaCliPlugin()
    );
  }
}
