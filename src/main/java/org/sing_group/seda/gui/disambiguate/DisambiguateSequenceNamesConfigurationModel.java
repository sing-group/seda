package org.sing_group.seda.gui.disambiguate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class DisambiguateSequenceNamesConfigurationModel extends AbstractTransformationProvider {
  private DisambiguateSequenceNamesTransformation.Mode mode;

  public DisambiguateSequenceNamesTransformation.Mode getMode() {
    return mode;
  }

  public void setMode(DisambiguateSequenceNamesTransformation.Mode mode) {
    if (!mode.equals(this.mode)) {
      this.mode = mode;
      fireTransformationsConfigurationModelEvent(
        DisambiguateSequenceNamesChangeType.MODE_CHANGED, this.mode
      );
    }
  }

  @Override
  public boolean isValidTransformation() {
    return true;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation groupTransformation =
      new DisambiguateSequenceNamesTransformation(mode, factory);

    return new ComposedSequencesGroupDatasetTransformation(groupTransformation);
  }
}
