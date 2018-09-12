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

import static java.util.Optional.of;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.sing_group.gc4s.ui.icons.Icons.ICON_INFO_2_16;
import static org.sing_group.gc4s.utilities.builder.JButtonBuilder.newJButtonBuilder;
import static org.sing_group.seda.clustalomega.execution.DefaultClustalOmegaBinariesExecutor.getClustalOmegaBinaryFileName;

import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.filechooser.SelectionMode;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.seda.clustalomega.execution.BinaryCheckException;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DefaultClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.gui.event.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.CommonFileChooser;

public class SystemBinaryExecutionConfigurationPanel extends JPanel implements BinaryExecutionConfigurationPanel {
  private static final long serialVersionUID = 1L;
  
  private static final String HELP_CLUSTAL_OMEGA_PATH = "<html>The Clustal Omega binary file.<br/> If the Clustal "
    + "Omega binary is in the path (<b>clustalo</b> in Unix systems and <b>clustalo.exe</b> in Windows systems), then "
    + "this can be empty and the <i>Check binary</i> would say that it is right.</html>";
  
  private JFileChooserPanel clustalOmegaPath;
  private JButton clustalOmegaPathButton;

  public SystemBinaryExecutionConfigurationPanel() {
    this.clustalOmegaPath =
      JFileChooserPanelBuilder
        .createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withFileChooserSelectionMode(SelectionMode.FILES)
        .withLabel("Custal Omega executable path: ")
        .build();

    this.clustalOmegaPath.addFileChooserListener(this::clustalOmegaPathChanged);

    this.clustalOmegaPathButton = newJButtonBuilder().thatDoes(getCheckClustalOmegaAction()).build();
    JLabel helpLabel = new JLabel(ICON_INFO_2_16);
    helpLabel.setToolTipText(HELP_CLUSTAL_OMEGA_PATH);

    this.setLayout(new FlowLayout());
    this.add(clustalOmegaPath);
    this.add(helpLabel);
    this.add(clustalOmegaPathButton);
  }

  private Action getCheckClustalOmegaAction() {
    return new ExtendedAbstractAction("Check binary", this::checkClustalOmegaButton);
  }

  private void checkClustalOmegaButton() {
    this.clustalOmegaPathChanged(new ChangeEvent(this));
  }

  private void clustalOmegaPathChanged(ChangeEvent event) {
    this.checkClustalOmegaPath();
    this.fireClustalOmegaExecutorChanged();
  }

  private void fireClustalOmegaExecutorChanged() {
    for (BinaryConfigurationPanelListener listener : getBinaryConfigurationPanelListeners()) {
      listener.onBinariesExecutorChanged(this);
    }
  }

  private void checkClustalOmegaPath() {
    try {
      getClustalOmegaBinariesExecutor().get().checkBinary();
      showMessageDialog(
        getParentForDialogs(),
        "Clustal Omega checked successfully.",
        "Check Clustal Omega",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (BinaryCheckException e) {
      showMessageDialog(
        getParentForDialogs(),
        "Error checking Clustal Omega: " + e.getCommand() + ".",
        "Error checking Clustal Omega",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  private File getClustalOmegaPath() {
    if (this.clustalOmegaPath.getSelectedFile() != null) {
      return this.clustalOmegaPath.getSelectedFile();
    } else {
      return Paths.get(getClustalOmegaBinaryFileName()).toFile();
    }
  }

  private Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  public Optional<ClustalOmegaBinariesExecutor> getClustalOmegaBinariesExecutor() {
    return of(new DefaultClustalOmegaBinariesExecutor(getClustalOmegaPath()));
  }

  public synchronized void addBinaryConfigurationPanelListener(BinaryConfigurationPanelListener l) {
    this.listenerList.add(BinaryConfigurationPanelListener.class, l);
  }

  public synchronized BinaryConfigurationPanelListener[] getBinaryConfigurationPanelListeners() {
    return this.listenerList.getListeners(BinaryConfigurationPanelListener.class);
  }
}
