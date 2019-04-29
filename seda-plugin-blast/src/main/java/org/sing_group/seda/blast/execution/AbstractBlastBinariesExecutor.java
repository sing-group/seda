/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.blast.execution;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.core.execution.AbstractBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;

public abstract class AbstractBlastBinariesExecutor extends AbstractBinariesExecutor implements BlastBinariesExecutor {

  @Override
  public void checkBinary() throws BinaryCheckException {
    BlastBinariesChecker.checkBlastPath(this::composeBlastCommand);
  }

  protected abstract String composeBlastCommand(String command);

  protected abstract String toFilePath(File file);

  public void blastDbCmd(List<String> blastCommand, File aliasFile, File entryBatchFile, File outFile)
    throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(blastCommand);
    parameters.addAll(
      asList(
        "-db", toFilePath(aliasFile),
        "-entry_batch", toFilePath(entryBatchFile),
        "-out", toFilePath(outFile)
      )
    );

    executeCommand(parameters);
  }

  public void blastDbCmd(
    List<String> blastCommand, File aliasFile, String subjectSequenceID, String range, File outFile
  )
    throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(blastCommand);
    parameters.addAll(
      asList(
        "-db", toFilePath(aliasFile),
        "-entry", subjectSequenceID,
        "-range", range,
        "-out", toFilePath(outFile)
      )
    );

    executeCommand(parameters);
  }

  public void blastDbCmd(List<String> blastCommand, File aliasFile, String subjectSequenceID, File outFile)
    throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(blastCommand);
    parameters.addAll(
      asList(
        "-db", toFilePath(aliasFile),
        "-entry", subjectSequenceID,
        "-out", toFilePath(outFile)
      )
    );

    executeCommand(parameters);
  }

  public void makeBlastDb(List<String> blastCommand, File inFile, String blastSequenceType, File dbFile, boolean parseSeqIds)
    throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(blastCommand);
    parameters.addAll(
      asList(
        "-in", toFilePath(inFile),
        "-dbtype", blastSequenceType,
        "-out", toFilePath(dbFile)
      )
    );

    if (parseSeqIds) {
      parameters.add("-parse_seqids");
    }

    executeCommand(parameters);
  }

  public void makeDbAlias(
    List<String> blastCommand, List<File> blastDatabases, String blastSequenceType, File outFile, String dbAliasTitle
  )
    throws IOException, InterruptedException {
    final List<String> parameters = new LinkedList<>(blastCommand);
    parameters.addAll(
      asList(
        "-dblist", blastDatabases.stream().map(this::toFilePath).collect(Collectors.joining(" ")),
        "-dbtype", blastSequenceType,
        "-out", toFilePath(outFile),
        "-title", dbAliasTitle
      )
    );

    executeCommand(parameters);
  }

  public void executeBlast(
    List<String> blastCommand, BlastType blastType, File queryFile, File database, double expectedValue,
    int maxTargetSeqs, File outFile,
    String outFormat, List<String> additionalBlastParameters
  ) throws IOException, InterruptedException {

    final List<String> parameters = new LinkedList<>(blastCommand);
    parameters.addAll(
      asList(
        "-query", toFilePath(queryFile),
        "-db", toFilePath(database),
        "-evalue", String.valueOf(expectedValue),
        "-max_target_seqs", String.valueOf(maxTargetSeqs),
        "-out", toFilePath(outFile),
        "-outfmt", outFormat
      )
    );

    if (!additionalBlastParameters.isEmpty()) {
      parameters.addAll(additionalBlastParameters);
    }

    executeCommand(parameters);
  }
}
