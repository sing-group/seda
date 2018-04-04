/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.filtering.header;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.IntegerRangeInputPanel;
import org.sing_group.gc4s.ui.Orientation;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.FilterType;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.Level;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.Mode;

public class HeaderFilteringConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_FILTER_CONFIGURATION = "filtering.configuration";

  private static final String HELP_MODE = "Whether sequences or files meeting the criteria should be removed or kept.";
  private static final String HELP_LEVEL = "Whether the filter should be applied to sequences or files.";
  private static final String HELP_RANGE = "The minimum and maximum number of sequences that must contain the specified"
    + " filter.";
  private static final String HELP_FILTER_TYPE = "<html><i><b>Sequence name</b></i> means that the count is done by using "
    + "sequence identifiers (or names).<br/><i><b>String</b></i> means that the count is done by matching headers using the "
    + "string specified below.</html>";
  private static final String HELP_STRING = "The string to match sequence headers. Note that it can be used as a "
    + "regular expression or not.";

  private static final boolean DEFAULT_CASE_SENSITIVE = false;

  private HeaderFilteringConfiguration oldValue;

  private JXTaskPane translationConfigurationTaskPane;
  private JCheckBox useFilter;
  private RadioButtonsPanel<Mode> modeRbtn;
  private RadioButtonsPanel<Level> levelRbtn;
  private IntegerRangeInputPanel rangePanel;
  private RadioButtonsPanel<FilterType> filterTypeRbtn;
  private JXTextField stringTextField;
  private JLabel caseSensitiveLabel;
  private JCheckBox caseSensitiveCheckBox;
  private JCheckBox useAsRegexCheckBox;

  public HeaderFilteringConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getUseFilterCombobox(), BorderLayout.NORTH);
    this.add(getCenterPanel(), BorderLayout.CENTER);
  }

  private Component getUseFilterCombobox() {
    this.useFilter = new JCheckBox("Use this filter", false);
    this.useFilter.addItemListener(this::useFilterChanged);

    return this.useFilter;
  }

  private void useFilterChanged(ItemEvent event) {
    this.translationConfigurationTaskPane.setCollapsed(!isUseFilterSelected());
    this.configurationChanged();
  }

  private boolean isUseFilterSelected() {
    return this.useFilter.isSelected();
  }

  private JComponent getCenterPanel() {
    final JXTaskPaneContainer customOptionsTaskPaneContainer = new JXTaskPaneContainer();
    customOptionsTaskPaneContainer.setOpaque(false);
    customOptionsTaskPaneContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    translationConfigurationTaskPane = new JXTaskPane();
    translationConfigurationTaskPane.setTitle("Configuration");
    translationConfigurationTaskPane.add(getInputParametersPanel());
    translationConfigurationTaskPane.setCollapsed(true);
    customOptionsTaskPaneContainer.add(translationConfigurationTaskPane);

    return customOptionsTaskPaneContainer;
  }

  private Component getInputParametersPanel() {
    InputParametersPanel parametersPanel = new InputParametersPanel(getInputParameters());

    return parametersPanel;
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getModeParameter());
    parameters.add(getLevelParameter());
    parameters.add(getRangeParameter());
    parameters.add(getFilterTypeParameter());
    parameters.add(getStringParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getModeParameter() {
    this.modeRbtn = new RadioButtonsPanel<>(Mode.values(), 1, 0);
    this.modeRbtn.addItemListener(this::itemEventListener);

    return new InputParameter("Mode: ", this.modeRbtn, HELP_MODE);
  }
  private InputParameter getLevelParameter() {
    this.levelRbtn = new RadioButtonsPanel<>(Level.values(), 1, 0);
    this.levelRbtn.addItemListener(this::itemEventListener);

    return new InputParameter("Level: ", this.levelRbtn, HELP_LEVEL);
  }

  private void itemEventListener(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.configurationChanged();
    }
  }

  private InputParameter getRangeParameter() {
    this.rangePanel = new IntegerRangeInputPanel(0, 10, "Min.:", "Max.:", Orientation.HORIZONTAL);
    this.rangePanel.addChangeListener(this::rangeChanged);

    return new InputParameter("Range: ", this.rangePanel, HELP_RANGE);
  }

  private void rangeChanged(ChangeEvent event) {
    this.configurationChanged();
  }

  private InputParameter getFilterTypeParameter() {
    this.filterTypeRbtn = new RadioButtonsPanel<>(FilterType.values(), 1, 0);
    this.filterTypeRbtn.addItemListener(this::filterTypeChanged);

    return new InputParameter("Filter type: ", this.filterTypeRbtn, HELP_FILTER_TYPE);
  }

  private void filterTypeChanged(ItemEvent event) {
    this.checkStringFields();
    this.itemEventListener(event);
  }

  private InputParameter getStringParameter() {
    this.stringTextField = new JXTextField("String to match");
    this.stringTextField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        stringChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        stringChanged();
      }
    });

    this.useAsRegexCheckBox = new JCheckBox("Regex?", false);
    this.useAsRegexCheckBox.addItemListener(this::useAsRegexChanged);

    JPanel stringParameterPanel = new JPanel();
    stringParameterPanel.setLayout(new BoxLayout(stringParameterPanel, BoxLayout.X_AXIS));
    stringParameterPanel.add(this.stringTextField);
    stringParameterPanel.add(Box.createHorizontalStrut(5));
    stringParameterPanel.add(this.useAsRegexCheckBox);
    stringParameterPanel.add(Box.createHorizontalStrut(5));
    stringParameterPanel.add(getCaseSensitiveButton());

    this.checkStringFields();

    return new InputParameter("String: ", stringParameterPanel, HELP_STRING);
  }

  private JPanel getCaseSensitiveButton() {
    this.caseSensitiveCheckBox = new JCheckBox("", DEFAULT_CASE_SENSITIVE);
    this.caseSensitiveCheckBox.addItemListener(this::caseSensitiveChanged);

    JPanel panel = new JPanel(new GridLayout(1, 2));

    this.caseSensitiveLabel = new JLabel(Icons.ICON_CASE_SENSITIVE_24);
    this.caseSensitiveLabel.setEnabled(DEFAULT_CASE_SENSITIVE);
    this.caseSensitiveLabel.setToolTipText("Whether the regular expression must be applied as case sensitive or not.");

    panel.add(this.caseSensitiveLabel);
    panel.add(this.caseSensitiveCheckBox);

    return panel;
  }

  private void caseSensitiveChanged(ItemEvent event) {
    this.checkStringFields();
    this.configurationChanged();
  }

  private void checkStringFields() {
    SwingUtilities.invokeLater(
      () -> {
        boolean enabled = getFilterType().equals(FilterType.STRING);
        this.stringTextField.setEnabled(enabled);
        this.useAsRegexCheckBox.setEnabled(enabled);
        this.caseSensitiveCheckBox.setEnabled(enabled);
        this.caseSensitiveLabel.setEnabled(enabled && this.caseSensitiveCheckBox.isSelected());
      }
    );
  }

  private FilterType getFilterType() {
    return this.filterTypeRbtn.getSelectedItem().get();
  }

  private void stringChanged() {
    this.configurationChanged();
  }

  private void useAsRegexChanged(ItemEvent event) {
    this.configurationChanged();
  }

  private void configurationChanged() {
    SwingUtilities.invokeLater(
      () -> {
        HeaderFilteringConfiguration newValue = getHeaderFilteringConfiguration();
        firePropertyChange(PROPERTY_FILTER_CONFIGURATION, oldValue, newValue);
        oldValue = newValue;
      }
    );
  }

  public HeaderFilteringConfiguration getHeaderFilteringConfiguration() {
    return new HeaderFilteringConfiguration(
      isUseFilterSelected(), this.modeRbtn.getSelectedItem().get(), this.levelRbtn.getSelectedItem().get(),
      this.rangePanel.getMinValue(), this.rangePanel.getMaxValue(), getFilterType(), this.stringTextField.getText(),
      this.useAsRegexCheckBox.isSelected(), this.caseSensitiveCheckBox.isSelected()
    );
  }

  public void setHeaderFilteringConfiguration(HeaderFilteringConfiguration headerFilteringConfiguration) {
    if (!headerFilteringConfiguration.equals(getHeaderFilteringConfiguration())) {

      this.useFilter.setSelected(headerFilteringConfiguration.isUseFilter());

      if (headerFilteringConfiguration.getMode() != null) {
        this.modeRbtn.setSelectedItem(headerFilteringConfiguration.getMode());
      }

      if (headerFilteringConfiguration.getLevel() != null) {
        this.levelRbtn.setSelectedItem(headerFilteringConfiguration.getLevel());
      }

      if (headerFilteringConfiguration.getFilterType() != null) {
        this.filterTypeRbtn.setSelectedItem(headerFilteringConfiguration.getFilterType());
      }

      this.rangePanel.setMinValue(headerFilteringConfiguration.getMin());
      this.rangePanel.setMaxValue(headerFilteringConfiguration.getMax());
      this.stringTextField.setText(headerFilteringConfiguration.getFilterString());
      this.useAsRegexCheckBox.setSelected(headerFilteringConfiguration.isUseRegex());

      this.configurationChanged();
    }
  }
}
