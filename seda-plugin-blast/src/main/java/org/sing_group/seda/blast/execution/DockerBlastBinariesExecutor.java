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
package org.sing_group.seda.blast.execution;

import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.sing_group.seda.blast.datatype.blast.BlastEnvironment;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.util.OsUtils;

public class DockerBlastBinariesExecutor extends AbstractBlastBinariesExecutor {
  private static final Map<String, Date> CHECKED_IMAGES = new HashMap<>();
  private static final long CHECKED_IMAGE_VALID_TIME = 5;

  private final BlastEnvironment blast = BlastEnvironment.getInstance();
  private final String dockerImage;

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
  public void makeBlastDb(File inFile, String blastSequenceType, File dbFile) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(inFile.getParent());
    directoriesToMount.add(dbFile.getParent());
    directoriesToMount.addAll(parseAliasFileDbList(dbFile));

    super.makeBlastDb(
      composeBlastCommand(this.dockerImage, directoriesToMount, blast.getMakeBlastDbCommand()),
      inFile, blastSequenceType, dbFile
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

  private List<String> composeBlastCommand(String blastImage, Set<String> directoriesToMount, String command) {
    return asList(
      ("docker run --rm " + getMountDockerDirectoriesString(directoriesToMount) + " " + blastImage + " " + command).split(" ")
    );
  }

  private String getMountDockerDirectoriesString(Set<String> directoriesToMount) {
    return directoriesToMount.stream().map(d -> "-v" + dockerPath(d) + ":" + dockerPath(d)).collect(joining(" "));
  }

  public static String getDefaultDockerImage() {
    return "singgroup/seda-blast";
  }

  @Override
  public void checkBinary() throws BinaryCheckException {
    try {
      if (shouldCheckCurrentDockerImage()) {
        this.checkDockerAvailability();
        super.checkBinary();
        this.storeImageTimestamp();
      }
    } catch (BinaryCheckException e) {
      throw e;
    }
  }

  private boolean shouldCheckCurrentDockerImage() {
    if (CHECKED_IMAGES.containsKey(this.dockerImage)) {
      Date now = getNow();
      long diffMillis = now.getTime() - CHECKED_IMAGES.get(this.dockerImage).getTime();
      if (TimeUnit.MINUTES.convert(diffMillis, TimeUnit.MILLISECONDS) < CHECKED_IMAGE_VALID_TIME) {
        return false;
      } else {
        CHECKED_IMAGES.remove(this.dockerImage);
        return true;
      }
    } else {
      return true;
    }
  }

  private void storeImageTimestamp() {
    CHECKED_IMAGES.put(this.dockerImage, getNow());
  }

  private Date getNow() {
    return Calendar.getInstance().getTime();
  }

  @Override
  protected String composeBlastCommand(String command) {
    return composeBlastCommand(this.dockerImage, command);
  }

  @Override
  protected String toFilePath(File file) {
    return dockerPath(file.getAbsolutePath());
  }

  private String composeBlastCommand(String blastImage, String command) {
    return ("docker run --rm " + blastImage + " " + command);
  }

  private void checkDockerAvailability() throws BinaryCheckException {
    try {
      BlastBinariesChecker.checkCommand("docker --version", 1);
    } catch (BinaryCheckException bce) {
      throw new BinaryCheckException("Error checking docker availability", bce, "docker --version");
    }
  }

  private String dockerPath (String path) {
    if (OsUtils.isWindows()) {
      return path.replaceAll("^(?i)c:", "/c").replaceAll("\\\\", "/");
    }
    if (OsUtils.isMacOs()) {
      return "/private" + path;
    }
    return path;
  }
}
