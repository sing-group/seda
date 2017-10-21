package org.sing_group.seda.gui.grow;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.GrowSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class GrowSequencesConfigurationModel extends AbstractTransformationProvider {

  private int minimumOverlapping = 500;

  public int getMinimumOverlapping() {
    return minimumOverlapping;
  }

  public void setMinimumOverlapping(int minimumOverlapping) {
    if (this.minimumOverlapping != minimumOverlapping) {
      this.minimumOverlapping = minimumOverlapping;
      fireTransformationsConfigurationModelEvent(
        GrowSequencesConfigurationChangeType.MINIMUM_OVERLAPPING_CHANGED, this.minimumOverlapping
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
      new GrowSequencesGroupTransformation(factory, this.minimumOverlapping);

    return new ComposedSequencesGroupDatasetTransformation(groupTransformation);
  }
}
