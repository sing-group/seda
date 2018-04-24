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
package org.sing_group.seda.blast;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sing_group.seda.blast.datatype.blast.BlastEnvironment;
import org.sing_group.seda.blast.datatype.blast.BlastType;

public class BlastBinariesExecutor {
  private final BlastEnvironment blast = BlastEnvironment.getInstance();
  private final Optional<Path> blastPath;

  public BlastBinariesExecutor(File blastPath) {
    if (blastPath == null) {
      this.blastPath = Optional.empty();
    } else {
      this.blastPath = Optional.of(blastPath.toPath());
    }
  }

  public void blastDbCmd(File aliasFile, File entryBatchFile, File outFile) throws IOException, InterruptedException {
    executeCommand(
      composeBlastCommand(blast.getBlastDbCmdCommand()),
      "-db", aliasFile.getAbsolutePath(),
      "-entry_batch", entryBatchFile.getAbsolutePath(),
      "-out", outFile.getAbsolutePath()
    );
  }

  public void blastDbCmd(File aliasFile, String subjectSequenceID, String range, File outFile)
    throws IOException, InterruptedException {
    executeCommand(
      composeBlastCommand(blast.getBlastDbCmdCommand()),
      "-db", aliasFile.getAbsolutePath(),
      "-entry", subjectSequenceID,
      "-range", range,
      "-out", outFile.getAbsolutePath()
    );
  }

  public void blastDbCmd(File aliasFile, String subjectSequenceID, File outFile)
    throws IOException, InterruptedException {
    executeCommand(
      composeBlastCommand(blast.getBlastDbCmdCommand()),
      "-db", aliasFile.getAbsolutePath(),
      "-entry", subjectSequenceID,
      "-out", outFile.getAbsolutePath()
    );
  }

  public void makeBlastDb(File inFile, String blastSequenceType, File dbFile) throws IOException, InterruptedException {
    executeCommand(
      composeBlastCommand(blast.getMakeBlastDbCommand()),
      "-in", inFile.getAbsolutePath(),
      "-dbtype", blastSequenceType,
      "-parse_seqids",
      "-out", dbFile.getAbsolutePath()
    );
  }

  public void makeDbAlias(List<File> blastDatabases, String blastSequenceType, File outFile, String dbAliasTitle)
    throws IOException, InterruptedException {
    executeCommand(
      composeBlastCommand(blast.getBlastDbAliasToolCommand()),
      "-dblist", blastDatabases.stream().map(File::getAbsolutePath).collect(Collectors.joining(" ")).toString(),
      "-dbtype", blastSequenceType,
      "-out", outFile.getAbsolutePath(),
      "-title", dbAliasTitle
    );
  }

  public void executeBlast(
    BlastType blastType, File queryFile, File database, double expectedValue, int maxTargetSeqs, File outFile,
    String outFormat, List<String> additionalBlastParameters
  ) throws IOException, InterruptedException {

    final List<String> parameters = new LinkedList<>();
    parameters.addAll(
      asList(
        composeBlastCommand(blastType.getCommand()),
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

  private String composeBlastCommand(String command) {
    return blastPath.map(path -> path.resolve(command))
      .orElse(Paths.get(command)).toString();
  }

  private void executeCommand(String... parameters) throws IOException, InterruptedException {
    executeCommand(Arrays.asList(parameters));
  }

  private void executeCommand(List<String> parameters) throws IOException, InterruptedException {
    final Runtime runtime = Runtime.getRuntime();
    final Process process = runtime.exec(parameters.toArray(new String[parameters.size()]));
    process.waitFor();
  }

  public void checkBlastPath() throws BinaryCheckException {
    BlastBinariesChecker.checkBlastPath(getBlastPath());
  }

  private File getBlastPath() {
    if (this.blastPath.isPresent()) {
      return this.blastPath.get().toFile();
    } else {
      return null;
    }
  }
}
