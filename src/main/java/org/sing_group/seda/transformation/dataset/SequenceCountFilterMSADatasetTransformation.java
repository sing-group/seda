package org.sing_group.seda.transformation.dataset;

import java.util.function.Function;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.MultipleSequenceAlignmentDataset;
import org.sing_group.seda.transformation.TransformationException;

public class SequenceCountFilterMSADatasetTransformation implements MSADatasetTransformation {
	private final Function<MultipleSequenceAlignment[], MultipleSequenceAlignmentDataset> builder;
	private final int minAligments;
	
	public SequenceCountFilterMSADatasetTransformation(int minAligments) {
		this.builder = MultipleSequenceAlignmentDataset::of;
		this.minAligments = minAligments;
	}
	
	public SequenceCountFilterMSADatasetTransformation(int minAligments, DatatypeFactory factory) {
		this.builder = factory::newMSADataset;
		this.minAligments = minAligments;
	}

	@Override
	public MultipleSequenceAlignmentDataset transform(MultipleSequenceAlignmentDataset dataset) {
		final MultipleSequenceAlignment[] alignments = dataset.getAlignments()
			.filter(alignment -> alignment.getSequenceCount() >= this.minAligments)
		.toArray(MultipleSequenceAlignment[]::new);

		if (alignments.length == 0)
			throw new TransformationException("Empty alignments after filtering");
		
		return this.builder.apply(alignments);
	}

}
