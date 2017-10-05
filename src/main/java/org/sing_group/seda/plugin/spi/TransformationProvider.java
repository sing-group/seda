package org.sing_group.seda.plugin.spi;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public interface TransformationProvider {
  public boolean isValidTransformation();

  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory);

  public boolean addTransformationChangeListener(TransformationChangeListener listener);

  public boolean removeTranformationChangeListener(TransformationChangeListener listener);

  public boolean containsTransformationChangeListener(TransformationChangeListener listener);
}
