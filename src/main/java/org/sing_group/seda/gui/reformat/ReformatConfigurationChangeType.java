package org.sing_group.seda.gui.reformat;

import org.sing_group.seda.plugin.spi.TransformationChangeType;

public enum ReformatConfigurationChangeType implements TransformationChangeType {
  REMOVE_LINE_BREAKS_CHANGED, 
  FRAGMENT_LENGTH_CHANGED, 
  LINE_BREAK_TYPE_CHANGED,
  SEQUENCE_CASE_CHANGED;
}
