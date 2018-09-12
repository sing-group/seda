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
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DockerClustalOmegaBinariesExecutor extends AbstractClustalOmegaBinariesExecutor {
  private final String dockerImage;

  public DockerClustalOmegaBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }

  public void executeAlignment(
    File inputFile, File outputFile, int numThreads, String additionalParameters
  ) throws IOException, InterruptedException {
    executeAlignment(
      getClustalOmegaCommandList(inputFile, outputFile), inputFile, outputFile, numThreads, additionalParameters
    );
  }
  
  private List<String> getClustalOmegaCommandList(File inputFile, File outputFile) {
    return asList(getClustalOmegaCommand(inputFile, outputFile).split(" "));
  }

  private String getClustalOmegaCommand(File inputFile, File outputFile) {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(inputFile.getParent());
    directoriesToMount.add(outputFile.getParent());
    String dockerDirectories =
      directoriesToMount.stream().map(d -> new String("-v" + d + ":" + d)).collect(joining(" "));

    return composeClustalOmegaCommand(this.dockerImage, dockerDirectories);
  }

  @Override
  protected String getClustalOmegaCommand() {
    return composeClustalOmegaCommand(this.dockerImage, "");
  }
  
  protected static String composeClustalOmegaCommand(String clustalOmegaImage, String dockerDirectories) {
    return "docker run --rm " + dockerDirectories + " " + clustalOmegaImage;
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-clustalomega";
  }

  @Override
  public void checkBinary() throws BinaryCheckException {
    this.checkDockerAvailability();
    super.checkBinary();
  }

  private void checkDockerAvailability() throws BinaryCheckException {
    try {
      ClustalOmegaBinariesChecker.checkCommand("docker --version", 1);
    } catch (BinaryCheckException bce) {
      throw new BinaryCheckException("Error checkin docker availability", bce, "docker --version");
    }
  }
}
