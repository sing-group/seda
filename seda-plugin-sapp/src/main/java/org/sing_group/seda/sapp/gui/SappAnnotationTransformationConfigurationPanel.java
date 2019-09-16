/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.gui.BedToolsExecutionConfigurationPanel;
import org.sing_group.seda.gui.execution.BinaryExecutionConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;
import org.sing_group.seda.sapp.gui.execution.SappExecutionConfigurationPanel;

public class SappAnnotationTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_SAPP_SPECIES = "The species to use.";
  private static final String HELP_CODON = "The codon table to use.";

  private SappAnnotationTransformationProvider transformationProvider;
  private JComboBox<SappCodon> codonCombobox;
  private JComboBox<SappSpecies> sappSpeciesCombobox;
  private SappExecutionConfigurationPanel sappExecutionConfigurationPanel;

  private BedToolsExecutionConfigurationPanel bedToolsExecutionConfigurationPanel;

  public SappAnnotationTransformationConfigurationPanel() {
    this.init();
    this.transformationProvider = new SappAnnotationTransformationProvider(this);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));
    mainPanel.add(getParametersPanel());

    return new CenteredJPanel(mainPanel);
  }

  private InputParametersPanel getParametersPanel() {
    InputParametersPanel queryConfigurationPanel = new InputParametersPanel(getParameters());

    return queryConfigurationPanel;
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<InputParameter>();
    parameters.add(getSappExecutableParameter());
    parameters.add(getBedToolsExecutableParameter());
    parameters.add(getSappSpeciesParameter());
    parameters.add(getSappCodonParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getSappExecutableParameter() {
    this.sappExecutionConfigurationPanel = new SappExecutionConfigurationPanel(this::sappExecutorChanged);
    this.sappExecutionConfigurationPanel.setBorder(BorderFactory.createTitledBorder("SAPP configuration"));

    return new InputParameter("", sappExecutionConfigurationPanel, "The mode to execute SAPP.");
  }

  private void sappExecutorChanged(BinaryExecutionConfigurationPanel<SappBinariesExecutor> source) {
    notifyTransformationProvider(this.transformationProvider::sappExecutorChanged);
  }

  public Optional<SappBinariesExecutor> getSappBinariesExecutor() {
    return this.sappExecutionConfigurationPanel.getBinariesExecutor();
  }

  private InputParameter getBedToolsExecutableParameter() {
    this.bedToolsExecutionConfigurationPanel = new BedToolsExecutionConfigurationPanel(this::bedToolsExecutorChanged);
    this.bedToolsExecutionConfigurationPanel.setBorder(createTitledBorder("bedtools configuration"));

    return new InputParameter("", bedToolsExecutionConfigurationPanel, "The mode to execute bedtools.");
  }

  private void bedToolsExecutorChanged(BinaryExecutionConfigurationPanel<BedToolsBinariesExecutor> source) {
    this.bedToolsExecutorChanged();
  }

  private void bedToolsExecutorChanged() {
    notifyTransformationProvider(this.transformationProvider::bedToolsExecutorChanged);
  }

  private void notifyTransformationProvider(Runnable r) {
    invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      r.run();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  public Optional<BedToolsBinariesExecutor> getBedToolsBinariesExecutor() {
    return this.bedToolsExecutionConfigurationPanel.getBinariesExecutor();
  }

  private InputParameter getSappSpeciesParameter() {
    this.sappSpeciesCombobox = new JComboBox<>(SappSpecies.values());
    this.sappSpeciesCombobox.addItemListener(this::sappSpeciesChanged);

    return new InputParameter("Species:", this.sappSpeciesCombobox, HELP_SAPP_SPECIES);
  }

  private void sappSpeciesChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      notifyTransformationProvider(this.transformationProvider::sappSpeciesChanged);
    }
  }

  public SappSpecies getSappSpecies() {
    return (SappSpecies) this.sappSpeciesCombobox.getSelectedItem();
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }

  private InputParameter getSappCodonParameter() {
    this.codonCombobox = new JComboBox<>(SappCodon.values());
    this.codonCombobox.addItemListener(this::codonChanged);

    return new InputParameter("Codon:", this.codonCombobox, HELP_CODON);
  }

  private void codonChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      notifyTransformationProvider(this.transformationProvider::sappCodonChanged);
    }
  }

  public SappCodon getSappCodon() {
    return (SappCodon) this.codonCombobox.getSelectedItem();
  }
}
