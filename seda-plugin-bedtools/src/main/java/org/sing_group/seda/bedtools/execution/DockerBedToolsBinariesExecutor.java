/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.execution;

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

public class DockerBedToolsBinariesExecutor extends AbstractBedToolsBinariesExecutor {
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  private final BedToolsEnvironment bedToolsEnvironment = BedToolsEnvironment.getInstance();
  private final String dockerImage;

  public DockerBedToolsBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }

  @Override
  public void getFasta(File inputFasta, File bedFile, File output, String additionalParameters)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(inputFasta.getParent());
    directoriesToMount.add(bedFile.getParent());
    directoriesToMount.add(output.getParent());
    
    super.getFasta(
      composeBedToolsCommand(
        this.dockerImage, this.bedToolsEnvironment.getBedToolsCommand(),
        getMountDockerDirectoriesString(directoriesToMount)
      ), inputFasta, bedFile, output, additionalParameters
    ); 
  }
  
  @Override
  protected String getBedToolsCommand() {
    return "docker run --rm " + this.dockerImage + " " + bedToolsEnvironment.getBedToolsCommand();
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  protected static List<String> composeBedToolsCommand(String bedToolsDockerImage, String bedToolsCommand, String dockerDirectories) {
    return asList(("docker run --rm " + dockerDirectories + " " + bedToolsDockerImage + " " + bedToolsCommand).split(" "));
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-bedtools";
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
  
  public static void main(String[] args) throws BinaryCheckException {
    new DockerBedToolsBinariesExecutor(getDefaultDockerImage()).checkBinary();
  }
}
