package org.sing_group.seda.gui.pattern.reallocate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.gui.pattern.PatternFilteringConfigurationPanel;
import org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation.SequenceTranslationConfiguration;
import org.sing_group.seda.transformation.sequencesgroup.ReallocateReferenceSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class ReallocateReferenceSequencesTransformationProvider extends PatternFilteringTransformationProvider {

  public ReallocateReferenceSequencesTransformationProvider(
    PatternFilteringConfigurationPanel patternFilteringConfigurationPanel
  ) {
    super(patternFilteringConfigurationPanel);
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation transformation;
    EvaluableSequencePattern pattern = getEvaluableSequencePattern();
    if (isTranslationSelected()) {
      SequenceTranslationConfiguration configuration =
        new SequenceTranslationConfiguration(
          getCodonTable(), getTranslationFrames()
        );
      transformation = new ReallocateReferenceSequencesTransformation(pattern, configuration, factory);
    } else {
      transformation = new ReallocateReferenceSequencesTransformation(pattern, getSelectedSequenceTarget(), factory);
    }

    SequencesGroupDatasetTransformation datasetTransformation =
      new ComposedSequencesGroupDatasetTransformation(factory, transformation);

    return datasetTransformation;
  }
}
