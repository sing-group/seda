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

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.sapp.transformation.sequencesgroup.SappAnnotationSequencesGroupTransformation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class SappAnnotationTransformationProvider extends AbstractTransformationProvider {

  private SappAnnotationTransformationConfigurationPanel configurationPanel;

  public SappAnnotationTransformationProvider(SappAnnotationTransformationConfigurationPanel configurationPanel) {
    this.configurationPanel = configurationPanel;
  }

  @Override
  public boolean isValidTransformation() {
    try {
      if (!isValidSappBinariesExecutor()) {
        return false;
      }

      if (!isValidSappBinariesExecutor()) {
        return false;
      }
      
      if (!isValidBedToolsBinariesExecutor()) {
        return false;
      }

      getSappAnnotationTransformation(DatatypeFactory.getDefaultDatatypeFactory());

      return true;
    } catch (RuntimeException ex) {
      return false;
    }
  }

  private boolean isValidSappBinariesExecutor() {
    if (!this.configurationPanel.getSappBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      this.configurationPanel.getSappBinariesExecutor().get().checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  private boolean isValidBedToolsBinariesExecutor() {
    if (!this.configurationPanel.getBedToolsBinariesExecutor().isPresent()) {
      return false;
    }

    try {
      this.configurationPanel.getBedToolsBinariesExecutor().get().checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new ComposedSequencesGroupDatasetTransformation(getSappAnnotationTransformation(factory));
  }

  private SappAnnotationSequencesGroupTransformation getSappAnnotationTransformation(DatatypeFactory factory) {
    return new SappAnnotationSequencesGroupTransformation(
      factory,
      this.configurationPanel.getSappBinariesExecutor().get(),
      this.configurationPanel.getBedToolsBinariesExecutor().get(),
      this.configurationPanel.getSappCodon(),
      this.configurationPanel.getSappSpecies()
    );
  }

  public void sappExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      SappAnnotationTransformationConfigurationChangeType.SAPP_PATH_CHANGED,
      configurationPanel.getSappBinariesExecutor()
    );
  }

  public void bedToolsExecutorChanged() {
    fireTransformationsConfigurationModelEvent(
      SappAnnotationTransformationConfigurationChangeType.BEDTOOLS_PATH_CHANGED,
      configurationPanel.getBedToolsBinariesExecutor()
    );
  }

  public void sappSpeciesChanged() {
    fireTransformationsConfigurationModelEvent(
      SappAnnotationTransformationConfigurationChangeType.SAPP_SPECIES_CHANGED, configurationPanel.getSappSpecies()
    );
  }

  public void sappCodonChanged() {
    fireTransformationsConfigurationModelEvent(
      SappAnnotationTransformationConfigurationChangeType.SAPP_CODON_CHANGED, configurationPanel.getSappCodon()
    );
  }
}
