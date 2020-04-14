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
package org.sing_group.seda.gui.reverse_complement;

import static java.awt.BorderLayout.CENTER;
import static java.util.Arrays.asList;
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.rename.AddStringHeaderRenamePanel;

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
    this.reverseCb = new JCheckBox("Reverse", true);
    this.reverseCb.addItemListener((e) -> reverseChanged());

    return new InputParameter("", this.reverseCb, "Check this option to reverse sequences.");
  }

  private void reverseChanged() {
    this.transformationProvider.setReverseSequences(this.reverseCb.isSelected());
  }

  private InputParameter getComplementParameter() {
    this.complementCb = new JCheckBox("Complement", true);
    this.complementCb.addItemListener((e) -> complementChanged());

    return new InputParameter("", this.complementCb, "Check this option to complement sequences.");
  }

  private void complementChanged() {
    this.transformationProvider.setComplementSequences(this.complementCb.isSelected());
  }

  private InputParameter getRenameSequenceHeadersParameter() {
    this.renameSequenceHeadersCb = new JCheckBox("Rename sequence headers", false);
    this.renameSequenceHeadersCb.addItemListener((e) -> renameSequenceHeadersConfigurationChanged());

    return new InputParameter(
      "", this.renameSequenceHeadersCb, "Check this option to rename sequence headers using the configuration below."
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
