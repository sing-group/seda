/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.uniprot.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;

import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.AlignmentCutoffOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.FilterOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.MatrixOption;

public class UniProtBlastTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_BLAST_DATABASE = "The target database.";
  private static final String HELP_OUTPUT_TYPE =
    "<html>The output type:<ul>"
      + "<li><i>Complete sequences</i>: creates a FASTA file with the complete sequences of each sequence that has an alignment against the query sequence.</li>"
      + "<li><i>Aligned sequences</i>: creates a FASTA file with the portions of the sequences aligned against the query.</li></ul>";
  private static final String HELP_MATRIX_TYPE =
    "<html>The scoring matrix to use.<br/>The matrix assigns a probability score for each "
      + "position in an alignment.<br/>The BLOSUM matrix assigns a probability score for each position in an alignment that is based on "
      + "the frequency with which that substitution is known to occur among consensus blocks within related proteins.<br/>BLOSUM62 is among the best "
      + "of the available matrices for detecting weak protein similarities. <br/>The PAM set of matrices is also available.</html>";
  private static final String HELP_GAPPED =
    "Whether the query is gapped or not. This will allow gaps to be introduced in the sequences when the comparison is done.";
  private static final String HELP_EXPECT_VALUE =
    "<html>The expectation value (E) threshold is a statistical measure of the number "
      + "of expected matches in a random database.<br/> The lower the e-value, the more likely the match is to be significant. "
      + "<br/>E-values between 0.1 and 10 are generally dubious, and over 10 are unlikely to have biological significance. "
      + "<br/>In all cases, those matches need to be verified manually. You may need to increase the E threshold if you have a very<br/>"
      + "short query sequence, to detect very weak similarities, or similarities in a short region, or if your sequence has a<br/>"
      + "low complexity region and you use the <i>filter</i> option.</html>";
  private static final String HELP_HITS = "Limits the number of returned alignments.";
  private static final String HELP_FILTER =
    "<html>Low-complexity regions (e.g. stretches of cysteine in Q03751, or hydrophobic regions in membrane proteins) tend to <br/>produce spurious, "
      + "insignificant matches with sequences in the database which have the same kind of low-complexity<br/>regions, but are unrelated biologically. "
      + "<br>If <i>Filter low complexity regions</i> is selected, the query sequence will be run through the program SEG, and all<br/>amino acids in "
      + "low-complexity regions will be replaced by X's. </html>";

  private UniProtBlastTransformationProvider transformationProvider;
  private RadioButtonsPanel<OutputTypeParameter> outputTypeParameter;
  private JComboBox<DatabaseOption> databaseCombobox;
  private JComboBox<ExpectValueOption> expectValue;
  private JComboBox<MatrixOption> matrixCombobox;
  private RadioButtonsPanel<FilterOption> filterRadioButtonsPanel;
  private JCheckBox gapped;
  private JComboBox<HitOption> hitsCombobox;

  public UniProtBlastTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider =
      new UniProtBlastTransformationProvider(
        this.getDatabase(), this.getOutputType(), this.getExpectValue(),
        this.getMatrix(), this.getFilter(), this.isGapped(), this.getHits()
      );
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    return new CenteredJPanel(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    List<InputParameter> dbParameters = new LinkedList<InputParameter>();
    dbParameters.add(getDatabaseParameter());
    dbParameters.add(getOutputTypeParameter());
    dbParameters.add(getExpectValueParameter());
    dbParameters.add(getMatrixParameter());
    dbParameters.add(getFilterParameter());
    dbParameters.add(getIsGappedParameter());
    dbParameters.add(getHitListSizeParameter());

    return dbParameters.toArray(new InputParameter[dbParameters.size()]);
  }

  private InputParameter getDatabaseParameter() {
    this.databaseCombobox = new JComboBox<>(DatabaseOption.values());
    this.databaseCombobox.addItemListener(this::blastDatabaseChanged);

    return new InputParameter("Database: ", databaseCombobox, HELP_BLAST_DATABASE);
  }

  private void blastDatabaseChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setDatabase(getDatabase());
      });
    }
  }

  private DatabaseOption getDatabase() {
    return (DatabaseOption) this.databaseCombobox.getSelectedItem();
  }

  private InputParameter getOutputTypeParameter() {
    this.outputTypeParameter = new RadioButtonsPanel<>(OutputTypeParameter.values(), 1, 2);
    this.outputTypeParameter.setSelectedItem(OutputTypeParameter.ALIGNED);
    this.outputTypeParameter.addItemListener(this::outputTypeChanged);

    return new InputParameter("Output type: ", this.outputTypeParameter, HELP_OUTPUT_TYPE);
  }

  private void outputTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setOutputType(getOutputType());
      });
    }
  }

  private OutputTypeParameter getOutputType() {
    return this.outputTypeParameter.getSelectedItem().get();
  }

  private InputParameter getExpectValueParameter() {
    this.expectValue = new JComboBox<>(getExpectValues());
    this.expectValue.setSelectedItem(new ExpectValueOption(ExpectationOption.TEN));
    this.expectValue.addItemListener(this::expectValueChanged);

    return new InputParameter("E-Threshold:", this.expectValue, HELP_EXPECT_VALUE);
  }

  private ExpectValueOption[] getExpectValues() {
    ExpectValueOption[] toret = new ExpectValueOption[ExpectationOption.values().length];
    for (int i = 0; i < toret.length; i++) {
      toret[i] = new ExpectValueOption(ExpectationOption.values()[i]);
    }

    return toret;
  }

  private void expectValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setExpectation(getExpectValue());
      });
    }
  }

  private ExpectationOption getExpectValue() {
    return ((ExpectValueOption) this.expectValue.getSelectedItem()).getExpectationOption();
  }

  private InputParameter getMatrixParameter() {
    this.matrixCombobox = new JComboBox<>(MatrixOption.values());
    this.matrixCombobox.setSelectedItem(MatrixOption.BLOSUM_62);
    this.matrixCombobox.addItemListener(this::matrixParameterChanged);

    return new InputParameter("Matrix: ", this.matrixCombobox, HELP_MATRIX_TYPE);
  }

  private void matrixParameterChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setMatrix(getMatrix());
      });
    }
  }

  private MatrixOption getMatrix() {
    return (MatrixOption) this.matrixCombobox.getSelectedItem();
  }

  private InputParameter getFilterParameter() {
    this.filterRadioButtonsPanel = new RadioButtonsPanel<>(FilterOption.values(), 1, 2);
    this.filterRadioButtonsPanel.setSelectedItem(FilterOption.YES);
    this.filterRadioButtonsPanel.addItemListener(this::filtereParameterChanged);

    return new InputParameter("Filtering: ", this.filterRadioButtonsPanel, HELP_FILTER);
  }

  private void filtereParameterChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setFilter(getFilter());
      });
    }
  }

  private FilterOption getFilter() {
    return this.filterRadioButtonsPanel.getSelectedItem().get();
  }

  private InputParameter getIsGappedParameter() {
    this.gapped = new JCheckBox();
    this.gapped.setSelected(true);
    this.gapped.addItemListener(this::gappedChanged);

    return new InputParameter("Gapped: ", gapped, HELP_GAPPED);
  }

  private void gappedChanged(ItemEvent event) {
    invokeLater(() -> {
      this.transformationProvider.setGappoed(isGapped());
    });
  }

  private boolean isGapped() {
    return this.gapped.isSelected();
  }

  private InputParameter getHitListSizeParameter() {
    this.hitsCombobox = new JComboBox<>(getHitValues());
    this.hitsCombobox.setSelectedItem(new HitOption(AlignmentCutoffOption.TWO_HUNDRED_FIFTY));
    this.hitsCombobox.addItemListener(this::hitsChanged);

    return new InputParameter("Hits: ", this.hitsCombobox, HELP_HITS);
  }

  public HitOption[] getHitValues() {
    HitOption[] toret = new HitOption[AlignmentCutoffOption.values().length];
    for (int i = 0; i < toret.length; i++) {
      toret[i] = new HitOption(AlignmentCutoffOption.values()[i]);
    }

    return toret;
  }

  private void hitsChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      invokeLater(() -> {
        this.transformationProvider.setHits(getHits());
      });
    }
  }

  private AlignmentCutoffOption getHits() {
    return ((HitOption) this.hitsCombobox.getSelectedItem()).getCutoff();
  }

  public UniProtBlastTransformationProvider getTransformationProvider() {
    return transformationProvider;
  }

  public void setTransformationProvider(UniProtBlastTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    DatabaseOption database = this.transformationProvider.getDatabase();
    if (database != null) {
      this.databaseCombobox.setSelectedItem(database);
    }

    OutputTypeParameter outputType = this.transformationProvider.getOutputType();
    if (outputType != null) {
      this.outputTypeParameter.setSelectedItem(outputType);
    }

    ExpectationOption expectValue = this.transformationProvider.getExpectValue();
    if (expectValue != null) {
      this.expectValue.setSelectedItem(new ExpectValueOption(expectValue));
    }

    FilterOption filter = this.transformationProvider.getFilter();
    if (filter != null) {
      this.filterRadioButtonsPanel.setSelectedItem(filter);
    }

    AlignmentCutoffOption hits = this.transformationProvider.getHits();
    if (hits != null) {
      this.hitsCombobox.setSelectedItem(new HitOption(hits));
    }

    MatrixOption matrix = this.transformationProvider.getMatrix();
    if (matrix != null) {
      this.matrixCombobox.setSelectedItem(matrix);
    }

    this.gapped.setSelected(this.transformationProvider.isGapped());
  }
}
