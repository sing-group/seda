package org.sing_group.seda.transformation.dataset;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.MultipleSequenceAlignmentDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.msa.MultipleSequenceAlignmentTransformation;

public class ComposedMSADatasetTransformation implements MSADatasetTransformation {
	private final Function<MultipleSequenceAlignment[], MultipleSequenceAlignmentDataset> builder;
	private final MultipleSequenceAlignmentTransformation[] transformations;
	
	public ComposedMSADatasetTransformation(MultipleSequenceAlignmentTransformation ... transformations) {
		this.builder = MultipleSequenceAlignmentDataset::of;
		this.transformations = transformations;
	}
	
	public ComposedMSADatasetTransformation(DatatypeFactory factory, MultipleSequenceAlignmentTransformation ... transformations) {
		this.builder = factory::newMSADataset;
		this.transformations = transformations;
	}
	
	public ComposedMSADatasetTransformation(Collection<MultipleSequenceAlignmentTransformation> transformations) {
		this(transformations.stream().toArray(MultipleSequenceAlignmentTransformation[]::new));
	}
	
	public ComposedMSADatasetTransformation(DatatypeFactory factory, Collection<MultipleSequenceAlignmentTransformation> transformations) {
		this(factory, transformations.stream().toArray(MultipleSequenceAlignmentTransformation[]::new));
	}

	@Override
	public MultipleSequenceAlignmentDataset transform(MultipleSequenceAlignmentDataset dataset) throws TransformationException {
		Stream<MultipleSequenceAlignment> alignments = dataset.getAlignments();
		
		for (MultipleSequenceAlignmentTransformation transformation : this.transformations) {
			alignments = alignments.map(transformation::transform);
		}
		
		return this.builder.apply(alignments.toArray(MultipleSequenceAlignment[]::new));
	}

}
