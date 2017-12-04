package org.sing_group.seda.plugin.core;

import java.util.stream.Stream;

import org.sing_group.seda.plugin.core.cli.TransformationsSedaCliPlugin;
import org.sing_group.seda.plugin.core.gui.GrowSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.MergeGuiPlugin;
import org.sing_group.seda.plugin.core.gui.NcbiRenameGuiPlugin;
import org.sing_group.seda.plugin.core.gui.PatternFilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReallocateReferenceSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReformatFastaSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveRedundantSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RenameHeaderSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SortSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SplitSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.TranslateSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.FilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.UndoAlignmentGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaCliPlugin;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;

public class CoreSedaPluginFactory implements SedaPluginFactory {

  @Override
  public Stream<SedaGuiPlugin> getGuiPlugins() {
    return Stream.of(
      new FilteringSedaGuiPlugin(),
      new PatternFilteringSedaGuiPlugin(),
      new RemoveRedundantSequencesSedaGuiPlugin(),
      new SortSedaGuiPlugin(),
      new SplitSedaGuiPlugin(),
      new ReallocateReferenceSequencesSedaGuiPlugin(),
      new RenameHeaderSedaGuiPlugin(),
      new ReformatFastaSedaGuiPlugin(),
      new GrowSequencesSedaGuiPlugin(),
      new NcbiRenameGuiPlugin(),
      new MergeGuiPlugin(),
      new UndoAlignmentGuiPlugin(),
      new TranslateSequencesSedaGuiPlugin()
    );
  }

  @Override
  public Stream<SedaCliPlugin> getCliPlugins() {
    return Stream.of(
      new TransformationsSedaCliPlugin()
    );
  }
}
