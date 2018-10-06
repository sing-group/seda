/*
 * #%L
 * SEquence DAtaset builder Blast plugin
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.blast.gui;

import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.seda.blast.execution.BinaryCheckException;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DefaultBlastBinariesExecutor;
import org.sing_group.seda.blast.gui.event.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.CommonFileChooser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.io.File;
import java.util.Optional;

import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.gc4s.ui.icons.Icons.ICON_INFO_2_16;
import static org.sing_group.gc4s.utilities.builder.JButtonBuilder.newJButtonBuilder;

public class SystemBinaryExecutionConfigurationPanel extends JPanel implements BinaryExecutionConfigurationPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_CLUSTAL_OMEGA_PATH = "The directory that contains the blast binaries." +
    "Leave it empty if they are in the path.";

  private JFileChooserPanel blastPath;
  private JButton blastPathButton;

  public SystemBinaryExecutionConfigurationPanel() {
    this.blastPath =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.FILES)
        .withLabel("Blast executable path: ")
        .build();

    this.blastPath.addFileChooserListener(this::blastPathChanged);

    this.blastPathButton = newJButtonBuilder().thatDoes(getCheckBlastAction()).build();
    JLabel helpLabel = new JLabel(ICON_INFO_2_16);
    helpLabel.setToolTipText(HELP_CLUSTAL_OMEGA_PATH);

    this.setLayout(new FlowLayout());
    this.add(blastPath);
    this.add(helpLabel);
    this.add(blastPathButton);
  }

  private Action getCheckBlastAction() {
    return new ExtendedAbstractAction("Check binary", this::checkBlastButton);
  }

  private void checkBlastButton() {
    this.blastPathChanged(new ChangeEvent(this));
  }

  private void blastPathChanged(ChangeEvent event) {
    this.checkBlastPath();
    this.fireBlastExecutorChanged();
  }

  private void fireBlastExecutorChanged() {
    for (BinaryConfigurationPanelListener listener : getBinaryConfigurationPanelListeners()) {
      listener.onBinariesExecutorChanged(this);
    }
  }

  private void checkBlastPath() {
    try {
      getBlastBinariesExecutor().get().checkBinary();
      showMessageDialog(
        getParentForDialogs(),
        "Blast checked successfully.",
        "Check Blast",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Blast: " + e.getCommand() + ".",
        "Error checking Blast",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  private File getBlastPath() {
    if (this.blastPath.getSelectedFile() != null) {
      return this.blastPath.getSelectedFile();
    } else {
      return null;
    }
  }

  private Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  public Optional<BlastBinariesExecutor> getBlastBinariesExecutor() {
    return of(new DefaultBlastBinariesExecutor(getBlastPath()));
  }

  public synchronized void addBinaryConfigurationPanelListener(BinaryConfigurationPanelListener l) {
    this.listenerList.add(BinaryConfigurationPanelListener.class, l);
  }

  public synchronized BinaryConfigurationPanelListener[] getBinaryConfigurationPanelListeners() {
    return this.listenerList.getListeners(BinaryConfigurationPanelListener.class);
  }
}
