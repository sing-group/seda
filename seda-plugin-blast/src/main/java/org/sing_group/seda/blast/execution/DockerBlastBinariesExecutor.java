/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.blast.execution;

import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.checkDockerAvailability;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.dockerPath;
import static org.sing_group.seda.core.execution.DockerExecutionUtils.getMountDockerDirectoriesString;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.core.execution.DockerImageChecker;

@XmlRootElement
public class DockerBlastBinariesExecutor extends AbstractBlastBinariesExecutor {
  @XmlTransient
  private final BlastEnvironment blast = BlastEnvironment.getInstance();
  @XmlTransient
  private final DockerImageChecker dockerImageChecker = DockerImageChecker.getInstance();
  @XmlElement
  private final String dockerImage;

  public DockerBlastBinariesExecutor() {
    this(getDefaultDockerImage());
  }

  public DockerBlastBinariesExecutor(String dockerImage) {
    this.dockerImage = dockerImage;
  }

  @Override
  public void blastDbCmd(File aliasFile, File entryBatchFile, File outFile) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(aliasFile.getParent());
    directoriesToMount.add(outFile.getParent());
    directoriesToMount.addAll(parseAliasFileDbList(aliasFile));

    super.blastDbCmd(
      composeBlastCommand(this.dockerImage, directoriesToMount, blast.getBlastDbCmdCommand()),
      aliasFile, entryBatchFile, outFile
    );
  }

  @Override
  public void blastDbCmd(File aliasFile, String subjectSequenceID, String range, File outFile)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(aliasFile.getParent());
    directoriesToMount.add(outFile.getParent());
    directoriesToMount.addAll(parseAliasFileDbList(aliasFile));

    super.blastDbCmd(
      composeBlastCommand(this.dockerImage, directoriesToMount, blast.getBlastDbCmdCommand()),
      aliasFile, subjectSequenceID, range, outFile
    );
  }

  @Override
  public void blastDbCmd(File aliasFile, String subjectSequenceID, File outFile)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(aliasFile.getParent());
    directoriesToMount.add(outFile.getParent());
    directoriesToMount.addAll(parseAliasFileDbList(aliasFile));

    super.blastDbCmd(
      composeBlastCommand(this.dockerImage, directoriesToMount, blast.getBlastDbCmdCommand()),
      aliasFile, subjectSequenceID, outFile
    );
  }

  /*
   * This method must be invoked on any BLAST alias/db files because alias files
   * contain reference to database paths which must be also mounted in the
   * Docker command.
   */
  private Set<String> parseAliasFileDbList(final File aliasFile) {
    Set<String> dbDirectories = new HashSet<>();

    File nalAliasFile = new File(aliasFile.getAbsolutePath() + ".nal");
    if (nalAliasFile.exists()) {
      try {
        readAllLines(nalAliasFile.toPath()).stream().forEach(l -> {
          if (l.startsWith("DBLIST")) {
            String[] dirs = l.replace("DBLIST ", "").split(" ");
            for (String dir : dirs) {
              String dbDir = dir.replace("\"", "");
              dbDir = dbDir.substring(0, dbDir.lastIndexOf("/"));
              dbDirectories.add(dbDir);
            }
          }
        });
      } catch (IOException e) {}
    }

    return dbDirectories;
  }

  @Override
  public void makeBlastDb(File inFile, String blastSequenceType, File dbFile, boolean parseSeqIds) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(inFile.getParent());
    directoriesToMount.add(dbFile.getParent());
    directoriesToMount.addAll(parseAliasFileDbList(dbFile));

    super.makeBlastDb(
      composeBlastCommand(this.dockerImage, directoriesToMount, blast.getMakeBlastDbCommand()),
      inFile, blastSequenceType, dbFile, parseSeqIds
    );
  }

  @Override
  public void makeDbAlias(List<File> blastDatabases, String blastSequenceType, File outFile, String dbAliasTitle)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    blastDatabases.forEach(file -> directoriesToMount.add(file.getParent()));
    directoriesToMount.add(outFile.getParent());

    super.makeDbAlias(
      composeBlastCommand(this.dockerImage, directoriesToMount, blast.getBlastDbAliasToolCommand()),
      blastDatabases, blastSequenceType, outFile, dbAliasTitle
    );
  }

  @Override
  public void executeBlast(
    BlastType blastType, File queryFile, File dbFile, double expectedValue, int maxTargetSeqs, File outFile,
    String outFormat, List<String> additionalBlastParameters
  ) throws IOException, InterruptedException {

    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(queryFile.getParent());
    directoriesToMount.add(dbFile.getParent());
    directoriesToMount.add(outFile.getParent());
    directoriesToMount.addAll(parseAliasFileDbList(dbFile));

    super.executeBlast(
      composeBlastCommand(this.dockerImage, directoriesToMount, blastType.getCommand()),
      blastType, queryFile, dbFile, expectedValue, maxTargetSeqs, outFile, outFormat, additionalBlastParameters
    );
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-blast";
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
  protected String composeBlastCommand(String command) {
    return composeBlastCommand(this.dockerImage, command);
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  private List<String> composeBlastCommand(String blastImage, Set<String> directoriesToMount, String command) {
    return asList(
      ("docker run --rm " + getMountDockerDirectoriesString(directoriesToMount) + " " + blastImage + " " + command).split(" ")
    );
  }

  private String composeBlastCommand(String blastImage, String command) {
    return ("docker run --rm " + blastImage + " " + command);
  }

  public String getDockerImage() {
    return dockerImage;
  }
}
