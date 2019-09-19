/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.gui.execution;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.gc4s.utilities.builder.JButtonBuilder.newJButtonBuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.util.Optional;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.sapp.execution.DefaultSappBinariesExecutor;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;

public class SystemBinaryExecutionConfigurationPanel extends JPanel
  implements BinaryExecutionConfigurationPanel<SappBinariesExecutor> {

  private static final long serialVersionUID = 1L;

  private static final String SEDA_JAVA_PATH = "seda.java.path";

  private JButton checkPathButton;
  private SystemSappCommandsConfigurationPanel sappCommandsConfigurationPanel;

  public SystemBinaryExecutionConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());

    this.add(getButtonsPanel(), NORTH);
    this.add(getSappCommandsConfigurationPanel(), CENTER);
  }

  private Component getButtonsPanel() {
    this.checkPathButton = newJButtonBuilder().thatDoes(getCheckBinaryPathAction()).build();

    JPanel buttonsPanel = new JPanel(new FlowLayout());
    buttonsPanel.add(checkPathButton);

    return buttonsPanel;
  }

  private Component getSappCommandsConfigurationPanel() {
    this.sappCommandsConfigurationPanel =
      new SystemSappCommandsConfigurationPanel(System.getProperty(SEDA_JAVA_PATH, ""), "");

    return this.sappCommandsConfigurationPanel;
  }

  protected Action getCheckBinaryPathAction() {
    return new ExtendedAbstractAction("Check SAPP jars", this::checkBinaryPath);
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
    for (BinaryConfigurationPanelListener<SappBinariesExecutor> listener : getBinaryConfigurationPanelListeners()) {
      listener.onBinariesExecutorChanged(this);
    }
  }

  protected void checkBinary() {
    try {
      getBinariesExecutor().get().checkBinary();
      showMessageDialog(
        getParentForDialogs(),
        "SAPP jars checked successfully.",
        "Check SAPP jars",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking SAPP jars: " + e.getCommand() + ".",
        "Error checking SAPP jars",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  protected Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  @Override
  public Optional<SappBinariesExecutor> getBinariesExecutor() {
    return of(new DefaultSappBinariesExecutor(this.sappCommandsConfigurationPanel.sappCommands()));
  }

  public synchronized void addBinaryConfigurationPanelListener(
    BinaryConfigurationPanelListener<SappBinariesExecutor> l
  ) {
    this.listenerList.add(BinaryConfigurationPanelListener.class, l);
  }

  public synchronized BinaryConfigurationPanelListener<SappBinariesExecutor>[] getBinaryConfigurationPanelListeners() {
    @SuppressWarnings("unchecked")
    BinaryConfigurationPanelListener<SappBinariesExecutor>[] listeners =
      this.listenerList.getListeners(BinaryConfigurationPanelListener.class);

    return listeners;
  }
}
