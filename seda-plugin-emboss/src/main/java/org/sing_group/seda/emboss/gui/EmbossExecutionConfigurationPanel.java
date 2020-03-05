/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.gui;

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.emboss.execution.DefaultEmbossBinariesExecutor;
import org.sing_group.seda.emboss.execution.DockerEmbossBinariesExecutor;
import org.sing_group.seda.emboss.execution.EmbossBinariesExecutor;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class EmbossExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String CARD_SYSTEM_BINARY = "System binary";
  private static final String CARD_DOCKER_IMAGE = "Docker image";

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION + ".emboss";

  private CardsPanel embossExecutableCardsPanel;
  private BinaryConfigurationPanelListener<EmbossBinariesExecutor> embossExecutorChanged;

  private DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel;
  private SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel;

  public EmbossExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<EmbossBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.embossExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    this.systemBinaryExecutionConfigurationPanel = new SystemBinaryExecutionConfigurationPanel();
    this.systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.embossExecutorChanged);

    this.dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    this.dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.embossExecutorChanged);

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

    this.embossExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.embossExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::embossBinaryExecutorCardChanged);
    
    this.add(this.embossExecutableCardsPanel);
  }

  private void embossBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.embossExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<EmbossBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }

  public BinaryExecutionConfigurationPanel<EmbossBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<EmbossBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<EmbossBinariesExecutor>) this.embossExecutableCardsPanel.getSelectedCard());

    return selectedCard;
  }

  public void setBinariesExecutor(EmbossBinariesExecutor binariesExecutor) {
    if (binariesExecutor instanceof DockerEmbossBinariesExecutor) {
      this.dockerExecutionConfigurationPanel
        .setSelectedDockerImage(((DockerEmbossBinariesExecutor) binariesExecutor).getDockerImage());
      this.embossExecutableCardsPanel.setSelectedCard(CARD_DOCKER_IMAGE);
    } else if (
      binariesExecutor instanceof DefaultEmbossBinariesExecutor
    ) {
      File directory = ((DefaultEmbossBinariesExecutor) binariesExecutor).getEmbossDirectory();
      if (directory != null) {
        this.systemBinaryExecutionConfigurationPanel.setSelectedFile(directory);
      } else {
        this.systemBinaryExecutionConfigurationPanel.clearSelectedFile();
      }
      this.embossExecutableCardsPanel.setSelectedCard(CARD_SYSTEM_BINARY);
    } else {
      throw new IllegalStateException("Unknown EmbossBinariesExecutor implementation");
    }
  }
}
