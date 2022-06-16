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
package org.sing_group.seda.gui.pattern;

import static org.sing_group.seda.gui.GuiUtils.COLOR_ERROR;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.text.ExtendedJXTextField;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.seda.datatype.pattern.SequencePattern;
import org.sing_group.seda.gui.pattern.PatternEditionEvent.PatternEditionType;

public class SequencePatternPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final boolean DEFAULT_CASE_SENSITIVE = true;

  private enum Mode {
    CONTAINS("Contains"), NOT_CONTAINS("Not contains");

    private String name;

    Mode(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  private JComboBox<Mode> modeCombo;
  private ExtendedJXTextField regexTextField;
  private JSpinner requiredNumberOfMatches;
  private JCheckBox caseSensitiveCheckBox;
  private JLabel caseSensitiveLabel;
  private SpinnerNumberModel model;

  public SequencePatternPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(getCombobox());
    this.add(Box.createHorizontalStrut(10));
    this.add(getRegexTextField());
    this.add(getRequiredNumberOfMatchesSlider());
    this.add(Box.createHorizontalStrut(5));
    this.add(getCaseSensitiveButton());
  }

  private Component getCombobox() {
    this.modeCombo = new JComboBox<>(Mode.values());
    this.modeCombo.addItemListener(this::modeComboChanged);

    return this.modeCombo;
  }

  private void modeComboChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
      this.notifyModeComboChanged();
    }
  }

  private void notifyModeComboChanged() {
    PatternEditionEvent patternEvent = new PatternEditionEvent(this, PatternEditionType.MODE);
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternEdited(patternEvent);
    }
  }

  private JTextField getRegexTextField() {
    this.regexTextField = new ExtendedJXTextField("Pattern");
    this.regexTextField.setColumns(30);
    this.regexTextField.setBackground(COLOR_ERROR);
    this.regexTextField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        regexPatternChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        regexPatternChanged();
      }
    });

    return this.regexTextField;
  }

  private void regexPatternChanged() {
    if (this.regexTextField.getText().isEmpty() || !getSequencePattern().validate().isValid()) {
      this.regexTextField.setBackground(COLOR_ERROR);
    } else {
      this.regexTextField.setBackground(null);
    }
    this.notifyRegexPatternChanged();
  }

  private void notifyRegexPatternChanged() {
    PatternEditionEvent event = new PatternEditionEvent(this, PatternEditionType.REGEX);
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternEdited(event);
    }
  }

  private JComponent getRequiredNumberOfMatchesSlider() {
    this.model = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
    this.requiredNumberOfMatches = new JSpinner(model);
    this.requiredNumberOfMatches.setToolTipText("The minimum number of ocurrences that the pattern must be found.");
    JComponent editor = this.requiredNumberOfMatches.getEditor();
    ((JSpinner.DefaultEditor) editor).getTextField().setColumns(2);
    this.model.addChangeListener(this::requiredNumberOfMatchesChanged);

    return this.requiredNumberOfMatches;
  }

  private void requiredNumberOfMatchesChanged(ChangeEvent e) {
    this.notifyRequiredNumberOfMatchesChanged();
  }

  private void notifyRequiredNumberOfMatchesChanged() {
    PatternEditionEvent patternEvent = new PatternEditionEvent(this, PatternEditionType.REQUIRED_MATCHES);
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternEdited(patternEvent);
    }
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
    this.caseSensitiveLabel.setEnabled(isCaseSensitive());
    this.notifyCaseSensitiveChanged();
  }

  private void notifyCaseSensitiveChanged() {
    PatternEditionEvent patternEvent = new PatternEditionEvent(this, PatternEditionType.CASE_SENSITIVE);
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternEdited(patternEvent);
    }
  }

  public boolean isValidUserSelection() {
    return !this.regexTextField.getText().isEmpty();
  }

  public SequencePattern getSequencePattern() {
    return new SequencePattern(
      this.regexTextField.getText(),
      this.model.getNumber().intValue(),
      isCaseSensitive(),
      this.modeCombo.getSelectedItem().equals(Mode.CONTAINS)
     );
  }

  private boolean isCaseSensitive() {
    return this.caseSensitiveCheckBox.isSelected();
  }

  public synchronized void addSequencePatternEditorListener(SequencePatternEditorListener l) {
    this.listenerList.add(SequencePatternEditorListener.class, l);
  }

  public synchronized SequencePatternEditorListener[] getSequencePatternEditorListener() {
    return this.listenerList.getListeners(SequencePatternEditorListener.class);
  }

  public void setPattern(String pattern) {
    this.regexTextField.setText(pattern);
  }

  public void setContainsRegex(boolean contains) {
    this.modeCombo.setSelectedItem(contains ? Mode.CONTAINS : Mode.NOT_CONTAINS);
  }

  public void setCaseSensitive(boolean caseSensitive) {
    this.caseSensitiveCheckBox.setSelected(caseSensitive);
  }

  public void setRequiredNumberOfMatches(int requiredNumberOfMatches) {
    this.requiredNumberOfMatches.setValue(requiredNumberOfMatches);
  }
}
