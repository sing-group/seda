package org.sing_group.seda.gui.pattern;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
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
import org.sing_group.seda.datatype.pattern.SequencePatternGroup;

public class MultipleSequencePatternGroupPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private JComboBox<EvaluableSequencePattern.GroupMode> patternsModeCombo;
  private JPanel groupsPanel;
  private List<SequencePatternGroupPanelComponent> groupsComponents = new ArrayList<>();

  public MultipleSequencePatternGroupPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getNorthComponent(), NORTH);
    this.add(getCenterComponent(), CENTER);
  }

  private Component getNorthComponent() {
    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
    northPanel.add(new JLabel("Required groups: "));
    northPanel.add(getPatternsModeCombobox());
    northPanel.add(Box.createHorizontalGlue());
    northPanel.add(getAddPatternButton());

    return northPanel;
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

  private JButton getAddPatternButton() {
    JButton addPattern = new JButton("Add pattern group");
    addPattern.addActionListener(event -> this.addSequencePatternGroupPanel());

    return addPattern;
  }

  private Component getCenterComponent() {
    JPanel centerPanel = new JPanel();
    this.groupsPanel = new JPanel();
    this.groupsPanel.setLayout(new BoxLayout(this.groupsPanel, BoxLayout.Y_AXIS));
    centerPanel.add(groupsPanel);
    this.addSequencePatternGroupPanel();

    return new JScrollPane(centerPanel);
  }

  private void addSequencePatternGroupPanel() {
    SequencePatternGroupPanelComponent newComponent = new SequencePatternGroupPanelComponent();
    newComponent.setBorder(
      BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), "Patterns group")
    );
    newComponent.addSequencePatternEditorListener(
      new SequencePatternEditorListener() {

        @Override
        public void patternRemoved(ChangeEvent event) {
          notifyPatternRemoved(event);
        }

        @Override
        public void patternEdited(ChangeEvent event) {
          notifyPatternEdited(event);
        }

        @Override
        public void patternAdded(ChangeEvent event) {
          notifyPatternAdded(event);
        }
      }
    );
    this.groupsComponents.add(newComponent);
    this.groupsPanel.add(newComponent);
    this.notifyPatternAdded();
  }

  private void removeSequencePatternGroupPanel(SequencePatternGroupPanelComponent component) {
    this.groupsComponents.remove(component);
    this.groupsPanel.remove(component);
    this.notifyPatternRemoved();
  }

  private class SequencePatternGroupPanelComponent extends SequencePatternGroupPanel {
    private static final long serialVersionUID = 1L;

    @Override
    protected JPanel getNorthComponent() {
      JPanel northComponent = super.getNorthComponent();
      northComponent.add(getRemoveGroupButton());
      return northComponent;
    }

    private Component getRemoveGroupButton() {
      JButton removeGroupButton = new JButton("Remove group");
      removeGroupButton.addActionListener(event -> removeSequencePatternGroupPanel(this));

      return removeGroupButton;
    }
  }

  private void notifyPatternAdded() {
    notifyPatternAdded(new ChangeEvent(this));
  }

  private void notifyPatternAdded(ChangeEvent event) {
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListeners()) {
      l.patternAdded(event);
    }
    this.updateUI();
  }

  private void notifyPatternRemoved() {
    notifyPatternRemoved(new ChangeEvent(this));
  }

  private void notifyPatternRemoved(ChangeEvent event) {
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListeners()) {
      l.patternRemoved(event);
    }
    this.updateUI();
  }

  private void notifyPatternEdited(ChangeEvent event) {
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListeners()) {
      l.patternEdited(event);
    }
  }

  public boolean isValidUserSelection() {
    return this.groupsComponents.size() > 0 && allPatternsGroupsAreValid();
  }

  private boolean allPatternsGroupsAreValid() {
    return !this.groupsComponents.stream()
      .map(SequencePatternGroupPanelComponent::isValidUserSelection)
      .filter(valid -> valid == false).findAny().isPresent();
  }

  public EvaluableSequencePattern getEvaluableSequencePattern() {
    return new SequencePatternGroup(getSelectedMode(), getPatterns());
  }

  private GroupMode getSelectedMode() {
    return (GroupMode) this.patternsModeCombo.getSelectedItem();
  }

  private EvaluableSequencePattern[] getPatterns() {
    return this.groupsComponents.stream()
      .map(SequencePatternGroupPanel::getEvaluableSequencePattern)
      .collect(Collectors.toList())
      .toArray(new EvaluableSequencePattern[this.groupsComponents.size()]);
  }


  public synchronized void addSequencePatternEditorListener(SequencePatternEditorListener l) {
    this.listenerList.add(SequencePatternEditorListener.class, l);
  }

  public synchronized SequencePatternEditorListener[] getSequencePatternEditorListeners() {
    return this.listenerList.getListeners(SequencePatternEditorListener.class);
  }
}
