package org.sing_group.seda.gui.sort;

import javax.swing.JCheckBox;

import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupSortTransformation;

public class SortConfigurationModel extends AbstractTransformationProvider {

  private RadioButtonsPanel<SequenceTarget> sequenceTarget;
  private RadioButtonsPanel<SequenceComparator> sortCriteria;
  private JCheckBox descendingSort;

  public SortConfigurationModel(
    RadioButtonsPanel<SequenceTarget> sequenceTarget, JCheckBox descendingSort,
    RadioButtonsPanel<SequenceComparator> sortCriteria
  ) {
    this.sequenceTarget = sequenceTarget;
    this.descendingSort = descendingSort;
    this.sortCriteria = sortCriteria;
  }

  @Override
  public boolean isValidTransformation() {
    return true;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
        new ComposedSequencesGroupDatasetTransformation(factory,
          new SequencesGroupSortTransformation(
            sortCriteria.getSelectedItem().get().getComparator(this.sequenceTarget.getSelectedItem().get()),
            this.descendingSort.isSelected()
          )
        )
      );
  }
}
