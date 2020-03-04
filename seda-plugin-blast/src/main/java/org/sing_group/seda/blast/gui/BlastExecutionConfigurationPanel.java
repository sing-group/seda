/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DefaultBlastBinariesExecutor;
import org.sing_group.seda.blast.execution.DockerBlastBinariesExecutor;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class BlastExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String CARD_SYSTEM_BINARY = "System binary";
  private static final String CARD_DOCKER_IMAGE = "Docker image";

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION + ".blast";

  private CardsPanel blastExecutableCardsPanel;

  private DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel;
  private SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel;
  private BinaryConfigurationPanelListener<BlastBinariesExecutor> blastExecutorChanged;

  public BlastExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<BlastBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.blastExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    this.systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    this.systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.blastExecutorChanged);

    this.dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    this.dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.blastExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard(CARD_DOCKER_IMAGE, dockerExecutionConfigurationPanel)
        .withSelectedCard(CARD_DOCKER_IMAGE)
        .disableSelectionWithOneCard(true);

    if (
      !getProperty(GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
        && !getProperty(PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
    ) {
      builder = builder.withCard(CARD_SYSTEM_BINARY, systemBinaryExecutionConfigurationPanel);
    }

    this.blastExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.blastExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::blastBinaryExecutorCardChanged);
    
    this.add(this.blastExecutableCardsPanel);
  }

  private void blastBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.blastExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<BlastBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }

  public BinaryExecutionConfigurationPanel<BlastBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<BlastBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<BlastBinariesExecutor>) this.blastExecutableCardsPanel.getSelectedCard());

    return selectedCard;
  }

  public void setBinariesExecutor(BlastBinariesExecutor binariesExecutor) {
    if (binariesExecutor instanceof DockerBlastBinariesExecutor) {
      this.dockerExecutionConfigurationPanel
        .setSelectedDockerImage(((DockerBlastBinariesExecutor) binariesExecutor).getDockerImage());
      this.blastExecutableCardsPanel.setSelectedCard(CARD_DOCKER_IMAGE);
    } else if (
      binariesExecutor instanceof DefaultBlastBinariesExecutor
    ) {
      File directory = ((DefaultBlastBinariesExecutor) binariesExecutor).getBlastDirectory();
      if (directory != null) {
        this.systemBinaryExecutionConfigurationPanel.setSelectedFile(directory);
      } else {
        this.systemBinaryExecutionConfigurationPanel.clearSelectedFile();
      }
      this.blastExecutableCardsPanel.setSelectedCard(CARD_SYSTEM_BINARY);
    } else {
      throw new IllegalStateException("Unknown BlastBinariesExecutor implementation");
    }
  }
}
