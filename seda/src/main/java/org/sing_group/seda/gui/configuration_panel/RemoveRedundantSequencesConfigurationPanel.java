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
package org.sing_group.seda.gui.configuration_panel;

import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_CONVERT_AMINO_ACID_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_CONVERT_AMINO_ACID_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_MERGE_HEADERS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_MERGE_HEADERS_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_REMOVE_SUBSEQUENCE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_REMOVE_SUBSEQUENCE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_SAVE_MERGED_HEADERS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveRedundantSequencesSedaPluginInfo.PARAM_SAVE_MERGED_HEADERS_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.configuration_panel.translation.SequenceTranslationPanel;
import org.sing_group.seda.gui.configuration_panel.translation.SequenceTranslationPanelPropertyChangeAdapter;
import org.sing_group.seda.transformation.provider.redundant.RemoveRedundantSequencesTransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

public class RemoveRedundantSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String TRANSLATION_LABEL = PARAM_CONVERT_AMINO_ACID_DESCRIPTION;
  private static final String TRANSLATION_INFO = PARAM_CONVERT_AMINO_ACID_HELP_GUI;

  private RemoveRedundantSequencesTransformationProvider transformationProvider;
  private JCheckBox removeContainedSequencesCb;
  private JCheckBox mergeHeadersCb;
  private JFileChooserPanel mergedHeadersFileChooser;
  private SequenceTranslationPanel sequenceTranslationPanel;

  public RemoveRedundantSequencesConfigurationPanel() {
    this.init();
    this.transformationProvider = new RemoveRedundantSequencesTransformationProvider();
    this.notifyConfigurationChanged();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(getParametersPanel(), BorderLayout.CENTER);
    mainPanel.add(getTranslationPanel(), BorderLayout.SOUTH);

    return new CenteredJPanel(mainPanel);
  }

  private JPanel getParametersPanel() {
    return new InputParametersPanel(getInputParameters());
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getRemoveContainedSequencesParameter());
    parameters.add(getMergeHeadersParameter());
    parameters.add(getSaveMergeHeadersFileParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getRemoveContainedSequencesParameter() {
    this.removeContainedSequencesCb = new JCheckBox(PARAM_REMOVE_SUBSEQUENCE_DESCRIPTION, true);
    this.removeContainedSequencesCb.addItemListener(this::configurationChanged);

    return new InputParameter("", removeContainedSequencesCb, PARAM_REMOVE_SUBSEQUENCE_HELP_GUI);
  }

  private InputParameter getMergeHeadersParameter() {
    this.mergeHeadersCb = new JCheckBox(PARAM_MERGE_HEADERS_DESCRIPTION, true);
    this.mergeHeadersCb.addItemListener(this::configurationChanged);

    return new InputParameter("", mergeHeadersCb, PARAM_MERGE_HEADERS_HELP_GUI);
  }

  private InputParameter getSaveMergeHeadersFileParameter() {
    mergedHeadersFileChooser =
      JFileChooserPanelBuilder
        .createSaveJFileChooserPanel()
        .withLabel(PARAM_SAVE_MERGED_HEADERS_DESCRIPTION + ":")
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.DIRECTORIES).build();
    mergedHeadersFileChooser.addFileChooserListener(this::selectedFileChanged);

    return new InputParameter("", mergedHeadersFileChooser, PARAM_SAVE_MERGED_HEADERS_HELP_GUI);
  }

  private void configurationChanged(ItemEvent event) {
    this.notifyConfigurationChanged();
  }

  private void selectedFileChanged(ChangeEvent event) {
    this.notifyConfigurationChanged();
  }

  private void notifyConfigurationChanged() {
    this.transformationProvider.setConfiguration(this.getConfiguration());
  }

  public SequenceTranslationPanel getTranslationPanel() {
    if (this.sequenceTranslationPanel == null) {
      this.sequenceTranslationPanel = new SequenceTranslationPanel(TRANSLATION_LABEL, TRANSLATION_INFO, false);
      this.sequenceTranslationPanel.addPropertyChangeListener(
        new SequenceTranslationPanelPropertyChangeAdapter() {
          @Override
          protected void translationPropertyChanged() {
            sequenceTranslationConfigurationChanged();
          }

          @Override
          protected void joinFramesPropertyChanged() {
            sequenceTranslationConfigurationChanged();
          }

          @Override
          protected void framesPropertyChanged() {
            sequenceTranslationConfigurationChanged();
          }

          @Override
          protected void codonTablePropertyChanged() {
            sequenceTranslationConfigurationChanged();
          }

          protected void reverseSequencesPropertyChanged() {
            sequenceTranslationConfigurationChanged();
          };
        }
      );
    }

    return this.sequenceTranslationPanel;
  }

  private void sequenceTranslationConfigurationChanged() {
    this.notifyConfigurationChanged();
  }

  public RemoveRedundantSequencesTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public RemoveRedundantSequencesTransformationConfiguration getConfiguration() {
    SequenceTranslationConfiguration translationConfiguration =
      this.sequenceTranslationPanel.getSequenceTranslationConfiguration();
    File saveMergedHeadersFile = getSaveMergedHeadersFile();
    if (saveMergedHeadersFile != null) {
      if (this.sequenceTranslationPanel.isTranslationSelected()) {
        return new RemoveRedundantSequencesTransformationConfiguration(
          isRemoveContainedSequences(), isMergeHeaders(), saveMergedHeadersFile, translationConfiguration
        );
      } else {
        return new RemoveRedundantSequencesTransformationConfiguration(
          isRemoveContainedSequences(), isMergeHeaders(), saveMergedHeadersFile
        );
      }
    } else {
      if (this.sequenceTranslationPanel.isTranslationSelected()) {
        return new RemoveRedundantSequencesTransformationConfiguration(
          isRemoveContainedSequences(), isMergeHeaders(), translationConfiguration
        );
      } else {
        return new RemoveRedundantSequencesTransformationConfiguration(
          isRemoveContainedSequences(), isMergeHeaders()
        );
      }
    }
  }

  private Mode isRemoveContainedSequences() {
    if (this.removeContainedSequencesCb.isSelected()) {
      return Mode.CONTAINED_SEQUENCES;
    } else {
      return Mode.EXACT_DUPLICATES;
    }
  }

  private boolean isMergeHeaders() {
    return this.mergeHeadersCb.isSelected();
  }

  private File getSaveMergedHeadersFile() {
    return this.mergedHeadersFileChooser.getSelectedFile();
  }

  public void setTransformationProvider(RemoveRedundantSequencesTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    RemoveRedundantSequencesTransformationConfiguration configuration = this.transformationProvider.getConfiguration();

    this.removeContainedSequencesCb.setSelected(configuration.getMode().equals(Mode.CONTAINED_SEQUENCES));

    this.mergeHeadersCb.setSelected(configuration.isMergeHeaders());

    Optional<File> mergedSequencesDirectory = configuration.getMergedSequencesListDirectory();
    if (mergedSequencesDirectory.isPresent()) {
      this.mergedHeadersFileChooser.setSelectedFile(mergedSequencesDirectory.get());
    } else {
      this.mergedHeadersFileChooser.clearSelectedFile();
    }

    this.sequenceTranslationPanel
      .setSequenceTranslationConfiguration(configuration.getSequenceTranslationConfiguration());
  }
}
