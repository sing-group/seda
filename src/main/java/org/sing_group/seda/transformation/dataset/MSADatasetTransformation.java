package org.sing_group.seda.transformation.dataset;

import java.util.Collection;

import org.sing_group.seda.datatype.MultipleSequenceAlignmentDataset;
import org.sing_group.seda.transformation.TransformationException;

public interface MSADatasetTransformation {
	
	public MultipleSequenceAlignmentDataset transform(MultipleSequenceAlignmentDataset dataset)
		throws TransformationException;
	
	public static MSADatasetTransformation concat(Collection<MSADatasetTransformation> transformations) {
		return concat(transformations.stream().toArray(MSADatasetTransformation[]::new));
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
}
