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
package org.sing_group.seda.gui.isoforms;

import static java.util.Arrays.asList;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.DEFAULT_REMOVE_ISOFORM_TIE_BREAK_OPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_REFERENCE_SIZE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_REFERENCE_SIZE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_TIE_BREAK_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_TIE_BREAK_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector.TieBreakOption;

public class DefaultSequenceIsoformConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String DESCRIPTION_REFERENCE_SIZE = PARAM_REFERENCE_SIZE_HELP_GUI;
  public static final String DESCRIPTION_TIE_BREAK = PARAM_TIE_BREAK_HELP_GUI;

  public static final int DEFAULT_REFERENCE_SIZE = 250;
  public static final TieBreakOption DEFAULT_TIE_BREAK_OPTION = DEFAULT_REMOVE_ISOFORM_TIE_BREAK_OPTION;

  public static final String PROPERTY_REFERENCE_SIZE = "property.reference.size";
  public static final String PROPERTY_TIE_BREAK_OPTION = "property.tie.break.option";

  public static final Set<String> PROPERTIES =
    new HashSet<>(
      asList(PROPERTY_TIE_BREAK_OPTION, PROPERTY_REFERENCE_SIZE)
    );

  private JIntegerTextField referenceSizeTf;
  private RadioButtonsPanel<TieBreakOption> tieBreakOptionRbtn;

  private int oldReferenceSizeValue = DEFAULT_REFERENCE_SIZE;
  private TieBreakOption oldTieBreakOption = DEFAULT_TIE_BREAK_OPTION;

  public DefaultSequenceIsoformConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(new InputParametersPanel(getInputParameters()));
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getReferenceSizeParameter());
    parameters.add(getTieBreakOptionParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getReferenceSizeParameter() {
    this.referenceSizeTf = new JIntegerTextField(DEFAULT_REFERENCE_SIZE);
    this.referenceSizeTf.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        referenceSizeChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        referenceSizeChanged();
      }
    });

    return new InputParameter(
      PARAM_REFERENCE_SIZE_DESCRIPTION + ": ", this.referenceSizeTf, DESCRIPTION_REFERENCE_SIZE
    );
  }

  private void referenceSizeChanged() {
    int newValue = getReferenceSizeValue();
    this.firePropertyChange(PROPERTY_REFERENCE_SIZE, oldReferenceSizeValue, newValue);
    this.oldReferenceSizeValue = newValue;
  }

  public int getReferenceSizeValue() {
    return this.referenceSizeTf.getValue();
  }

  private InputParameter getTieBreakOptionParameter() {
    this.tieBreakOptionRbtn = new RadioButtonsPanel<>(TieBreakOption.values(), 1, TieBreakOption.values().length);
    this.tieBreakOptionRbtn.setSelectedItem(DEFAULT_TIE_BREAK_OPTION);
    this.tieBreakOptionRbtn.addItemListener(this::tieBreakOptionChanged);

    return new InputParameter(PARAM_TIE_BREAK_DESCRIPTION + ": ", this.tieBreakOptionRbtn, DESCRIPTION_TIE_BREAK);
  }

  private void tieBreakOptionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      TieBreakOption newValue = getTieBreakOption();
      this.firePropertyChange(PROPERTY_TIE_BREAK_OPTION, oldTieBreakOption, newValue);
      this.oldTieBreakOption = newValue;
    }
  }

  private TieBreakOption getTieBreakOption() {
    return this.tieBreakOptionRbtn.getSelectedItem().get();
  }

  public DefaultSequenceIsoformSelector getSelector() {
    return new DefaultSequenceIsoformSelector(this.getReferenceSizeValue(), this.getTieBreakOption());
  }

  public void setSelector(DefaultSequenceIsoformSelector selector) {
    this.referenceSizeTf.setValue(selector.getReferenceSize());
    this.tieBreakOptionRbtn.setSelectedItem(selector.getTieBreak());
  }
}
