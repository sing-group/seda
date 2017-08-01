package org.sing_group.seda.transformation.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public interface SequencesGroupDatasetTransformation {
  public SequencesGroupDataset transform(SequencesGroupDataset dataset)
    throws TransformationException;
  
  public static SequencesGroupDatasetTransformation concat(Collection<SequencesGroupDatasetTransformation> transformations) {
    return concat(transformations.stream());
  }
  
  public static SequencesGroupDatasetTransformation concat(Stream<SequencesGroupDatasetTransformation> transformations) {
    return concat(transformations.toArray(SequencesGroupDatasetTransformation[]::new));
  }
  
  public static SequencesGroupDatasetTransformation concat(SequencesGroupDatasetTransformation ... transformations) {
    return dataset -> {
      SequencesGroupDataset transformedDataset = dataset;
      
      for (SequencesGroupDatasetTransformation transformation : transformations) {
        transformedDataset = transformation.transform(transformedDataset);
      }
      
      return transformedDataset;
    };
  }
  
  public static Collector<? super SequencesGroupDatasetTransformation, Collection<SequencesGroupDatasetTransformation>, SequencesGroupDatasetTransformation> toSequencesGroupDatasetTransformation() {
    return Collector.of(
      ArrayList::new,
      (accumulator, transformation) -> accumulator.add(transformation),
      (accumulator1, accumulator2) -> {
        accumulator1.addAll(accumulator2);
        return accumulator1;
      },
      SequencesGroupDatasetTransformation::concat
    );
  }
}
