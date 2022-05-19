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
package org.sing_group.seda.gui.reformat;

import static org.sing_group.seda.gui.GuiUtils.bindCheckBox;
import static org.sing_group.seda.gui.GuiUtils.bindIntegerTextField;
import static org.sing_group.seda.gui.GuiUtils.bindRadioButtonsPanel;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_FRAGMENT_LENGHT_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_LINE_BREAKS_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_REMOVE_LINE_BREAKS_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReformatFastaSedaPluginInfo.PARAM_SEQUENCE_CASE_HELP_GUI;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.datatype.SequenceCase;
import org.sing_group.seda.io.LineBreakType;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class ReformatFastaConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private TransformationChangeListener transformationChangeListener = new TransformationChangeListener() {

    @Override
    public void onTransformationChange(TransformationChangeEvent event) {
      modelChanged(event);
    }
  };

  private ReformatFastaTransformationProvider transformationProvider;

  private JIntegerTextField fragmentLength;
  private JCheckBox removeLineBreaks;
  private RadioButtonsPanel<LineBreakType> lineBreakTypeRbtn;
  private RadioButtonsPanel<SequenceCase> sequenceCaseRbtn;

  public ReformatFastaConfigurationPanel() {
    this.transformationProvider = new ReformatFastaTransformationProvider();
    this.init();
    this.bindGuiControls();
    this.transformationProvider.addTransformationChangeListener(transformationChangeListener);
  }

  private void bindGuiControls() {
    bindIntegerTextField(this.fragmentLength, this.transformationProvider::setFragmentLength);
    bindCheckBox(this.removeLineBreaks, this.transformationProvider::setRemoveLineBreaks);
    bindRadioButtonsPanel(this.lineBreakTypeRbtn, this.transformationProvider::setLineBreakType);
    bindRadioButtonsPanel(this.sequenceCaseRbtn, this.transformationProvider::setSequenceCase);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new InputParametersPanel(getParameters());

    return new CenteredJPanel(mainPanel);
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getFragmentLengthParameter());
    parameters.add(getRemoveLineBreaksParameter());
    parameters.add(getLineBreakTypeParameter());
    parameters.add(getSequenceCaseParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getFragmentLengthParameter() {
    this.fragmentLength = new JIntegerTextField(this.transformationProvider.getFragmentLength());

    return new InputParameter(
      PARAM_FRAGMENT_LENGHT_DESCRIPTION + ":", this.fragmentLength, PARAM_FRAGMENT_LENGHT_HELP_GUI
    );
  }

  private InputParameter getRemoveLineBreaksParameter() {
    this.removeLineBreaks =
      new JCheckBox(
        PARAM_REMOVE_LINE_BREAKS_DESCRIPTION, this.transformationProvider.isRemoveLineBreaks()
      );

    return new InputParameter("", this.removeLineBreaks, PARAM_REMOVE_LINE_BREAKS_HELP_GUI);
  }

  private InputParameter getLineBreakTypeParameter() {
    this.lineBreakTypeRbtn = new RadioButtonsPanel<>(LineBreakType.values(), 1, 0);
    this.lineBreakTypeRbtn.setSelectedItem(this.transformationProvider.getLineBreakType());

    return new InputParameter(PARAM_LINE_BREAKS_DESCRIPTION + ":", this.lineBreakTypeRbtn, PARAM_LINE_BREAKS_HELP_GUI);
  }

  private InputParameter getSequenceCaseParameter() {
    this.sequenceCaseRbtn = new RadioButtonsPanel<>(SequenceCase.values());
    this.sequenceCaseRbtn.setSelectedItem(this.transformationProvider.getSequenceCase());

    return new InputParameter(
      PARAM_SEQUENCE_CASE_DESCRIPTION + ":", this.sequenceCaseRbtn, PARAM_SEQUENCE_CASE_HELP_GUI
    );
  }

  private void modelChanged(TransformationChangeEvent event) {
    SwingUtilities.invokeLater(() -> {
      switch ((ReformatConfigurationChangeType) event.getType()) {
        case FRAGMENT_LENGTH_CHANGED:
          updateFragmentLength();
          break;
        case REMOVE_LINE_BREAKS_CHANGED:
          updateRemoveLineBreaks();
          break;
        case LINE_BREAK_TYPE_CHANGED:
          updateLineBreakType();
          break;
        case SEQUENCE_CASE_CHANGED:
          updateSequenceCase();
          break;
      }
    });
  }

  private void updateFragmentLength() {
    this.fragmentLength.setValue(this.transformationProvider.getFragmentLength());
  }

  private void updateRemoveLineBreaks() {
    this.removeLineBreaks.setSelected(this.transformationProvider.isRemoveLineBreaks());
    this.fragmentLength.setEditable(!this.transformationProvider.isRemoveLineBreaks());
  }

  private void updateLineBreakType() {
    this.lineBreakTypeRbtn.setSelectedItem(this.transformationProvider.getLineBreakType());
  }

  private void updateSequenceCase() {
    this.sequenceCaseRbtn.setSelectedItem(this.transformationProvider.getSequenceCase());
  }

  public ReformatFastaTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(ReformatFastaTransformationProvider transformationProvider) {
    this.transformationProvider.removeTranformationChangeListener(transformationChangeListener);
    this.transformationProvider = transformationProvider;
    this.bindGuiControls();
    this.transformationProvider.addTransformationChangeListener(transformationChangeListener);

    this.updateFragmentLength();
    this.updateRemoveLineBreaks();
    this.updateLineBreakType();
    this.updateSequenceCase();
  }
}
