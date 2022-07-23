/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.clustalomega.gui;

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;
import static org.sing_group.seda.clustalomega.plugin.core.ClustalOmegaAlignmentSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_CLUSTAL_OMEGA;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.*;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DefaultClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.execution.DockerClustalOmegaBinariesExecutor;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class ClustalOmegaExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String CARD_SYSTEM_BINARY = "System binary";
  private static final String CARD_DOCKER_IMAGE = "Docker image";

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = PROPERTY_ENABLE_LOCAL_EXECUTION_CLUSTAL_OMEGA;

  private CardsPanel clustalOmegaExecutableCardsPanel;
  private DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel;
  private SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel;
  private BinaryConfigurationPanelListener<ClustalOmegaBinariesExecutor> clustalOmegaExecutorChanged;

  public ClustalOmegaExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<ClustalOmegaBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.clustalOmegaExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    systemBinaryExecutionConfigurationPanel = new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.clustalOmegaExecutorChanged);

    dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.clustalOmegaExecutorChanged);

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

    this.clustalOmegaExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.clustalOmegaExecutableCardsPanel.setBorder(BorderFactory.createTitledBorder("Clustal Omega configuration"));

    this.clustalOmegaExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::clustalOmegaBinaryExecutorCardChanged);

    this.add(this.clustalOmegaExecutableCardsPanel);
  }

  private void clustalOmegaBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.clustalOmegaExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<ClustalOmegaBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }

  public BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<ClustalOmegaBinariesExecutor>) this.clustalOmegaExecutableCardsPanel
        .getSelectedCard());

    return selectedCard;
  }

  public void setBinariesExecutor(ClustalOmegaBinariesExecutor binariesExecutor) {
    if (binariesExecutor instanceof DockerClustalOmegaBinariesExecutor) {
      this.dockerExecutionConfigurationPanel
        .setSelectedDockerImage(((DockerClustalOmegaBinariesExecutor) binariesExecutor).getDockerImage());
      this.clustalOmegaExecutableCardsPanel.setSelectedCard(CARD_DOCKER_IMAGE);
    } else if (binariesExecutor instanceof DefaultClustalOmegaBinariesExecutor) {
      File file = ((DefaultClustalOmegaBinariesExecutor) binariesExecutor).getClustalOmegaExecutable();
      if (file != null) {
        this.systemBinaryExecutionConfigurationPanel.setSelectedFile(file);
      } else {
        this.systemBinaryExecutionConfigurationPanel.clearSelectedFile();
      }
      this.clustalOmegaExecutableCardsPanel.setSelectedCard(CARD_SYSTEM_BINARY);
    } else {
      throw new IllegalStateException("Unknown ClustalOmegaBinariesExecutor implementation");
    }
  }
}
