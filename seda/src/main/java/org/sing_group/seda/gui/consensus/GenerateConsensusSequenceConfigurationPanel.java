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
package org.sing_group.seda.gui.consensus;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_CONSENSUS_BASE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_CONSENSUS_BASE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_MINIMUM_PRESENCE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_MINIMUM_PRESENCE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_SEQUENCE_TYPE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_SEQUENCE_TYPE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_VERBOSE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.GenerateConsensusSequenceSedaPluginInfo.PARAM_VERBOSE_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.DoubleTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.bio.consensus.ConsensusBaseStrategy;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;

public class GenerateConsensusSequenceConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String HELP_SEQUENCE_TYPE = PARAM_SEQUENCE_TYPE_HELP_GUI;
  private static final String HELP_CONSENSUS_BASE_STRATEGY = PARAM_CONSENSUS_BASE_HELP_GUI;
  private static final String HELP_MINIMUM_PRESENCE = PARAM_MINIMUM_PRESENCE_HELP_GUI;
  private static final String HELP_VERBOSE = PARAM_VERBOSE_HELP_GUI;

  private GenerateConsensusSequenceTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;
  private JComboBox<SequenceType> sequenceTypeCombobox;
  private JComboBox<ConsensusBaseStrategy> consensusBaseStrategyCombobox;
  private DoubleTextField minimumPresenceTextField;
  private JCheckBox verboseCheckBox;

  public GenerateConsensusSequenceConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));
    mainPanel.add(getParametersPanel());
    mainPanel.add(getReformatFastaConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private Component getParametersPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getSequenceTypeParameter());
    parameters.add(getConsensusBaseStrategyParameter());
    parameters.add(getMinimumPresenceParameter());
    parameters.add(getVerboseParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getSequenceTypeParameter() {
    this.sequenceTypeCombobox = new JComboBox<>(SequenceType.values());
    this.sequenceTypeCombobox.addItemListener(this::sequenceTypeChanged);

    return new InputParameter(PARAM_SEQUENCE_TYPE_DESCRIPTION + ": ", this.sequenceTypeCombobox, HELP_SEQUENCE_TYPE);
  }

  private void sequenceTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.sequenceTypeChanged();
    }
  }

  private void sequenceTypeChanged() {
    this.transformationProvider.setSequenceType(getSequenceType());
    this.verboseCheckBox.setEnabled(getSequenceType().equals(SequenceType.PROTEIN));
  }

  private InputParameter getConsensusBaseStrategyParameter() {
    this.consensusBaseStrategyCombobox = new JComboBox<>(ConsensusBaseStrategy.values());
    this.consensusBaseStrategyCombobox.addItemListener(this::consensusBaseStrategyChanged);

    return new InputParameter(
      PARAM_CONSENSUS_BASE_DESCRIPTION + ": ", this.consensusBaseStrategyCombobox,
      HELP_CONSENSUS_BASE_STRATEGY
    );
  }

  private void consensusBaseStrategyChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.consensusBaseStrategyChanged();
    }
  }

  private void consensusBaseStrategyChanged() {
    this.transformationProvider.setConsensusBaseStrategy(getConsensusBaseStrategy());
  }

  private InputParameter getMinimumPresenceParameter() {
    this.minimumPresenceTextField = new DoubleTextField(0.5);
    this.minimumPresenceTextField.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          minimumPresenceValueChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          minimumPresenceValueChanged();
        }
      }
    );

    return new InputParameter(
      PARAM_MINIMUM_PRESENCE_DESCRIPTION + ": ", this.minimumPresenceTextField,
      HELP_MINIMUM_PRESENCE
    );
  }

  private void minimumPresenceValueChanged() {
    invokeLater(
      () -> {
        boolean valid = isValidMinimumPresenceValue();
        this.minimumPresenceTextField.setBackground(valid ? null : GuiUtils.COLOR_ERROR);
        this.transformationProvider.setMinimumPresence(getMinimumPresence());
      }
    );
  }

  private boolean isValidMinimumPresenceValue() {
    double value = getMinimumPresence();
    return value >= 0 && value <= 1.0;
  }

  private InputParameter getVerboseParameter() {
    this.verboseCheckBox = new JCheckBox();
    this.verboseCheckBox.addItemListener(e -> this.verboseChanged());

    return new InputParameter(PARAM_VERBOSE_DESCRIPTION + ": ", this.verboseCheckBox, HELP_VERBOSE);
  }

  private void verboseChanged() {
    this.transformationProvider.setVerbose(isVerbose());
  }

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(createTitledBorder("Reformat output file"));

    return this.reformatPanel;
  }

  private void initTransformationProvider() {
    this.transformationProvider = new GenerateConsensusSequenceTransformationProvider();
    this.transformationProvider.setReformatFastaTransformationProvider(this.reformatPanel.getTransformationProvider());

    this.consensusBaseStrategyChanged();
    this.minimumPresenceValueChanged();
    this.sequenceTypeChanged();
    this.verboseChanged();
  }

  public GenerateConsensusSequenceTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  private SequenceType getSequenceType() {
    return (SequenceType) this.sequenceTypeCombobox.getSelectedItem();
  }

  private ConsensusBaseStrategy getConsensusBaseStrategy() {
    return (ConsensusBaseStrategy) this.consensusBaseStrategyCombobox.getSelectedItem();
  }

  private double getMinimumPresence() {
    return this.minimumPresenceTextField.getValue();
  }

  private boolean isVerbose() {
    return this.verboseCheckBox.isSelected();
  }

  public void setTransformationProvider(GenerateConsensusSequenceTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    this.verboseCheckBox.setSelected(this.transformationProvider.isVerbose());
    this.minimumPresenceTextField.setValue(this.transformationProvider.getMinimumPresence());
    this.sequenceTypeCombobox.setSelectedItem(this.transformationProvider.getSequenceType());
    this.consensusBaseStrategyCombobox.setSelectedItem(this.transformationProvider.getConsensusBaseStrategy());
    this.reformatPanel.setTransformationProvider(this.transformationProvider.getReformatFastaTransformationProvider());
  }
}
