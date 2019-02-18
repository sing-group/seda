package org.sing_group.seda.blast.gui;

import static java.lang.System.getProperty;
import static org.sing_group.gc4s.ui.CardsPanel.PROPERTY_VISIBLE_CARD;

import java.beans.PropertyChangeEvent;
import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.execution.BinaryConfigurationPanelListener;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;

public class BlastExecutionConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private CardsPanel blastExecutableCardsPanel;
  private BinaryConfigurationPanelListener<BlastBinariesExecutor> blastExecutorChanged;

  public BlastExecutionConfigurationPanel(
    BinaryConfigurationPanelListener<BlastBinariesExecutor> binaryConfigurationPanelListener
  ) {
    this.blastExecutorChanged = binaryConfigurationPanelListener;
    this.init();
  }

  private void init() {
    SystemBinaryExecutionConfigurationPanel systemBinaryExecutionConfigurationPanel =
      new SystemBinaryExecutionConfigurationPanel();
    systemBinaryExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.blastExecutorChanged);

    DockerExecutionConfigurationPanel dockerExecutionConfigurationPanel = new DockerExecutionConfigurationPanel();
    dockerExecutionConfigurationPanel.addBinaryConfigurationPanelListener(this.blastExecutorChanged);

    CardsPanelBuilder builder =
      CardsPanelBuilder.newBuilder()
        .withCard("System binary", systemBinaryExecutionConfigurationPanel);

    if (!getProperty(GuiUtils.PROPERTY_ENABLE_DOCKER_EXECUTION, "true").equals("false")) {
      builder = builder.withCard("Docker image", dockerExecutionConfigurationPanel);
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

  public Optional<BlastBinariesExecutor> getBlastBinariesExecutor() {
    return getSelectedCard().getBinariesExecutor();
  }
  
  public BinaryExecutionConfigurationPanel<BlastBinariesExecutor> getSelectedCard() {
    @SuppressWarnings("unchecked")
    BinaryExecutionConfigurationPanel<BlastBinariesExecutor> selectedCard =
      ((BinaryExecutionConfigurationPanel<BlastBinariesExecutor>) this.blastExecutableCardsPanel.getSelectedCard());
    
    return selectedCard;
  }
}
