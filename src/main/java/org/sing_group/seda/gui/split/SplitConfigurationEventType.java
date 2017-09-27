package org.sing_group.seda.gui.split;

import org.sing_group.seda.plugin.spi.TransformationChangeType;

public enum SplitConfigurationEventType implements TransformationChangeType {
  RANDOMIZE_SELECTION_CHANGED,
  SPLIT_MODE_CHANGED,
  NUMBER_OF_FILES_CHANGED,
  NUMBER_OF_SEQUENCES_CHANGED;
}
