/*
 * #%L
 * SEquence DAtaset builder Clustal Omega plugin
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
package org.sing_group.seda.clustalomega.execution;

import static java.nio.file.Files.createTempFile;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;

public abstract class AbstractClustalOmegaBinariesExecutor extends AbstractBinariesExecutor implements ClustalOmegaBinariesExecutor {

  public void checkBinary() throws BinaryCheckException {
    ClustalOmegaBinariesChecker.checkClustalOmegaBinary(getClustalOmegaCommand());
  }

  protected abstract String getClustalOmegaCommand();

  protected abstract String toFilePath(File file);

  protected void executeAlignment(
    List<String> clustalCommand, File inputFile, File outputFile, int numThreads, String additionalParameters
  ) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(clustalCommand);
    parameters.addAll(
      asList(
        "--threads=" + numThreads,
        "-i", toFilePath(inputFile),
        "-o", toFilePath(outputFile),
        "--force"
      )
    );
    if (!additionalParameters.isEmpty()) {
      parameters.addAll(getAdditionalParameters(additionalParameters));
    }

    executeCommand(parameters);
  }

  private Collection<? extends String> getAdditionalParameters(String additionalParameters) {
    return asList(additionalParameters.split(" "));
  }

  protected void executeCommand(List<String> parameters) throws IOException, InterruptedException {
    final File errorFile = createTempFile("clustal-omega-command-error", ".txt").toFile();
    final File outputFile = createTempFile("clustal-omega-command-output", ".txt").toFile();

    executeCommand(parameters, errorFile, outputFile);
  }
}
