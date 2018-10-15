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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.gc4s.ui.icons.Icons.ICON_INFO_2_16;
import static org.sing_group.gc4s.utilities.builder.JButtonBuilder.newJButtonBuilder;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.util.Optional;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.seda.blast.execution.BinaryCheckException;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DockerBlastBinariesExecutor;
import org.sing_group.seda.blast.gui.event.BinaryConfigurationPanelListener;

public class DockerExecutionConfigurationPanel extends JPanel implements BinaryExecutionConfigurationPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_BLAST_OMEGA_PATH =
    "<html>The BLAST docker image.<br/> By default, the official SEDA image for BLAST is used.<br/>"
      + "If you provide a custom image, it should have the BLAST commands available in the path.</html>";

  private JTextField blastImage;
  private JButton blastPathButton;

  public DockerExecutionConfigurationPanel() {
    this.blastImage = new JXTextField("Docker image");
    this.blastImage.setText(DockerBlastBinariesExecutor.getDefaultDockerImage());
    this.blastImage.setColumns(20);

    this.blastPathButton = newJButtonBuilder().thatDoes(getCheckBlastAction()).build();
    JLabel helpLabel = new JLabel(ICON_INFO_2_16);
    helpLabel.setToolTipText(HELP_BLAST_OMEGA_PATH);

    this.setLayout(new FlowLayout());
    this.add(blastImage);
    this.add(helpLabel);
    this.add(blastPathButton);
  }

  private Action getCheckBlastAction() {
    return new ExtendedAbstractAction("Check image", this::checkBlastButton);
  }

  private void checkBlastButton() {
    blastPathChanged(new ChangeEvent(this));
  }

  private void blastPathChanged(ChangeEvent event) {
    this.blastPathButton.setEnabled(!this.blastImage.getText().isEmpty());
    invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      this.checkBlastPath();
      this.fireBlastExecutorChanged();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  private void fireBlastExecutorChanged() {
    for (BinaryConfigurationPanelListener listener : getBinaryConfigurationPanelListeners()) {
      listener.onBinariesExecutorChanged(this);
    }
  }

  private void checkBlastPath() {
    try {
      Optional<BlastBinariesExecutor> executor = getBlastBinariesExecutor();
      if (executor.isPresent()) {
        getBlastBinariesExecutor().get().checkBinary();
        showMessageDialog(
          getParentForDialogs(),
          "Blast checked successfully.",
          "Check Blast",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Blast: " + e.getCommand() + ".",
        "Error checking Blast",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  private Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  @Override
  public Optional<BlastBinariesExecutor> getBlastBinariesExecutor() {
    if (this.blastImage.getText().isEmpty()) {
      return empty();
    }

    return of(new DockerBlastBinariesExecutor(this.blastImage.getText()));
  }

  public synchronized void addBinaryConfigurationPanelListener(BinaryConfigurationPanelListener l) {
    this.listenerList.add(BinaryConfigurationPanelListener.class, l);
  }

  public synchronized BinaryConfigurationPanelListener[] getBinaryConfigurationPanelListeners() {
    return this.listenerList.getListeners(BinaryConfigurationPanelListener.class);
  }
}
