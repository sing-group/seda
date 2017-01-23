package org.sing_group.seda.transformation.msa;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;

public abstract class FilterMSATransformation implements MultipleSequenceAlignmentTransformation {
	private final BiFunction<String, Sequence[], MultipleSequenceAlignment> builder;
	private final BiPredicate<MultipleSequenceAlignment, Sequence> filter;

	public FilterMSATransformation(Predicate<Sequence> filter) {
		this.builder = MultipleSequenceAlignment::of;
		this.filter = (msa, sequence) -> filter.test(sequence);
	}

	public FilterMSATransformation(BiPredicate<MultipleSequenceAlignment, Sequence> filter) {
		this.builder = MultipleSequenceAlignment::of;
		this.filter = filter;
	}
	
	public FilterMSATransformation(Predicate<Sequence> filter, DatatypeFactory factory) {
		this.builder = factory::newMSA;
		this.filter = (msa, sequence) -> filter.test(sequence);
	}
	
	public FilterMSATransformation(BiPredicate<MultipleSequenceAlignment, Sequence> filter, DatatypeFactory factory) {
		this.builder = factory::newMSA;
		this.filter = filter;
	}

	@Override
	public MultipleSequenceAlignment transform(MultipleSequenceAlignment msa) {
		try {
			final Sequence[] sequences = msa.getSequences()
				.filter(sequence -> filter.test(msa, sequence))
			.toArray(Sequence[]::new);

			if (sequences.length == 0)
				throw new TransformationException("Empty sequences after filtering");

			return this.builder.apply(msa.getName(), sequences);
		} catch (RuntimeException e) {
			if (e instanceof TransformationException)
				throw e;
			else
				throw new TransformationException("Unexpected error during transformation", e);
		}
	}

}
