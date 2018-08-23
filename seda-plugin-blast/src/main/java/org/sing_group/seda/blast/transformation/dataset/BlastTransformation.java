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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.sing_group.seda.blast.BinaryCheckException;
import org.sing_group.seda.blast.BlastBinariesExecutor;
import org.sing_group.seda.blast.BlastUtils;
import org.sing_group.seda.blast.datatype.DatabaseQueryMode;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.DefaultDatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.io.FastaWriter;
import org.sing_group.seda.io.LazyDatatypeFactory;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.util.OsUtils;

public class BlastTransformation implements SequencesGroupDatasetTransformation {
  public final static SequenceType DEFAULT_SEQUENCE_TYPE = SequenceType.NUCLEOTIDES;
  public final static BlastType DEFAULT_BLAST_TYPE = BlastType.BLASTN;
  public final static double DEFAULT_EVALUE = 0.05d;
  public final static int DEFAULT_MAX_TARGET_SEQS = 500000;
  public static final boolean DEFAULT_EXTRACT_ONLY_HIT_REGIONS = false;
  public static final int DEFAULT_HIT_REGIONS_WINDOW_SIZE = 0;

  private BlastBinariesExecutor blastBinariesExecutor;

  private final SequenceType databaseType;
  private final BlastType blastType;
  private final DatabaseQueryMode databaseQueryMode;
  private final File databasesDirectory;
  private final Optional<File> aliasFile;
  private final double evalue;
  private final int maxTargetSeqs;
  private DatatypeFactory factory;
  private File queryFile;
  private String blastParams;
  private boolean extractOnlyHitRegions;
  private int hitRegionsWindowSize;

  public BlastTransformation(
    BlastType blastType,
    DatabaseQueryMode databaseQueryMode,
    File blastPath,
    File queryFile,
    File databasesPath,
    double evalue,
    int maxTargetSeqs,
    boolean extractOnlyHitRegions,
    int hitRegionsWindowSize,
    String blastParams,
    DatatypeFactory factory
  ) {
    this(
      blastType,
      databaseQueryMode,
      blastPath,
      queryFile,
      databasesPath,
      null,
      evalue,
      maxTargetSeqs,
      extractOnlyHitRegions,
      hitRegionsWindowSize,
      blastParams,
      factory
     );
  }

  public BlastTransformation(
    BlastType blastType,
    DatabaseQueryMode databaseQueryMode,
    File blastPath,
    File queryFile,
    File databasesPath,
    File aliasFile,
    double evalue,
    int maxTargetSeqs,
    boolean extractOnlyHitRegions,
    int hitRegionsWindowSize,
    String blastParams,
    DatatypeFactory factory
  ) {
    this.databaseType = blastType.getDatabaseType();
    this.databaseQueryMode = databaseQueryMode;
    this.blastType = blastType;
    this.blastBinariesExecutor = new BlastBinariesExecutor(blastPath);
    this.databasesDirectory = databasesPath;
    this.aliasFile = Optional.ofNullable(aliasFile);
    this.queryFile = queryFile;
    this.evalue = evalue;
    this.maxTargetSeqs = maxTargetSeqs;
    this.extractOnlyHitRegions = extractOnlyHitRegions;
    this.hitRegionsWindowSize = hitRegionsWindowSize;
    this.blastParams = blastParams;
    this.factory = factory;

    if (!this.isValidConfiguration()) {
      throw new RuntimeException("Invalid configuration");
    }
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset)
    throws TransformationException {
    requireNonNull(dataset);

   return blast(dataset);
  }

  private SequencesGroupDataset blast(SequencesGroupDataset dataset) {
    if (this.databaseQueryMode.equals(DatabaseQueryMode.ALL)) {
      return factory.newSequencesGroupDataset(getSequenceGroups(blastDataset(dataset, "")));
    } else {
      List<File> sequenceResultFiles = new LinkedList<>();
      for (SequencesGroup group : dataset.getSequencesGroups().collect(Collectors.toList())) {
        SequencesGroupDataset currentDataset = this.factory.newSequencesGroupDataset(group);
        sequenceResultFiles.addAll(blastDataset(currentDataset, "_" + group.getName()));
      }

      return factory.newSequencesGroupDataset(getSequenceGroups(sequenceResultFiles));
    }
  }

  private List<File> blastDataset(SequencesGroupDataset dataset, String resultsSuffix) {
    List<File> blastDatabases = Collections.emptyList();
    try {
      blastDatabases = makeBlastDatabases(dataset, this.databasesDirectory);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("An error occurred while creating the databases");
    }

    File aliasFile;
    try {
      aliasFile = this.aliasFile.orElse(Files.createTempFile("seda-blastdb-alias", "").toFile());
    } catch (IOException e) {
      throw new TransformationException("An error occurred while creating the alias");
    }

    try {
      makeBlastDatabasesAlias(blastDatabases, aliasFile);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("An error occurred while creating the alias");
    }

    List<File> blastResults;
    try {
      blastResults = executeBlast(aliasFile);
    } catch (IOException | InterruptedException e1) {
      throw new TransformationException("An error occurred while running blast");
    }

    try {
      List<File> sequenceResultsFiles = extractSequences(blastResults, aliasFile, resultsSuffix);

      return sequenceResultsFiles;
    } catch (InterruptedException | IOException e) {
      throw new TransformationException("An error occurred while extracting result sequences");
    }
  }

  private SequencesGroup[] getSequenceGroups(List<File> sequenceResultsFiles) {
    List<SequencesGroup> groups = new LinkedList<>();
    for (File result : sequenceResultsFiles) {
      groups.add(this.factory.newSequencesGroup(result.toPath()));
    }

    return groups.toArray(new SequencesGroup[groups.size()]);
  }

  private List<File> extractSequences(List<File> blastResults, File aliasFile, String fileSuffix)
    throws InterruptedException, IOException {
    List<File> sequenceFiles = new LinkedList<>();
    for (File blastResult : blastResults) {
      if (this.extractOnlyHitRegions) {
        sequenceFiles.add(extractOnlyHitRegionsAsSequences(blastResult, aliasFile, fileSuffix));
      } else {
        sequenceFiles.add(extractBatchSequences(blastResult, aliasFile, fileSuffix));
      }
    }

    return sequenceFiles;
  }

  private File extractBatchSequences(File blastResult, File aliasFile, String fileSuffix)
    throws InterruptedException, IOException {
    File sequencesFile = new File(blastResult.getParentFile(), blastResult.getName().replace(".out", "") + fileSuffix + ".sequences");
    File blastSubjectList = extractSubjectList(blastResult);

    this.blastBinariesExecutor.blastDbCmd(aliasFile, blastSubjectList, sequencesFile);

    return sequencesFile;
  }

  private File extractSubjectList(File blastResult) throws IOException {
    File blastSubjectList = new File(blastResult.getParentFile(), blastResult.getName() + ".ids.txt");
    final Set<String> subjectIds = new HashSet<>();
    Files.readAllLines(blastResult.toPath()).forEach(
      l -> {
        String[] fields = l.split("\t");
        subjectIds.add(fields[1]);
      }
    );

    Files.write(blastSubjectList.toPath(), subjectIds);

    return blastSubjectList;
  }

  private File extractOnlyHitRegionsAsSequences(File blastResult, File aliasFile, String fileSuffix)
    throws InterruptedException, IOException {
    List<BlastFormat6Hit> blastHits = extractBlastHits(blastResult);

    List<Sequence> sequences = new LinkedList<>();
    File sequencesFile = new File(blastResult.getParentFile(), blastResult.getName().replace(".out", "") + fileSuffix + ".sequences");

    DefaultDatatypeFactory temporaryDatatypeFactory = new DefaultDatatypeFactory();

    for (BlastFormat6Hit hit : blastHits) {
      int rangeStart = Math.min(hit.getSubjectStart(), hit.getSubjectEnd());
      int rangeEnd = Math.max(hit.getSubjectStart(), hit.getSubjectEnd()) + this.hitRegionsWindowSize;

      if (rangeStart - this.hitRegionsWindowSize > 0) {
        rangeStart = rangeStart - this.hitRegionsWindowSize;
      }

      File temporaryHitFile =
        Files.createTempFile(blastResult.getParentFile().toPath(), "hit-" + hit.getSubjectSequenceId(), ".fasta")
          .toFile();

      this.blastBinariesExecutor.blastDbCmd(
        aliasFile, hit.getSubjectSequenceId(), new String(rangeStart + "-" + rangeEnd), temporaryHitFile
      );

      SequencesGroup subjectSequencesGroup = temporaryDatatypeFactory.newSequencesGroup(temporaryHitFile.toPath());
      if (subjectSequencesGroup.getSequenceCount() > 0) {
        sequences.add(subjectSequencesGroup.getSequence(0));
      }
    }

    FastaWriter.writeFasta(sequencesFile.toPath(), sequences.stream());

    return sequencesFile;
  }

  private static List<BlastFormat6Hit> extractBlastHits(File blastResult) throws IOException {
    final List<BlastFormat6Hit> hits = new LinkedList<>();
    Files.readAllLines(blastResult.toPath()).forEach(
      l -> {
        String[] fields = l.split("\t");
        hits.add(new BlastFormat6Hit(fields[1], fields[8], fields[9]));
      }
    );

    return hits;
  }

  private static class BlastFormat6Hit {
    private String subjectSequenceId;
    private String subjectStart;
    private String subjectEnd;

    BlastFormat6Hit(String subject, String subjectStart, String subjectEnd) {
      this.subjectSequenceId = subject;
      this.subjectStart = subjectStart;
      this.subjectEnd = subjectEnd;
    }

    public String getSubjectSequenceId() {
      return subjectSequenceId;
    }

    public int getSubjectEnd() {
      return Integer.valueOf(this.subjectEnd);
    }

    public int getSubjectStart() {
      return Integer.valueOf(this.subjectStart);
    }
  }

  private List<File> makeBlastDatabases(
    SequencesGroupDataset dataset,
    File databasesDirectory
  ) throws IOException, InterruptedException {
    List<File> blastDatabases = new LinkedList<>();
    for (SequencesGroup fasta : dataset.getSequencesGroups().collect(Collectors.toList())) {
      final Path fastaFile = Files.createTempFile(fasta.getName(), "fasta");
      FastaWriter.writeFasta(fastaFile, fasta.getSequences());

      final File dbFile = new File(databasesDirectory, fasta.getName() + "/" + fasta.getName());

      if (!BlastUtils.existDatabase(dbFile)) {
        makeblastdb(fastaFile.toFile(), dbFile);
      }
      blastDatabases.add(dbFile);

      fastaFile.toFile().delete();
    }

    return blastDatabases;
  }

  private void makeblastdb(File inFile, File dbFile) throws IOException, InterruptedException {
    this.blastBinariesExecutor.makeBlastDb(inFile, getBlastSequenceType(), dbFile);
  }

  private void makeBlastDatabasesAlias(List<File> blastDatabases, File outFile)
    throws IOException, InterruptedException {
    this.blastBinariesExecutor.makeDbAlias(blastDatabases, getBlastSequenceType(), outFile, "dbalias");
  }

  private List<File> executeBlast(File aliasFile) throws IOException, InterruptedException {
    SequencesGroup querySequences = new LazyDatatypeFactory().newSequencesGroup(this.queryFile.toPath());
    List<File> blastResults = new LinkedList<>();

    File blastDir = Files.createTempDirectory("seda-blast-results").toFile();

    for (int i = 0; i < querySequences.getSequenceCount(); i++) {
      Sequence querySequence = querySequences.getSequence(i);
      String querySequenceName = getQuerySequenceName(querySequence);

      File querySequenceFile = Files.createTempFile(blastDir.toPath(), querySequenceName, ".fasta").toFile();
      FastaWriter.writeFasta(querySequenceFile.toPath(), querySequence);

      File output =
        executeBlast(
          blastType, blastDir, aliasFile, querySequenceFile, this.evalue, this.maxTargetSeqs, querySequenceName
        );

      blastResults.add(output);
    }

    return blastResults;
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

    this.blastBinariesExecutor.executeBlast(
      blastType, queryFile, database, expectedValue, maxTargetSeqs, outFile, "6", getAdditionalBlastParameters()
    );

    return outFile;
  }

  private List<String> getAdditionalBlastParameters() {
    if (this.blastParams != null && !this.blastParams.isEmpty()) {
      return Arrays.asList(blastParams.split(" "));
    } else {
      return Collections.emptyList();
    }
  }

  private String getBlastSequenceType() {
    return this.databaseType.getBlastName();
  }

  private boolean isValidConfiguration() {
    try {
      this.blastBinariesExecutor.checkBlastPath();
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
      getAdditionalBlastParameters().contains("-evalue") || getAdditionalBlastParameters().contains("max_target_seqs")
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
