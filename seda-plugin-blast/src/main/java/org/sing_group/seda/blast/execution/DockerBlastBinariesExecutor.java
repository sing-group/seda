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

import org.sing_group.seda.blast.datatype.blast.BlastEnvironment;
import org.sing_group.seda.blast.datatype.blast.BlastType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class DockerBlastBinariesExecutor implements BlastBinariesExecutor {
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
    String dockerDirectories =
      directoriesToMount.stream().map(d -> "-v" + d + ":" + d).collect(joining(" "));
    final List<String> parameters = new LinkedList<>(composeBlastCommand(this.dockerImage, dockerDirectories, blast.getBlastDbCmdCommand()));
    parameters.
      addAll(
        asList(
          "-db", aliasFile.getAbsolutePath(),
          "-entry_batch", entryBatchFile.getAbsolutePath(),
          "-out", outFile.getAbsolutePath()
        )
      );

    executeCommand(parameters);
  }

  @Override
  public void blastDbCmd(File aliasFile, String subjectSequenceID, String range, File outFile)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(aliasFile.getParent());
    directoriesToMount.add(outFile.getParent());
    String dockerDirectories =
      directoriesToMount.stream().map(d -> "-v" + d + ":" + d).collect(joining(" "));
    final List<String> parameters = new LinkedList<>(composeBlastCommand(this.dockerImage, dockerDirectories, blast.getBlastDbCmdCommand()));
    parameters.
      addAll(
        asList(
          "-db", aliasFile.getAbsolutePath(),
          "-entry", subjectSequenceID,
          "-range", range,
          "-out", outFile.getAbsolutePath()
        )
      );

    executeCommand(parameters);
  }

  @Override
  public void blastDbCmd(File aliasFile, String subjectSequenceID, File outFile)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(aliasFile.getParent());
    directoriesToMount.add(outFile.getParent());
    String dockerDirectories =
      directoriesToMount.stream().map(d -> "-v" + d + ":" + d).collect(joining(" "));
    final List<String> parameters = new LinkedList<>(composeBlastCommand(this.dockerImage, dockerDirectories, blast.getBlastDbCmdCommand()));
    parameters.
      addAll(
        asList(
          "-db", aliasFile.getAbsolutePath(),
          "-entry", subjectSequenceID,
          "-out", outFile.getAbsolutePath()
        )
      );

    System.out.println(Arrays.toString(parameters.toArray()));
    executeCommand(parameters);
  }

  @Override
  public void makeBlastDb(File inFile, String blastSequenceType, File dbFile) throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(inFile.getParent());
    directoriesToMount.add(dbFile.getParent());
    String dockerDirectories =
      directoriesToMount.stream().map(d -> "-v" + d + ":" + d).collect(joining(" "));
    final List<String> parameters = new LinkedList<>(composeBlastCommand(this.dockerImage, dockerDirectories, blast.getMakeBlastDbCommand()));
    parameters.
      addAll(
        asList(
          "-in", inFile.getAbsolutePath(),
          "-dbtype", blastSequenceType,
          "-parse_seqids",
          "-out", dbFile.getAbsolutePath()
        )
      );

    executeCommand(parameters);
  }

  @Override
  public void makeDbAlias(List<File> blastDatabases, String blastSequenceType, File outFile, String dbAliasTitle)
    throws IOException, InterruptedException {
    Set<String> directoriesToMount = new HashSet<>();
    blastDatabases.forEach(file -> directoriesToMount.add(file.getParent()));
    directoriesToMount.add(outFile.getParent());
    String dockerDirectories =
      directoriesToMount.stream().map(d -> "-v" + d + ":" + d).collect(joining(" "));
    final List<String> parameters = new LinkedList<>(composeBlastCommand(this.dockerImage, dockerDirectories, blast.getBlastDbAliasToolCommand()));
    parameters.
      addAll(
        asList(
          "-dblist", blastDatabases.stream().map(File::getAbsolutePath).collect(Collectors.joining(" ")),
          "-dbtype", blastSequenceType,
          "-out", outFile.getAbsolutePath(),
          "-title", dbAliasTitle
        )
      );

    executeCommand(parameters);
  }

  @Override
  public void executeBlast(
    BlastType blastType, File queryFile, File database, double expectedValue, int maxTargetSeqs, File outFile,
    String outFormat, List<String> additionalBlastParameters
  ) throws IOException, InterruptedException {

    Set<String> directoriesToMount = new HashSet<>();
    directoriesToMount.add(queryFile.getParent());
    directoriesToMount.add(database.getParent());
    directoriesToMount.add(outFile.getParent());
    String dockerDirectories =
      directoriesToMount.stream().map(d -> "-v" + d + ":" + d).collect(joining(" "));
    final List<String> parameters = new LinkedList<>(composeBlastCommand(this.dockerImage, dockerDirectories, blastType.getCommand()));
    parameters.addAll(
      asList(
        "-query", queryFile.getAbsolutePath(),
        "-db", database.getAbsolutePath(),
        "-evalue", String.valueOf(expectedValue),
        "-max_target_seqs", String.valueOf(maxTargetSeqs),
        "-out", outFile.getAbsolutePath(),
        "-outfmt", outFormat
      )
    );
    parameters.addAll(additionalBlastParameters);

    executeCommand(parameters);
  }

  private List<String> composeBlastCommand(String blastImage, String dockerDirectories, String command) {
    return asList(("docker run --rm " + dockerDirectories + " " + blastImage + " " + command).split(" ")) ;
  }

  private void executeCommand(String... parameters) throws IOException, InterruptedException {
    System.out.println("Execute command: " + Arrays.toString(parameters));
    executeCommand(Arrays.asList(parameters));
  }

  private void executeCommand(List<String> parameters) throws IOException, InterruptedException {
    final Runtime runtime = Runtime.getRuntime();
    final Process process = runtime.exec(parameters.toArray(new String[parameters.size()]));
    process.waitFor();
  }

  public static String getDefaultDockerImage() {
    return "biocontainers/blast";
  }

  @Override
  public void checkBinary() throws BinaryCheckException {
    System.out.println("dockerblastbinariesexecutor::checkbinary");
    this.checkDockerAvailability();
  }

  private void checkDockerAvailability() throws BinaryCheckException {
    System.out.println("checkdockeravailability");
    try {
      BlastBinariesChecker.checkCommand("docker --version", 1);
    } catch (BinaryCheckException bce) {
      throw new BinaryCheckException("Error checking docker availability", bce, "docker --version");
    }
  }
}
