package org.sing_group.seda.gui.transformation;

import org.sing_group.seda.plugin.spi.TransformationChangeType;

public enum TransformationConfigurationEventType implements TransformationChangeType {
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