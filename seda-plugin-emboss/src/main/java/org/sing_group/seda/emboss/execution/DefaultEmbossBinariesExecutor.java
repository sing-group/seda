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

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class DefaultEmbossBinariesExecutor extends AbstractEmbossBinariesExecutor {
  private final EmbossEnvironment emboss = EmbossEnvironment.getInstance();
  private final Optional<Path> embossPath;

  public DefaultEmbossBinariesExecutor(File embossPath) {
    if (embossPath == null) {
      this.embossPath = empty();
    } else {
      this.embossPath = of(embossPath.toPath());
    }
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
    return embossPath.map(path -> path.resolve(command))
      .orElse(Paths.get(command)).toString();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }
}
