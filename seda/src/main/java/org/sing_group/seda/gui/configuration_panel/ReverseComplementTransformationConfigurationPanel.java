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
package org.sing_group.seda.gui.configuration_panel;

import static java.awt.BorderLayout.CENTER;
import static java.util.Arrays.asList;
import static javax.swing.BorderFactory.createTitledBorder;
import static org.sing_group.seda.plugin.core.info.plugin.ReverseComplementSedaPluginInfo.PARAM_COMPLEMENT_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReverseComplementSedaPluginInfo.PARAM_COMPLEMENT_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.ReverseComplementSedaPluginInfo.PARAM_RENAME_SEQUENCE_HEADERS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReverseComplementSedaPluginInfo.PARAM_RENAME_SEQUENCE_HEADERS_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.ReverseComplementSedaPluginInfo.PARAM_REVERSE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.ReverseComplementSedaPluginInfo.PARAM_REVERSE_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;

import javax.swing.*;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.configuration_panel.rename.AddStringHeaderRenamePanel;
import org.sing_group.seda.transformation.provider.reverse_complement.ReverseComplementTransformationProvider;

public class ReverseComplementTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private ReverseComplementTransformationProvider transformationProvider;

  private JCheckBox reverseCb;
  private JCheckBox complementCb;
  private JCheckBox renameSequenceHeadersCb;
  private RadioButtonsPanel<HeaderTarget> headerTargetRbtnPanel;
  private AddStringHeaderRenamePanel addStringHeaderRenamePanel;

  public ReverseComplementTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] parameters = new InputParameter[4];
    parameters[0] = getReverseParameter();
    parameters[1] = getComplementParameter();
    parameters[2] = getRenameSequenceHeadersParameter();
    parameters[3] = getHeaderRenamerParameter();

    return parameters;
  }

  private InputParameter getReverseParameter() {
    this.reverseCb = new JCheckBox(PARAM_REVERSE_DESCRIPTION, true);
    this.reverseCb.addItemListener((e) -> reverseChanged());

    return new InputParameter("", this.reverseCb, PARAM_REVERSE_HELP_GUI);
  }

  private void reverseChanged() {
    this.transformationProvider.setReverseSequences(this.reverseCb.isSelected());
  }

  private InputParameter getComplementParameter() {
    this.complementCb = new JCheckBox(PARAM_COMPLEMENT_DESCRIPTION, true);
    this.complementCb.addItemListener((e) -> complementChanged());

    return new InputParameter("", this.complementCb, PARAM_COMPLEMENT_HELP_GUI);
  }

  private void complementChanged() {
    this.transformationProvider.setComplementSequences(this.complementCb.isSelected());
  }

  private InputParameter getRenameSequenceHeadersParameter() {
    this.renameSequenceHeadersCb = new JCheckBox(PARAM_RENAME_SEQUENCE_HEADERS_DESCRIPTION, false);
    this.renameSequenceHeadersCb.addItemListener((e) -> renameSequenceHeadersConfigurationChanged());

    return new InputParameter(
      "", this.renameSequenceHeadersCb, PARAM_RENAME_SEQUENCE_HEADERS_HELP_GUI
    );
  }

  private void renameSequenceHeadersConfigurationChanged() {
    this.renameConfigurationChanged();
  }

  private InputParameter getHeaderRenamerParameter() {
    this.headerTargetRbtnPanel = new RadioButtonsPanel<>(HeaderTarget.values(), 1, 2);
    this.headerTargetRbtnPanel.addItemListener(this::headerTargetValueChanged);

    this.addStringHeaderRenamePanel =
      new AddStringHeaderRenamePanel(
        false,
        asList(new InputParameter("Target", this.headerTargetRbtnPanel, "The header target."))
      );
    this.addStringHeaderRenamePanel.addRenamePanelEventListener(source -> renameConfigurationChanged());
    this.addStringHeaderRenamePanel.setBorder(createTitledBorder("Renaming configuration"));

    return new InputParameter("", addStringHeaderRenamePanel, "The sequence headers renaming configuration.");
  }

  private void headerTargetValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      renameConfigurationChanged();
    }
  }

  private void renameConfigurationChanged() {
    if (this.renameSequenceHeadersCb.isSelected() && this.addStringHeaderRenamePanel.isValidConfiguration()) {
      this.transformationProvider.setHeaderRenamer((AddStringHeaderRenamer) getHeaderRenamer());
    } else {
      this.transformationProvider.clearHeaderRenamer();
    }
  }

  public HeaderRenamer getHeaderRenamer() {
    return this.addStringHeaderRenamePanel.getHeaderRenamer(getHeaderTarget());
  }

  private HeaderTarget getHeaderTarget() {
    return this.headerTargetRbtnPanel.getSelectedItem().get();
  }

  public ReverseComplementTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  private void initTransformationProvider() {
    this.transformationProvider =
      new ReverseComplementTransformationProvider(this.reverseCb.isSelected(), this.complementCb.isSelected());
  }

  public void setTransformationProvider(ReverseComplementTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;
    this.reverseCb.setSelected(this.transformationProvider.isReverseSequences());
    this.complementCb.setSelected(this.transformationProvider.isComplementSequences());

    HeaderRenamer renamer = this.transformationProvider.getHeaderRenamer();
    if (renamer != null) {
      this.addStringHeaderRenamePanel.setHeaderRenamer((AddStringHeaderRenamer) renamer);
      this.headerTargetRbtnPanel.setSelectedItem(renamer.getHeaderTarget());
      this.renameSequenceHeadersCb.setSelected(true);
    }
  }
}
