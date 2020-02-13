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
package org.sing_group.seda.gui.redundant;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.translation.SequenceTranslationPanel;
import org.sing_group.seda.gui.translation.SequenceTranslationPanelPropertyChangeAdapter;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

public class RemoveRedundantSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String TRANSLATION_LABEL = "Convert to amino acid sequence before sequence comparison";
  private static final String TRANSLATION_INFO = "<html>If this option is selected, then input nucleic acid sequences are "
    + "translated into amino acid sequences before applying the sequence comparison. <br/>Note tht the input nucleic "
    + "acid sequences are reported.</html>";

  private RemoveRedundantSequencesTransformationProvider transformationProvider;
  private JCheckBox removeContainedSequencesCb;
  private JCheckBox mergeHeadersCb;
  private JCheckBox saveMergedHeadersCb;
  private JFileChooserPanel mergedHeadersFileChooser;
  private SequenceTranslationPanel sequenceTranslationPanel;

  public RemoveRedundantSequencesConfigurationPanel() {
    this.init();
    this.transformationProvider = new RemoveRedundantSequencesTransformationProvider(this);
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
    parameters.add(getSaveMergeHeadersParameter());
    parameters.add(getSaveMergeHeadersFileParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getRemoveContainedSequencesParameter() {
    this.removeContainedSequencesCb = new JCheckBox("Remove also subsequences", true);
    this.removeContainedSequencesCb.addItemListener(this::configurationChanged);

    return new InputParameter("", removeContainedSequencesCb, "");
  }

  private InputParameter getMergeHeadersParameter() {
    this.mergeHeadersCb = new JCheckBox("Merge headers", true);
    this.mergeHeadersCb.addItemListener(this::configurationChanged);

    return new InputParameter("", mergeHeadersCb, "");
  }

  private InputParameter getSaveMergeHeadersParameter() {
    this.saveMergedHeadersCb = new JCheckBox("Save merged headers into a file", false);
    this.saveMergedHeadersCb.addItemListener(this::configurationChanged);

    return new InputParameter("", saveMergedHeadersCb, "");
  }

  private InputParameter getSaveMergeHeadersFileParameter() {
    mergedHeadersFileChooser = JFileChooserPanelBuilder
      .createSaveJFileChooserPanel()
      .withLabel("Merge list directory: ")
      .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
      .withFileChooserSelectionMode(SelectionMode.DIRECTORIES).build();
    mergedHeadersFileChooser.getBrowseAction().setEnabled(false);
    mergedHeadersFileChooser.addFileChooserListener(this::selectedFileChanged);

    return new InputParameter("", mergedHeadersFileChooser, "");
  }

  private void configurationChanged(ItemEvent event) {
      this.notifyConfigurationChanged();
      this.mergedHeadersFileChooser.getBrowseAction().setEnabled(this.saveMergedHeadersCb.isSelected());
  }

  private void selectedFileChanged(ChangeEvent event) {
    this.notifyConfigurationChanged();
  }

  private void notifyConfigurationChanged() {
    ChangeEvent event = new ChangeEvent(this);
    for (
      RemoveRedundantSequencesConfigurationPanelListener l : this
        .getRemoveRedundantSequencesConfigurationPanelListeners()
    ) {
      l.configurationChanged(event);
    }
  }

  public SequenceTranslationPanel getTranslationPanel() {
    if (this.sequenceTranslationPanel == null) {
      this.sequenceTranslationPanel = new SequenceTranslationPanel(TRANSLATION_LABEL, TRANSLATION_INFO, false);
      this.sequenceTranslationPanel.addPropertyChangeListener(
        new SequenceTranslationPanelPropertyChangeAdapter() {
          @Override
          protected void translationPropertyChanged() {
            notifyConfigurationChanged();
          }

          @Override
          protected void joinFramesPropertyChanged() {
            notifyConfigurationChanged();
          }

          @Override
          protected void framesPropertyChanged() {
            notifyConfigurationChanged();
          }

          @Override
          protected void codonTablePropertyChanged() {
            notifyConfigurationChanged();
          }
        }
      );
    }

    return this.sequenceTranslationPanel;
  }

  public synchronized void addRemoveRedundantSequencesConfigurationPanelListener(
    RemoveRedundantSequencesConfigurationPanelListener l
  ) {
    this.listenerList.add(RemoveRedundantSequencesConfigurationPanelListener.class, l);
  }

  public synchronized RemoveRedundantSequencesConfigurationPanelListener[] getRemoveRedundantSequencesConfigurationPanelListeners() {
    return this.listenerList.getListeners(RemoveRedundantSequencesConfigurationPanelListener.class);
  }

  public boolean isValidUserSelection() {
    return (!this.saveMergedHeadersCb.isSelected() || this.getSaveMergedHeadersFile() != null)
      && this.sequenceTranslationPanel.isValidUserSelection();
  }

  public TransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public RemoveRedundantSequencesTransformationConfiguration getConfiguration() {
    if (this.saveMergedHeadersCb.isSelected()) {
      return new RemoveRedundantSequencesTransformationConfiguration(
        isRemoveContainedSequences(), isMergeHeaders(), getSaveMergedHeadersFile()
      );
    } else {
      return new RemoveRedundantSequencesTransformationConfiguration(isRemoveContainedSequences(), isMergeHeaders());
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
}
