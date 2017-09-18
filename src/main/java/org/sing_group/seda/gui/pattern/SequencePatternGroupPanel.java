package org.sing_group.seda.gui.pattern;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;

import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern.GroupMode;
import org.sing_group.seda.datatype.pattern.SequencePattern;
import org.sing_group.seda.datatype.pattern.SequencePatternGroup;

public class SequencePatternGroupPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private JComboBox<EvaluableSequencePattern.GroupMode> patternsModeCombo;
  private JPanel sequencePatternsPanel;
  private List<SequencePatternPanelComponent> sequencePatternComponents = new ArrayList<>();

  public SequencePatternGroupPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getNorthComponent(), NORTH);
    this.add(getCenterComponent(), CENTER);
  }

  protected JPanel getNorthComponent() {
    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
    northPanel.add(new JLabel("Required patterns: "));
    northPanel.add(getPatternsModeCombobox());
    northPanel.add(Box.createHorizontalGlue());
    northPanel.add(getAddPatternButton());

    return northPanel;
  }

  private JButton getAddPatternButton() {
    JButton addPattern = new JButton("Add pattern");
    addPattern.addActionListener(event -> this.addSequencePatternPanelComponent());

    return addPattern;
  }

  private Component getPatternsModeCombobox() {
    this.patternsModeCombo = new JComboBox<>(EvaluableSequencePattern.GroupMode.values());
    this.patternsModeCombo.addItemListener(this::modeComboChanged);

    return this.patternsModeCombo;
  }

  private void modeComboChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
      this.notifyPatternEdited(new ChangeEvent(this));
    }
  }

  private Component getCenterComponent() {
    JPanel centerPanel = new JPanel();
    this.sequencePatternsPanel = new JPanel();
    this.sequencePatternsPanel.setLayout(new BoxLayout(this.sequencePatternsPanel, BoxLayout.Y_AXIS));
    centerPanel.add(sequencePatternsPanel);
    this.addSequencePatternPanelComponent();

    return new JScrollPane(centerPanel);
  }

  private void addSequencePatternPanelComponent() {
    SequencePatternPanelComponent newComponent = new SequencePatternPanelComponent();
    this.sequencePatternComponents.add(newComponent);
    this.sequencePatternsPanel.add(newComponent);
    this.notifyPatternAdded();
    this.updateUI();
  }

  private void removeSequencePatternComponent(SequencePatternPanelComponent component) {
    this.sequencePatternComponents.remove(component);
    this.sequencePatternsPanel.remove(component);
    this.notifyPatternRemoved();
    this.updateUI();
  }

  private class SequencePatternPanelComponent extends JPanel {
    private static final long serialVersionUID = 1L;
    private SequencePatternPanel sequencePatternPanel;

    SequencePatternPanelComponent() {
      this.init();
    }

    private void init() {
      this.setLayout(new FlowLayout());
      this.add(getSequencePatternPanel());
      this.add(getRemoveButton());
    }

    private JButton getRemoveButton() {
      JButton removeButton = new JButton("Remove");
      removeButton.addActionListener(event -> removeSequencePatternComponent(this));
      return removeButton;
    }

    public SequencePatternPanel getSequencePatternPanel() {
      if (this.sequencePatternPanel == null) {
        this.sequencePatternPanel = new SequencePatternPanel();
        this.sequencePatternPanel.addSequencePatternEditorListener(new SequencePatternEditorAdapter() {

          @Override
          public void patternEdited(ChangeEvent event) {
            notifyPatternEdited(event);
          }
        });
      }
      return this.sequencePatternPanel;
    }
  }

  private void notifyPatternAdded() {
    ChangeEvent event = new ChangeEvent(this);
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternAdded(event);
    }
  }

  private void notifyPatternRemoved() {
    ChangeEvent event = new ChangeEvent(this);
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternRemoved(event);
    }
  }

  private void notifyPatternEdited(ChangeEvent event) {
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternEdited(event);
    }
  }

  public boolean isValidUserSelection() {
    return this.sequencePatternComponents.size() > 0 && allPatternsAreValid();
  }

  private boolean allPatternsAreValid() {
    return !this.sequencePatternComponents.stream()
      .map(SequencePatternPanelComponent::getSequencePatternPanel)
      .map(SequencePatternPanel::isValidUserSelection)
      .filter(valid -> valid == false).findAny().isPresent();
  }

  public EvaluableSequencePattern getEvaluableSequencePattern() {
    return new SequencePatternGroup(getSelectedMode(), getPatterns());
  }

  private GroupMode getSelectedMode() {
    return (GroupMode) this.patternsModeCombo.getSelectedItem();
  }

  private SequencePattern[] getPatterns() {
    return this.sequencePatternComponents.stream()
      .map(SequencePatternPanelComponent::getSequencePatternPanel)
      .map(SequencePatternPanel::getSequencePattern)
      .collect(Collectors.toList())
      .toArray(new SequencePattern[this.sequencePatternComponents.size()]);
  }

  public synchronized void addSequencePatternEditorListener(SequencePatternEditorListener l) {
    this.listenerList.add(SequencePatternEditorListener.class, l);
  }

  public synchronized SequencePatternEditorListener[] getSequencePatternEditorListener() {
    return this.listenerList.getListeners(SequencePatternEditorListener.class);
  }
}
