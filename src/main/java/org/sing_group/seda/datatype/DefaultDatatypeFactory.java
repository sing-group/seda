package org.sing_group.seda.datatype;

public class DefaultDatatypeFactory implements DatatypeFactory {

	@Override
	public Sequence newSequence(String name, String sequence) {
		return new DefaultSequence(name, sequence);
	}

	@Override
	public MultipleSequenceAlignment newMSA(String name, Sequence... sequences) {
		return new DefaultMultipleSequenceAlignment(name, sequences);
	}

	@Override
	public MultipleSequenceAlignmentDataset newMSADataset(MultipleSequenceAlignment... alignments) {
		return new DefaultMultipleSequenceAlignmentDataset(alignments);
	}

}
