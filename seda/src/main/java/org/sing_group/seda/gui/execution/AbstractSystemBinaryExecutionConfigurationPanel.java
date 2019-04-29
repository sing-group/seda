/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.gc4s.ui.icons.Icons.ICON_INFO_2_16;
import static org.sing_group.gc4s.utilities.builder.JButtonBuilder.newJButtonBuilder;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.seda.gui.CommonFileChooser;

public abstract class AbstractSystemBinaryExecutionConfigurationPanel<T> extends JPanel
  implements BinaryExecutionConfigurationPanel<T> {
  private static final long serialVersionUID = 1L;

  private JFileChooserPanel binaryPath;
  private JButton checkPathButton;

  public AbstractSystemBinaryExecutionConfigurationPanel(
    SelectionMode selectionMode, String selectionLabel, String helpTooltip
  ) {
    this.binaryPath =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(selectionMode)
        .withLabel(selectionLabel)
        .build();

    this.binaryPath.addFileChooserListener(this::binaryPathChanged);

    this.checkPathButton = newJButtonBuilder().thatDoes(getCheckBinaryPathAction()).build();
    JLabel helpLabel = new JLabel(ICON_INFO_2_16);
    helpLabel.setToolTipText(helpTooltip);

    this.setLayout(new FlowLayout());
    this.add(binaryPath);
    this.add(helpLabel);
    this.add(checkPathButton);
  }

  protected Action getCheckBinaryPathAction() {
    return new ExtendedAbstractAction("Check binary", this::checkBinaryPath);
  }

  private void checkBinaryPath() {
    this.binaryPathChanged(new ChangeEvent(this));
  }

  private void binaryPathChanged(ChangeEvent event) {
    invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      this.checkBinary();
      this.fireBinariesExecutorChanged();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  private void fireBinariesExecutorChanged() {
    for (BinaryConfigurationPanelListener<T> listener : getBinaryConfigurationPanelListeners()) {
      listener.onBinariesExecutorChanged(this);
    }
  }

  protected abstract void checkBinary();

  protected File getSelectedBinaryPath() {
    if (this.binaryPath.getSelectedFile() != null) {
      return this.binaryPath.getSelectedFile();
    } else {
      return null;
    }
  }

  protected Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  public synchronized void addBinaryConfigurationPanelListener(BinaryConfigurationPanelListener<T> l) {
    this.listenerList.add(BinaryConfigurationPanelListener.class, l);
  }

  public synchronized BinaryConfigurationPanelListener<T>[] getBinaryConfigurationPanelListeners() {
    @SuppressWarnings("unchecked")
    BinaryConfigurationPanelListener<T>[] listeners =
      this.listenerList.getListeners(BinaryConfigurationPanelListener.class);

    return listeners;
  }
}
