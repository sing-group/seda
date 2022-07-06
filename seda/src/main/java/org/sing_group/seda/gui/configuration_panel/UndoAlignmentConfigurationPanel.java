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
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.*;

import javax.swing.*;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.transformation.provider.undoalignment.UndoAlignmentTransformationProvider;

public class UndoAlignmentConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private UndoAlignmentTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;

  public UndoAlignmentConfigurationPanel() {
    this.init();
    this.transformationProvider = new UndoAlignmentTransformationProvider();
    this.transformationProvider.setReformatFastaTransformationProvider(this.reformatPanel.getTransformationProvider());
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(getReformatFastaConfigurationPanel());
  }

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(createTitledBorder("Reformat output files"));
    return this.reformatPanel;
  }

  public UndoAlignmentTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(UndoAlignmentTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;
    if (this.transformationProvider.getReformatFastaTransformationProvider() != null) {
      this.reformatPanel
        .setTransformationProvider(this.transformationProvider.getReformatFastaTransformationProvider());
    }
  }
}
