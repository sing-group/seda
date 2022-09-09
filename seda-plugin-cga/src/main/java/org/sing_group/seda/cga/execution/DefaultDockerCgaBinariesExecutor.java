/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.execution;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.checkDockerAvailability;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.getMountDockerDirectoriesString;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.core.execution.DockerImageChecker;

@XmlRootElement
public class DefaultDockerCgaBinariesExecutor extends AbstractBinariesExecutor implements CgaBinariesExecutor {
  @XmlTransient
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  @XmlElement
  private final String dockerImage;

  public DefaultDockerCgaBinariesExecutor() {
    this(getDefaultDockerImage());
  }

  public DefaultDockerCgaBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }

  @Override
  public void initWorkingDir(File workingDir, String... removeFiles) throws IOException, InterruptedException {
    workingDir.mkdirs();

    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(workingDir.getAbsolutePath());

    super.executeCommand(
      composeCommand(
        "docker run --rm", 
        getMountDockerDirectoriesString(directoriesToMount), 
        this.dockerImage, 
        "init_working_dir.sh",
        workingDir.getAbsolutePath()
      )
    );

    List<String> removeFilesList = Arrays.asList(removeFiles);
    if(!removeFilesList.isEmpty()) {
    super.executeCommand(
      composeCommand(
        "docker run --rm --entrypoint=/usr/bin/rm", 
        getMountDockerDirectoriesString(directoriesToMount), 
        this.dockerImage,
        removeFilesList.stream().map(file -> workingDir.getAbsolutePath() + "/" + file).collect(joining(" "))
        )
      );
    }
  }

  @Override
  public void run(File workingDir, String additionalCompiParameters) throws IOException, InterruptedException {
    super.executeCommand(
      asList(workingDir.getAbsolutePath() + "/run.sh", workingDir.getAbsolutePath(), additionalCompiParameters)
    );
  }

  private List<String> composeCommand(String... parameters) {
    return asList(stream(parameters).collect(joining(" ")).split(" "));
  }

  public static String getDefaultDockerImage() {
    return "pegi3s/cga";
  }

  @Override
  public void checkBinary() throws BinaryCheckException {
    try {
      if (this.dockerImageChecker.shouldCheckDockerImage(this.dockerImage)) {
        checkDockerAvailability();
        DefaultDockerCgaBinariesChecker.checkCgaAvailability(this.dockerImage);
        this.dockerImageChecker.storeImageTimestamp(this.dockerImage);
      }
    } catch (BinaryCheckException e) {
      throw e;
    }
  }

  public String getDockerImage() {
    return dockerImage;
  }
}
