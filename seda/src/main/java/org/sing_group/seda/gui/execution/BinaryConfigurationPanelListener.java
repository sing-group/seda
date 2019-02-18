package org.sing_group.seda.gui.execution;

import java.util.EventListener;

public interface BinaryConfigurationPanelListener<T> extends EventListener {
  void onBinariesExecutorChanged(BinaryExecutionConfigurationPanel<T> source);
}
