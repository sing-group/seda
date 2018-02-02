package org.sing_group.seda.gui.pattern;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.text.ExtendedJXTextField;
import org.sing_group.seda.datatype.pattern.SequencePattern;

public class SequencePatternPanel extends JPanel {
  private static final long serialVersionUID = 1L;

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
  }

  private Component getCombobox() {
    this.modeCombo = new JComboBox<>(Mode.values());
    this.modeCombo.addItemListener(this::modeComboChanged);

    return this.modeCombo;
  }

  private void modeComboChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
      this.notifyPatternEdited();
    }
  }

  private JTextField getRegexTextField() {
    this.regexTextField = new ExtendedJXTextField("Pattern");
    this.regexTextField.setColumns(30);
    this.regexTextField.setEmptyTextFieldColor(Color.RED);
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
    this.notifyPatternEdited();
  }

  private void notifyPatternEdited() {
    ChangeEvent event = new ChangeEvent(this);
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
    this.notifyPatternEdited();
  }

  public boolean isValidUserSelection() {
    return !this.regexTextField.getText().isEmpty();
  }

  public SequencePattern getSequencePattern() {
    return new SequencePattern(
      this.regexTextField.getText(),
      this.model.getNumber().intValue(),
      this.modeCombo.getSelectedItem().equals(Mode.CONTAINS)
     );
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
}
