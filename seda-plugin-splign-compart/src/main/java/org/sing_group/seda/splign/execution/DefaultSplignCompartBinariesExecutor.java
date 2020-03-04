/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.execution;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class DefaultSplignCompartBinariesExecutor extends AbstractSplignCompartBinariesExecutor {
  @XmlTransient
  private final SplignCompartEnvironment environment = SplignCompartEnvironment.getInstance();
  @XmlElement
  private final File splignCompartDirectory;
  @XmlTransient
  private Optional<Path> directoryPath;

  public DefaultSplignCompartBinariesExecutor() {
    this(null);
  }

  public DefaultSplignCompartBinariesExecutor(File splignCompartDirectory) {
    this.splignCompartDirectory = splignCompartDirectory;
  }

  private Optional<Path> getDirectoryPath() {
    if (this.directoryPath == null) {
      if (this.splignCompartDirectory == null) {
        this.directoryPath = empty();
      } else {
        this.directoryPath = of(this.splignCompartDirectory.toPath());
      }
    }
    return directoryPath;
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
    return getDirectoryPath().map(path -> path.resolve(command))
      .orElse(Paths.get(command)).toString();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }

  public File getSplignCompartDirectory() {
    return this.splignCompartDirectory;
  }
}
