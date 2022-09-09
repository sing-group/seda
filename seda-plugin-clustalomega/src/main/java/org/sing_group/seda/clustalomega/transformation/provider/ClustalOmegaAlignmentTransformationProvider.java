/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.clustalomega.transformation.provider;

import static org.sing_group.seda.clustalomega.transformation.provider.ClustalOmegaAlignmentTransformationConfigurationChangeType.ADDITIONAL_PARAMETERS_CHANGED;
import static org.sing_group.seda.clustalomega.transformation.provider.ClustalOmegaAlignmentTransformationConfigurationChangeType.CLUSTAL_OMEGA_EXECUTOR_CHANGED;
import static org.sing_group.seda.clustalomega.transformation.provider.ClustalOmegaAlignmentTransformationConfigurationChangeType.NUM_THREADS_CHANGED;

import java.util.Optional;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.clustalomega.transformation.sequencesgroup.ClustalOmegaAlignmentSequencesGroupTransformation;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class ClustalOmegaAlignmentTransformationProvider extends AbstractTransformationProvider {
  @XmlAnyElement(lax = true)
  private ClustalOmegaBinariesExecutor binariesExecutor;
  @XmlElement
  private int numThreads;
  @XmlElement
  private String additionalParameters;

  public ClustalOmegaAlignmentTransformationProvider() {}

  @Override
  public Validation validate() {
    if (this.binariesExecutor == null) {
      return new DefaultValidation("The Clustal Omega binaries executor has not been set.");
    }

    try {
      this.binariesExecutor.checkBinary();

      return new DefaultValidation();
    } catch (BinaryCheckException e) {
      return new DefaultValidation("The Clustal Omega binaries executor can't be used.");
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new ComposedSequencesGroupDatasetTransformation(getClustalOmegaTransformation(factory));
  }

  private ClustalOmegaAlignmentSequencesGroupTransformation getClustalOmegaTransformation(DatatypeFactory factory) {
    return new ClustalOmegaAlignmentSequencesGroupTransformation(
      factory,
      this.binariesExecutor,
      this.numThreads,
      this.additionalParameters
    );
  }

  public int getNumThreads() {
    return this.numThreads;
  }

  public void setNumThreads(int numThreads) {
    if (this.numThreads != numThreads) {
      this.numThreads = numThreads;
      fireTransformationsConfigurationModelEvent(NUM_THREADS_CHANGED, this.numThreads);
    }
  }

  public void setAdditionalParameters(String additionalParameters) {
    if (
      additionalParameters != null
        && (this.additionalParameters == null || !this.additionalParameters.equals(additionalParameters))
    ) {
      this.additionalParameters = additionalParameters;
      fireTransformationsConfigurationModelEvent(ADDITIONAL_PARAMETERS_CHANGED, this.additionalParameters);
    }
  }

  public String getAdditionalParameters() {
    return additionalParameters;
  }

  public void setBinariesExecutor(Optional<ClustalOmegaBinariesExecutor> clustalOmegaBinariesExecutor) {
    if (clustalOmegaBinariesExecutor.isPresent()) {
      this.binariesExecutor = clustalOmegaBinariesExecutor.get();
    } else {
      this.binariesExecutor = null;
    }
    fireTransformationsConfigurationModelEvent(CLUSTAL_OMEGA_EXECUTOR_CHANGED, this.binariesExecutor);
  }

  public ClustalOmegaBinariesExecutor getBinariesExecutor() {
    return binariesExecutor;
  }
}