package org.sing_group.seda.gui.execution;

import java.util.Optional;

public interface BinaryExecutionConfigurationPanel<T> {
  Optional<T> getBinariesExecutor();

  /**
   * Adds the specified binary configuration panel listener to receive component events from this component. If listener
   * {@code l} is {@code null}, no exception is thrown and no action is performed.
   *
   * @param l the {@code BinaryConfigurationPanelListener}
   */
  void addBinaryConfigurationPanelListener(BinaryConfigurationPanelListener<T> l);

  /**
   * Returns an array of all the binary configuration panel listeners registered on this component.
   *
   * @return all {@code BinaryConfigurationPanelListener}s of this component or an empty array if no component listeners
   *         are currently registered
   */
  BinaryConfigurationPanelListener<T>[] getBinaryConfigurationPanelListeners();
}
