/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.gui;

import static java.lang.System.getProperty;
import static javax.swing.BorderFactory.createTitledBorder;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;
import static org.sing_group.seda.prosplign.plugin.core.ProSplignCompartPipelineSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_PROSPLIGN_PROCOMPART;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.prosplign.execution.DefaultProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.execution.DockerProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;
import org.sing_group.seda.util.SedaProperties;

public class ProSplignCompartExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String CARD_SYSTEM_BINARY = "System binary";
  private static final String CARD_DOCKER_IMAGE = "Docker image";

  private CardsPanel proSplignCompartExecutableCardsPanel;
  private BinaryConfigurationPanelListener<ProSplignCompartBinariesExecutor> proSplignCompartExecutorChanged;

  private SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel;
  private DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel;

  public ProSplignCompartExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<ProSplignCompartBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.proSplignCompartExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    this.systemBinaryExecutionConfigurationPanel = new SystemBinaryExecutionConfigurationPanel();
    this.systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.proSplignCompartExecutorChanged);

    this.dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    this.dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.proSplignCompartExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard(CARD_DOCKER_IMAGE, dockerExecutionConfigurationPanel)
        .withSelectedCard(CARD_DOCKER_IMAGE)
        .disableSelectionWithOneCard(true);

    if (
      !getProperty(SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
        && !getProperty(PROPERTY_ENABLE_LOCAL_EXECUTION_PROSPLIGN_PROCOMPART, "true").equals("false")
    ) {
      builder = builder.withCard(CARD_SYSTEM_BINARY, systemBinaryExecutionConfigurationPanel);
    }

    this.proSplignCompartExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.proSplignCompartExecutableCardsPanel.setBorder(createTitledBorder("ProSplign/ProCompart configuration"));

    this.proSplignCompartExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::proSplignCompartBinaryExecutorCardChanged);

    this.add(this.proSplignCompartExecutableCardsPanel);
  }

  private void proSplignCompartBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.proSplignCompartExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<ProSplignCompartBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }

  public BinaryExecutionConfigurationPanel<ProSplignCompartBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<ProSplignCompartBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<ProSplignCompartBinariesExecutor>) this.proSplignCompartExecutableCardsPanel
        .getSelectedCard());

    return selectedCard;
  }

  public void setBinariesExecutor(ProSplignCompartBinariesExecutor binariesExecutor) {
    if (binariesExecutor instanceof DockerProSplignCompartBinariesExecutor) {
      this.dockerExecutionConfigurationPanel
        .setSelectedDockerImage(((DockerProSplignCompartBinariesExecutor) binariesExecutor).getDockerImage());
      this.proSplignCompartExecutableCardsPanel.setSelectedCard(CARD_DOCKER_IMAGE);
    } else if (
      binariesExecutor instanceof DefaultProSplignCompartBinariesExecutor
    ) {
      File directory = ((DefaultProSplignCompartBinariesExecutor) binariesExecutor).getProSplignCompartDirectory();
      if (directory != null) {
        this.systemBinaryExecutionConfigurationPanel.setSelectedFile(directory);
      } else {
        this.systemBinaryExecutionConfigurationPanel.clearSelectedFile();
      }
      this.proSplignCompartExecutableCardsPanel.setSelectedCard(CARD_SYSTEM_BINARY);
    } else {
      throw new IllegalStateException("Unknown ProSplignCompartBinariesExecutor implementation");
    }
  }
}
