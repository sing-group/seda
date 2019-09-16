/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.gui;

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class BedToolsExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = GuiUtils.PROPERTY_ENABLE_LOCAL_EXECUTION + ".bedtools";

  private CardsPanel bedToolsExecutableCardsPanel;
  private BinaryConfigurationPanelListener<BedToolsBinariesExecutor> bedToolsExecutorChanged;

  public BedToolsExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<BedToolsBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.bedToolsExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.bedToolsExecutorChanged);

    DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.bedToolsExecutorChanged);

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
}
