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

import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENTS_MAP_FILE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENT_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACEMENT_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_BLANK_SPACES_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_SAVE_REPLACEMENTS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.NcbiRenameSedaPluginInfo.PARAM_CONFIG_SAVE_REPLACEMENTS_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.seda.gui.CommonFileChooser;

public class ReplaceOptionsConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_REPLACEMENT_STRING = "seda.replaceoptionsconfiguration.replacementstring";
  public static final String PROPERTY_REPLACE_BLANK_SPACES = "seda.replaceoptionsconfiguration.replaceblankspaces";
  public static final String PROPERTY_REPLACE_SPECIAL_CHARACTERS =
    "seda.replaceoptionsconfiguration.replacespecialcharacters";
  public static final String PROPERTY_SAVE_REPLACEMENTS_MAP = "seda.replaceoptionsconfiguration.savereplacementsmap";
  public static final String PROPERTY_REPLACEMENTS_MAP_FILE = "seda.replaceoptionsconfiguration.replacementsmapfile";

  private static final String HELP_REPLACE_BLANK_SPACES = PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP_GUI;
  private static final String HELP_REPLACE_SPECIAL_CHARACTERS = PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP_GUI;
  private static final String HELP_REPLACEMENT = PARAM_CONFIG_REPLACEMENT_HELP_GUI;
  private static final String HELP_SAVE_REPLACEMENTS_MAP = PARAM_CONFIG_SAVE_REPLACEMENTS_HELP_GUI;
  private static final String HELP_REPLACEMENTS_MAP_FILE = PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP_GUI;

  private JXTextField replacementStringTextField;
  private JCheckBox replaceBlankSpacesCheckbox;
  private JCheckBox replaceSpecialCharactersCheckbox;
  private JCheckBox saveReplacementsMapCheckbox;
  private JFileChooserPanel replacementsMapFileChooserPanel;

  public ReplaceOptionsConfigurationPanel() {
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
    parameters.add(getReplaceBlankSpacesParameter());
    parameters.add(getReplaceSpecialCharactersParameter());
    parameters.add(getReplacementStringParameter());
    parameters.add(getSaveReplacementsMapParameter());
    parameters.add(getReplacementsMapFileParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getReplaceBlankSpacesParameter() {
    this.replaceBlankSpacesCheckbox = new JCheckBox();
    this.replaceBlankSpacesCheckbox.addItemListener(this::replaceBlankSpacesChanged);

    return new InputParameter(
      PARAM_CONFIG_REPLACE_BLANK_SPACES_DESCRIPTION + ":", this.replaceBlankSpacesCheckbox, HELP_REPLACE_BLANK_SPACES
    );
  }

  private void replaceBlankSpacesChanged(ItemEvent event) {
    this.firePropertyChange(PROPERTY_REPLACE_BLANK_SPACES, null, isReplaceBlankSpaces());
  }

  public boolean isReplaceBlankSpaces() {
    return this.replaceBlankSpacesCheckbox.isSelected();
  }

  private InputParameter getReplaceSpecialCharactersParameter() {
    this.replaceSpecialCharactersCheckbox = new JCheckBox();
    this.replaceSpecialCharactersCheckbox.addItemListener(this::replaceSpecialCharactersChanged);

    return new InputParameter(
      PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_DESCRIPTION + ":", this.replaceSpecialCharactersCheckbox,
      HELP_REPLACE_SPECIAL_CHARACTERS
    );
  }

  private void replaceSpecialCharactersChanged(ItemEvent event) {
    this.firePropertyChange(PROPERTY_REPLACE_SPECIAL_CHARACTERS, null, isReplaceSpecialCharacters());
  }

  public boolean isReplaceSpecialCharacters() {
    return this.replaceSpecialCharactersCheckbox.isSelected();
  }

  private InputParameter getReplacementStringParameter() {
    this.replacementStringTextField = new JXTextField(PARAM_CONFIG_REPLACEMENT_DESCRIPTION);
    this.replacementStringTextField.getDocument().addDocumentListener(
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
          SwingUtilities.invokeLater(ReplaceOptionsConfigurationPanel.this::replacementStringChanged);
        }
      }
    );

    return new InputParameter(
      PARAM_CONFIG_REPLACEMENT_DESCRIPTION + ":", this.replacementStringTextField, HELP_REPLACEMENT
    );
  }

  private void replacementStringChanged() {
    this.firePropertyChange(PROPERTY_REPLACEMENT_STRING, null, getReplacementString());
  }

  public String getReplacementString() {
    return this.replacementStringTextField.getText();
  }

  private InputParameter getSaveReplacementsMapParameter() {
    this.saveReplacementsMapCheckbox = new JCheckBox();
    this.saveReplacementsMapCheckbox.addItemListener(this::saveReplacementsMapChanged);

    return new InputParameter(
      PARAM_CONFIG_SAVE_REPLACEMENTS_DESCRIPTION + ":", this.saveReplacementsMapCheckbox, HELP_SAVE_REPLACEMENTS_MAP
    );
  }

  private void saveReplacementsMapChanged(ItemEvent event) {
    this.firePropertyChange(PROPERTY_SAVE_REPLACEMENTS_MAP, null, isSaveReplacementsMap());
  }

  public boolean isSaveReplacementsMap() {
    return this.saveReplacementsMapCheckbox.isSelected();
  }

  private InputParameter getReplacementsMapFileParameter() {
    this.replacementsMapFileChooserPanel =
      JFileChooserPanelBuilder.createSaveJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withLabel("")
        .withClearSelectedFileOnShow(true)
        .build();
    this.replacementsMapFileChooserPanel.addFileChooserListener(this::replacementsMapFileChanged);

    return new InputParameter(
      PARAM_CONFIG_REPLACEMENTS_MAP_FILE_DESCRIPTION + ":", this.replacementsMapFileChooserPanel,
      HELP_REPLACEMENTS_MAP_FILE
    );
  }

  private void replacementsMapFileChanged(ChangeEvent arg0) {
    this.firePropertyChange(PROPERTY_REPLACEMENTS_MAP_FILE, null, getReplacementsMapFile());
  }

  public File getReplacementsMapFile() {
    return this.replacementsMapFileChooserPanel.getSelectedFile();
  }

  public void setReplaceBlankSpaces(boolean newReplaceBlankSpaces) {
    this.replaceBlankSpacesCheckbox.setSelected(newReplaceBlankSpaces);
  }

  public void setReplaceSpecialCharacters(boolean replaceSpecialCharacters) {
    this.replaceSpecialCharactersCheckbox.setSelected(replaceSpecialCharacters);
  }

  public void setReplacementString(String newReplacementString) {
    this.replacementStringTextField.setText(newReplacementString);
  }

  public void setSaveReplacementsMap(boolean newSaveReplacementsMap) {
    this.saveReplacementsMapCheckbox.setSelected(newSaveReplacementsMap);
  }

  public void setReplacementsMapFile(File newReplacementsMapFile) {
    this.replacementsMapFileChooserPanel.setSelectedFile(newReplacementsMapFile);
  }
}
