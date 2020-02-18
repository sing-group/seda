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
package org.sing_group.seda.sapp.execution;

import static java.lang.String.valueOf;
import static java.nio.file.Files.createTempDirectory;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;

public abstract class AbstractSappBinariesExecutor extends AbstractBinariesExecutor
  implements SappBinariesExecutor {

  @Override
  public void checkBinary() throws BinaryCheckException {
    SappBinariesChecker.checkJar(getConversionCommand());
    SappBinariesChecker.checkJar(getGeneCallerCommand());
  }

  protected abstract String getConversionCommand();

  protected abstract String getGeneCallerCommand();

  protected abstract String toFilePath(File file);

  public void fasta2hdt(
    List<String> sappCommand,
    File input, File output, String sampleIdentifier, SappCodon sappCodon, SappSpecies sappSpecies, String additionalParameters
  ) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(sappCommand);
    parameters.addAll(
      asList(
        "-fasta2hdt",
        "-input", toFilePath(input),
        "-output", toFilePath(output),
        "-identifier", sampleIdentifier,
        "-organism", sappSpecies.getSpecies(),
        "-codon", valueOf(sappCodon.getParamValue())
      )
    );

    if (!additionalParameters.isEmpty()) {
      parameters.addAll(getAdditionalParameters(additionalParameters));
    }

    executeCommand(parameters);
  }

  public void augustus(
    List<String> sappCommand,
    File input, File output, SappCodon sappCodon, SappSpecies sappSpecies
  ) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(sappCommand);
    parameters.addAll(
      asList(
        "-augustus",
        "-input", toFilePath(input),
        "-output", toFilePath(output),
        "-codon", valueOf(sappCodon.getParamValue()),
        "-species", sappSpecies.getIdentifier()
      )
    );

    executeCommand(parameters, createTempDirectory("augustus-tmp-working-dir").toFile());
  }

  private Collection<? extends String> getAdditionalParameters(String additionalParameters) {
    return asList(additionalParameters.split(" "));
  }
}
