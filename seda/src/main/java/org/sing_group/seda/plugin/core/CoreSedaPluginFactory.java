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

import org.sing_group.seda.plugin.core.cli.CompareSequencesSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.ConcatenateSequenceSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.DisambiguateSequenceNamesSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.FilterByBasePresenceSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.FilteringSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.GenerateConsensusSequenceSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.GrowSequencesSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.MergeSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.NcbiRenameSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.PatternFilteringSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.ReallocateReferenceSequencesSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.ReformatFastaSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RegexSplitSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RemoveIsoformsSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RemoveRedundantSequencesSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RemoveStopCodonsSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RenameHeaderAddWordSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RenameHeaderMultipartSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RenameHeaderReplaceIntervalSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.RenameHeaderReplaceWordSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.ReverseComplementSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.SortSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.SplitSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.TranslateSequencesSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.TrimAlignmentSedaCliPlugin;
import org.sing_group.seda.plugin.core.cli.UndoAlignmentSedaCliPlugin;
import org.sing_group.seda.plugin.core.gui.CompareSequencesGroupDatasetSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ConcatenateSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.DisambiguateSequenceNamesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.FilterByBasePresenceSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.FilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.GenerateConsensusSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.GrowSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.MergeGuiSedaPlugin;
import org.sing_group.seda.plugin.core.gui.NcbiRenameSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.PatternFilteringSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReallocateReferenceSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReformatFastaSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RegexSplitSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveIsoformsSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveRedundantSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RemoveStopCodonsSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.RenameHeaderSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.ReverseComplementSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SortSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.SplitSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.TranslateSequencesSedaGuiPlugin;
import org.sing_group.seda.plugin.core.gui.TrimAlignmentSedaGuiPlugin;
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
      new NcbiRenameSedaGuiPlugin(),
      new MergeGuiSedaPlugin(),
      new UndoAlignmentGuiPlugin(),
      new TranslateSequencesSedaGuiPlugin(),
      new DisambiguateSequenceNamesSedaGuiPlugin(),
      new GenerateConsensusSequencesSedaGuiPlugin(),
      new ConcatenateSequencesSedaGuiPlugin(),
      new RemoveIsoformsSedaGuiPlugin(),
      new CompareSequencesGroupDatasetSedaGuiPlugin(),
      new RemoveStopCodonsSedaGuiPlugin(),
      new TrimAlignmentSedaGuiPlugin(),
      new ReverseComplementSedaGuiPlugin()
    );
  }

  @Override
  public Stream<SedaCliPlugin> getCliPlugins() {
    return Stream.of(
      new DisambiguateSequenceNamesSedaCliPlugin(),
      new ReformatFastaSedaCliPlugin(),
      new TrimAlignmentSedaCliPlugin(),
      new UndoAlignmentSedaCliPlugin(),
      new ConcatenateSequenceSedaCliPlugin(),
      new GenerateConsensusSequenceSedaCliPlugin(),
      new RenameHeaderAddWordSedaCliPlugin(),
      new RenameHeaderMultipartSedaCliPlugin(),
      new RenameHeaderReplaceIntervalSedaCliPlugin(),
      new RenameHeaderReplaceWordSedaCliPlugin(),
      new NcbiRenameSedaCliPlugin(),
      new ReallocateReferenceSequencesSedaCliPlugin(),
      new SortSedaCliPlugin(),
      new FilterByBasePresenceSedaCliPlugin(),
      new FilteringSedaCliPlugin(),
      new TranslateSequencesSedaCliPlugin(),
      new PatternFilteringSedaCliPlugin(),
      new RemoveIsoformsSedaCliPlugin(),
      new RemoveRedundantSequencesSedaCliPlugin(),
      new CompareSequencesSedaCliPlugin(),
      new GrowSequencesSedaCliPlugin(),
      new MergeSedaCliPlugin(),
      new RegexSplitSedaCliPlugin(),
      new RemoveStopCodonsSedaCliPlugin(),
      new ReverseComplementSedaCliPlugin(),
      new SplitSedaCliPlugin()
    );
  }
}
