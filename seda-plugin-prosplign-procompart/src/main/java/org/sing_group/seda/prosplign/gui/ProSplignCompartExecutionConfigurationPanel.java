/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.gui;

import static java.lang.System.getProperty;
import static javax.swing.BorderFactory.createTitledBorder;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;

public class ProSplignCompartExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION + ".prosplignprocompart";

  private CardsPanel proSplignCompartExecutableCardsPanel;
  private BinaryConfigurationPanelListener<ProSplignCompartBinariesExecutor> proSplignCompartExecutorChanged;

  public ProSplignCompartExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<ProSplignCompartBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.proSplignCompartExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.proSplignCompartExecutorChanged);

    DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.proSplignCompartExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard("Docker image", dockerExecutionConfigurationPanel)
        .withSelectedCard("Docker image")
        .disableSelectionWithOneCard(true);

    if (
      !getProperty(GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
        && !getProperty(PROPERTY_ENABLE_LOCAL_EXECUTION, "true").equals("false")
    ) {
      builder = builder.withCard("System binary", systemBinaryExecutionConfigurationPanel);
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
}
