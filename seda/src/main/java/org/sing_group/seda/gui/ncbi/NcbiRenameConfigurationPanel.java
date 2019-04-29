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

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class NcbiRenameConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private FileRenameConfigurationPanel fileRenamePanel;
  private SequenceHeaderRenameConfigurationPanel sequenceHeaderRenamePanel;
  private ReplaceOptionsConfigurationPanel replaceOptionsConfigurationPanel;
  private NcbiTaxonomyConfigurationPanel ncbiTaxonomyConfigurationPanel;
  private NcbiRenameTransformationProvider transformationProvider;

  public NcbiRenameConfigurationPanel() {
    this.init();
    this.transformationProvider =
      new NcbiRenameTransformationProvider(
        this.fileRenamePanel, this.sequenceHeaderRenamePanel,
        this.replaceOptionsConfigurationPanel, this.ncbiTaxonomyConfigurationPanel
      );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(getFileRenameConfigurationPanel());
    mainPanel.add(getSequenceHeaderRenameConfigurationPanel());
    mainPanel.add(getReplaceOptionsConfigurationPanel());
    mainPanel.add(getNcbiTaxonomyConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private Component getFileRenameConfigurationPanel() {
    fileRenamePanel = new FileRenameConfigurationPanel();
    addTitledBorder(fileRenamePanel, "File name");

    return fileRenamePanel;
  }

  private Component getSequenceHeaderRenameConfigurationPanel() {
    sequenceHeaderRenamePanel = new SequenceHeaderRenameConfigurationPanel();
    addTitledBorder(sequenceHeaderRenamePanel, "Sequence headers");

    return sequenceHeaderRenamePanel;
  }

  private Component getReplaceOptionsConfigurationPanel() {
    replaceOptionsConfigurationPanel = new ReplaceOptionsConfigurationPanel();
    addTitledBorder(replaceOptionsConfigurationPanel, "Configuration");

    return replaceOptionsConfigurationPanel;
  }

  private Component getNcbiTaxonomyConfigurationPanel() {
    ncbiTaxonomyConfigurationPanel = new NcbiTaxonomyConfigurationPanel();
    addTitledBorder(ncbiTaxonomyConfigurationPanel, "NCBI Taxonomy information");

    return ncbiTaxonomyConfigurationPanel;
  }

  private static void addTitledBorder(JComponent component, String title) {
    component.setBorder(BorderFactory.createTitledBorder(title));
  }

  public NcbiRenameTransformationProvider getModel() {
    return this.transformationProvider;
  }
}
