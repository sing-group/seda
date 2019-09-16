/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.gui;

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
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;

public class SplignCompartExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION + ".spligncompart";

  private CardsPanel splignCompartExecutableCardsPanel;
  private BinaryConfigurationPanelListener<SplignCompartBinariesExecutor> splignCompartExecutorChanged;

  public SplignCompartExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<SplignCompartBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.splignCompartExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.splignCompartExecutorChanged);

    DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.splignCompartExecutorChanged);

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

    this.splignCompartExecutableCardsPanel =
      builder
        .withSelectionLabel("Execution mode")
        .build();

    this.splignCompartExecutableCardsPanel.setBorder(createTitledBorder("Splign/Compart configuration"));

    this.splignCompartExecutableCardsPanel
      .addPropertyChangeListener(PROPERTY_VISIBLE_CARD, this::splignCompartBinaryExecutorCardChanged);

    this.add(this.splignCompartExecutableCardsPanel);
  }

  private void splignCompartBinaryExecutorCardChanged(PropertyChangeEvent event) {
    this.splignCompartExecutorChanged.onBinariesExecutorChanged(getSelectedCard());
  }

  public Optional<SplignCompartBinariesExecutor> getBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }

  public BinaryExecutionConfigurationPanel<SplignCompartBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<SplignCompartBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<SplignCompartBinariesExecutor>) this.splignCompartExecutableCardsPanel
        .getSelectedCard());

    return selectedCard;
  }
}
