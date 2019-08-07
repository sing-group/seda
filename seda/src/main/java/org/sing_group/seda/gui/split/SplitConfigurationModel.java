/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.split;

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.seda.core.split.DefaultSequencesSort;
import org.sing_group.seda.core.split.NumberOfFilesSplitter;
import org.sing_group.seda.core.split.NumberOfSequencesAndNumberOfFilesSplitter;
import org.sing_group.seda.core.split.NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter;
import org.sing_group.seda.core.split.NumberOfSequencesSplitter;
import org.sing_group.seda.core.split.RandomSequencesSort;
import org.sing_group.seda.core.split.SequencesGroupSplitMode;
import org.sing_group.seda.core.split.SequencesGroupSplitter;
import org.sing_group.seda.core.split.SequencesSort;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SplitSequencesGroupDatasetTransformation;

public class SplitConfigurationModel extends AbstractTransformationProvider {

  private RadioButtonsPanel<SequencesGroupSplitMode> splitMode;
  private JCheckBox randomize;
  private JIntegerTextField randomSeedTf;
  private JCheckBox independentExtractions;
  private JIntegerTextField numberOfFilesTf;
  private JIntegerTextField numberOfSequencesTf;

  public SplitConfigurationModel(
    RadioButtonsPanel<SequencesGroupSplitMode> splitMode, 
    JCheckBox randomize,
    JIntegerTextField randomSeedTf,
    JCheckBox independentExtractions,
    JIntegerTextField numberOfFilesTf,
    JIntegerTextField numberOfSequencesTf
  ) {
    this.splitMode = splitMode;
    this.randomize = randomize;
    this.numberOfSequencesTf = numberOfSequencesTf;
    this.randomSeedTf = randomSeedTf;
    this.numberOfFilesTf = numberOfFilesTf;
    this.numberOfSequencesTf = numberOfSequencesTf;

    this.addListeners();
  }

  @Override
  public boolean isValidTransformation() {
    return true;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    final SequencesGroupSplitter splitter;

    switch (getSplitMode()) {
      case FIXED_FILES:
        splitter = new NumberOfFilesSplitter(getNumFiles(), getSequencesSort(), factory);
        break;
      case FIXED_SEQUENCES_PER_FILE:
        splitter = new NumberOfSequencesSplitter(getNumSequences(), getSequencesSort(), factory);
        break;
      case SEQUENCES_PER_FILE_AND_FILES:
        if (isIndependentExtractions()) {
          splitter =
            new NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter(
              getNumFiles(), getNumSequences(), getSequencesSort(), factory
            );
        } else {
          splitter =
            new NumberOfSequencesAndNumberOfFilesSplitter(getNumFiles(), getNumSequences(), getSequencesSort(), factory);
        }
        break;
      default:
        throw new IllegalStateException("Illegal split mode");
    }

    return new SplitSequencesGroupDatasetTransformation(splitter, factory);
  }

  private SequencesGroupSplitMode getSplitMode() {
    return splitMode.getSelectedItem().get();
  }

  private int getNumSequences() {
    return this.numberOfSequencesTf.getValue();
  }

  private int getNumFiles() {
    return this.numberOfFilesTf.getValue();
  }

  private SequencesSort getSequencesSort() {
    if (this.randomize.isSelected()) {
      return new RandomSequencesSort(this.randomSeedTf.getValue());
    } else {
      return new DefaultSequencesSort();
    }
  }

  private boolean isIndependentExtractions() {
    return this.independentExtractions.isSelected();
  }

  private void addListeners() {
    this.randomize.addItemListener(this::randomizeChanged);
    this.randomSeedTf.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void insertUpdate(DocumentEvent e) {
          randomSeedChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
          randomSeedChanged();
        }
      }
    );
    this.splitMode.addItemListener(this::splitModeChanged);
    this.numberOfFilesTf.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void insertUpdate(DocumentEvent e) {
          numberOfFilesChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
          numberOfFilesChanged();
        }
      }
    );
    this.numberOfSequencesTf.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void insertUpdate(DocumentEvent e) {
          numberOfSequencesChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
          numberOfSequencesChanged();
        }
      }
    );
  }

  private void randomizeChanged(ItemEvent event) {
    this.fireTransformationsConfigurationModelEvent(
      SplitConfigurationEventType.RANDOMIZE_SELECTION_CHANGED, getSequencesSort()
    );
  }

  private void randomSeedChanged() {
    this.fireTransformationsConfigurationModelEvent(
      SplitConfigurationEventType.RANDOM_SEED_CHANGED, getSplitMode()
    );
  }

  private void splitModeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.fireTransformationsConfigurationModelEvent(
        SplitConfigurationEventType.SPLIT_MODE_CHANGED, getSplitMode()
      );
    }
  }

  private void numberOfFilesChanged() {
    this.fireTransformationsConfigurationModelEvent(
      SplitConfigurationEventType.NUMBER_OF_FILES_CHANGED, getSplitMode()
    );
  }

  private void numberOfSequencesChanged() {
    this.fireTransformationsConfigurationModelEvent(
      SplitConfigurationEventType.NUMBER_OF_SEQUENCES_CHANGED, getSplitMode()
    );
  }
}
