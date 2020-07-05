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

import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.DATABASE_CHANGED;
import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.EXPECT_VALUE_CHANGED;
import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.FILTER_OPTION_CHANGED;
import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.GAPPED_VALUE_CHANGED;
import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.HITS_CHANGED;
import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.MATRIX_CHANGED;
import static org.sing_group.seda.blast.uniprot.gui.UniProtBlastTransformationConfigurationChangeType.OUTPUT_TYPE_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.transformation.dataset.UniProtBlastTransformation;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.AlignmentCutoffOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.FilterOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.MatrixOption;

@XmlRootElement
public class UniProtBlastTransformationProvider extends AbstractTransformationProvider {

  @XmlElement
  private DatabaseOption database;

  @XmlElement
  private OutputTypeParameter outputType;

  @XmlElement
  private ExpectationOption expectValue;

  @XmlElement
  private MatrixOption matrix;

  @XmlElement
  private FilterOption filter;

  @XmlElement
  private boolean gapped;

  @XmlElement
  private AlignmentCutoffOption hits;

  public UniProtBlastTransformationProvider() {}

  public UniProtBlastTransformationProvider(
    DatabaseOption database, OutputTypeParameter outputType, ExpectationOption expectValue,
    MatrixOption matrix, FilterOption filter, boolean gapped, AlignmentCutoffOption hits
  ) {
    this.database = database;
    this.outputType = outputType;
    this.expectValue = expectValue;
    this.matrix = matrix;
    this.filter = filter;
    this.gapped = gapped;
    this.hits = hits;
  }

  @Override
  public boolean isValidTransformation() {
    if (
      this.database == null || this.outputType == null || this.expectValue == null ||
        this.matrix == null || this.filter == null || this.hits == null
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
    return new UniProtBlastTransformation(
      this.database, this.outputType, this.expectValue, this.matrix, this.filter, this.gapped, this.hits, factory
    );
  }

  public void setDatabase(DatabaseOption database) {
    if (this.database == null || !this.database.equals(database)) {
      this.database = database;
      fireTransformationsConfigurationModelEvent(DATABASE_CHANGED, this.database);
    }
  }

  public DatabaseOption getDatabase() {
    return this.database;
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

  public void setMatrix(MatrixOption matrix) {
    if (this.matrix == null || !this.matrix.equals(matrix)) {
      this.matrix = matrix;
      fireTransformationsConfigurationModelEvent(MATRIX_CHANGED, this.matrix);
    }
  }

  public MatrixOption getMatrix() {
    return this.matrix;
  }

  public void setExpectation(ExpectationOption expectValue) {
    if (this.expectValue == null || !this.expectValue.equals(expectValue)) {
      this.expectValue = expectValue;
      fireTransformationsConfigurationModelEvent(EXPECT_VALUE_CHANGED, this.expectValue);
    }
  }

  public ExpectationOption getExpectValue() {
    return this.expectValue;
  }

  public void setFilter(FilterOption filterOption) {
    if (this.filter == null || !this.filter.equals(filterOption)) {
      this.filter = filterOption;
      fireTransformationsConfigurationModelEvent(FILTER_OPTION_CHANGED, this.filter);
    }
  }

  public FilterOption getFilter() {
    return this.filter;
  }

  public void setGappoed(boolean gapped) {
    if (this.gapped != gapped) {
      this.gapped = gapped;
      fireTransformationsConfigurationModelEvent(GAPPED_VALUE_CHANGED, this.gapped);
    }
  }

  public boolean isGapped() {
    return this.gapped;
  }

  public void setHits(AlignmentCutoffOption hits) {
    if (this.hits == null || !this.hits.equals(hits)) {
      this.hits = hits;
      fireTransformationsConfigurationModelEvent(HITS_CHANGED, this.hits);
    }
  }

  public AlignmentCutoffOption getHits() {
    return this.hits;
  }
}
