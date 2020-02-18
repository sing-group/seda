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

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.sing_group.seda.blast.datatype.blast.BlastType;

public class DefaultBlastBinariesExecutor extends AbstractBlastBinariesExecutor {
  private final BlastEnvironment blast = BlastEnvironment.getInstance();
  private final Optional<Path> blastPath;

  public DefaultBlastBinariesExecutor(File blastPath) {
    if (blastPath == null) {
      this.blastPath = empty();
    } else {
      this.blastPath = of(blastPath.toPath());
    }
  }

  @Override
  public void blastDbCmd(File aliasFile, File entryBatchFile, File outFile) throws IOException, InterruptedException {
    super.blastDbCmd(
      asList(composeBlastCommand(blast.getBlastDbCmdCommand())),
      aliasFile, entryBatchFile, outFile
    );
  }

  @Override
  public void blastDbCmd(File aliasFile, String subjectSequenceID, String range, File outFile)
    throws IOException, InterruptedException {
    super.blastDbCmd(
      asList(composeBlastCommand(blast.getBlastDbCmdCommand())),
      aliasFile, subjectSequenceID, range, outFile
    );
  }

  @Override
  public void blastDbCmd(File aliasFile, String subjectSequenceID, File outFile)
    throws IOException, InterruptedException {
    super.blastDbCmd(
      asList(composeBlastCommand(blast.getBlastDbCmdCommand())),
      aliasFile, subjectSequenceID, outFile
    );
  }

  @Override
  public void makeBlastDb(File inFile, String blastSequenceType, File dbFile, boolean parseSeqIds) throws IOException, InterruptedException {
    super.makeBlastDb(
      asList(composeBlastCommand(blast.getMakeBlastDbCommand())),
      inFile, blastSequenceType, dbFile, parseSeqIds
    );
  }

  @Override
  public void makeDbAlias(List<File> blastDatabases, String blastSequenceType, File outFile, String dbAliasTitle)
    throws IOException, InterruptedException {
    super.makeDbAlias(
      asList(composeBlastCommand(blast.getBlastDbAliasToolCommand())),
      blastDatabases, blastSequenceType, outFile, dbAliasTitle
    );
  }

  @Override
  public void executeBlast(
    BlastType blastType, File queryFile, File database, double expectedValue, int maxTargetSeqs, File outFile,
    String outFormat, List<String> additionalBlastParameters
  ) throws IOException, InterruptedException {
    super.executeBlast(
      asList(composeBlastCommand(blastType.getCommand())),
      blastType, queryFile, database, expectedValue, maxTargetSeqs, outFile, outFormat, additionalBlastParameters
    );
  }

  @Override
  protected String composeBlastCommand(String command) {
    return blastPath.map(path -> path.resolve(command))
      .orElse(Paths.get(command)).toString();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }
}
