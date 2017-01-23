package org.sing_group.seda.transformation.msa;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;

public class FilterByStartCodonTransformation extends FilterMSATransformation {
	public FilterByStartCodonTransformation(String[] validStartCodons) {
		this(asList(validStartCodons));
	}
	
	public FilterByStartCodonTransformation(Collection<String> validStartCodons) {
		super(buildPredicate(validStartCodons));
	}
	
	public FilterByStartCodonTransformation(String[] validStartCodons, DatatypeFactory factory) {
		this(asList(validStartCodons), factory);
	}
	
	public FilterByStartCodonTransformation(Collection<String> validStartCodons, DatatypeFactory factory) {
		super(buildPredicate(validStartCodons), factory);
	}
	
	private final static Predicate<Sequence> buildPredicate(
		Collection<String> validStartCodons
	) {
		final Set<String> validStartCodonsSet = new HashSet<>(validStartCodons);
		
		return (sequence) -> {
			final String startCodon = SequenceUtils.toCodons(sequence)
				.findFirst()
			.orElseThrow(() -> new TransformationException("Invalid sequence: " + sequence.getChain()));
			
			return validStartCodonsSet.contains(startCodon);
		};
	}
}
