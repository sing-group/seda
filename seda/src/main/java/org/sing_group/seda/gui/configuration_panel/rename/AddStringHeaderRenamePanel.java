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
package org.sing_group.seda.gui.configuration_panel.rename;

import static java.util.Collections.emptyList;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_ADD_INDEX_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_ADD_INDEX_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_DELIMITER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_DELIMITER_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_INDEX_DELIMITER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_INDEX_DELIMITER_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_POSITION_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_POSITION_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_START_INDEX_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_START_INDEX_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_STRING_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderAddWordSedaPluginInfo.PARAM_STRING_HELP_GUI;

import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer.Position;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;

public class AddStringHeaderRenamePanel extends AbstractRenameHeaderPanel {
  private static final long serialVersionUID = 1L;

  private final boolean addIndexVisible;

  private JXTextField prefixStringTextField;
  private JXTextField delimiterStringTextField;
  private RadioButtonsPanel<Position> positionRbtnPanel;
  private JCheckBox addIndexCheckBox;
  private JXTextField indexDelimiterTextField;
  private JIntegerTextField startIndexTextField;
  private List<InputParameter> additionalParameters;

  public AddStringHeaderRenamePanel() {
    this(true, emptyList());
  }

  public AddStringHeaderRenamePanel(boolean addIndexVisible, List<InputParameter> additionalParameters) {
    this.addIndexVisible = addIndexVisible;
    this.additionalParameters = additionalParameters;
    this.init();
  }

  private void init() {
    this.add(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    List<InputParameter> toret = new LinkedList<>(additionalParameters);
    toret.add(getPositionParameter());
    toret.add(getPrefixStringParameter());
    toret.add(getDelimiterStringParameter());

    if (this.addIndexVisible) {
      toret.add(getAddIndexParameter());
      toret.add(getIndexDelimiterParameter());
      toret.add(getStartIndexParameter());
    }

    return toret.toArray(new InputParameter[toret.size()]);
  }

  private InputParameter getPositionParameter() {
    this.positionRbtnPanel = new RadioButtonsPanel<>(Position.values(), 1, 2);
    this.positionRbtnPanel.addItemListener(this::positionValueChanged);

    return new InputParameter(PARAM_POSITION_DESCRIPTION, this.positionRbtnPanel, PARAM_POSITION_HELP_GUI);
  }

  private void positionValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.renameConfigurationChanged();
    }
  }

  private InputParameter getPrefixStringParameter() {
    this.prefixStringTextField = new JXTextField(PARAM_STRING_DESCRIPTION);
    this.prefixStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(PARAM_STRING_DESCRIPTION, this.prefixStringTextField, PARAM_STRING_HELP_GUI);
  }

  private InputParameter getDelimiterStringParameter() {
    this.delimiterStringTextField = new JXTextField(PARAM_DELIMITER_DESCRIPTION);
    this.delimiterStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(PARAM_DELIMITER_DESCRIPTION, this.delimiterStringTextField, PARAM_DELIMITER_HELP_GUI);
  }

  private InputParameter getAddIndexParameter() {
    this.addIndexCheckBox = new JCheckBox(PARAM_ADD_INDEX_DESCRIPTION + "?");
    this.addIndexCheckBox.addItemListener(this::addIndexCheckBoxItemEvent);

    return new InputParameter("", this.addIndexCheckBox, PARAM_ADD_INDEX_HELP_GUI);
  }

  private InputParameter getIndexDelimiterParameter() {
    this.indexDelimiterTextField = new JXTextField(PARAM_INDEX_DELIMITER_DESCRIPTION);
    this.indexDelimiterTextField.setEnabled(false);
    this.indexDelimiterTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(
      PARAM_INDEX_DELIMITER_DESCRIPTION, this.indexDelimiterTextField, PARAM_INDEX_DELIMITER_HELP_GUI
    );
  }

  private InputParameter getStartIndexParameter() {
    this.startIndexTextField = new JIntegerTextField(1);
    this.startIndexTextField.setEnabled(false);
    this.startIndexTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(PARAM_START_INDEX_DESCRIPTION, this.startIndexTextField, PARAM_START_INDEX_HELP_GUI);
  }

  private void addIndexCheckBoxItemEvent(ItemEvent event) {
    this.renameConfigurationChanged();
    this.indexDelimiterTextField.setEnabled(this.addIndexCheckBox.isSelected());
    this.startIndexTextField.setEnabled(this.addIndexCheckBox.isSelected());
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

  private int getStartIndex() {
    return this.startIndexTextField.getValue();
  }

  private Position getPosition() {
    return this.positionRbtnPanel.getSelectedItem().get();
  }

  @Override
  public boolean isValidConfiguration() {
    return isAddIndex() || !getPrefixString().isEmpty();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(HeaderTarget target) {
    return new AddStringHeaderRenamer(
      target, getPrefixString(), getDelimiterString(), getPosition(), isAddIndex(), getIndexDelimiterString(),
      getStartIndex()
    );
  }

  public void setHeaderRenamer(AddStringHeaderRenamer renamer) {
    this.prefixStringTextField.setText(renamer.getString());
    this.delimiterStringTextField.setText(renamer.getDelimiter());
    this.positionRbtnPanel.setSelectedItem(renamer.getPosition());
    this.addIndexCheckBox.setSelected(renamer.isAddIndex());
    this.indexDelimiterTextField.setText(renamer.getIndexDelimiter());
  }
}
