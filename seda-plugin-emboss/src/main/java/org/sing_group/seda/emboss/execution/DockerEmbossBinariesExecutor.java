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
import static org.sing_group.seda.core.execution.DockerExecutionUtils.checkDockerAvailability;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.dockerPath;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.getMountDockerDirectoriesString;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.core.execution.DockerImageChecker;

public class DockerEmbossBinariesExecutor extends AbstractEmbossBinariesExecutor {
  private final EmbossEnvironment emboss = EmbossEnvironment.getInstance();
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  private final String dockerImage;

  public DockerEmbossBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }
  
  @Override
  public void getOrf(
    File input, File output, int table, int minSize, int maxSize, int find, String additionalEmbossParameters
  ) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(input.getParent());
    directoriesToMount.add(output.getParent());
    
    super.getOrf(
      composeEmbossCommand(this.dockerImage, directoriesToMount, emboss.getOrfCommand()), 
      input, output, table, minSize, maxSize, find, additionalEmbossParameters
    );
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-emboss";
  }

  @Override
  public void checkBinary() throws BinaryCheckException {
    try {
      if (this.dockerImageChecker.shouldCheckDockerImage(this.dockerImage)) {
        checkDockerAvailability();
        super.checkBinary();
        this.dockerImageChecker.storeImageTimestamp(this.dockerImage);
      }
    } catch (BinaryCheckException e) {
      throw e;
    }
  }

  @Override
  protected String composeEmbossCommand(String command) {
    return composeEmbossCommand(this.dockerImage, command);
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  private List<String> composeEmbossCommand(String embossImage, Set<String> directoriesToMount, String command) {
    return asList(
      ("docker run --rm " + getMountDockerDirectoriesString(directoriesToMount) + " " + embossImage + " " + command).split(" ")
    );
  }

  private String composeEmbossCommand(String embossImage, String command) {
    return ("docker run --rm " + embossImage + " " + command);
  }
}
