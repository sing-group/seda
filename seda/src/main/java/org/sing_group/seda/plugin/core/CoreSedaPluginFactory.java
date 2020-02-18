/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.seda.plugin.core;

import java.util.stream.Stream;

import org.sing_group.seda.plugin.core.cli.TransformationsSedaCliPlugin;
import org.sing_group.seda.plugin.core.gui.CompareSequencesGroupDatasetSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ConcatenateSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.DisambiguateSequenceNamesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.FilterByBasePresenceSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.FilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.GenerateConsensusSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.GrowSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.MergeGuiPlugin;
import org.sing_group.seda.plugin.core.gui.NcbiRenameGuiPlugin;
import org.sing_group.seda.plugin.core.gui.PatternFilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReallocateReferenceSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReformatFastaSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RegexSplitSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveIsoformsSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveRedundantSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RenameHeaderSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SortSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SplitSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.TranslateSequencesSedaGuiPlugin;
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
      new FilterByBasePresenceSedaGuiPlugin(),
      new RemoveRedundantSequencesSedaGuiPlugin(),
      new SortSedaGuiPlugin(),
      new SplitSedaGuiPlugin(),
      new RegexSplitSedaGuiPlugin(),
      new ReallocateReferenceSequencesSedaGuiPlugin(),
      new RenameHeaderSedaGuiPlugin(),
      new ReformatFastaSedaGuiPlugin(),
      new GrowSequencesSedaGuiPlugin(),
      new NcbiRenameGuiPlugin(),
      new MergeGuiPlugin(),
      new UndoAlignmentGuiPlugin(),
      new TranslateSequencesSedaGuiPlugin(),
      new DisambiguateSequenceNamesSedaGuiPlugin(),
      new GenerateConsensusSequencesSedaGuiPlugin(),
      new ConcatenateSequencesSedaGuiPlugin(),
      new RemoveIsoformsSedaGuiPlugin(),
      new CompareSequencesGroupDatasetSedaGuiPlugin()
    );
  }

  @Override
  public Stream<SedaCliPlugin> getCliPlugins() {
    return Stream.of(
      new TransformationsSedaCliPlugin()
    );
  }
}
