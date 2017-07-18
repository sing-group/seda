package org.sing_group.seda.plugin.spi;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.DefaultDatatypeFactory;
import org.sing_group.seda.transformation.dataset.MSADatasetTransformation;

public interface TransformationProvider {
  public default MSADatasetTransformation getTransformation() {
    return this.getTransformation(new DefaultDatatypeFactory());
  }
  
  public MSADatasetTransformation getTransformation(DatatypeFactory factory);
  
  public boolean addTransformationChangeListener(TransformationChangeListener listener);
  
  public boolean removeTranformationChangeListener(TransformationChangeListener listener);
  
  public boolean containsTransformationChangeListener(TransformationChangeListener listener);
}
