/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.execution;

import static java.util.Arrays.asList;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.checkDockerAvailability;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.dockerPath;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.getMountDockerDirectoriesString;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.core.execution.DockerImageChecker;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;

@XmlRootElement
public class DockerSappBinariesExecutor extends AbstractSappBinariesExecutor {
  @XmlTransient
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  @XmlAnyElement(lax = true)
  private final DockerSappCommands configuration;

  public DockerSappBinariesExecutor() {
    this(new DefaultDockerSappCommands());
  }

  public DockerSappBinariesExecutor(DockerSappCommands configuration) {
    this.configuration = configuration;
  }

  @Override
  public void fasta2hdt(
    File input, File output, String sampleIdentifier, SappCodon sappCodon, SappSpecies sappSpecies,
    String additionalParameters
  ) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(input.getParent());
    directoriesToMount.add(output.getParent());

    super.fasta2hdt(
      this.composeCommand(directoriesToMount, this.configuration.conversion()),
      input, output, sampleIdentifier, sappCodon, sappSpecies, additionalParameters
    );
  }

  @Override
  public void augustus(File input, File output, SappCodon sappCodon, SappSpecies sappSpecies)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(input.getParent());
    directoriesToMount.add(output.getParent());

    super.augustus(
      this.composeCommand(directoriesToMount, this.configuration.geneCaller()),
      input, output, sappCodon, sappSpecies
    );
  }

  @Override
  protected String getConversionCommand() {
    return this.composeCommand(this.configuration.conversion());
  }

  @Override
  protected String getGeneCallerCommand() {
    return this.composeCommand(this.configuration.geneCaller());
  }

  @Override
  public void checkBinary() throws BinaryCheckException {
    try {
      if (this.dockerImageChecker.shouldCheckDockerImage(this.configuration.dockerImage())) {
        checkDockerAvailability();
        super.checkBinary();
        this.dockerImageChecker.storeImageTimestamp(this.configuration.dockerImage());
      }
    } catch (BinaryCheckException e) {
      throw e;
    }
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  private String composeCommand(String command) {
    return "docker run --rm " + this.configuration.dockerImage() + " " + command;
  }

  private List<String> composeCommand(Set<String> directoriesToMount, String command) {
    return asList(
      ("docker run --rm " + getMountDockerDirectoriesString(directoriesToMount) + " " + this.configuration.dockerImage() + " " + command).split(" ")
    );
  }

  public DockerSappCommands getDockerSappCommands() {
    return this.configuration;
  }
}
