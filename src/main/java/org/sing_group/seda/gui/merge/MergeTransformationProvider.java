package org.sing_group.seda.gui.merge;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationModel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.MergeSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class MergeTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaConfigurationModel reformatModel;
  private String name = null;

  public MergeTransformationProvider(ReformatFastaConfigurationModel reformatModel) {
    this.reformatModel = reformatModel;
    this.reformatModel.addTransformationChangeListener(
      new TransformationChangeListener() {

        @Override
        public void onTransformationChange(TransformationChangeEvent event) {
          fireTransformationsConfigurationModelEvent(event.getType(), event.getNewValue());
        }
      }
    );
  }

  @Override
  public boolean isValidTransformation() {
    return this.name != null && reformatModel.isValidTransformation();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new MergeSequencesGroupDatasetTransformation(getName()),
      this.reformatModel.getTransformation(factory)
    );
  }

  public void setName(String name) {
    this.name = name;
    this.fireTransformationsConfigurationModelEvent(MergeTransformationChangeType.NAME_CHANGED, getName());
  }

  private String getName() {
    return this.name;
  }
}
