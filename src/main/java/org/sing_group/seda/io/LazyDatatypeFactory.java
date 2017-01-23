package org.sing_group.seda.io;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.DefaultDatatypeFactory;
import org.sing_group.seda.datatype.MultipleSequenceAlignment;
import org.sing_group.seda.datatype.MultipleSequenceAlignmentDataset;
import org.sing_group.seda.datatype.Sequence;

public class LazyDatatypeFactory implements DatatypeFactory {
	private final DatatypeFactory defaultFactory = new DefaultDatatypeFactory();

	@Override
	public Sequence newSequence(String name, String sequence) {
		return this.defaultFactory.newSequence(name, sequence);
	}

	@Override
	public MultipleSequenceAlignment newMSA(String name, Sequence... sequences) {
		return new LazyFileMultipleSequenceAlignment(name, sequences);
	}

	@Override
	public MultipleSequenceAlignmentDataset newMSADataset(MultipleSequenceAlignment... alignments) {
		return this.defaultFactory.newMSADataset(alignments);
	}

}
