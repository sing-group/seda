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
package org.sing_group.seda.gui.rename;

import static java.util.stream.Collectors.joining;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REGEX_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REGEX_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REPLACEMENT_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceWordSedaPluginInfo.PARAM_REPLACEMENT_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceWordSedaPluginInfo.PARAM_TARGET_WORDS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceWordSedaPluginInfo.PARAM_TARGET_WORDS_HELP_GUI;

import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.list.JInputList;
import org.sing_group.gc4s.input.list.event.DefaultListDataListener;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.WordReplaceRenamer;

public class WordReplaceRenamePanel extends AbstractRenameHeaderPanel {
  private static final long serialVersionUID = 1L;

  private JCheckBox isRegexCheckBox;
  private JXTextField replacementTextField;
  private JInputList targetsListPanel;

  public WordReplaceRenamePanel() {
    this.init();
  }

  private void init() {
    this.add(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] toret = new InputParameter[3];
    toret[0] = getTargetsParameter();
    toret[1] = getIsRegexParameter();
    toret[2] = getReplacementParameter();

    return toret;
  }

  private boolean isValidRegex(String regex) {
    try {
      Pattern.compile(regex);
      return true;
    } catch (PatternSyntaxException e) {
      return false;
    }
  }
  
  private InputParameter getTargetsParameter() {
    this.targetsListPanel = new JInputList(true, false, false) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void addElement() {
        if (isRegex()) {
          String currentPatternToAdd = getElementToAdd();
          if (isValidRegex(currentPatternToAdd)) {
            super.addElement();
          } else {
            showRegexPatternWarning(currentPatternToAdd);
          }
        } else {
          super.addElement();
        }
      }
    };
    this.targetsListPanel.setElementIntroductionEnabled(true);
    this.targetsListPanel.getListPanel().getBtnMoveDown().setVisible(false);
    this.targetsListPanel.getListPanel().getBtnMoveUp().setVisible(false);
    this.targetsListPanel.addListDataListener(new DefaultListDataListener() {

      @Override
      public void intervalAdded(ListDataEvent e) {
        renameConfigurationChanged();
      }

      @Override
      public void intervalRemoved(ListDataEvent e) {
        renameConfigurationChanged();
      }
    });

    return new InputParameter(PARAM_TARGET_WORDS_DESCRIPTION, this.targetsListPanel, PARAM_TARGET_WORDS_HELP_GUI);
  }

  private InputParameter getIsRegexParameter() {
    this.isRegexCheckBox = new JCheckBox(PARAM_REGEX_DESCRIPTION);
    this.isRegexCheckBox.addItemListener(this::regexChanged);

    return new InputParameter("", this.isRegexCheckBox, PARAM_REGEX_HELP_GUI);
  }

  private void regexChanged(ItemEvent event) {
    invokeLater(() -> {
      this.checkPatternsList();
      this.renameConfigurationChanged();
    });
  }

  private void checkPatternsList() {
    List<String> newTargets = new LinkedList<String>();
    List<String> invalidTargets = new LinkedList<String>();
    for (String target : this.getTargets()) {
      if (!isValidRegex(target)) {
        invalidTargets.add(target);
      } else {
        newTargets.add(target);
      }
    }
    if (!invalidTargets.isEmpty()) {
      this.targetsListPanel.removeAllElements();
      this.targetsListPanel.addElements(newTargets.toArray(new String[newTargets.size()]));
      showMessageDialog(
        this,
        "The following targets were not valid regular expressions and have been removed: "
          + invalidTargets.stream().collect(joining(", ")),
        "Regular expression check", JOptionPane.WARNING_MESSAGE
      );
    }
  }

  private InputParameter getReplacementParameter() {
    this.replacementTextField = new JXTextField(PARAM_REPLACEMENT_DESCRIPTION);
    this.replacementTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(PARAM_REPLACEMENT_DESCRIPTION, this.replacementTextField, PARAM_REPLACEMENT_HELP_GUI);
  }

  private List<String> getTargets() {
    return this.targetsListPanel.getInputItems();
  }

  private boolean isRegex() {
    return this.isRegexCheckBox.isSelected();
  }

  private String getReplacement() {
    return this.replacementTextField.getText();
  }

  @Override
  public boolean isValidConfiguration() {
    return !getTargets().isEmpty();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(HeaderTarget target) {
    return new WordReplaceRenamer(target, getReplacement(), isRegex(), getTargets());
  }

  public void setHeaderRenamer(WordReplaceRenamer renamer) {
    this.isRegexCheckBox.setSelected(renamer.isRegex());
    this.replacementTextField.setText(renamer.getReplacement());
    this.targetsListPanel.removeAllElements();
    this.targetsListPanel.addElements(renamer.getTargets().toArray(new String[renamer.getTargets().size()]));
  }

  private void showRegexPatternWarning(String regex) {
    showMessageDialog(this, regex + " is not a valid regular expression.", "Regular expression error", ERROR_MESSAGE);
  }
}
