package org.sing_group.seda.gui.concatenate;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationModel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ConcatenateSequencesTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaConfigurationModel reformatModel;
  private ConcatenateSequencesConfigurationPanel concatenateSequencesModel;

  public ConcatenateSequencesTransformationProvider(
    ConcatenateSequencesConfigurationPanel concatenateSequencesModel,
    ReformatFastaConfigurationModel reformatModel
  ) {
    this.concatenateSequencesModel = concatenateSequencesModel;
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
    return reformatModel.isValidTransformation()
      && this.concatenateSequencesModel.isValidConfiguration();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ConcatenateSequencesGroupDatasetTransformation(factory, getMergeName(), getHeaderTarget()),
      this.reformatModel.getTransformation(factory)
    );
  }

  private HeaderTarget getHeaderTarget() {
    return this.concatenateSequencesModel.getHeaderTarget();
  }

  private String getMergeName() {
    return this.concatenateSequencesModel.getMergeName();
  }

  public void headerTargetChanged() {
    fireTransformationsConfigurationModelEvent(
      ConcatenateSequencesTransformationChangeType.HEADER_TARGET_CHANGED,
      concatenateSequencesModel.getHeaderTarget()
    );
  }

  public void nameChanged() {
    fireTransformationsConfigurationModelEvent(
      ConcatenateSequencesTransformationChangeType.MERGE_NAME_CHANGED,
      concatenateSequencesModel.getMergeName()
    );
  }
}
