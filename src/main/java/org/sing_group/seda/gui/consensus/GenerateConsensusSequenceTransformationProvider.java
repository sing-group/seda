package org.sing_group.seda.gui.consensus;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationModel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.GenerateConsensusSequencesGroupTransformation;

public class GenerateConsensusSequenceTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaConfigurationModel reformatModel;
  private GenerateConsensusSequenceConfigurationPanel generateConsensusSequenceConfigurationPanel;

  public GenerateConsensusSequenceTransformationProvider(
    GenerateConsensusSequenceConfigurationPanel generateConsensusSequenceConfigurationPanel,
    ReformatFastaConfigurationModel reformatModel
  ) {
    this.generateConsensusSequenceConfigurationPanel = generateConsensusSequenceConfigurationPanel;
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
      && this.generateConsensusSequenceConfigurationPanel.isValidConfiguration();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        new GenerateConsensusSequencesGroupTransformation(
          factory,
          generateConsensusSequenceConfigurationPanel.getSequenceType(),
          generateConsensusSequenceConfigurationPanel.getMinimumPresence(),
          generateConsensusSequenceConfigurationPanel.isVerbose()
        )
      ),
      this.reformatModel.getTransformation(factory)
    );
  }

  public void sequenceTypeChanged() {
    fireTransformationsConfigurationModelEvent(
      GenerateConsensusSequenceTransformationChangeType.SEQUENCE_TYPE_CHANGED,
      generateConsensusSequenceConfigurationPanel.getSequenceType()
    );
  }

  public void minimumPresenceChanged() {
    fireTransformationsConfigurationModelEvent(
      GenerateConsensusSequenceTransformationChangeType.MINIMUM_PRESENCE_CHANGED,
      generateConsensusSequenceConfigurationPanel.getMinimumPresence()
    );
  }

  public void verboseChanged() {
    fireTransformationsConfigurationModelEvent(
      GenerateConsensusSequenceTransformationChangeType.VERBOSE_CHANGED,
      generateConsensusSequenceConfigurationPanel.isVerbose()
    );
  }
}
