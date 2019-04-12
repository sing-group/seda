/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.execution;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.core.execution.InputLineCallback;

public abstract class AbstractSplignCompartBinariesExecutor extends AbstractBinariesExecutor
  implements SplignCompartBinariesExecutor {

  public void checkBinary() throws BinaryCheckException {
    SplignCompartBinariesChecker.checkSplignCompartPath(this::composeCommand);
  }

  protected abstract String composeCommand(String command);

  protected abstract String toFilePath(File file);

  protected void mklds(List<String> splignCommand, File path) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(splignCommand);
    parameters.addAll(asList("-mklds", toFilePath(path)));

    executeCommand(parameters);
  }
  
  protected void ldsdir(List<String> splignCommand, File ldsdir, File comps, File ldsdirFile) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(splignCommand);
    parameters.addAll(asList(
      "-ldsdir", toFilePath(ldsdir),
      "-comps", toFilePath(comps)
    ));
    
    InputLineCallback callBack = new InputLineToFileCallback(ldsdirFile);
    
    executeCommand(parameters, new File("."), callBack);
  }
  
  protected void compart(List<String> compartCommand, File qdb, File sdb, File compartmentsFile) throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(compartCommand);
    parameters.addAll(asList(
      "-qdb", toFilePath(qdb),
      "-sdb", toFilePath(sdb)
    ));
    
    File workingDirectory = new File(toFilePath(qdb.getParentFile()));
    InputLineCallback callBack = new InputLineToFileCallback(compartmentsFile);
    
    executeCommand(parameters, workingDirectory, callBack);
  }
  
  private static final class InputLineToFileCallback implements InputLineCallback {
    private final File file;
    private PrintWriter pw;

    public InputLineToFileCallback(File file) throws FileNotFoundException {
      this.file = file;
      this.pw = new PrintWriter(this.file);
    }

    @Override
    public void inputFinished() {
      if (this.pw != null)
        this.pw.close();
    }

    @Override
    public void inputStarted() {}

    @Override
    public void line(String line) {
      if (this.pw != null)
        this.pw.println(line);
    }

    @Override
    public void error(String message, Exception e) {}

    @Override
    public void info(String message) {}
  }
}
