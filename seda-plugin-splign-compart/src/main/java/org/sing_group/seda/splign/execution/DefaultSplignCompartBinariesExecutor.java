/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.execution;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class DefaultSplignCompartBinariesExecutor extends AbstractSplignCompartBinariesExecutor {
  private final SplignCompartEnvironment environment = SplignCompartEnvironment.getInstance();
  private final Optional<Path> splignCompartPath;

  public DefaultSplignCompartBinariesExecutor(File splignCompartPath) {
    if (splignCompartPath == null) {
      this.splignCompartPath = empty();
    } else {
      this.splignCompartPath = of(splignCompartPath.toPath());
    }
  }

  @Override
  public void mklds(File path) throws IOException, InterruptedException {
    super.mklds(asList(composeCommand(environment.getSplignCommand())), path);

  }

  @Override
  public void ldsdir(File ldsdir, File comps, File ldsdirFile) throws IOException, InterruptedException {
    super.ldsdir(asList(composeCommand(environment.getSplignCommand())), ldsdir, comps, ldsdirFile);
  }

  @Override
  public void compart(File qdb, File sdb, File compartmentsFile) throws IOException, InterruptedException {
    super.compart(asList(composeCommand(environment.getCompartCommand())), qdb, sdb, compartmentsFile);
  }

  @Override
  protected String composeCommand(String command) {
    return splignCompartPath.map(path -> path.resolve(command))
      .orElse(Paths.get(command)).toString();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }
}
