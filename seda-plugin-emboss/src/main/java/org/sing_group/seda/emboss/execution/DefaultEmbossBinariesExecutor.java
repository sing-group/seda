/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.execution;

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
public class DefaultEmbossBinariesExecutor extends AbstractEmbossBinariesExecutor {
  @XmlTransient
  private final EmbossEnvironment emboss = EmbossEnvironment.getInstance();
  @XmlElement
  private final File embossDirectory;
  @XmlTransient
  private Optional<Path> directoryPath;

  public DefaultEmbossBinariesExecutor() {
    this(null);
  }

  public DefaultEmbossBinariesExecutor(File embossDirectory) {
    this.embossDirectory = embossDirectory;
  }

  private Optional<Path> getDirectoryPath() {
    if (this.directoryPath == null) {
      if (this.embossDirectory == null) {
        this.directoryPath = empty();
      } else {
        this.directoryPath = of(this.embossDirectory.toPath());
      }
    }
    return directoryPath;
  }

  @Override
  public void getOrf(
    File input, File output, int table, int minSize, int maxSize, int find, String additionalEmbossParameters
  ) throws IOException, InterruptedException {
    super.getOrf(
      asList(composeEmbossCommand(emboss.getOrfCommand())),
      input, output, table, minSize, maxSize, find, additionalEmbossParameters
    );
  }

  @Override
  protected String composeEmbossCommand(String command) {
    return getDirectoryPath().map(path -> path.resolve(command))
      .orElse(Paths.get(command)).toString();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }

  public File getEmbossDirectory() {
    return embossDirectory;
  }
}
