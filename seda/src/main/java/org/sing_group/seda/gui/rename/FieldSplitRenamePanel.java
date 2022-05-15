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

import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_FIELDS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_FIELD_DELIMITER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_FIELD_DELIMITER_HELP_GUI;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_FIELD_MODE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_FIELD_MODE_HELP_GUI;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_JOIN_DELIMITER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.RenameHeaderMultipartSedaPluginInfo.PARAM_JOIN_DELIMITER_HELP_GUI;

import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.seda.core.rename.FieldSplitRenamer;
import org.sing_group.seda.core.rename.FieldSplitRenamer.Mode;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;

public class FieldSplitRenamePanel extends AbstractRenameHeaderPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_FIELDS =
    "<html>The comma-separated list of fields starting at 1.<br>Note that when the <i>Keep</i> mode is used, then "
      + "the order of the fields is preserved in the output, meaning that it is possible to swap fields.";

  private JXTextField fieldDelimiterTextField;
  private JXTextField joinDelimiterTextField;
  private RadioButtonsPanel<Mode> modeRbtnPanel;
  private JXTextField fieldsTextField;

  public FieldSplitRenamePanel() {
    this.init();
  }

  private void init() {
    this.add(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] toret = new InputParameter[4];
    toret[0] = getFieldDelimiterParameter();
    toret[1] = getJoinDelimiterParameter();
    toret[2] = getModeParameter();
    toret[3] = getFieldsParameter();

    return toret;
  }

  private InputParameter getFieldDelimiterParameter() {
    this.fieldDelimiterTextField = new JXTextField(PARAM_FIELD_DELIMITER_DESCRIPTION);
    this.fieldDelimiterTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(
      PARAM_FIELD_DELIMITER_DESCRIPTION, this.fieldDelimiterTextField, PARAM_FIELD_DELIMITER_HELP_GUI
    );
  }

  private InputParameter getJoinDelimiterParameter() {
    this.joinDelimiterTextField = new JXTextField(PARAM_JOIN_DELIMITER_DESCRIPTION);
    this.joinDelimiterTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(
      PARAM_JOIN_DELIMITER_DESCRIPTION, this.joinDelimiterTextField, PARAM_JOIN_DELIMITER_HELP_GUI
    );
  }

  private InputParameter getModeParameter() {
    this.modeRbtnPanel = new RadioButtonsPanel<>(Mode.values(), 1, 2);
    this.modeRbtnPanel.addItemListener(this::modeValueChanged);

    return new InputParameter(PARAM_FIELD_MODE_DESCRIPTION, this.modeRbtnPanel, PARAM_FIELD_MODE_HELP_GUI);
  }

  private void modeValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.renameConfigurationChanged();
    }
  }

  private InputParameter getFieldsParameter() {
    this.fieldsTextField = new JXTextField("1, 2, 3");
    this.fieldsTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter(PARAM_FIELDS_DESCRIPTION, this.fieldsTextField, HELP_FIELDS);
  }

  private String getFieldDelimiter() {
    return this.fieldDelimiterTextField.getText();
  }

  private String getJoinDelimiter() {
    return this.joinDelimiterTextField.getText();
  }

  private List<Integer> getFields() {
    String fields = this.fieldsTextField.getText();
    if (fields.isEmpty()) {
      return emptyList();
    }
    List<Integer> toret = new LinkedList<>();
    String[] split = fields.split(",");
    for (String field : split) {
      try {
        Integer fieldInt = parseInt(field) - 1;
        if (!toret.contains(fieldInt)) {
          toret.add(fieldInt);
        }
      } catch (Exception ex) {
        return emptyList();
      }
    }
    return toret;
  }

  private Mode getMode() {
    return this.modeRbtnPanel.getSelectedItem().get();
  }

  @Override
  public boolean isValidConfiguration() {
    return !getFieldDelimiter().isEmpty() && !getFields().isEmpty();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(HeaderTarget target) {
    return new FieldSplitRenamer(target, getFieldDelimiter(), getJoinDelimiter(), getMode(), getFields());
  }

  public void setHeaderRenamer(FieldSplitRenamer renamer) {
    this.fieldDelimiterTextField.setText(renamer.getFieldDelimiter());
    this.joinDelimiterTextField.setText(renamer.getJoinDelimiter());
    this.modeRbtnPanel.setSelectedItem(renamer.getMode());
    this.fieldsTextField.setText(renamer.getFields().stream().map(i -> String.valueOf(i + 1)).collect(joining(", ")));
  }
}
