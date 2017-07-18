package org.sing_group.seda.transformation.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.MultipleSequenceAlignmentDataset;
import org.sing_group.seda.transformation.TransformationException;

public interface MSADatasetTransformation {
	public MultipleSequenceAlignmentDataset transform(MultipleSequenceAlignmentDataset dataset)
		throws TransformationException;
	
	public static MSADatasetTransformation concat(Collection<MSADatasetTransformation> transformations) {
		return concat(transformations.stream());
	}
	
	public static MSADatasetTransformation concat(Stream<MSADatasetTransformation> transformations) {
	  return concat(transformations.toArray(MSADatasetTransformation[]::new));
	}
	
	public static MSADatasetTransformation concat(MSADatasetTransformation ... transformations) {
		return dataset -> {
			MultipleSequenceAlignmentDataset transformedDataset = dataset;
			
			for (MSADatasetTransformation transformation : transformations) {
				transformedDataset = transformation.transform(transformedDataset);
			}
			
			return transformedDataset;
		};
	}
	
	public static Collector<? super MSADatasetTransformation, Collection<MSADatasetTransformation>, MSADatasetTransformation> toMSADatasetTransformation() {
	  return Collector.of(
	    ArrayList::new,
	    (accumulator, transformation) -> accumulator.add(transformation),
	    (accumulator1, accumulator2) -> {
	      accumulator1.addAll(accumulator2);
	      return accumulator1;
	    },
	    MSADatasetTransformation::concat
	  );
	}
}
