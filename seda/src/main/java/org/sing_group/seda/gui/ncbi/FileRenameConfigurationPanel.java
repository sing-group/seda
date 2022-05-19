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
package org.sing_group.seda.gui.ncbi;

import static java.awt.BorderLayout.CENTER;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_FILE_DELIMITER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_FILE_DELIMITER_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_FILE_POSITION_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_FILE_POSITION_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;

public class FileRenameConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_POSITION = "seda.filerenameconfiguration.position";
  public static final String PROPERTY_DELIMITER = "seda.filerenameconfiguration.delimiter";

  private static final String HELP_POSITION = PARAM_FILE_POSITION_HELP_GUI;
  private static final String HELP_DELIMITER = PARAM_FILE_DELIMITER_HELP_GUI;

  private JComboBox<RenameMode> positionCombo;
  private JXTextField delimiterTextField;

  public FileRenameConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getParametersPanel(), CENTER);
    this.checkDelimiterStatus();
  }

  private Component getParametersPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getPositionParameter());
    parameters.add(getDelimiterParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getPositionParameter() {
    this.positionCombo = new JComboBox<>(RenameMode.values());
    this.positionCombo.addItemListener(this::positionChanged);

    return new InputParameter(PARAM_FILE_POSITION_DESCRIPTION + ":", this.positionCombo, HELP_POSITION);
  };

  private void positionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.firePropertyChange(PROPERTY_POSITION, null, getPosition());
      this.checkDelimiterStatus();
    }
  }

  private void checkDelimiterStatus() {
    RenameMode position = getPosition();
    boolean enabled = position.equals(RenameMode.PREFIX) || position.equals(RenameMode.SUFFIX);
    this.delimiterTextField.setEnabled(enabled);
  }

  public RenameMode getPosition() {
    return (RenameMode) this.positionCombo.getSelectedItem();
  }

  private InputParameter getDelimiterParameter() {
    this.delimiterTextField = new JXTextField(PARAM_FILE_DELIMITER_DESCRIPTION);
    this.delimiterTextField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        documentChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        documentChanged();
      }

      private void documentChanged() {
        SwingUtilities.invokeLater(FileRenameConfigurationPanel.this::delimiterChanged);
      }
    });

    return new InputParameter(PARAM_FILE_DELIMITER_DESCRIPTION + ":", this.delimiterTextField, HELP_DELIMITER);
  }

  private void delimiterChanged() {
    this.firePropertyChange(PROPERTY_DELIMITER, null, getDelimiter());
  }

  public String getDelimiter() {
    return this.delimiterTextField.getText();
  }

  public void setPosition(RenameMode newPosition) {
    this.positionCombo.setSelectedItem(newPosition);
  }

  public void setDelimiter(String newDelimiter) {
    this.delimiterTextField.setText(newDelimiter);
  }
}
