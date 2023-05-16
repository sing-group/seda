/*
 * #%L
 * SEquence DAtaset builder PfamScan plugin
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
package org.sing_group.seda.pfam;

import static org.sing_group.seda.pfam.PfamScanSequenceErrorPolicy.MARK_ERROR;

import java.util.Optional;

public class PfamScanRequestConfiguration {

  public static final String DEFAULT_DATABASE = "pfam-a";
  public static final double DEFAULT_EVALUE = 10;
  public static final int DEFAULT_BATCH_DELAY_FACTOR = 1;
  public static final PfamScanSequenceErrorPolicy DEFAULT_ERROR_POLICY = MARK_ERROR;

  private String eMail;
  private String database;
  private boolean activeSitePrediction;
  private Double eValue;
  private PfamScanSequenceErrorPolicy errorPolicy;
  private int batchDelayFactor;

  public PfamScanRequestConfiguration(
    String eMail, String database, boolean activeSitePredition, PfamScanSequenceErrorPolicy errorPolicy
  ) {
    this(eMail, database, activeSitePredition, errorPolicy, DEFAULT_BATCH_DELAY_FACTOR, null);
  }

  public PfamScanRequestConfiguration(
    String eMail, String database, boolean activeSitePredition, PfamScanSequenceErrorPolicy errorPolicy,
    int batchDelayFactor
  ) {
    this(eMail, database, activeSitePredition, errorPolicy, batchDelayFactor, null);
  }

  public PfamScanRequestConfiguration(
    String eMail, String database, boolean activeSitePrediction, PfamScanSequenceErrorPolicy errorPolicy,
    int batchDelayFactor, Double eValue
  ) {
    this.eMail = eMail;
    this.database = database;
    this.activeSitePrediction = activeSitePrediction;
    this.errorPolicy = errorPolicy;
    this.eValue = eValue;
    this.batchDelayFactor = batchDelayFactor;
  }

  public String getEmail() {
    return eMail;
  }

  public String getDatabase() {
    return database;
  }

  public boolean isActiveSitePrediction() {
    return activeSitePrediction;
  }

  public Optional<Double> getEvalue() {
    return Optional.ofNullable(eValue);
  }

  public PfamScanSequenceErrorPolicy getErrorPolicy() {
    return errorPolicy;
  }

  public int getBatchDelayFactor() {
    return batchDelayFactor;
  }
}
