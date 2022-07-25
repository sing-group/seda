/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.gui.execution;

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.sapp.execution.DefaultSappBinariesExecutor;
import org.sing_group.seda.sapp.execution.DockerSappBinariesExecutor;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;
import org.sing_group.seda.util.SedaProperties;

public class SappExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String CARD_SYSTEM_BINARY = "System binary";
  private static final String CARD_DOCKER_IMAGE = "Docker image";

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION + ".sapp";

  private CardsPanel sappExecutableCardsPanel;
  private BinaryConfigurationPanelListener<SappBinariesExecutor> sappExecutorChanged;
  private SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel;
  private DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel;

  public SappExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<SappBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.sappExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    this.systemBinaryExecutionConfigurationPanel = new SystemBinaryExecutionConfigurationPanel();
    this.systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.sappExecutorChanged);

    this.dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    this.dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.sappExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard(CARD_DOCKER_IMAGE, dockerExecutionConfigurationPanel)
        .withSelectedCard(CARD_DOCKER_IMAGE)
        .disableSelectionWithOneCard(true);

    if (
      !getProperty(SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
        && !getProperty(PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
    ) {
      builder = builder.withCard(CARD_SYSTEM_BINARY, systemBinaryExecutionConfigurationPanel);
    }

    this.sappExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.sappExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::sappBinaryExecutorCardChanged);

    this.add(this.sappExecutableCardsPanel);
  }

  private void sappBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.sappExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<SappBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }

  public BinaryExecutionConfigurationPanel<SappBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<SappBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<SappBinariesExecutor>) this.sappExecutableCardsPanel.getSelectedCard());

    return selectedCard;
  }

  public void setBinariesExecutor(SappBinariesExecutor  binariesExecutor) {
    if (binariesExecutor instanceof DockerSappBinariesExecutor) {
      this.dockerExecutionConfigurationPanel
        .setSappCommands(((DockerSappBinariesExecutor) binariesExecutor).getDockerSappCommands());
      this.sappExecutableCardsPanel.setSelectedCard(CARD_DOCKER_IMAGE);
    } else if (
      binariesExecutor instanceof DefaultSappBinariesExecutor
    ) {
      this.systemBinaryExecutionConfigurationPanel.setSappCommands(
        ((DefaultSappBinariesExecutor) binariesExecutor).getSappCommands()
      );
      this.sappExecutableCardsPanel.setSelectedCard(CARD_SYSTEM_BINARY);
    } else {
      throw new IllegalStateException("Unknown BedToolsBinariesExecutor implementation");
    }
  }
}
