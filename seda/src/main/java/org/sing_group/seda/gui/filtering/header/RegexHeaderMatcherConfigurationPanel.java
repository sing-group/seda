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
package org.sing_group.seda.gui.filtering.header;

import static java.util.Arrays.asList;
import static org.sing_group.seda.gui.GuiUtils.COLOR_ERROR;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_CASE_SENSITIVE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_CASE_SENSITIVE_HELP_GUI;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_GROUP_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_GROUP_HELP_GUI;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_HEADER_TARGET_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_HEADER_TARGET_HELP_GUI;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_QUOTE_PATTERN_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_QUOTE_PATTERN_HELP_GUI;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_STRING_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RegexHeaderMatcherInfo.PARAM_REGEX_STRING_HELP_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.rename.HeaderTarget;

public class RegexHeaderMatcherConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final int DEFAULT_REGEX_GROUP = 0;
  private static final boolean DEFAULT_IS_QUOTE_PATTERN = false;
  private static final boolean DEFAULT_CASE_SENSITIVE = false;
  private static final HeaderTarget DEFAULT_HEADER_TARGET = HeaderTarget.NAME;

  private static final String PROPERTY_REGEX_CHANGED = "property.regex.string";
  private static final String PROPERTY_IS_QUOTE_PATTERN_CHANGED = "property.regex.quotepattern";
  private static final String PROPERTY_REGEX_GROUP_CHANGED = "property.regex.group";
  private static final String PROPERTY_CASE_SENSITIVE_CHANGED = "property.regex.casesensitive";
  private static final String PROPERTY_HEADER_TARGET_CHANGED = "property.regex.headertarget";

  public static final Set<String> PROPERTIES =
    new HashSet<>(
      asList(
        PROPERTY_REGEX_CHANGED, PROPERTY_IS_QUOTE_PATTERN_CHANGED,
        PROPERTY_REGEX_GROUP_CHANGED, PROPERTY_CASE_SENSITIVE_CHANGED, PROPERTY_HEADER_TARGET_CHANGED
      )
    );

  private JXTextField stringTextField;
  private JCheckBox isQuotePatternCheckBox;
  private JSpinner regexGroupSpinner;
  private JComboBox<HeaderTarget> headerTargetComboBox;
  private JCheckBox caseSensitiveCheckBox;

  private String oldStringValue = "";
  private boolean oldIsQuotePatternValue = DEFAULT_IS_QUOTE_PATTERN;
  private int oldRegexGroupValue = DEFAULT_REGEX_GROUP;
  private HeaderTarget oldHeaderTargetValue = DEFAULT_HEADER_TARGET;
  private boolean oldCaseSensitiveValue = DEFAULT_CASE_SENSITIVE;

  public RegexHeaderMatcherConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(new InputParametersPanel(getInputParameters()));
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> inputParameters = new LinkedList<>();
    inputParameters.add(getStringParameter());
    inputParameters.add(getCaseSensitiveParameter());
    inputParameters.add(getIsQuotePatternParameter());
    inputParameters.add(getRegexGroupParameter());
    inputParameters.add(getHeaderTargetParameter());

    return inputParameters.toArray(new InputParameter[inputParameters.size()]);
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

    return new InputParameter(PARAM_REGEX_STRING_DESCRIPTION + ":", this.stringTextField, PARAM_REGEX_STRING_HELP_GUI);
  }

  private void stringChanged() {
    String newValue = getString();
    firePropertyChange(PROPERTY_REGEX_CHANGED, oldStringValue, newValue);
    oldStringValue = newValue;
    checkString();
  }

  private void checkString() {
    Color backgroundColor = isValidString() ? null : COLOR_ERROR;
    String tooltip = isValid() ? null : "The introduced regular expression is not valid, please, revise it.";
    this.stringTextField.setBackground(backgroundColor);
    this.stringTextField.setToolTipText(tooltip);
  }

  private InputParameter getCaseSensitiveParameter() {
    this.caseSensitiveCheckBox = new JCheckBox(PARAM_REGEX_CASE_SENSITIVE_DESCRIPTION + "?", DEFAULT_CASE_SENSITIVE);
    this.caseSensitiveCheckBox.addItemListener(this::caseSensitiveChanged);

    return new InputParameter("", this.caseSensitiveCheckBox, PARAM_REGEX_CASE_SENSITIVE_HELP_GUI);
  }

  private void caseSensitiveChanged(ItemEvent event) {
    boolean newValue = isCaseSensitive();
    firePropertyChange(PROPERTY_CASE_SENSITIVE_CHANGED, oldCaseSensitiveValue, newValue);
    oldCaseSensitiveValue = newValue;
  }

  private InputParameter getIsQuotePatternParameter() {
    this.isQuotePatternCheckBox = new JCheckBox(PARAM_REGEX_QUOTE_PATTERN_DESCRIPTION + "?", DEFAULT_IS_QUOTE_PATTERN);
    this.isQuotePatternCheckBox.addItemListener(this::isQuotePatternChanged);

    return new InputParameter("", this.isQuotePatternCheckBox, PARAM_REGEX_QUOTE_PATTERN_HELP_GUI);
  }

  private void isQuotePatternChanged(ItemEvent event) {
    boolean newValue = isQuotePattern();
    firePropertyChange(PROPERTY_IS_QUOTE_PATTERN_CHANGED, oldIsQuotePatternValue, newValue);
    oldIsQuotePatternValue = newValue;
    this.setInputControlsEnabled(this.isQuotePatternCheckBox.isEnabled());
    checkString();
  }

  private InputParameter getRegexGroupParameter() {
    this.regexGroupSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_REGEX_GROUP, 0, Integer.MAX_VALUE, 1));
    this.regexGroupSpinner.addChangeListener(this::regexGroupChanged);

    return new InputParameter(
      PARAM_REGEX_GROUP_DESCRIPTION + ":", this.regexGroupSpinner, PARAM_REGEX_GROUP_HELP_GUI
    );
  }

  private void regexGroupChanged(ChangeEvent event) {
    int newValue = getRegexGroup();
    firePropertyChange(PROPERTY_REGEX_GROUP_CHANGED, oldRegexGroupValue, newValue);
    oldRegexGroupValue = newValue;
  }

  private InputParameter getHeaderTargetParameter() {
    this.headerTargetComboBox = new JComboBox<HeaderTarget>(HeaderTarget.values());
    this.headerTargetComboBox.setSelectedItem(DEFAULT_HEADER_TARGET);
    this.headerTargetComboBox.addItemListener(this::headerTargetChanged);

    return new InputParameter(
      PARAM_REGEX_HEADER_TARGET_DESCRIPTION + ":", this.headerTargetComboBox, PARAM_REGEX_HEADER_TARGET_HELP_GUI
    );
  }

  private void headerTargetChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      HeaderTarget newValue = getHeaderTarget();
      firePropertyChange(PROPERTY_HEADER_TARGET_CHANGED, oldHeaderTargetValue, newValue);
      oldHeaderTargetValue = newValue;
    }
  }

  public Optional<RegexHeaderMatcher> getRegexHeaderMatcher() {
    if (getString().isEmpty() || !isValidConfiguration()) {
      return Optional.empty();
    }

    return Optional.of(createRegexHeaderMatcher());
  }

  public boolean isValidConfiguration() {
    return isValidString();
  }

  private RegexHeaderMatcher createRegexHeaderMatcher() {
    return new RegexHeaderMatcher(
      getString(),
      getHeaderTarget(),
      new RegexConfiguration(isCaseSensitive(), getRegexGroup(), isQuotePattern())
    );
  }

  private boolean isValidString() {
    try {
      createRegexHeaderMatcher();
      return true;
    } catch (PatternSyntaxException e) {
      return false;
    }
  }

  public boolean isQuotePattern() {
    return this.isQuotePatternCheckBox.isSelected();
  }

  public void setQuotePattern(boolean newQuotePattern) {
    this.isQuotePatternCheckBox.setSelected(newQuotePattern);
  }

  public int getRegexGroup() {
    return (int) this.regexGroupSpinner.getValue();
  }

  public void setRegexGroup(int newRegexGroup) {
    this.regexGroupSpinner.setValue(newRegexGroup);
  }

  public boolean isCaseSensitive() {
    return this.caseSensitiveCheckBox.isSelected();
  }

  public void setCaseSensitive(boolean newCaseSensitive) {
    this.caseSensitiveCheckBox.setSelected(newCaseSensitive);
  }

  public HeaderTarget getHeaderTarget() {
    return (HeaderTarget) this.headerTargetComboBox.getSelectedItem();
  }

  public void setHeaderTarget(HeaderTarget newHeaderTarget) {
    this.headerTargetComboBox.setSelectedItem(newHeaderTarget);
  }

  public String getString() {
    return this.stringTextField.getText();
  }

  public void setString(String newString) {
    this.stringTextField.setText(newString);
  }

  public void setInputControlsEnabled(boolean enabled) {
    this.stringTextField.setEnabled(enabled);
    this.isQuotePatternCheckBox.setEnabled(enabled);
    this.regexGroupSpinner.setEnabled(enabled);
    this.headerTargetComboBox.setEnabled(enabled);
    this.caseSensitiveCheckBox.setEnabled(enabled);
  }

  public void clearRegexHeaderMatcher() {
    this.stringTextField.setText("");
    this.caseSensitiveCheckBox.setSelected(DEFAULT_CASE_SENSITIVE);
    this.isQuotePatternCheckBox.setSelected(DEFAULT_IS_QUOTE_PATTERN);
    this.regexGroupSpinner.setValue(DEFAULT_REGEX_GROUP);
    this.headerTargetComboBox.setSelectedItem(DEFAULT_HEADER_TARGET);
  }

  public void setRegexHeaderMatcher(RegexHeaderMatcher regexHeaderMatcher) {
    this.stringTextField.setText(regexHeaderMatcher.getString());
    this.caseSensitiveCheckBox.setSelected(regexHeaderMatcher.getRegexConfig().isCaseSensitive());
    this.isQuotePatternCheckBox.setSelected(regexHeaderMatcher.getRegexConfig().isQuotePattern());
    this.regexGroupSpinner.setValue(regexHeaderMatcher.getRegexConfig().getGroup());
    this.headerTargetComboBox.setSelectedItem(regexHeaderMatcher.getHeaderTarget());
  }
}
