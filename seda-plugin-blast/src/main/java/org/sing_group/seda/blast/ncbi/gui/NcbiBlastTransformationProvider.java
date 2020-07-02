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

import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.BLAST_DATABASE_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.BLAST_TYPE_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.OUTPUT_TYPE_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.MATRIX_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.FILTER_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.EXPECT_VALUE_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.HIT_LIST_SIZE_VALUE_CHANGED;
import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.WORD_SIZE_VALUE_CHANGED;

import java.util.LinkedList;
import java.util.List;

import static org.sing_group.seda.blast.ncbi.gui.NcbiBlastTransformationConfigurationChangeType.THRESHOLD_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.blast.ncbi.NcbiBlastDatabase;
import org.sing_group.seda.blast.ncbi.NcbiBlastType;
import org.sing_group.seda.blast.ncbi.parameters.ExpectValueParameter;
import org.sing_group.seda.blast.ncbi.parameters.FilterParameter;
import org.sing_group.seda.blast.ncbi.parameters.HitListSizeParameter;
import org.sing_group.seda.blast.ncbi.parameters.MatrixParameter;
import org.sing_group.seda.blast.ncbi.parameters.NcbiBlastParameter;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.ncbi.parameters.ThresholdParameter;
import org.sing_group.seda.blast.ncbi.parameters.WordSizeParameter;
import org.sing_group.seda.blast.transformation.dataset.NcbiBlastTransformation;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class NcbiBlastTransformationProvider extends AbstractTransformationProvider {

  @XmlElement
  private NcbiBlastType blastType;

  @XmlElement
  private NcbiBlastDatabase blastDatabase;

  @XmlElement
  private OutputTypeParameter outputType;

  @XmlElement
  private MatrixParameter matrix;

  @XmlElement
  private FilterParameter filter;

  @XmlElement
  private Double expectValue;

  @XmlElement
  private Integer hitListSize;

  @XmlElement
  private Integer wordSize;

  @XmlElement
  private Integer threshold;

  public NcbiBlastTransformationProvider() {}

  public NcbiBlastTransformationProvider(
    NcbiBlastType blastType, NcbiBlastDatabase blastDatabase, OutputTypeParameter outputType
  ) {
    this.blastType = blastType;
    this.blastDatabase = blastDatabase;
    this.outputType = outputType;
  }

  @Override
  public boolean isValidTransformation() {
    if (
      this.blastType == null || this.blastDatabase == null || this.outputType == null ||
      (!this.blastType.getDatabaseType().equals(this.blastDatabase.getSequenceType())) ||
      (this.expectValue != null && this.expectValue < 0) ||
      (this.hitListSize != null && this.hitListSize < 0) ||
      (this.wordSize != null && this.wordSize < 0) ||
      (this.threshold != null && this.threshold < 0)
    ) {
      return false;
    }

    return true;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    if (!this.isValidTransformation()) {
      throw new IllegalStateException("The current transformation configuration is not valid");
    }
    return new NcbiBlastTransformation(
      this.blastType, this.blastDatabase, this.outputType, this.getBlastParameters(), factory
    );
  }

  private List<NcbiBlastParameter> getBlastParameters() {
    List<NcbiBlastParameter> parameters = new LinkedList<>();

    if (this.expectValue != null && this.expectValue > 0) {
      parameters.add(new ExpectValueParameter(this.expectValue));
    }

    if (this.hitListSize != null && this.hitListSize > 0) {
      parameters.add(new HitListSizeParameter(this.hitListSize));
    }

    if (this.wordSize != null && this.wordSize > 0) {
      parameters.add(new WordSizeParameter(this.wordSize));
    }

    if (this.threshold != null && this.threshold > 0) {
      parameters.add(new ThresholdParameter(this.threshold));
    }

    if (this.matrix != null) {
      parameters.add(this.matrix);
    }

    if (this.filter != null) {
      parameters.add(filter);
    }

    return parameters;
  }

  public void setBlastType(NcbiBlastType blastType) {
    if (this.blastType == null || !this.blastType.equals(blastType)) {
      this.blastType = blastType;
      fireTransformationsConfigurationModelEvent(BLAST_TYPE_CHANGED, this.blastType);
    }
  }
  
  public NcbiBlastType getBlastType() {
    return blastType;
  }

  public void setBlastDatabase(NcbiBlastDatabase blastDatabase) {
    if (this.blastDatabase == null || !this.blastDatabase.equals(blastDatabase)) {
      this.blastDatabase = blastDatabase;
      fireTransformationsConfigurationModelEvent(BLAST_DATABASE_CHANGED, this.blastDatabase);
    }
  }
  
  public NcbiBlastDatabase getBlastDatabase() {
    return blastDatabase;
  }
  
  public void setOutputType(OutputTypeParameter outputType) {
    if (this.outputType == null || !this.outputType.equals(outputType)) {
      this.outputType = outputType;
      fireTransformationsConfigurationModelEvent(OUTPUT_TYPE_CHANGED, this.outputType);
    }
  }

  public OutputTypeParameter getOutputType() {
    return outputType;
  }
  
  public void setMatrix(MatrixParameter matrix) {
    if (this.matrix == null || !this.matrix.equals(matrix)) {
      this.matrix = matrix;
      fireTransformationsConfigurationModelEvent(MATRIX_CHANGED, this.matrix);
    }
  }

  public MatrixParameter getMatrix() {
    return matrix;
  }
  
  public void setFilter(FilterParameter filter) {
    if (this.filter == null || !this.filter.equals(filter)) {
      this.filter = filter;
      fireTransformationsConfigurationModelEvent(FILTER_CHANGED, this.filter);
    }
  }

  public FilterParameter getFilter() {
    return filter;
  }

  public void setExpectValue(double expectValue) {
    if (this.expectValue == null || !this.expectValue.equals(expectValue)) {
      this.expectValue = expectValue;
      fireTransformationsConfigurationModelEvent(EXPECT_VALUE_CHANGED, this.expectValue);
    }
  }

  public void clearExpectValue() {
    if (this.expectValue != null) {
      this.expectValue = null;
      fireTransformationsConfigurationModelEvent(EXPECT_VALUE_CHANGED, this.expectValue);
    }
  }

  public void setHitListSizeValue(int hitListSize) {
    if (this.hitListSize == null || !this.hitListSize.equals(hitListSize)) {
      this.hitListSize = hitListSize;
      fireTransformationsConfigurationModelEvent(HIT_LIST_SIZE_VALUE_CHANGED, this.hitListSize);
    }
  }
  
  public Double getExpectValue() {
    return expectValue;
  }
  
  public void clearHitListSize() {
    if (this.hitListSize != null) {
      this.hitListSize = null;
      fireTransformationsConfigurationModelEvent(HIT_LIST_SIZE_VALUE_CHANGED, this.hitListSize);
    }
  }

  public Integer getHitListSize() {
    return hitListSize;
  }
  
  public void setWordSizeValue(int wordSize) {
    if (this.wordSize == null || !this.wordSize.equals(wordSize)) {
      this.wordSize = wordSize;
      fireTransformationsConfigurationModelEvent(WORD_SIZE_VALUE_CHANGED, this.wordSize);
    }
  }
  public void clearWordSize() {
    if (this.wordSize != null) {
      this.wordSize = null;
      fireTransformationsConfigurationModelEvent(WORD_SIZE_VALUE_CHANGED, this.wordSize);
    }
  }
  
  public Integer getWordSize() {
    return wordSize;
  }

  public void setThresholdValue(int threshold) {
    if (this.threshold == null || !this.threshold.equals(threshold)) {
      this.threshold = threshold;
      fireTransformationsConfigurationModelEvent(THRESHOLD_CHANGED, this.threshold);
    }
  }

  public void clearThresholdValue() {
    if (this.threshold != null) {
      this.threshold = null;
      fireTransformationsConfigurationModelEvent(THRESHOLD_CHANGED, this.threshold);
    }
  }
  
  public Integer getThreshold() {
    return threshold;
  }
}
