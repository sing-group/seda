/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_CODON_DESCRIPTION;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_CODON_HELP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_SPECIES_DESCRIPTION;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.PARAM_SAPP_SPECIES_HELP;

import java.awt.BorderLayout;
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
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;
import org.sing_group.seda.sapp.gui.execution.SappExecutionConfigurationPanel;
import org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationProvider;

public class SappAnnotationTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SappAnnotationTransformationProvider transformationProvider;
  private JComboBox<SappCodon> codonCombobox;
  private JComboBox<SappSpecies> speciesCombobox;
  private SappExecutionConfigurationPanel sappExecutionConfigurationPanel;
  private BedToolsExecutionConfigurationPanel bedToolsExecutionConfigurationPanel;

  public SappAnnotationTransformationConfigurationPanel() {
    this.init();
    this.initTranformationProvider();
  }

  private void initTranformationProvider() {
    this.transformationProvider =
      new SappAnnotationTransformationProvider(
        (SappSpecies) this.speciesCombobox.getSelectedItem(),
        (SappCodon) this.codonCombobox.getSelectedItem()
      );
    this.sappExecutorChanged();
    this.bedToolsExecutorChanged();
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
    return new InputParametersPanel(getParameters());
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
    this.sappExecutorChanged();
  }

  private void sappExecutorChanged() {
    invokeLater(() -> {
      this.transformationProvider
        .setSappBinariesExecutor(this.sappExecutionConfigurationPanel.getBinariesExecutor());
    });
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
    invokeLater(() -> {
      this.transformationProvider
        .setBedToolsBinariesExecutor(this.bedToolsExecutionConfigurationPanel.getBinariesExecutor());
    });
  }

  public Optional<BedToolsBinariesExecutor> getBedToolsBinariesExecutor() {
    return this.bedToolsExecutionConfigurationPanel.getBinariesExecutor();
  }

  private InputParameter getSappSpeciesParameter() {
    this.speciesCombobox = new JComboBox<>(SappSpecies.values());
    this.speciesCombobox.addItemListener(this::sappSpeciesChanged);

    return new InputParameter(PARAM_SAPP_SPECIES_DESCRIPTION + ":", this.speciesCombobox, PARAM_SAPP_SPECIES_HELP);
  }

  private void sappSpeciesChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setSappSpecies((SappSpecies) this.speciesCombobox.getSelectedItem());
      });
    }
  }

  private InputParameter getSappCodonParameter() {
    this.codonCombobox = new JComboBox<>(SappCodon.values());
    this.codonCombobox.addItemListener(this::codonChanged);

    return new InputParameter(PARAM_CODON_DESCRIPTION + ":", this.codonCombobox, PARAM_CODON_HELP);
  }

  private void codonChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setSappCodon((SappCodon) this.codonCombobox.getSelectedItem());
      });
    }
  }

  public SappAnnotationTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(SappAnnotationTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    if (this.transformationProvider.getSappCodon() != null) {
      this.codonCombobox.setSelectedItem(this.transformationProvider.getSappCodon());
    }
    if (this.transformationProvider.getSappSpecies() != null) {
      this.speciesCombobox.setSelectedItem(this.transformationProvider.getSappSpecies());
    }
    if (this.transformationProvider.getSappBinariesExecutor() != null) {
      this.sappExecutionConfigurationPanel.setBinariesExecutor(this.transformationProvider.getSappBinariesExecutor());
    }
    if (this.transformationProvider.getBedToolsBinariesExecutor() != null) {
      this.bedToolsExecutionConfigurationPanel
        .setBinariesExecutor(this.transformationProvider.getBedToolsBinariesExecutor());
    }
  }
}
