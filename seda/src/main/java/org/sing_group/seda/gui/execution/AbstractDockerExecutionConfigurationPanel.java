/*
 * #%L
 * SEquence DAtaset builder Blast plugin
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;

public abstract class AbstractDockerExecutionConfigurationPanel<T> extends JPanel
  implements BinaryExecutionConfigurationPanel<T> {
  private static final long serialVersionUID = 1L;

  protected final JTextField dockerImage;
  private JButton checkDockerButton;

  public AbstractDockerExecutionConfigurationPanel(String defaultDockerImage, String helpTooltip) {
    this.dockerImage = new JXTextField("Docker image");
    this.dockerImage.setText(defaultDockerImage);
    this.dockerImage.setColumns(20);

    this.checkDockerButton = newJButtonBuilder().thatDoes(getCheckDockerAction()).build();
    JLabel helpLabel = new JLabel(ICON_INFO_2_16);
    helpLabel.setToolTipText(helpTooltip);

    JPanel dockerImagePanel = new JPanel(new FlowLayout());
    dockerImagePanel.add(dockerImage);
    dockerImagePanel.add(helpLabel);
    dockerImagePanel.add(checkDockerButton);

    this.setLayout(new BorderLayout());
    this.add(dockerImagePanel, BorderLayout.NORTH);
  }

  private Action getCheckDockerAction() {
    return new ExtendedAbstractAction("Check image", this::checkDockerButton);
  }

  private void checkDockerButton() {
    dockerImageChanged(new ChangeEvent(this));
  }

  private void dockerImageChanged(ChangeEvent event) {
    this.checkDockerButton.setEnabled(!this.dockerImage.getText().isEmpty());
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

  protected Component getParentForDialogs() {
    return SwingUtilities.getRootPane(this);
  }

  public String getSelectedDockerImage() {
    return this.dockerImage.getText();
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
