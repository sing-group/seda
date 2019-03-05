/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.execution;

import static java.nio.file.Files.readAllLines;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AbstractBinariesExecutor {
  protected void executeCommand(List<String> parameters, File errorFile, File outputFile) throws IOException, InterruptedException {
    ProcessBuilder pBuilder = new ProcessBuilder(parameters.toArray(new String[parameters.size()]));
    pBuilder.redirectError(errorFile);
    pBuilder.redirectOutput(outputFile);

    int result = pBuilder.start().waitFor();

    if (result > 0) {
      String error = readAllLines(errorFile.toPath()).stream().collect(joining("\n"));
      String output = readAllLines(outputFile.toPath()).stream().collect(joining("\n"));

      throw new IOException(
        "Error running command. Exit status: " + result + ". Error: " + output + "\n" + error
      );
    }
  }
  
  protected void executeCommand(List<String> parameters) throws IOException, InterruptedException {
    final Runtime runtime = Runtime.getRuntime();
    final Process process = runtime.exec(parameters.toArray(new String[parameters.size()]));
    process.waitFor();
  }
}
