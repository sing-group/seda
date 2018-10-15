/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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

import org.sing_group.seda.blast.datatype.blast.BlastType;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BlastBinariesExecutor {
  void blastDbCmd(File aliasFile, File entryBatchFile, File outFile) throws IOException, InterruptedException;

  void blastDbCmd(File aliasFile, String subjectSequenceID, String range, File outFile)
    throws IOException, InterruptedException;

  void blastDbCmd(File aliasFile, String subjectSequenceID, File outFile)
    throws IOException, InterruptedException;

  void makeBlastDb(File inFile, String blastSequenceType, File dbFile) throws IOException, InterruptedException;

  void makeDbAlias(List<File> blastDatabases, String blastSequenceType, File outFile, String dbAliasTitle)
    throws IOException, InterruptedException;

  void executeBlast(
    BlastType blastType, File queryFile, File database, double expectedValue, int maxTargetSeqs, File outFile,
    String outFormat, List<String> additionalBlastParameters
  ) throws IOException, InterruptedException;

  void checkBinary() throws BinaryCheckException;
}
