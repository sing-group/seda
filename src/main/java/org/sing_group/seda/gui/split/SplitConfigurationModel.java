package org.sing_group.seda.gui.split;

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.split.NumberOfFilesSplitter;
import org.sing_group.seda.split.NumberOfSequencesAndNumberOfFilesSplitter;
import org.sing_group.seda.split.NumberOfSequencesSplitter;
import org.sing_group.seda.split.SequencesGroupSplitMode;
import org.sing_group.seda.split.SequencesGroupSplitter;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SplitSequencesGroupDatasetTransformation;

public class SplitConfigurationModel extends AbstractTransformationProvider {

  private RadioButtonsPanel<SequencesGroupSplitMode> splitMode;
  private JCheckBox randomize;
  private JCheckBox independentExtractions;
  private JIntegerTextField numberOfFilesTf;
  private JIntegerTextField numberOfSequencesTf;

  public SplitConfigurationModel(
    RadioButtonsPanel<SequencesGroupSplitMode> splitMode, JCheckBox randomize, JCheckBox independentExtractions,
    JIntegerTextField numberOfFilesTf,
    JIntegerTextField numberOfSequencesTf
  ) {
    this.splitMode = splitMode;
    this.randomize = randomize;
    this.independentExtractions = independentExtractions;
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
        splitter = new NumberOfFilesSplitter(getNumFiles(), isRandomize(), factory);
        break;
      case FIXED_SEQUENCES_PER_FILE:
        splitter = new NumberOfSequencesSplitter(getNumSequences(), isRandomize(), factory);
        break;
      case SEQUENCES_PER_FILE_AND_FILES:
        splitter = new NumberOfSequencesAndNumberOfFilesSplitter(getNumFiles(), getNumSequences(), isRandomize(), isIndependentExtractions(), factory);
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

  private boolean isRandomize() {
    return this.randomize.isSelected();
  }

  private boolean isIndependentExtractions() {
    return this.independentExtractions.isSelected();
  }

  private void addListeners() {
    this.randomize.addItemListener(this::randomizeChanged);
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
      SplitConfigurationEventType.RANDOMIZE_SELECTION_CHANGED, isRandomize()
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
