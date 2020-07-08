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
package org.sing_group.seda.pfam.gui;

import static org.sing_group.seda.pfam.gui.PfamScanTransformationConfigurationChangeType.ACTIVE_SITE_PREDICTION_CHANGED;
import static org.sing_group.seda.pfam.gui.PfamScanTransformationConfigurationChangeType.BATCH_DELAY_CHANGED;
import static org.sing_group.seda.pfam.gui.PfamScanTransformationConfigurationChangeType.EMAIL_CHANGED;
import static org.sing_group.seda.pfam.gui.PfamScanTransformationConfigurationChangeType.ERROR_POLICY_CHANGED;
import static org.sing_group.seda.pfam.gui.PfamScanTransformationConfigurationChangeType.EVALUE_CHANGED;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.pfam.PfamScanRequestConfiguration;
import org.sing_group.seda.pfam.PfamScanSequenceErrorPolicy;
import org.sing_group.seda.pfam.transformations.PfamScanSequencesGroupDatasetTransformation;
import org.sing_group.seda.pfam.transformations.PfamScanSequencesGroupTransformation;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PfamScanTransformationProvider extends AbstractTransformationProvider {

  public static final String DEFAULT_DATABASE = "pfam-a";

  @XmlElement
  private String eMail;

  @XmlElement
  private boolean activeSitePrediction;

  @XmlElement
  private Double eValue;

  @XmlElement
  private PfamScanSequenceErrorPolicy errorPolicy;

  @XmlElement
  private int batchDelayFactor;

  public PfamScanTransformationProvider() {}

  public PfamScanTransformationProvider(
    boolean activeSitePredition, PfamScanSequenceErrorPolicy errorPolicy,
    int batchDelayFactor
  ) {
    this.activeSitePrediction = activeSitePredition;
    this.errorPolicy = errorPolicy;
    this.batchDelayFactor = batchDelayFactor;
  }

  @Override
  public boolean isValidTransformation() {
    return this.eMail != null && this.errorPolicy != null && (this.eValue == null || this.eValue > 0)
      && this.batchDelayFactor >= 0;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new PfamScanSequencesGroupDatasetTransformation(getPfamScanTransformation(factory));
  }

  private PfamScanSequencesGroupTransformation getPfamScanTransformation(DatatypeFactory factory) {
    return new PfamScanSequencesGroupTransformation(getPfamScanConfiguration(), factory);
  }

  private PfamScanRequestConfiguration getPfamScanConfiguration() {
    if (this.eValue != null) {
      return new PfamScanRequestConfiguration(
        this.eMail, DEFAULT_DATABASE, this.activeSitePrediction, this.errorPolicy, this.batchDelayFactor, this.eValue
      );
    } else {
      return new PfamScanRequestConfiguration(
        this.eMail, DEFAULT_DATABASE, this.activeSitePrediction, this.errorPolicy, this.batchDelayFactor
      );
    }
  }

  public void setEmail(String eMail) {
    if (this.eMail == null || !this.eMail.equals(eMail)) {
      this.eMail = eMail;
      fireTransformationsConfigurationModelEvent(EMAIL_CHANGED, this.eMail);
    }
  }

  public void clearEmail() {
    if (this.eMail != null) {
      this.eMail = null;
      fireTransformationsConfigurationModelEvent(EMAIL_CHANGED, this.eMail);
    }
  }

  public String geteMail() {
    return eMail;
  }

  public void setActiveSitePrediction(boolean activeSitePrediction) {
    if (this.activeSitePrediction != activeSitePrediction) {
      this.activeSitePrediction = activeSitePrediction;
      fireTransformationsConfigurationModelEvent(ACTIVE_SITE_PREDICTION_CHANGED, this.eMail);
    }
  }

  public boolean isActiveSitePrediction() {
    return activeSitePrediction;
  }

  public void setEvalue(Double eValue) {
    if (this.eValue == null || !this.eValue.equals(eValue)) {
      this.eValue = eValue;
      fireTransformationsConfigurationModelEvent(EVALUE_CHANGED, this.eMail);
    }
  }

  public void clearEvalue() {
    if (this.eValue == null) {
      this.eValue = null;
      fireTransformationsConfigurationModelEvent(EVALUE_CHANGED, this.eMail);
    }
  }

  public Double getEvalue() {
    return eValue;
  }

  public void setErrorPolicy(PfamScanSequenceErrorPolicy errorPolicy) {
    if (this.errorPolicy == null || !this.errorPolicy.equals(errorPolicy)) {
      this.errorPolicy = errorPolicy;
      fireTransformationsConfigurationModelEvent(ERROR_POLICY_CHANGED, this.eMail);
    }
  }

  public PfamScanSequenceErrorPolicy getErrorPolicy() {
    return errorPolicy;
  }

  public void setBatchDelayFactor(int batchDelayFactor) {
    if (this.batchDelayFactor != batchDelayFactor) {
      this.batchDelayFactor = batchDelayFactor;
      fireTransformationsConfigurationModelEvent(BATCH_DELAY_CHANGED, this.eMail);
    }
  }

  public int getBatchDelayFactor() {
    return batchDelayFactor;
  }
}