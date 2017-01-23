package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;

import java.io.Serializable;
import java.util.stream.Stream;

public class DefaultMultipleSequenceAlignment implements MultipleSequenceAlignment, Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final Sequence[] sequences;

	public DefaultMultipleSequenceAlignment(String name, Sequence ... sequences) {
		this.name = name;
		this.sequences = sequences;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Stream<Sequence> getSequences() {
		return stream(this.sequences);
	}

	@Override
	public int getSequenceCount() {
		return this.sequences.length;
	}

}
