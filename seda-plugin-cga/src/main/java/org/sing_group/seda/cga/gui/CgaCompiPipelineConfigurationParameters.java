/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.gui;

import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration.DEFAULT_INTRON_BP;
import static org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration.DEFAULT_MAX_DIST;
import static org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration.DEFAULT_MIN_FULL_NUCLEOTIDE_SIZE;
import static org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration.DEFAULT_SELECTION_CORRECTION;
import static org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration.DEFAULT_SELECTION_CRITERION;

import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import org.sing_group.gc4s.event.RunnableDocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration;

public class CgaCompiPipelineConfigurationParameters {

  private static final String HELP_MAX_DIST = "<html>Maximum distance between exons (in this case sequences identified by getorf) from the same gene.<br/><br/>"
    + "It only applies to large genome sequences where there is some chance that two genes with similar features are present.</html>";
  private static final String HELP_INTRON_BP = "Distance around the junction point between two sequences where to look for splicing signals.";
  private static final String HELP_MIN_FULL_NUCLEOTIDE_SIZE = "Minimum size for CDS to be reported";
  private static final String HELP_SELECTION_CRITERION = "<html>The selection model to be used: <ol><li>"
    + CgaCompiPipelineConfiguration.SelectionCriterion.CRITERION_1.getDescription() + "</li><li>"
    + CgaCompiPipelineConfiguration.SelectionCriterion.CRITERION_2.getDescription() + "</li><li>"
    + CgaCompiPipelineConfiguration.SelectionCriterion.CRITERION_3.getDescription() + "</li></ol></html>";
  private static final String HELP_SELECTION_CORRECTION = "A bonus percentage times 10. For instance, 20 means 2% bonus. Something with 18% similarity acts as having 20% similarity.";
  private static final String HELP_SKIP_PULL_DOCKER_IMAGES = "Use this flag to skip the pull-docker-images task.";

  private JIntegerTextField maxDist;
  private JIntegerTextField intronBp;
  private JIntegerTextField minFullNucleotideSize;
  private JComboBox<CgaCompiPipelineConfiguration.SelectionCriterion> selectionCriterion;
  private JIntegerTextField selectionCorrection;
  private JCheckBox skipPullDockerImages;

  private Runnable onConfigurationChanged;

  public CgaCompiPipelineConfigurationParameters(Runnable onConfigurationChanged) {
    this.onConfigurationChanged = onConfigurationChanged;
  }

  public InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<InputParameter>();
    parameters.add(getMaxDistParameter());
    parameters.add(getIntronBpParameter());
    parameters.add(getMinFullNucleotideSizeParameter());
    parameters.add(getSelectionCriterionParameter());
    parameters.add(getSelectionCorrectionParameter());
    parameters.add(getSkipPullDockerImagesParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getMaxDistParameter() {
    this.maxDist = new JIntegerTextField(DEFAULT_MAX_DIST);
    this.maxDist.getDocument().addDocumentListener(new RunnableDocumentAdapter(onConfigurationChanged));

    return new InputParameter("Max. dist.:", this.maxDist, HELP_MAX_DIST);
  }

  private InputParameter getIntronBpParameter() {
    this.intronBp = new JIntegerTextField(DEFAULT_INTRON_BP);
    this.intronBp.getDocument().addDocumentListener(new RunnableDocumentAdapter(onConfigurationChanged));

    return new InputParameter("Intron BP:", this.intronBp, HELP_INTRON_BP);
  }

  private InputParameter getMinFullNucleotideSizeParameter() {
    this.minFullNucleotideSize = new JIntegerTextField(DEFAULT_MIN_FULL_NUCLEOTIDE_SIZE);
    this.minFullNucleotideSize.getDocument().addDocumentListener(new RunnableDocumentAdapter(onConfigurationChanged));

    return new InputParameter("Min. CDS size:", this.minFullNucleotideSize, HELP_MIN_FULL_NUCLEOTIDE_SIZE);
  }

  private InputParameter getSelectionCriterionParameter() {
    this.selectionCriterion = new JComboBox<>(CgaCompiPipelineConfiguration.SelectionCriterion.values());
    this.selectionCriterion.setSelectedItem(DEFAULT_SELECTION_CRITERION);
    this.selectionCriterion.addItemListener(this::selectionCriterionChanged);

    return new InputParameter("Selection criterion: ", this.selectionCriterion, HELP_SELECTION_CRITERION);
  }

  private void selectionCriterionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(this.onConfigurationChanged);
    }
  }

  private InputParameter getSelectionCorrectionParameter() {
    this.selectionCorrection = new JIntegerTextField(DEFAULT_SELECTION_CORRECTION);
    this.selectionCorrection.getDocument().addDocumentListener(new RunnableDocumentAdapter(onConfigurationChanged));

    return new InputParameter("Selection correction:", this.selectionCorrection, HELP_SELECTION_CORRECTION);
  }

  private InputParameter getSkipPullDockerImagesParameter() {
    this.skipPullDockerImages = new JCheckBox();
    this.skipPullDockerImages.addItemListener(this::skipPullDockerImagesChanged);

    return new InputParameter("Skip pull Docker images:", this.skipPullDockerImages, HELP_SKIP_PULL_DOCKER_IMAGES);
  }

  private void skipPullDockerImagesChanged(ItemEvent event) {
    invokeLater(this.onConfigurationChanged);
  }

  public CgaCompiPipelineConfiguration getConfiguration() {
    return new CgaCompiPipelineConfiguration(
      this.maxDist.getValue(), this.intronBp.getValue(), this.minFullNucleotideSize.getValue(),
      (CgaCompiPipelineConfiguration.SelectionCriterion) this.selectionCriterion.getSelectedItem(),
      this.selectionCorrection.getValue(), this.skipPullDockerImages.isSelected()
    );
  }

  public void setConfiguration(CgaCompiPipelineConfiguration cgaCompiPipelineConfiguration) {
    this.intronBp.setValue(cgaCompiPipelineConfiguration.getIntronBp());
    this.maxDist.setValue(cgaCompiPipelineConfiguration.getMaxDist());
    this.minFullNucleotideSize.setValue(cgaCompiPipelineConfiguration.getMinFullNucleotideSize());
    this.selectionCorrection.setValue(cgaCompiPipelineConfiguration.getSelectionCorrection());
    if (cgaCompiPipelineConfiguration.getSelectionCriterion() != null) {
      this.selectionCriterion.setSelectedItem(cgaCompiPipelineConfiguration.getSelectionCriterion());
    }
    this.skipPullDockerImages.setSelected(cgaCompiPipelineConfiguration.isSkipPullDockerImages());
    invokeLater(this.onConfigurationChanged);
  }
}
