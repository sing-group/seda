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
package org.sing_group.seda.sapp.transformation.provider;

import static org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationConfigurationChangeType.BEDTOOLS_EXECUTOR_CHANGED;
import static org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationConfigurationChangeType.SAPP_CODON_CHANGED;
import static org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationConfigurationChangeType.SAPP_EXECUTOR_CHANGED;
import static org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationConfigurationChangeType.SAPP_SPECIES_CHANGED;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutorWrapper;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;
import org.sing_group.seda.sapp.transformation.sequencesgroup.SappAnnotationSequencesGroupTransformation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class SappAnnotationTransformationProvider extends AbstractTransformationProvider {
  @XmlAnyElement(lax = true)
  private SappBinariesExecutor sappBinariesExecutor;
  @XmlElement
  private BedToolsBinariesExecutorWrapper bedToolsBinariesExecutor;
  @XmlElement
  private SappCodon sappCodon;
  @XmlElement
  private SappSpecies sappSpecies;

  public SappAnnotationTransformationProvider() {
    this(SappSpecies.HOMO_SAPIENS, SappCodon.STANDARD);
  }

  public SappAnnotationTransformationProvider(SappSpecies sappSpecies, SappCodon sappCodon) {
    this.sappSpecies = sappSpecies;
    this.sappCodon = sappCodon;
    this.bedToolsBinariesExecutor = new BedToolsBinariesExecutorWrapper();
  }

  @Override
  public Validation validate() {
    try {
      List<String> validationErrors = new LinkedList<String>();

      if (this.sappSpecies == null) {
        validationErrors.add("The species mode can't be null");
      }

      if (this.sappCodon == null) {
        validationErrors.add("The codon can't be null");
      }
      
      if (!isValidSappBinariesExecutor()) {
        validationErrors.add("The SAPP binaries executor is not valid");
      }

      if (!isValidBedToolsBinariesExecutor()) {
        validationErrors.add("The bedtools binaries executor is not valid");
      }
      if (validationErrors.isEmpty()) {
        getTransformation(DatatypeFactory.getDefaultDatatypeFactory());
        
        return new DefaultValidation();
      } else {
        return new DefaultValidation(validationErrors);
      }
    } catch (RuntimeException ex) {
      return new DefaultValidation(ex.toString());
    }
  }

  private boolean isValidSappBinariesExecutor() {
    if (this.sappBinariesExecutor == null) {
      return false;
    }

    try {
      this.sappBinariesExecutor.checkBinary();

      return true;
    } catch (BinaryCheckException e) {
      return false;
    }
  }

  private boolean isValidBedToolsBinariesExecutor() {
    if (this.bedToolsBinariesExecutor.get() == null) {
      return false;
    }

    try {
      this.bedToolsBinariesExecutor.get().checkBinary();

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
      this.sappBinariesExecutor,
      this.bedToolsBinariesExecutor.get(),
      this.sappCodon,
      this.sappSpecies
    );
  }

  public void setSappBinariesExecutor(Optional<SappBinariesExecutor> sappBinariesExecutor) {
    this.sappBinariesExecutor = sappBinariesExecutor.orElse(null);
    fireTransformationsConfigurationModelEvent(SAPP_EXECUTOR_CHANGED, this.sappBinariesExecutor);
  }

  public SappBinariesExecutor getSappBinariesExecutor() {
    return sappBinariesExecutor;
  }

  public void setBedToolsBinariesExecutor(Optional<BedToolsBinariesExecutor> bedToolsBinariesExecutor) {
    this.bedToolsBinariesExecutor.set(bedToolsBinariesExecutor.orElse(null));
    fireTransformationsConfigurationModelEvent(BEDTOOLS_EXECUTOR_CHANGED, this.bedToolsBinariesExecutor.get());
  }

  public BedToolsBinariesExecutor getBedToolsBinariesExecutor() {
    return bedToolsBinariesExecutor.get();
  }

  public void setSappSpecies(SappSpecies sappSpecies) {
    if (this.sappSpecies == null || !this.sappSpecies.equals(sappSpecies)) {
      this.sappSpecies = sappSpecies;
      fireTransformationsConfigurationModelEvent(SAPP_SPECIES_CHANGED, this.sappSpecies);
    }
  }

  public SappSpecies getSappSpecies() {
    return sappSpecies;
  }

  public void setSappCodon(SappCodon sappCodon) {
    if (this.sappCodon == null || !this.sappCodon.equals(sappCodon)) {
      this.sappCodon = sappCodon;
      fireTransformationsConfigurationModelEvent(SAPP_CODON_CHANGED, this.sappCodon);
    }
  }

  public SappCodon getSappCodon() {
    return sappCodon;
  }
}
