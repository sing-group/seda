/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.getorf.gui;

import static org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationChangeType.ADDITONAL_PARAMETERS_CHANGED;
import static org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationChangeType.EMBOSS_EXECUTOR_CHANGED;
import static org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationChangeType.FIND_CHANGED;
import static org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationChangeType.MAXSIZE_CHANGED;
import static org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationChangeType.MINSIZE_CHANGED;
import static org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationChangeType.TABLE_CHANGED;

import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.emboss.execution.EmbossBinariesExecutor;
import org.sing_group.seda.emboss.getorf.datatype.FindParam;
import org.sing_group.seda.emboss.getorf.datatype.TableParam;
import org.sing_group.seda.emboss.transformation.sequencesgroup.GetOrfSequencesGroupTransformation;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class GetOrfTransformationProvider extends AbstractTransformationProvider {
  @XmlAnyElement(lax = true)
  private EmbossBinariesExecutor embossBinariesExecutor;
  @XmlElement
  private TableParam table;
  @XmlElement
  private int minSize;
  @XmlElement
  private int maxSize;
  @XmlElement
  private FindParam find;
  @XmlElement
  private String additionalParameters;

  public GetOrfTransformationProvider() {}

  public GetOrfTransformationProvider(
    TableParam table, FindParam find, Integer minSize, Integer maxSize, String additionalParameters
  ) {
    this.table = table;
    this.find = find;
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.additionalParameters = additionalParameters;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (!isValidEmbossBinariesExecutor() || this.table == null || this.find == null) {
        return false;
      }

      getEmbossTransformation(DatatypeFactory.getDefaultDatatypeFactory());

      return true;
    } catch (RuntimeException ex) {
      return false;
    }
  }

  private boolean isValidEmbossBinariesExecutor() {
    if (this.embossBinariesExecutor == null) {
      return false;
    }

    try {
      this.embossBinariesExecutor.checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new ComposedSequencesGroupDatasetTransformation(getEmbossTransformation(factory));
  }

  private GetOrfSequencesGroupTransformation getEmbossTransformation(DatatypeFactory factory) {
    return new GetOrfSequencesGroupTransformation(
      factory, this.embossBinariesExecutor,
      this.table.getParamValue(),
      this.minSize, this.maxSize,
      this.find.getParamValue(),
      this.additionalParameters == null ? "" : this.additionalParameters
    );
  }

  public void setEmbossBinariesExecutor(Optional<EmbossBinariesExecutor> embossBinariesExecutor) {
    this.embossBinariesExecutor = embossBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(EMBOSS_EXECUTOR_CHANGED, this.embossBinariesExecutor);
  }

  public EmbossBinariesExecutor getEmbossBinariesExecutor() {
    return embossBinariesExecutor;
  }

  public void setTable(TableParam table) {
    if (this.table == null || !this.table.equals(table)) {
      this.table = table;
      fireTransformationsConfigurationModelEvent(TABLE_CHANGED, this.table);
    }
  }

  public TableParam getTable() {
    return table;
  }

  public void setFind(FindParam find) {
    if (this.find == null || !this.find.equals(find)) {
      this.find = find;
      fireTransformationsConfigurationModelEvent(FIND_CHANGED, this.find);
    }
  }

  public FindParam getFind() {
    return find;
  }

  public void setMinSize(int minSize) {
    if (this.minSize != minSize) {
      this.minSize = minSize;
      fireTransformationsConfigurationModelEvent(MINSIZE_CHANGED, this.minSize);
    }
  }

  public int getMinSize() {
    return minSize;
  }

  public void setMaxSize(int maxSize) {
    if (this.maxSize != maxSize) {
      this.maxSize = maxSize;
      fireTransformationsConfigurationModelEvent(MAXSIZE_CHANGED, this.maxSize);
    }
  }

  public int getMaxSize() {
    return maxSize;
  }

  public void setAdditionalParameters(String additionalParameters) {
    if (this.additionalParameters == null || !this.additionalParameters.equals(additionalParameters)) {
      this.additionalParameters = additionalParameters;
      fireTransformationsConfigurationModelEvent(ADDITONAL_PARAMETERS_CHANGED, this.additionalParameters);
    }
  }

  public String getAdditionalParameters() {
    return additionalParameters;
  }
}
