/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;

import org.sing_group.seda.util.OsUtils;

public class DefaultClustalOmegaBinariesExecutor extends AbstractClustalOmegaBinariesExecutor {
  private final File clustalOmegaExecutable;

  public DefaultClustalOmegaBinariesExecutor(File clustalOmegaExecutable) {
    this.clustalOmegaExecutable = clustalOmegaExecutable;
  }

  public void executeAlignment(
    File inputFile, File outputFile, int numThreads, String additionalParameters
  ) throws IOException, InterruptedException {
    executeAlignment(
      asList(this.clustalOmegaExecutable.getAbsolutePath()), inputFile, outputFile, numThreads, additionalParameters
    );
  }

  @Override
  protected String getClustalOmegaCommand() {
    return this.clustalOmegaExecutable.getAbsolutePath();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }

  public static String getClustalOmegaBinaryFileName() {
    if (OsUtils.isWindows()) {
      return "clustalo.exe";
    } else {
      return "clustalo";
    }
  }
}
