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

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static javax.swing.Box.createHorizontalGlue;
import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.JOptionPane.DEFAULT_OPTION;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.sing_group.gc4s.dialog.JOptionPaneMessage;
import org.sing_group.gc4s.ui.menu.HamburgerMenu;
import org.sing_group.gc4s.ui.menu.HamburgerMenu.Size;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern.GroupMode;
import org.sing_group.seda.datatype.pattern.SequencePattern;
import org.sing_group.seda.datatype.pattern.SequencePatternGroup;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.pattern.PatternEditionEvent.PatternEditionType;

public class SequencePatternGroupPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final JOptionPaneMessage EDIT_OTHER_PATTERNS_MESSAGE =
    new JOptionPaneMessage(
      "Dou you want to set the same value in the rest of patterns of this group?"
    );
  private static boolean lastEditOtherPatternsResponse = false;

  private SequencePatternGroup initialGroup;

  private JComboBox<EvaluableSequencePattern.GroupMode> patternsModeCombo;
  private JPanel sequencePatternsPanel;
  private List<SequencePatternPanelComponent> sequencePatternComponents = new ArrayList<>();
  private boolean ignorePatternEditionEvents = false;

  public SequencePatternGroupPanel() {
    this.init();
  }

  public SequencePatternGroupPanel(SequencePatternGroup group) {
    this.initialGroup = group;
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
    northPanel.add(createHorizontalGlue());
    northPanel.add(createHorizontalStrut(5));
    northPanel.add(getAddPatternButton());
    northPanel.add(createHorizontalStrut(5));
    northPanel.add(getImportPatternsMenu());
    northPanel.add(createHorizontalStrut(5));
    northPanel.add(getRemoveAllPatternsButton());

    return northPanel;
  }

  private JButton getAddPatternButton() {
    JButton addPattern = new JButton("Add pattern");
    addPattern.addActionListener(event -> this.addSequencePatternPanelComponent());

    return addPattern;
  }

  private JComponent getImportPatternsMenu() {
    HamburgerMenu menu = new HamburgerMenu(Size.SIZE16);
    menu.setIcon(null);
    menu.setText("Import patterns");
    menu.setMaximumSize(new Dimension(130, 27));

    menu.add(new AbstractAction("From plain-text file") {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        importPatternsList();
      }
    });

    menu.add(new AbstractAction("From FASTA file (sequence IDs)") {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        importSequenceIdentifiersPatterns();
      }
    });

    menu.add(new AbstractAction("From FASTA file (headers)") {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        importSequenceHeadersPatterns();
      }
    });

    menu.add(new AbstractAction("From FASTA file (sequences)") {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        importSequencePatterns();
      }
    });

    return menu;
  }

  private JButton getRemoveAllPatternsButton() {
    JButton removeAllPatternsButton = new JButton("Remove all");
    removeAllPatternsButton.addActionListener(event -> this.removeAllPatternsList());

    return removeAllPatternsButton;
  }

  private Component getPatternsModeCombobox() {
    this.patternsModeCombo = new JComboBox<>(EvaluableSequencePattern.GroupMode.values());
    if (this.initialGroup != null) {
      this.patternsModeCombo.setSelectedItem(initialGroup.getMode());
    }
    this.patternsModeCombo.addItemListener(this::modeComboChanged);

    return this.patternsModeCombo;
  }

  private void modeComboChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.notifyPatternEdited(new PatternEditionEvent(this, PatternEditionType.MODE));
    }
  }

  private Component getCenterComponent() {
    JPanel centerPanel = new JPanel();
    this.sequencePatternsPanel = new JPanel();
    this.sequencePatternsPanel.setLayout(new BoxLayout(this.sequencePatternsPanel, BoxLayout.Y_AXIS));
    centerPanel.add(sequencePatternsPanel);
    if (this.initialGroup == null) {
      this.addSequencePatternPanelComponent();
    } else {
      this.addSequencePatternPanels();
    }

    return new JScrollPane(centerPanel);
  }

  private void addSequencePatternPanelComponent() {
    addSequencePatternPanelComponent("");
  }

  private void addSequencePatternPanelComponent(String pattern) {
    this.addSequencePatternPanelComponent(new SequencePatternPanelComponent(pattern));
  }

  private void addSequencePatternPanelComponent(SequencePatternPanelComponent component) {
    this.sequencePatternComponents.add(component);
    this.sequencePatternsPanel.add(component);
    this.notifyPatternAdded();
    this.updateUI();
  }

  private void addSequencePatternPanels() {
    this.ignorePatternEditionEvents = true;
    for (EvaluableSequencePattern esp : this.initialGroup.getPatterns()) {
      if (esp instanceof SequencePattern) {
        SequencePattern pattern = (SequencePattern) esp;
        this.addSequencePatternPanelComponent(new SequencePatternPanelComponent(pattern));
      }
    }
    this.ignorePatternEditionEvents = false;
  }

  private void removeSequencePatternComponent(SequencePatternPanelComponent component) {
    this.sequencePatternComponents.remove(component);
    this.sequencePatternsPanel.remove(component);
    this.notifyPatternRemoved();
    this.updateUI();
  }

  private void removeAllPatternsList() {
    this.sequencePatternComponents.clear();
    this.sequencePatternsPanel.removeAll();
    this.notifyPatternRemoved();
    this.updateUI();
  }

  private class SequencePatternPanelComponent extends JPanel {
    private static final long serialVersionUID = 1L;
    private SequencePatternPanel sequencePatternPanel;

    SequencePatternPanelComponent(String pattern) {
      this.init();
      this.sequencePatternPanel.setPattern(pattern);
    }

    public SequencePatternPanelComponent(SequencePattern pattern) {
      this.init();
      this.sequencePatternPanel.setPattern(pattern.getRegex());
      this.sequencePatternPanel.setRequiredNumberOfMatches(pattern.getRequiredNumberOfMatches());
      this.sequencePatternPanel.setCaseSensitive(pattern.isCaseSensitive());
      this.sequencePatternPanel.setContainsRegex(pattern.isContainsRegex());
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
          public void patternEdited(PatternEditionEvent event) {
            if (!ignorePatternEditionEvents) {
              notifyPatternEdited(event);
              SwingUtilities.invokeLater(
                () -> {
                  checkComponentsPatternEvent(event);
                }
              );
            }
          }
        }
        );
      }
      return this.sequencePatternPanel;
    }
  }

  private Optional<File> getPatternsFile() {
    JFileChooser fileChooser = CommonFileChooser.getInstance().getFilechooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int option = fileChooser.showSaveDialog(this);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    if (option == JFileChooser.APPROVE_OPTION) {
      return Optional.of(fileChooser.getSelectedFile());
    } else {
      return Optional.empty();
    }
  }

  private void importPatternsList() {
    Optional<File> patternsFile = getPatternsFile();
    if (patternsFile.isPresent()) {
      importPatternsList(patternsFile.get());
    }
  }

  private void importPatternsList(File file) {
    try {
      List<String> lines = Files.readAllLines(file.toPath());
      for (String line : lines) {
        this.addSequencePatternPanelComponent(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void importSequencePatterns() {
    Optional<File> patternsFile = getPatternsFile();
    if (patternsFile.isPresent()) {
      importSequencePatterns(patternsFile.get(), Sequence::getChain);
    }
  }

  private void importSequenceHeadersPatterns() {
    Optional<File> patternsFile = getPatternsFile();
    if (patternsFile.isPresent()) {
      importSequencePatterns(patternsFile.get(), s -> {
        return s.getHeader().substring(1);
      });
    }
  }

  private void importSequenceIdentifiersPatterns() {
    Optional<File> patternsFile = getPatternsFile();
    if (patternsFile.isPresent()) {
      importSequencePatterns(patternsFile.get(), Sequence::getName);
    }
  }

  private void importSequencePatterns(File file, Function<Sequence, String> sequenceMapper) {
    try {
      SequencesGroup sequencesGroup =
        DatatypeFactory.getDefaultDatatypeFactory().newSequencesGroup(file.toPath());

      sequencesGroup.getSequences().map(sequenceMapper::apply).forEach(s -> {
        addSequencePatternPanelComponent(s);
      });

    } catch (RuntimeException e) {
      showConfirmDialog(
        this, "The selected file can't be read as FASTA format.", "Error", DEFAULT_OPTION, ERROR_MESSAGE
      );
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

  private void notifyPatternEdited(PatternEditionEvent event) {
    for (SequencePatternEditorListener l : this.getSequencePatternEditorListener()) {
      l.patternEdited(event);
    }
  }

  private synchronized void checkComponentsPatternEvent(PatternEditionEvent event) {
    if (!event.getType().equals(PatternEditionType.REGEX) && this.sequencePatternComponents.size() > 1) {

      if (EDIT_OTHER_PATTERNS_MESSAGE.shouldBeShown()) {

        int n =
          JOptionPane.showConfirmDialog(
            SwingUtilities.getRootPane(this),
            EDIT_OTHER_PATTERNS_MESSAGE.getMessage(),
            "Edit other patterns?",
            JOptionPane.YES_NO_OPTION
          );

        if (n == JOptionPane.YES_OPTION) {
          lastEditOtherPatternsResponse = true;
        } else {
          lastEditOtherPatternsResponse = false;
        }
      }

      if (lastEditOtherPatternsResponse) {
        ignorePatternEditionEvents = true;
        switch (event.getType()) {
          case CASE_SENSITIVE:
            setOtherPanelsCaseSensitiveFrom(((SequencePatternPanel) event.getSource()));
            break;
          case MODE:
            setOtherPanelsContainsRegexFrom(((SequencePatternPanel) event.getSource()));
            break;
          case REGEX:
            break;
          case REQUIRED_MATCHES:
            setOtherPanelsRequiredMatchesFrom(((SequencePatternPanel) event.getSource()));
            break;
        }
        ignorePatternEditionEvents = false;
        this.notifyPatternEdited(new PatternEditionEvent(this, event.getType()));
      }
    }
  }

  private void setOtherPanelsContainsRegexFrom(SequencePatternPanel sourcePanel) {
    boolean containsRegex = sourcePanel.getSequencePattern().isContainsRegex();
    this.sequencePatternComponents.stream()
      .map(SequencePatternPanelComponent::getSequencePatternPanel)
      .filter(panel -> !panel.equals(sourcePanel))
      .forEach(
        p -> {
          p.setContainsRegex(containsRegex);
        }
      );
  }

  private void setOtherPanelsCaseSensitiveFrom(SequencePatternPanel sourcePanel) {
    boolean caseSensitive = sourcePanel.getSequencePattern().isCaseSensitive();
    this.sequencePatternComponents.stream()
      .map(SequencePatternPanelComponent::getSequencePatternPanel)
      .filter(panel -> !panel.equals(sourcePanel))
      .forEach(
        p -> {
          p.setCaseSensitive(caseSensitive);
        }
      );
  }

  private void setOtherPanelsRequiredMatchesFrom(SequencePatternPanel sourcePanel) {
    int requiredMatches = sourcePanel.getSequencePattern().getRequiredNumberOfMatches();
    this.sequencePatternComponents.stream()
      .map(SequencePatternPanelComponent::getSequencePatternPanel)
      .filter(panel -> !panel.equals(sourcePanel))
      .forEach(
        p -> {
          p.setRequiredNumberOfMatches(requiredMatches);
        }
      );
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

  public SequencePatternGroup getSequencePatternGroup() {
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
