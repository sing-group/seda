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
package org.sing_group.seda.gui.rename;

import java.awt.event.ItemEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.event.ListDataEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.list.JInputList;
import org.sing_group.gc4s.input.list.event.DefaultListDataListener;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.WordReplaceRenamer;
import org.sing_group.seda.datatype.DatatypeFactory;

public class WordReplaceRenamePanel extends AbstractRenamePanel {
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
    InputParameter[] toret = new  InputParameter[3];
    toret[0] = getTargetsParameter();
    toret[1] = getIsRegexParameter();
    toret[2] = getReplacementParameter();

    return toret;
  }

  private InputParameter getTargetsParameter() {
    this.targetsListPanel = new JInputList(true, false, false);
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

    return new InputParameter("Targets", this.targetsListPanel, "The target words.");
  }

  private InputParameter getIsRegexParameter() {
    this.isRegexCheckBox = new JCheckBox("Regex");
    this.isRegexCheckBox.addItemListener(this::isRegexCheckBoxItemEvent);

    return new InputParameter("", this.isRegexCheckBox, "Whether targets must be applied as regex or not.");
  }

  private void isRegexCheckBoxItemEvent(ItemEvent event) {
    this.renameConfigurationChanged();
  }

  private InputParameter getReplacementParameter() {
    this.replacementTextField = new JXTextField("Replacement");
    this.replacementTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("Replacement", this.replacementTextField, "The replacement.");
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
  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory, HeaderTarget target) {
    return new WordReplaceRenamer(factory, target, getReplacement(), isRegex(), getTargets());
  }
}
