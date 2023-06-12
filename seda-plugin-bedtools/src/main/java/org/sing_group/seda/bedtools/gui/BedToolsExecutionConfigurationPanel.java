/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.gui;

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;
import static org.sing_group.seda.bedtools.core.BedToolsSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.DefaultBedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.DockerBedToolsBinariesExecutor;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.util.SedaProperties;

public class BedToolsExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String CARD_SYSTEM_BINARY = "System binary";
  private static final String CARD_DOCKER_IMAGE = "Docker image";

  private CardsPanel bedToolsExecutableCardsPanel;

  private DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel;
  private SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel;
  private BinaryConfigurationPanelListener<BedToolsBinariesExecutor> bedToolsExecutorChanged;

  public BedToolsExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<BedToolsBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.bedToolsExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    this.systemBinaryExecutionConfigurationPanel = new SystemBinaryExecutionConfigurationPanel();
    this.systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.bedToolsExecutorChanged);

    this.dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    this.dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.bedToolsExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard(CARD_DOCKER_IMAGE, dockerExecutionConfigurationPanel)
        .withSelectedCard(CARD_DOCKER_IMAGE)
        .disableSelectionWithOneCard(true);

    if (
      !getProperty(SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
        && !getProperty(PROPERTY_ENABLE_LOCAL_EXECUTION_BEDTOOLS, "true").equals("false")
    ) {
      builder = builder.withCard(CARD_SYSTEM_BINARY, systemBinaryExecutionConfigurationPanel);
    }

    this.bedToolsExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.bedToolsExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::bedToolsBinaryExecutorCardChanged);
    
    this.add(this.bedToolsExecutableCardsPanel);
  }

  private void bedToolsBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.bedToolsExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<BedToolsBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }
  
  public BinaryExecutionConfigurationPanel<BedToolsBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<BedToolsBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<BedToolsBinariesExecutor>) this.bedToolsExecutableCardsPanel.getSelectedCard());
    
    return selectedCard;
  }

  public void setBinariesExecutor(BedToolsBinariesExecutor binariesExecutor) {
    if (binariesExecutor instanceof DockerBedToolsBinariesExecutor) {
      this.dockerExecutionConfigurationPanel
        .setSelectedDockerImage(((DockerBedToolsBinariesExecutor) binariesExecutor).getDockerImage());
      this.bedToolsExecutableCardsPanel.setSelectedCard(CARD_DOCKER_IMAGE);
    } else if (
      binariesExecutor instanceof DefaultBedToolsBinariesExecutor
    ) {
      File directory = ((DefaultBedToolsBinariesExecutor) binariesExecutor).getBedToolsBinary();
      if (directory != null) {
        this.systemBinaryExecutionConfigurationPanel.setSelectedFile(directory);
      } else {
        this.systemBinaryExecutionConfigurationPanel.clearSelectedFile();
      }
      this.bedToolsExecutableCardsPanel.setSelectedCard(CARD_SYSTEM_BINARY);
    } else {
      throw new IllegalStateException("Unknown BedToolsBinariesExecutor implementation");
    }
  }
}
