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

import static java.util.Arrays.asList;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.sing_group.seda.core.filtering.StringHeaderMatcher;
import org.sing_group.seda.core.rename.HeaderTarget;

public class RegexHeaderMatcherConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String DESCRIPTION_STRING = "The string that must be matched in the sequence header.";
  private static final String DESCRIPTION_IS_REGEX = "Whether the string must be matched as a regular "
  		+ "expression or not.";
  private static final String DESCRIPTION_REGEX_GROUP = "<html>The regular expression group that must be extracted. "
  		+ "Default value is <b>0</b>, meaning that the entire result must be considered. <br/>Use values higher than 0 "
  		+ "when there are brackets in the regular expression in order to select the desired group.";
  private static final String DESCRIPTION_CASE_SENSITIVE = "Whether the string must be matched as case sensitive or "
  		+ "not.";
  private static final String DESCRIPTION_HEADER_TARGET = "The part of the sequence header where the string must be "
  		+ "found.";

	private static final int DEFAULT_REGEX_GROUP = 0;
	private static final boolean DEFAULT_IS_REGEX = false;
	private static final boolean DEFAULT_CASE_SENSITIVE = false;
	private static final HeaderTarget DEFAULT_HEADER_TARGET = HeaderTarget.NAME;

  private static final String PROPERTY_STRING_CHANGED = "property.regex.string";
  private static final String PROPERTY_IS_REGEX_CHANGED = "property.regex.useasregex";
  private static final String PROPERTY_REGEX_GROUP_CHANGED = "property.regex.group";
  private static final String PROPERTY_CASE_SENSITIVE_CHANGED = "property.regex.casesensitive";
  private static final String PROPERTY_HEADER_TARGET_CHANGED = "property.regex.headertarget";

	public static final Set<String> PROPERTIES = new HashSet<>(asList(
			PROPERTY_STRING_CHANGED, PROPERTY_IS_REGEX_CHANGED,
	    PROPERTY_REGEX_GROUP_CHANGED, PROPERTY_CASE_SENSITIVE_CHANGED, PROPERTY_HEADER_TARGET_CHANGED));

	private JXTextField stringTextField;
	private JCheckBox useAsRegexCheckBox;
	private JSpinner regexGroupSpinner;
	private JComboBox<HeaderTarget> headerTargetComboBox;
	private JCheckBox caseSensitiveCheckBox;

	private String oldStringValue = "";
	private boolean oldUseAsRegexValue = DEFAULT_IS_REGEX;
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
		inputParameters.add(getUseAsRegexParameter());
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

		return new InputParameter("String to match:", this.stringTextField, DESCRIPTION_STRING);
	}

	private void stringChanged() {
		String newValue = getString();
		firePropertyChange(PROPERTY_STRING_CHANGED, oldStringValue, newValue);
		oldStringValue = newValue;
	}

	private InputParameter getCaseSensitiveParameter() {
    this.caseSensitiveCheckBox = new JCheckBox("Case sensitive?", DEFAULT_CASE_SENSITIVE);
    this.caseSensitiveCheckBox.addItemListener(this::caseSensitiveChanged);

    return new InputParameter("", this.caseSensitiveCheckBox, DESCRIPTION_CASE_SENSITIVE);
	}

	private void caseSensitiveChanged(ItemEvent event) {
		boolean newValue = isCaseSensitive();
		firePropertyChange(PROPERTY_CASE_SENSITIVE_CHANGED, oldCaseSensitiveValue, newValue);
		oldCaseSensitiveValue = newValue;
	}

	private InputParameter getUseAsRegexParameter() {
    this.useAsRegexCheckBox = new JCheckBox("Regex?", DEFAULT_IS_REGEX);
    this.useAsRegexCheckBox.addItemListener(this::useAsRegexChanged);

    return new InputParameter("", this.useAsRegexCheckBox, DESCRIPTION_IS_REGEX);
	}

	private void useAsRegexChanged(ItemEvent event) {
		boolean newValue = isUseAsRegex();
		firePropertyChange(PROPERTY_IS_REGEX_CHANGED, oldUseAsRegexValue, newValue);
		oldUseAsRegexValue = newValue;
		this.setInputControlsEnabled(this.useAsRegexCheckBox.isEnabled());
	}

	private InputParameter getRegexGroupParameter() {
		this.regexGroupSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_REGEX_GROUP, 0, Integer.MAX_VALUE, 1));
    this.regexGroupSpinner.addChangeListener(this::regexGroupChanged);
    this.regexGroupSpinner.setEnabled(false);

    return new InputParameter("Regex group:" , this.regexGroupSpinner, DESCRIPTION_REGEX_GROUP);
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

		return new InputParameter("Header target:", this.headerTargetComboBox, DESCRIPTION_HEADER_TARGET);
	}

	private void headerTargetChanged(ItemEvent event) {
		if (event.getStateChange() == ItemEvent.SELECTED) {
			HeaderTarget newValue = getHeaderTarget();
			firePropertyChange(PROPERTY_HEADER_TARGET_CHANGED, oldHeaderTargetValue, newValue);
			oldHeaderTargetValue = newValue;
		}
	}

	public Optional<RegexHeaderMatcher> getRegexHeaderMatcher() {
		if (getString().isEmpty()) {
			return Optional.empty();
		}

		if (isUseAsRegex()) {
			return Optional.of(new RegexHeaderMatcher(
					getString(),
					getHeaderTarget(),
			    new RegexConfiguration(isCaseSensitive(), getRegexGroup()))
			);
		} else {
			return Optional.of(new StringHeaderMatcher(getString(), getHeaderTarget(), isCaseSensitive()));
		}
	}

	public boolean isUseAsRegex() {
		return this.useAsRegexCheckBox.isSelected();
	}
	
	public void setUseAsRegex(boolean newUseAsRegex) {
		this.useAsRegexCheckBox.setSelected(newUseAsRegex);
	}

	public int getRegexGroup() {
		if (isUseAsRegex()) {
			return (int) this.regexGroupSpinner.getValue();
		} else {
			return 0;
		}
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
    this.useAsRegexCheckBox.setEnabled(enabled);
    this.regexGroupSpinner.setEnabled(enabled && this.useAsRegexCheckBox.isSelected());
    this.headerTargetComboBox.setEnabled(enabled);
    this.caseSensitiveCheckBox.setEnabled(enabled);
	}
}
