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

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer.Position;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;

public class AddStringHeaderRenamePanel extends AbstractRenamePanel {
  private static final long serialVersionUID = 1L;

  private JXTextField prefixStringTextField;
  private JXTextField delimiterStringTextField;
  private RadioButtonsPanel<Position> positionRbtnPanel;
  private JCheckBox addIndexCheckBox;
  private JXTextField indexDelimiterTextField;

  public AddStringHeaderRenamePanel() {
    this.init();
  }

  private void init() {
    this.add(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] toret = new  InputParameter[5];
    toret[0] = getPositionParameter();
    toret[1] = getPrefixStringParameter();
    toret[2] = getDelimiterStringParameter();
    toret[3] = getAddIndexParameter();
    toret[4] = getIndexDelimiterParameter();

    return toret;
  }

  private InputParameter getPositionParameter() {
    this.positionRbtnPanel = new RadioButtonsPanel<>(Position.values(), 1, 2);
    this.positionRbtnPanel.addItemListener(this::positionValueChanged);

    return new InputParameter("Position", this.positionRbtnPanel, "The position where the string must be added.");
  }

  private void positionValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.renameConfigurationChanged();
    }
  }

  private InputParameter getPrefixStringParameter() {
    this.prefixStringTextField = new JXTextField("String");
    this.prefixStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("String", this.prefixStringTextField, "The string to add.");
  }

  private InputParameter getDelimiterStringParameter() {
    this.delimiterStringTextField = new JXTextField("Delimiter");
    this.delimiterStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("Delimiter", this.delimiterStringTextField, "The string delimiter.");
  }

  private InputParameter getAddIndexParameter() {
    this.addIndexCheckBox = new JCheckBox("Add index?");
    this.addIndexCheckBox.addItemListener(this::addIndexCheckBoxItemEvent);

    return new InputParameter("", this.addIndexCheckBox, "Whether an index must be added or not.");
  }

  private InputParameter getIndexDelimiterParameter() {
    this.indexDelimiterTextField = new JXTextField("Index delimiter");
    this.indexDelimiterTextField.setEnabled(false);
    this.indexDelimiterTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("Index delimiter", this.indexDelimiterTextField, "The index delimiter.");
  }

  private void addIndexCheckBoxItemEvent(ItemEvent event) {
    this.renameConfigurationChanged();
    this.indexDelimiterTextField.setEnabled(this.addIndexCheckBox.isSelected());
  }

  private String getPrefixString() {
    return this.prefixStringTextField.getText();
  }

  private String getDelimiterString() {
    return this.delimiterStringTextField.getText();
  }

  private boolean isAddIndex() {
    return this.addIndexCheckBox.isSelected();
  }

  private String getIndexDelimiterString() {
    return this.indexDelimiterTextField.getText();
  }

  private Position getPosition() {
    return this.positionRbtnPanel.getSelectedItem().get();
  }

  @Override
  public boolean isValidConfiguration() {
    return isAddIndex() || !getPrefixString().isEmpty();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory, HeaderTarget target) {
    return new AddStringHeaderRenamer(
      factory, target, getPrefixString(), getDelimiterString(), getPosition(), isAddIndex(), getIndexDelimiterString()
    );
  }
}
