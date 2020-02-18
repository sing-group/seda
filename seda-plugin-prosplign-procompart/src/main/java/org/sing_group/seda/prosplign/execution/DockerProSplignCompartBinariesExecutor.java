/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.execution;

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

public class DockerProSplignCompartBinariesExecutor extends AbstractProSplignCompartBinariesExecutor {
  private final ProSplignCompartEnvironment environment = ProSplignCompartEnvironment.getInstance();
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  private final String dockerImage;

  public DockerProSplignCompartBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-prosplign-procompart";
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
  public void proSplign(
    File compart, File nucleotidesFasta, File proteinQueryFasta, File outputDir, String outputFileName
  ) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(compart.getParent());
    directoriesToMount.add(nucleotidesFasta.getParent());
    directoriesToMount.add(proteinQueryFasta.getParent());
    directoriesToMount.add(outputDir.getAbsolutePath());

    super.proSplign(
      composeProSplignCompartCommand(this.dockerImage, directoriesToMount, this.environment.getProSplignCommand()),
      compart, nucleotidesFasta, proteinQueryFasta, outputDir, outputFileName
    );
  }

  @Override
  public void proCompart(File database, File outputFile) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(database.getParent());
    directoriesToMount.add(outputFile.getParent());

    super.proCompart(
      composeProSplignCompartCommand(this.dockerImage, directoriesToMount, this.environment.getProCompartCommand()),
      database, outputFile
    );
  }

  @Override
  protected String composeCommand(String command) {
    return composeProSplignCompartCommand(this.dockerImage, command);
  }

  private String composeProSplignCompartCommand(String proSplignCompart, String command) {
    return ("docker run --rm " + proSplignCompart + " " + command);
  }

  private List<String> composeProSplignCompartCommand(
    String proSplignCompart, Set<String> directoriesToMount, String command
  ) {
    return asList(
      ("docker run --rm " + getMountDockerDirectoriesString(directoriesToMount) + " " + proSplignCompart + " " + command).split(" ")
    );
  }
}
