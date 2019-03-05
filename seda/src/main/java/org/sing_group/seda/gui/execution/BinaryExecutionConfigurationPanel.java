/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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