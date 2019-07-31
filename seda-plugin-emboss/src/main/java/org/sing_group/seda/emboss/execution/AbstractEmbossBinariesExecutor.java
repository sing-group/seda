/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.execution;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;

public abstract class AbstractEmbossBinariesExecutor extends AbstractBinariesExecutor
  implements EmbossBinariesExecutor {

  @Override
  public void checkBinary() throws BinaryCheckException {
    EmbossBinariesChecker.checkEmbossPath(this::composeEmbossCommand);
  }

  protected abstract String composeEmbossCommand(String command);

  protected abstract String toFilePath(File file);

  public void getOrf(
    List<String> embossCommand,
    File input, File output, int table, int minSize, int maxSize, int find, String additionalEmbossParameters
  ) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(embossCommand);
    parameters.addAll(
      asList(
        "-sequence", toFilePath(input),
        "-outseq", toFilePath(output),
        "-table", valueOf(table),
        "-minsize", valueOf(minSize),
        "-maxsize", valueOf(maxSize),
        "-find", valueOf(find)
      )
    );

    if (!additionalEmbossParameters.isEmpty()) {
      parameters.addAll(getAdditionalParameters(additionalEmbossParameters));
    }

    executeCommand(parameters);
  }
  
  private Collection<? extends String> getAdditionalParameters(String additionalParameters) {
    return asList(additionalParameters.split(" "));
  }
}
