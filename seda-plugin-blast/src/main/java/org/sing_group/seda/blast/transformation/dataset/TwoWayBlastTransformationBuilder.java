/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.blast.transformation.dataset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.transformation.TransformationException;

public class TwoWayBlastTransformationBuilder {
  private TwoWayBlastMode mode;
  private BlastType blastType;
  private File queryFile;
  private double eValue = TwoWayBlastTransformation.DEFAULT_EVALUE;
  private File databasesDirectory = null;
  private BlastBinariesExecutor blastBinariesExecutor;
  private DatatypeFactory factory = DatatypeFactory.getDefaultDatatypeFactory();
  private String blastAdditionalParameters = "";

  public TwoWayBlastTransformationBuilder(BlastType blastType, File queryFile, TwoWayBlastMode mode) {
    this.blastType = blastType;
    this.queryFile = queryFile;
    this.mode = mode;
  }

  public TwoWayBlastTransformationBuilder withEvalue(double eValue) {
    this.eValue = eValue;
    return this;
  }

  public TwoWayBlastTransformationBuilder withDatabasesDirectory(File databasesDirectory) {
    this.databasesDirectory = databasesDirectory;
    return this;
  }

  public TwoWayBlastTransformationBuilder withBlastBinariesExecutor(BlastBinariesExecutor blastBinariesExecutor) {
    this.blastBinariesExecutor = blastBinariesExecutor;
    return this;
  }

  public TwoWayBlastTransformationBuilder withDatatypeFactory(DatatypeFactory factory) {
    this.factory = factory;
    return this;
  }

  public TwoWayBlastTransformationBuilder withBlastAditionalParameters(String blastAdditionalParameters) {
    this.blastAdditionalParameters = blastAdditionalParameters;
    return this;
  }

  public TwoWayBlastTransformation build() {
    try {
        return new TwoWayBlastTransformation(
          blastType,
          mode,
          blastBinariesExecutor,
          queryFile,
          getDatabasesDirectory(),
          eValue,
          blastAdditionalParameters,
          factory
        );
    } catch (IOException e) {
      throw new TransformationException(e);
    }
  }

  private File getDatabasesDirectory() throws IOException {
    return this.databasesDirectory == null
      ? Files.createTempDirectory("seda-blastdb").toFile()
      : this.databasesDirectory;
  }
}
