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
import static org.sing_group.seda.core.execution.DockerExecutionUtils.checkDockerAvailability;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.dockerPath;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.getMountDockerDirectoriesString;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.core.execution.DockerImageChecker;

public class DockerSplignCompartBinariesExecutor extends AbstractSplignCompartBinariesExecutor {
  private final SplignCompartEnvironment environment = SplignCompartEnvironment.getInstance();
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  private final String dockerImage;

  public DockerSplignCompartBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-splign-compart";
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
  public void mklds(File path) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(path.getParent());

    List<String> parameters = new LinkedList<>();
    parameters.addAll(composeSplignCompartCommand(this.dockerImage, directoriesToMount, ""));
    parameters.add("bash");
    parameters.add("-c");
    parameters.add(this.environment.getSplignCommand()
     + " -mklds " + toFilePath(path) + " && chmod -R 777 " + toFilePath(path) + "/_SplignLDS_");

    executeCommand(parameters);
  }

  @Override
  public void ldsdir(File ldsdir, File comps, File ldsdirFile) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(ldsdir.getAbsolutePath());
    directoriesToMount.add(comps.getParent());
    
    super.ldsdir(composeSplignCompartCommand(this.dockerImage, directoriesToMount, this.environment.getSplignCommand()), ldsdir, comps, ldsdirFile);
  }

  @Override
  public void compart(File qdb, File sdb, File compartmentsFile) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(qdb.getParent());
    directoriesToMount.add(sdb.getParent());
    directoriesToMount.add(compartmentsFile.getParent());

    super.compart(
      composeSplignCompartCommand(this.dockerImage, directoriesToMount, this.environment.getCompartCommand()), qdb, sdb, compartmentsFile
    );
  }

  @Override
  protected String composeCommand(String command) {
    return composeProSplignCompartCommand(this.dockerImage, command);
  }

  private static String composeProSplignCompartCommand(String proSplignCompart, String command) {
    return ("docker run --rm " + proSplignCompart + " " + command);
  }

  private static List<String> composeSplignCompartCommand(
    String proSplignCompart, Set<String> directoriesToMount, String command
  ) {
    return asList(
      ("docker run --rm " + getMountDockerDirectoriesString(directoriesToMount) + " " + proSplignCompart + (command.isEmpty() ? "" : " ") + command).split(" ")
    );
  }
}
