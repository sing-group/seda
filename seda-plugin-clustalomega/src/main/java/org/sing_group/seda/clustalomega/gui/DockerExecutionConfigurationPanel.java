/*
 * #%L
 * SEquence DAtaset builder Clustal Omega plugin
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
package org.sing_group.seda.clustalomega.gui;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.gc4s.ui.icons.Icons.ICON_INFO_2_16;
import static org.sing_group.gc4s.utilities.builder.JButtonBuilder.newJButtonBuilder;
import static org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor.getDefaultDockerImage;

import java.awt.Component;
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
import org.sing_group.seda.clustalomega.execution.BinaryCheckException;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.gui.event.BinaryConfigurationPanelListener;

public class DockerExecutionConfigurationPanel extends JPanel implements BinaryExecutionConfigurationPanel {
  private static final long serialVersionUID = 1L;
  
  private static final String HELP_CLUSTAL_OMEGA_PATH =
    "<html>The Clustal Omega docker image.<br/> By default, the official SEDA image for Clustal Omega is used.<br/>"
    + "If you provide a custom image, you may also need to specify the Clustal Omega executable command inside "
    + "the image, in case it is not defined as ENTRYPOINT.</html>";

  private JTextField clustalOmegaImage;
  private JButton clustalOmegaPathButton;

  public DockerExecutionConfigurationPanel() {
    this.clustalOmegaImage = new JXTextField("Docker image");
    this.clustalOmegaImage.setText(getDefaultDockerImage());
    this.clustalOmegaImage.setColumns(20);

    this.clustalOmegaPathButton = newJButtonBuilder().thatDoes(getCheckClustalOmegaAction()).build();
    JLabel helpLabel = new JLabel(ICON_INFO_2_16);
    helpLabel.setToolTipText(HELP_CLUSTAL_OMEGA_PATH);

    this.setLayout(new FlowLayout());
    this.add(clustalOmegaImage);
    this.add(helpLabel);
    this.add(clustalOmegaPathButton);
  }

  private Action getCheckClustalOmegaAction() {
    return new ExtendedAbstractAction("Check image", this::checkClustalOmegaButton);
  }

  private void checkClustalOmegaButton() {
    clustalOmegaPathChanged(new ChangeEvent(this));
  }

  private void clustalOmegaPathChanged(ChangeEvent event) {
    this.clustalOmegaPathButton.setEnabled(!this.clustalOmegaImage.getText().isEmpty());
    checkClustalOmegaPath();
    this.fireClustalOmegaExecutorChanged();
  }

  private void fireClustalOmegaExecutorChanged() {
    for (BinaryConfigurationPanelListener listener : getBinaryConfigurationPanelListeners()) {
      listener.onBinariesExecutorChanged(this);
    }
  }

  private void checkClustalOmegaPath() {
    try {
      Optional<ClustalOmegaBinariesExecutor> executor = getClustalOmegaBinariesExecutor();
      if (executor.isPresent()) {
        getClustalOmegaBinariesExecutor().get().checkBinary();
        showMessageDialog(
          getParentForDialogs(),
          "Clustal Omega checked successfully.",
          "Check Clustal Omega",
          JOptionPane.INFORMATION_MESSAGE
        );
      }
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Clustal Omega: " + e.getCommand() + ".",
        "Error checking Clustal Omega",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  private Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  @Override
  public Optional<ClustalOmegaBinariesExecutor> getClustalOmegaBinariesExecutor() {
    if (this.clustalOmegaImage.getText().isEmpty()) {
      return empty();
    }

    return of(new DockerClustalOmegaBinariesExecutor(this.clustalOmegaImage.getText()));
  }

  public synchronized void addBinaryConfigurationPanelListener(BinaryConfigurationPanelListener l) {
    this.listenerList.add(BinaryConfigurationPanelListener.class, l);
  }

  public synchronized BinaryConfigurationPanelListener[] getBinaryConfigurationPanelListeners() {
    return this.listenerList.getListeners(BinaryConfigurationPanelListener.class);
  }
}
