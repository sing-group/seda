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
package org.sing_group.seda.blast.transformation.dataset;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sing_group.seda.blast.BlastUtils;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.io.FastaWriter;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;
import org.sing_group.seda.util.OsUtils;

public class TwoWayBlastTransformation implements SequencesGroupDatasetTransformation {
  public final static SequenceType DEFAULT_SEQUENCE_TYPE = SequenceType.NUCLEOTIDES;
  public final static BlastType DEFAULT_BLAST_TYPE = BlastType.BLASTN;
  public final static double DEFAULT_EVALUE = 0.05d;

  private BlastBinariesExecutor defaultBlastBinariesExecutor;

  private final TwoWayBlastMode mode;
  private final SequenceType databaseType;
  private final BlastType blastType;
  private final File databasesDirectory;
  private final double evalue;
  private DatatypeFactory factory;
  private File queryFile;
  private final String blastAdditionalParameters;

  public TwoWayBlastTransformation(
    BlastType blastType,
    TwoWayBlastMode mode,
    BlastBinariesExecutor blastBinariesExecutor,
    File queryFile,
    File databasesPath,
    double evalue,
    String blastAdditionalParameters,
    DatatypeFactory factory
  ) {
    this.databaseType = blastType.getDatabaseType();
    this.mode = mode;
    this.blastType = blastType;
    this.defaultBlastBinariesExecutor = blastBinariesExecutor;
    this.databasesDirectory = databasesPath;
    this.queryFile = queryFile;
    this.evalue = evalue;
    this.blastAdditionalParameters = blastAdditionalParameters;
    this.factory = factory;

    if (!this.isValidConfiguration()) {
      throw new RuntimeException("Invalid configuration");
    }
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset)
    throws TransformationException {
    requireNonNull(dataset);

    return twoWayBlast(dataset);
  }

  private SequencesGroupDataset twoWayBlast(SequencesGroupDataset dataset) {
    SequencesGroup queryFasta = this.factory.newSequencesGroup(this.queryFile.toPath());
    Map<SequencesGroup, File> blastDatabases = null;
    try {
      blastDatabases = makeBlastDatabases(dataset, queryFasta, this.databasesDirectory);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("An error occurred while creating the databases");
    }
    try {
      return twoWayBlast(queryFasta, dataset, blastDatabases);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("An error occurred while running blast");
    }
  }

  private Optional<String> extractFirstSubject(File blastResult) throws IOException {
    Optional<String> firstHit = Files.readAllLines(blastResult.toPath()).stream().findFirst();
    if (firstHit.isPresent()) {
      String[] split = firstHit.get().split("\t");
      if (split.length > 1) {
        return Optional.of(split[1]);
      }
    }
    return Optional.empty();
  }

  private Map<SequencesGroup, File> makeBlastDatabases(
    SequencesGroupDataset dataset,
    SequencesGroup queryFasta,
    File databasesDirectory
  ) throws IOException, InterruptedException {
    Map<SequencesGroup, File> blastDatabases = new HashMap<>();

    blastDatabases.put(queryFasta, makeBlastDatabase(queryFasta, databasesDirectory));

    for (SequencesGroup fasta : dataset.getSequencesGroups().collect(Collectors.toList())) {
      blastDatabases.put(fasta, makeBlastDatabase(fasta, databasesDirectory));
    }

    return blastDatabases;
  }

  private File makeBlastDatabase(SequencesGroup fasta, File databasesDirectory)
    throws IOException, InterruptedException {
    final Path fastaFile = Files.createTempFile(fasta.getName(), "fasta");
    FastaWriter.writeFasta(fastaFile, fasta.getSequences());

    final File dbDirectory = new File(databasesDirectory, fasta.getName());
    dbDirectory.mkdir();
    final File dbFile = new File(dbDirectory, fasta.getName());

    if (!BlastUtils.existDatabase(dbFile)) {
      makeblastdb(fastaFile.toFile(), dbFile);
    }
    fastaFile.toFile().delete();

    return dbFile;
  }

  private SequencesGroupDataset twoWayBlast(
    SequencesGroup queryFasta, SequencesGroupDataset dataset, Map<SequencesGroup, File> blastDatabases
  ) throws IOException, InterruptedException {
    File twoWayBlastTemporaryDir = Files.createTempDirectory("seda-two-way-blast").toFile();

    List<SequencesGroup> sequencesGroups = new LinkedList<>();
    RemoveRedundantSequencesTransformation removeRedundantSequences =
      new RemoveRedundantSequencesTransformation(
        new RemoveRedundantSequencesTransformationConfiguration(Mode.EXACT_DUPLICATES, false)
      );

    for (int i = 0; i < queryFasta.getSequenceCount(); i++) {
      Sequence querySequence = queryFasta.getSequence(i);
      List<Sequence> sequenceOrtologs = new LinkedList<>();
      sequenceOrtologs.add(querySequence);

      for (SequencesGroup targetFasta : dataset.getSequencesGroups().collect(Collectors.toList())) {
        if (!targetFasta.getName().equals(queryFasta.getName())) {
          sequenceOrtologs.addAll(
            twoWayBlast(querySequence, blastDatabases.get(queryFasta), targetFasta, blastDatabases.get(targetFasta), twoWayBlastTemporaryDir)
          );
        }
      }

      sequencesGroups.add(
        removeRedundantSequences.transform(
          this.factory.newSequencesGroup(getQuerySequenceName(querySequence), queryFasta.getProperties(), sequenceOrtologs)
        )
      );
    }
    return this.factory.newSequencesGroupDataset(sequencesGroups.toArray(new SequencesGroup[sequencesGroups.size()]));
  }

  private List<Sequence> twoWayBlast(
    Sequence querySequence, File queryDatabase, SequencesGroup targetFasta, File targetDatabase,
    File twoWayBlastTemporaryDir
  ) throws IOException, InterruptedException {
    List<Sequence> toret = new LinkedList<>();

    String querySequenceName = getQuerySequenceName(querySequence);
    Path temporaryDir = Files.createTempDirectory(twoWayBlastTemporaryDir.toPath(), querySequenceName + "_");
    File querySequenceFile = Files.createTempFile(temporaryDir, querySequenceName, ".fasta").toFile();
    FastaWriter.writeFasta(querySequenceFile.toPath(), querySequence);

    File blastAgainsTargetOutput =
      executeBlast(
        blastType, temporaryDir.toFile(), targetDatabase, querySequenceFile, this.evalue, 1, querySequenceName
      );

    Optional<String> firstSubject = extractFirstSubject(blastAgainsTargetOutput);

    if (firstSubject.isPresent()) {
      String firstSubjectName = firstSubject.get();
      File firstSubjectSequenceFile = Files.createTempFile(temporaryDir, firstSubjectName, ".fasta").toFile();
      this.defaultBlastBinariesExecutor.blastDbCmd(targetDatabase, firstSubjectName, firstSubjectSequenceFile);

      File blatAgainstReferenceOutput =
        executeBlast(
          blastType, temporaryDir.toFile(), queryDatabase, firstSubjectSequenceFile, this.evalue, 1, firstSubjectName
        );

      Optional<String> firstQueryResult = extractFirstSubject(blatAgainstReferenceOutput);
      if (firstQueryResult.isPresent()) {
        String firstQueryResultName = firstQueryResult.get();
        File firstQueryResultSequenceFile = Files.createTempFile(temporaryDir, firstQueryResultName, ".fasta").toFile();
        this.defaultBlastBinariesExecutor.blastDbCmd(queryDatabase, firstQueryResultName, firstQueryResultSequenceFile);
        if (firstQueryResultName.equals(querySequence.getName())) {
          toret.add(getSequence(firstSubjectSequenceFile));
        } else {
          if (mode.equals(TwoWayBlastMode.NON_EXACT)) {
            toret.add(getSequence(firstSubjectSequenceFile));
            toret.add(getSequence(firstQueryResultSequenceFile));
          }
        }
      }
    }

    return toret;
  }

  private Sequence getSequence(File firstSubjectSequenceFile) {
    return this.factory.newSequencesGroup(firstSubjectSequenceFile.toPath()).getSequences().findFirst().get();
  }

  private void makeblastdb(File inFile, File dbFile) throws IOException, InterruptedException {
    this.defaultBlastBinariesExecutor.makeBlastDb(inFile, getBlastSequenceType(), dbFile, true);
  }

  private String getQuerySequenceName(Sequence querySequence) {
    String name = querySequence.getName();
    for (String character : OsUtils.getInvalidOsFileCharacters()) {
      name.replace(character, "_");
    }
    return name;
  }

  private File executeBlast(
    BlastType blastType,
    File outDirectory,
    File database,
    File queryFile,
    double expectedValue,
    int maxTargetSeqs,
    String outputName
  ) throws InterruptedException, IOException {
    final File outFile = new File(outDirectory, outputName + ".out");

    if (!outDirectory.isDirectory() && !outDirectory.mkdirs()) {
      throw new IOException("Output directory could not be created: " + outDirectory);
    }

    List<String> additionalBlastParameters = getAdditionalBlastParameters();
    additionalBlastParameters.add("-max_hsps");
    additionalBlastParameters.add("1");

    this.defaultBlastBinariesExecutor.executeBlast(
      blastType, queryFile, database, expectedValue, maxTargetSeqs, outFile, "6", additionalBlastParameters
    );

    return outFile;
  }

  private List<String> getAdditionalBlastParameters() {
    if (this.blastAdditionalParameters != null && !blastAdditionalParameters.isEmpty()) {
      return new LinkedList<>(Arrays.asList(blastAdditionalParameters.split(" ")));
    } else {
      return new LinkedList<>();
    }
  }

  private String getBlastSequenceType() {
    return this.databaseType.getBlastName();
  }

  private boolean isValidConfiguration() {
    try {
      this.defaultBlastBinariesExecutor.checkBinary();
    } catch (BinaryCheckException e) {
      return false;
    }

    if (getQueryFile() == null) {
      return false;
    }

    if (getDatabasesDirectory() == null) {
      return false;
    }

    if (
      getAdditionalBlastParameters().contains("-evalue") || getAdditionalBlastParameters().contains("-max_target_seqs")
        || getAdditionalBlastParameters().contains("-max_hsps")
    ) {
      return false;
    }

    return true;
  }

  private File getDatabasesDirectory() {
    return this.databasesDirectory;
  }

  private File getQueryFile() {
    return this.queryFile;
  }
}
