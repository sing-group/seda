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
package org.sing_group.seda.blast.ncbi.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.sing_group.gc4s.event.RunnableDocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParameterSeparator;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.DoubleTextField;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.utilities.ColorUtils;
import org.sing_group.seda.blast.ncbi.NcbiBlastDatabase;
import org.sing_group.seda.blast.ncbi.NcbiBlastType;
import org.sing_group.seda.blast.ncbi.parameters.FilterParameter;
import org.sing_group.seda.blast.ncbi.parameters.MatrixParameter;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;

public class NcbiBlastTransformationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final Color COLOR_VALID_INPUT = getColor("TextField.background");

  private static final String HELP_BLAST_TYPE = "The blast program to execute.";
  private static final String HELP_BLAST_DATABASE = "The NCBI database.";
  private static final String HELP_OUTPUT_TYPE =
    "<html>The output type:<ul>"
      + "<li><i>Complete sequences</i>: creates a FASTA file with the complete sequences of each sequence that has an alignment against the query sequence.</li>"
      + "<li><i>Aligned sequences</i>: creates a FASTA file with the portions of the sequences aligned against the query.</li></ul>";
  private static final String HELP_MATRIX_TYPE = "The scoring matrix to use.";
  private static final String HELP_FILTER = "Low complexity filtering.";
  private static final String HELP_EXPECT_VALUE = "Expect value.";
  private static final String HELP_HIT_LIST_SIZE_VALUE = "Number of databases sequences to keep.";
  private static final String HELP_WORD_SIZE_VALUE = "Size of word for initial matches.";
  private static final String HELP_THRESHOLD_VALUE =
    "<html>Neighboring score for initial words.<br/>Does not apply to BLASTN or MegaBLAST.</html>";

  private RadioButtonsPanel<OutputTypeParameter> outputTypeParameter;
  private NcbiBlastTransformationProvider transformationProvider;
  private JComboBox<NcbiBlastType> blastTypeCombobox;
  private JComboBox<NcbiBlastDatabase> blastDatabaseCombobox;
  private JComboBox<MatrixParameter> matrixCombobox;
  private JLabel blastDatabaseWarning;
  private JCheckBox useFilter;
  private JCheckBox lookupMask;
  private DoubleTextField expectValue;
  private JIntegerTextField hitListSize;
  private JIntegerTextField wordSize;
  private JIntegerTextField threshold;

  public NcbiBlastTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void initTransformationProvider() {
    this.transformationProvider =
      new NcbiBlastTransformationProvider(
        this.getBlastType(), this.getBlastDatabase(), this.getOutputType()
      );
    this.checkBlastDatabase();
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
    dbParameters.add(getBlastTypeParameter());
    dbParameters.add(getBlastDatabaseParameter());
    dbParameters.add(getOutputTypeParameter());

    dbParameters.add(new InputParameterSeparator("Optional parameters:", 25));
    dbParameters.add(getMatrixParameter());
    dbParameters.add(getFilterParameter());
    dbParameters.add(getExpectValueParameter());
    dbParameters.add(getHitListSizeParameter());
    dbParameters.add(getWordSizeParameter());
    dbParameters.add(getThresholdParameter());

    return dbParameters.toArray(new InputParameter[dbParameters.size()]);
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

  private InputParameter getBlastTypeParameter() {
    this.blastTypeCombobox = new JComboBox<>(NcbiBlastType.values());
    this.blastTypeCombobox.addItemListener(this::blastTypeChanged);

    return new InputParameter("Blast type: ", this.blastTypeCombobox, HELP_BLAST_TYPE);
  }

  private void blastTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.checkBlastDatabase();
      invokeLater(() -> {
        this.transformationProvider.setBlastType(getBlastType());
      });
    }
  }

  private NcbiBlastType getBlastType() {
    return (NcbiBlastType) this.blastTypeCombobox.getSelectedItem();
  }

  private InputParameter getBlastDatabaseParameter() {
    this.blastDatabaseCombobox = new JComboBox<>(NcbiBlastDatabase.values());
    this.blastDatabaseCombobox.addItemListener(this::blastDatabaseChanged);
    this.blastDatabaseCombobox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

    this.blastDatabaseWarning = new JLabel(Icons.ICON_WARNING_COLOR_24);
    JPanel input = new JPanel();
    input.setLayout(new BoxLayout(input, BoxLayout.X_AXIS));
    input.add(this.blastDatabaseCombobox);
    input.add(this.blastDatabaseWarning);
    this.blastDatabaseWarning.setVisible(false);

    return new InputParameter("Blast database: ", input, HELP_BLAST_DATABASE);
  }

  private void blastDatabaseChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.checkBlastDatabase();
      invokeLater(() -> {
        this.transformationProvider.setBlastDatabase(getBlastDatabase());
      });
    }
  }

  private NcbiBlastDatabase getBlastDatabase() {
    return (NcbiBlastDatabase) this.blastDatabaseCombobox.getSelectedItem();
  }

  private void checkBlastDatabase() {
    NcbiBlastType blastType = this.getBlastType();
    this.threshold.setEnabled(!blastType.equals(NcbiBlastType.BLASTN) && !blastType.equals(NcbiBlastType.MEGABLAST));

    boolean warningVisible = !this.getBlastType().getDatabaseType().equals(this.getBlastDatabase().getSequenceType());
    this.blastDatabaseWarning.setVisible(warningVisible);

    StringBuilder tooltip = new StringBuilder("The selected blast program requires a ");
    tooltip
      .append(this.getBlastDatabase().getSequenceType().toString().toLowerCase())
      .append(" database and the selected one is a ")
      .append(this.getBlastType().getDatabaseType().toString().toLowerCase())
      .append(" database.");

    this.blastDatabaseWarning.setToolTipText(warningVisible ? tooltip.toString() : null);
  }

  private InputParameter getMatrixParameter() {
    this.matrixCombobox = new JComboBox<>(MatrixParameter.values());
    this.matrixCombobox.setSelectedItem(null);
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

  private MatrixParameter getMatrix() {
    return (MatrixParameter) this.matrixCombobox.getSelectedItem();
  }

  private InputParameter getFilterParameter() {
    this.useFilter = new JCheckBox("Apply", false);
    this.lookupMask = new JCheckBox("Mask at lookup", false);

    this.useFilter.addItemListener(this::filterChanged);
    this.lookupMask.addItemListener(this::filterChanged);

    JPanel input = new JPanel();
    input.setLayout(new GridLayout(1, 2));
    input.add(this.useFilter);
    input.add(this.lookupMask);

    return new InputParameter("Filter: ", input, HELP_FILTER);
  }

  private void filterChanged(ItemEvent event) {
    invokeLater(() -> {
      this.transformationProvider.setFilter(getFilter());
    });
  }

  private FilterParameter getFilter() {
    if (this.useFilter.isSelected()) {
      return new FilterParameter(true, this.lookupMask.isSelected());
    } else {
      return new FilterParameter(false);
    }
  }

  private InputParameter getExpectValueParameter() {
    this.expectValue = new DoubleTextField();
    this.expectValue.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::expectValueChanged));

    return new InputParameter("Expect value:", this.expectValue, HELP_EXPECT_VALUE);
  }

  private void expectValueChanged() {
    invokeLater(() -> {
      if (this.expectValue.getText().isEmpty()) {
        this.expectValue.setBackground(COLOR_VALID_INPUT);
        this.transformationProvider.clearExpectValue();
      } else {
        try {
          Double eValue = getExpectValue();
          this.transformationProvider.setExpectValue(eValue);
          this.expectValue.setBackground(eValue > 0 ? COLOR_VALID_INPUT : ColorUtils.COLOR_INVALID_INPUT);
        } catch (NumberFormatException e) {
          this.transformationProvider.setExpectValue(-1);
          this.expectValue.setBackground(ColorUtils.COLOR_INVALID_INPUT);
        }
      }
    });
  }

  private Double getExpectValue() {
    return Double.parseDouble(this.expectValue.getText());
  }

  private InputParameter getHitListSizeParameter() {
    this.hitListSize = new JIntegerTextField();
    this.hitListSize.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::hitListSizeValueChanged));

    return new InputParameter("Hit list size:", this.hitListSize, HELP_HIT_LIST_SIZE_VALUE);
  }

  private void hitListSizeValueChanged() {
    invokeLater(() -> {
      if (this.hitListSize.getText().isEmpty()) {
        this.hitListSize.setBackground(COLOR_VALID_INPUT);
        this.transformationProvider.clearHitListSize();
      } else {
        try {
          Integer hitListSize = getHitListSizeValue();
          this.transformationProvider.setHitListSizeValue(hitListSize);
          this.hitListSize.setBackground(hitListSize > 0 ? COLOR_VALID_INPUT : ColorUtils.COLOR_INVALID_INPUT);
        } catch (NumberFormatException e) {
          this.transformationProvider.setHitListSizeValue(-1);
          this.hitListSize.setBackground(ColorUtils.COLOR_INVALID_INPUT);
        }
      }
    });
  }

  private Integer getHitListSizeValue() {
    return Integer.parseInt(this.hitListSize.getText());
  }

  private InputParameter getWordSizeParameter() {
    this.wordSize = new JIntegerTextField();
    this.wordSize.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::wordSizeValueChanged));

    return new InputParameter("Word size:", this.wordSize, HELP_WORD_SIZE_VALUE);
  }

  private void wordSizeValueChanged() {
    invokeLater(() -> {
      if (this.wordSize.getText().isEmpty()) {
        this.wordSize.setBackground(COLOR_VALID_INPUT);
        this.transformationProvider.clearWordSize();
      } else {
        try {
          Integer wordSize = getWordSizeValue();
          this.transformationProvider.setWordSizeValue(wordSize);
          this.wordSize.setBackground(wordSize > 0 ? COLOR_VALID_INPUT : ColorUtils.COLOR_INVALID_INPUT);
        } catch (NumberFormatException e) {
          this.transformationProvider.setWordSizeValue(-1);
          this.wordSize.setBackground(ColorUtils.COLOR_INVALID_INPUT);
        }
      }
    });
  }

  private Integer getWordSizeValue() {
    return Integer.parseInt(this.wordSize.getText());
  }

  private InputParameter getThresholdParameter() {
    this.threshold = new JIntegerTextField();
    this.threshold.getDocument().addDocumentListener(new RunnableDocumentAdapter(this::thresholdValueChanged));

    return new InputParameter("Threshold:", this.threshold, HELP_THRESHOLD_VALUE);
  }

  private void thresholdValueChanged() {
    invokeLater(() -> {
      if (this.threshold.getText().isEmpty()) {
        this.threshold.setBackground(COLOR_VALID_INPUT);
        this.transformationProvider.clearThresholdValue();
      } else {
        try {
          Integer threshold = getThresholdValue();
          this.transformationProvider.setThresholdValue(threshold);
          this.threshold.setBackground(threshold > 0 ? COLOR_VALID_INPUT : ColorUtils.COLOR_INVALID_INPUT);
        } catch (NumberFormatException e) {
          this.transformationProvider.setThresholdValue(-1);
          this.threshold.setBackground(ColorUtils.COLOR_INVALID_INPUT);
        }
      }
    });
  }

  private Integer getThresholdValue() {
    return Integer.parseInt(this.threshold.getText());
  }

  public NcbiBlastTransformationProvider getTransformationProvider() {
    return transformationProvider;
  }

  public void setTransformationProvider(NcbiBlastTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    NcbiBlastType blastType = this.transformationProvider.getBlastType();
    if (blastType != null) {
      this.blastTypeCombobox.setSelectedItem(blastType);
    }

    NcbiBlastDatabase blastDatabase = this.transformationProvider.getBlastDatabase();
    if (blastDatabase != null) {
      this.blastDatabaseCombobox.setSelectedItem(blastDatabase);
    }

    OutputTypeParameter outputType = this.transformationProvider.getOutputType();
    if (outputType != null) {
      this.outputTypeParameter.setSelectedItem(outputType);
    }

    MatrixParameter matrix = this.transformationProvider.getMatrix();
    if (matrix != null) {
      this.matrixCombobox.setSelectedItem(matrix);
    }

    FilterParameter filter = this.transformationProvider.getFilter();
    if (filter != null) {
      this.useFilter.setSelected(filter.isUseFilter());
      this.lookupMask.setSelected(filter.isMaskAtLookup());
    }

    Double expectValue = this.transformationProvider.getExpectValue();
    if (expectValue != null) {
      this.expectValue.setValue(expectValue);
    }

    Integer hitListSize = this.transformationProvider.getHitListSize();
    if (hitListSize != null) {
      this.hitListSize.setValue(hitListSize);
    }

    Integer wordSize = this.transformationProvider.getWordSize();
    if (wordSize != null) {
      this.wordSize.setValue(wordSize);
    }
    
    Integer threshold = this.transformationProvider.getThreshold();
    if(threshold != null) {
      this.threshold.setValue(threshold);
    }
  }
}
