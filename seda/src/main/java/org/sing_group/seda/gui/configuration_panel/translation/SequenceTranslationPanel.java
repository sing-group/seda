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
package org.sing_group.seda.gui.configuration_panel.translation;

import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

import javax.swing.*;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;

public class SequenceTranslationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String INFO_LABEL =
    "<html>Check this option to show the sequence translation configuration.</html>";

  public static final String PROPERTY_TRANSLATION = "seda.sequencetranslationpanel.translation";
  public static final String PROPERTY_JOIN_FRAMES = SequenceTranslationConfigurationPanel.PROPERTY_JOIN_FRAMES;
  public static final String PROPERTY_FRAMES = SequenceTranslationConfigurationPanel.PROPERTY_FRAMES;
  public static final String PROPERTY_CODON_TABLE = SequenceTranslationConfigurationPanel.PROPERTY_CODON_TABLE;
  public static final String PROPERTY_REVERSE_SEQUENCES =
    SequenceTranslationConfigurationPanel.PROPERTY_REVERSE_SEQUENCES;

  private String checkBoxLabel;
  private String checkBoxTooltip;
  private boolean showJoinFramesCheckbox;

  private JCheckBox convertCb;
  private JXTaskPane translationConfigurationTaskPane;
  private SequenceTranslationConfigurationPanel translationConfigurationPanel;

  public SequenceTranslationPanel() {
    this("Convert to amino acid sequence", INFO_LABEL, true);
  }

  public SequenceTranslationPanel(String checkBoxLabel, String checkBoxTooltip, boolean showJoinFramesCheckbox) {
    this.checkBoxLabel = checkBoxLabel;
    this.checkBoxTooltip = checkBoxTooltip;
    this.showJoinFramesCheckbox = showJoinFramesCheckbox;

    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getConversionCheckPanel(), BorderLayout.NORTH);
    this.add(getConversionConfigurationPanelContainer(), BorderLayout.CENTER);

    this.convertCb.doClick();
  }

  private JPanel getConversionCheckPanel() {
    JPanel checkPanel = new JPanel();
    checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.X_AXIS));
    convertCb = new JCheckBox(checkBoxLabel, true);
    checkPanel.add(convertCb);
    convertCb.addItemListener(this::conversionStatusChanged);
    JLabel convertInfoLabel = new JLabel(Icons.ICON_INFO_2_16);
    convertInfoLabel.setToolTipText(this.checkBoxTooltip);
    checkPanel.add(Box.createHorizontalStrut(2));
    checkPanel.add(convertInfoLabel);

    return checkPanel;
  }

  private JPanel getConversionConfigurationPanelContainer() {
    final JXTaskPaneContainer customOptionsTaskPaneContainer =
      new JXTaskPaneContainer();
    customOptionsTaskPaneContainer.setOpaque(false);
    customOptionsTaskPaneContainer.setBorder(
      BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );

    translationConfigurationTaskPane = new JXTaskPane();
    translationConfigurationTaskPane.setTitle("Translation configuration");
    translationConfigurationTaskPane.add(getConversionConfigurationPanel());
    translationConfigurationTaskPane.setCollapsed(true);
    customOptionsTaskPaneContainer.add(translationConfigurationTaskPane);

    return customOptionsTaskPaneContainer;
  }

  private JPanel getConversionConfigurationPanel() {
    this.translationConfigurationPanel = new SequenceTranslationConfigurationPanel(this.showJoinFramesCheckbox);
    this.translationConfigurationPanel.setBorder(createTitledBorder(createEtchedBorder(), "Configuration"));

    return this.translationConfigurationPanel;
  }

  private void conversionStatusChanged(ItemEvent event) {
    this.updateControlsStatus();
    this.firePropertyChange(PROPERTY_TRANSLATION, null, this.convertCb.isSelected());
  }

  private void updateControlsStatus() {
    boolean enabled = this.convertCb.isSelected() && this.convertCb.isEnabled();

    this.translationConfigurationPanel.enableControls(enabled);
    this.translationConfigurationTaskPane.setCollapsed(!enabled);
  }

  public boolean isTranslationSelected() {
    return this.convertCb.isSelected();
  }

  public boolean isValidUserSelection() {
    return !isTranslationSelected() || this.translationConfigurationPanel.isValidUserSelection();
  }

  public void setConversionEnabled(boolean enabled) {
    this.convertCb.setEnabled(enabled);
    this.updateControlsStatus();
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    super.addPropertyChangeListener(listener);
    if (this.translationConfigurationPanel != null) {
      this.translationConfigurationPanel.addPropertyChangeListener(listener);
    }
  }

  @Override
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    super.addPropertyChangeListener(propertyName, listener);
    if (this.translationConfigurationPanel != null) {
      this.translationConfigurationPanel.addPropertyChangeListener(propertyName, listener);
    }
  }

  public SequenceTranslationConfiguration getSequenceTranslationConfiguration() {
    return this.translationConfigurationPanel.getSequenceTranslationConfiguration();
  }

  public void setSequenceTranslationConfiguration(Optional<SequenceTranslationConfiguration> configuration) {
    this.convertCb.setSelected(configuration.isPresent());
    if (configuration.isPresent()) {
      this.translationConfigurationPanel.setSequenceTranslationConfiguration(configuration.get());
    }
  }
}
