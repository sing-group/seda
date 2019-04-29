/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;

public class SequenceHeaderRenameConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_POSITION = "seda.sequenceheaderrenameconfiguration.position";
  public static final String PROPERTY_DELIMITER = "seda.sequenceheaderconfiguration.delimiter";
  public static final String PROPERTY_ADD_INDEX = "seda.sequenceheaderconfiguration.addindex";
  public static final String PROPERTY_INDEX_DELIMITER = "seda.sequenceheaderconfiguration.indexdelimiter";

  private static final String HELP_POSITION = "The position where the substitution must be placed.";
  private static final String HELP_DELIMITER = "The delimiter for the substitution.";
  private static final String HELP_INDEX = "Whether an index must be added or not.";
  private static final String HELP_INDEX_DELIMITER = "The delimiter for the index.";

  private JComboBox<RenameMode> positionCombo;
  private JXTextField delimiterTextField;
  private JCheckBox addIndexCheckbox;

  private JXTextField indexDelimiterTextField;

  public SequenceHeaderRenameConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getParametersPanel(), BorderLayout.CENTER);
  }

  private Component getParametersPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getPositionParameter());
    parameters.add(getDelimiterParameter());
    parameters.add(getAddIndexParameter());
    parameters.add(getIndexDelimiterParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getPositionParameter() {
    this.positionCombo = new JComboBox<>(getRenameModeValues());
    this.positionCombo.addItemListener(this::positionChanged);

    return new InputParameter("Position:", this.positionCombo, HELP_POSITION);
  };

  private RenameMode[] getRenameModeValues() {
    List<RenameMode> renameModes = new LinkedList<>(Arrays.asList(RenameMode.values()));
    renameModes.remove(RenameMode.REPLACE);

    return renameModes.toArray(new RenameMode[renameModes.size()]);
  }

  private void positionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.firePropertyChange(PROPERTY_POSITION, null, getPosition());
    }
  }

  public RenameMode getPosition() {
    return (RenameMode) this.positionCombo.getSelectedItem();
  }

  private InputParameter getDelimiterParameter() {
    this.delimiterTextField = new JXTextField("Delimiter");
    this.delimiterTextField.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          documentChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          documentChanged();
        }

        private void documentChanged() {
          SwingUtilities.invokeLater(SequenceHeaderRenameConfigurationPanel.this::delimiterChanged);
        }
      }
    );

    return new InputParameter("Delimiter:", this.delimiterTextField, HELP_DELIMITER);
  }

  private void delimiterChanged() {
    this.firePropertyChange(PROPERTY_DELIMITER, null, getDelimiter());
  }

  public String getDelimiter() {
    return this.delimiterTextField.getText();
  }

  private InputParameter getAddIndexParameter() {
    this.addIndexCheckbox = new JCheckBox();
    this.addIndexCheckbox.addItemListener(this::addIndexChanged);

    return new InputParameter("Add index:", this.addIndexCheckbox, HELP_INDEX);
  }

  private void addIndexChanged(ItemEvent event) {
    this.firePropertyChange(PROPERTY_ADD_INDEX, null, isAddIndex());
  }

  public boolean isAddIndex() {
    return this.addIndexCheckbox.isSelected();
  }

  private InputParameter getIndexDelimiterParameter() {
    this.indexDelimiterTextField = new JXTextField("Index delimiter");
    this.indexDelimiterTextField.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          documentChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          documentChanged();
        }

        private void documentChanged() {
          SwingUtilities.invokeLater(SequenceHeaderRenameConfigurationPanel.this::indexDelimiterChanged);
        }
      }
    );

    return new InputParameter("Index delimiter:", this.indexDelimiterTextField, HELP_INDEX_DELIMITER);
  }

  private void indexDelimiterChanged() {
    this.firePropertyChange(PROPERTY_INDEX_DELIMITER, null, getIndexDelimiter());
  }

  public String getIndexDelimiter() {
    return this.indexDelimiterTextField.getText();
  }

  public boolean isValidInput() {
    return true;
  }
}
