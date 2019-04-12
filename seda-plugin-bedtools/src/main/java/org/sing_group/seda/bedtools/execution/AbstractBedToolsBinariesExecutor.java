/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.execution;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;

public abstract class AbstractBedToolsBinariesExecutor extends AbstractBinariesExecutor implements BedToolsBinariesExecutor {

  public void checkBinary() throws BinaryCheckException {
    BedToolsBinariesChecker.checkBedToolsBinary(getBedToolsCommand());
  }

  protected abstract String getBedToolsCommand();

  protected abstract String toFilePath(File file);

  protected void getFasta( List<String> bedToolsCommand, File inputFasta, File bedFile, File output) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(bedToolsCommand);
    parameters.addAll(
      asList(
        "getfasta", 
        "-fi", toFilePath(inputFasta),
        "-bed", toFilePath(bedFile),
        "-fo", toFilePath(output),
        "-name"
        )
      );
    
    executeCommand(parameters);
  }
}
