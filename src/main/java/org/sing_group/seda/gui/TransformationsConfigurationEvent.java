package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;

public class TransformationsConfigurationEvent {
	public static enum TransformationConfigurationEventType {
		STARTING_CODON_ADDED,
		STARTING_CODON_REMOVED,
		REMOVE_STOP_CODONS_CHANGED,
		REMOVE_NON_MULTIPLE_OF_THREE_CHANGED,
		REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED,
		REMOVE_BY_SIZE_DIFFERENCE_CHANGED,
		SIZE_DIFFERENCE_CHANGED,
		REFERENCE_INDEX_CHANGED,
		MIN_NUM_OF_SEQUENCES_CHANGED;
	}

	private final TransformationConfigurationEventType type;
	private final Object newValue;
	
	public static TransformationsConfigurationEvent of(TransformationConfigurationEventType type, Object value) {
		return new TransformationsConfigurationEvent(type, value);
	}
	
	protected TransformationsConfigurationEvent(TransformationConfigurationEventType type, Object value) {
		this.type = requireNonNull(type);
		this.newValue = requireNonNull(value);
	}
	
	public TransformationConfigurationEventType getType() {
		return type;
	}

	public Object getNewValue() {
		return newValue;
	}
}
