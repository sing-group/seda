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

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.emboss.transformation.sequencesgroup.GetOrfSequencesGroupTransformation;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class GetOrfTransformationProvider extends AbstractTransformationProvider {

  private GetOrfTransformationConfigurationPanel configurationPanel;

  public GetOrfTransformationProvider(GetOrfTransformationConfigurationPanel configurationPanel) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (!isValidEmbossBinariesExecutor()) {
        return false;
      }

      getEmbossTransformation(DatatypeFactory.getDefaultDatatypeFactory());

      return true;
    } catch (RuntimeException ex) {
      return false;
    }
  }

  private boolean isValidEmbossBinariesExecutor() {
    if (!this.configurationPanel.getEmbossBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      this.configurationPanel.getEmbossBinariesExecutor().get().checkBinary();

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
      factory, this.configurationPanel.getEmbossBinariesExecutor().get(),
      this.configurationPanel.getTable().getParamValue(),
      this.configurationPanel.getMinSize(), this.configurationPanel.getMaxSize(),
      this.configurationPanel.getFind().getParamValue(),
      this.configurationPanel.getGetOrfAditionalParameters()
    );
  }

  public void embossExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      GetOrfTransformationConfigurationChangeType.EMBOSS_PATH_CHANGED, configurationPanel.getEmbossBinariesExecutor()
    );
  }

  public void tableChanged() {
    fireTransformationsConfigurationModelEvent(
      GetOrfTransformationConfigurationChangeType.TABLE_CHANGED, configurationPanel.getTable()
    );
  }
  
  public void findChanged() {
    fireTransformationsConfigurationModelEvent(
      GetOrfTransformationConfigurationChangeType.FIND_CHANGED, configurationPanel.getFind()
      );
  }

  public void minSizeChanged() {
    fireTransformationsConfigurationModelEvent(
      GetOrfTransformationConfigurationChangeType.MINSIZE_CHANGED, configurationPanel.getMinSize()
    );
  }

  public void maxSizeChanged() {
    fireTransformationsConfigurationModelEvent(
      GetOrfTransformationConfigurationChangeType.MAXSIZE_CHANGED, configurationPanel.getMaxSize()
    );
  }

  public void getOrfAdditionalParametersChanged() {
    fireTransformationsConfigurationModelEvent(
      GetOrfTransformationConfigurationChangeType.EMBOSS_GETORF_ADDITONAL_PARAMETERS_CHANGED, configurationPanel.getGetOrfAditionalParameters()
    );
  }
}
