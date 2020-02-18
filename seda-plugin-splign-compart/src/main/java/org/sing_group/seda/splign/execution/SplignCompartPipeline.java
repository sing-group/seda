/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.execution;

import static org.sing_group.seda.bio.SequenceUtils.reverseComplement;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer.Position;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.pattern.SequencePattern;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.MergeSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation;
import org.sing_group.seda.util.FileUtils;

public class SplignCompartPipeline {
  public static final String SEDA_SPLIGNCOMPART_KEEPTEMPORARYFILES = "seda.spligncompart.keeptemporaryfiles";

  private static final String TO_BE_ERASED_SEQUENCE_NAME = "To_be_erased";

  private DatatypeFactory factory;
  private BlastBinariesExecutor blastBinaries;
  private BedToolsBinariesExecutor bedToolsBinaries;
  private SplignCompartBinariesExecutor splignCompartBinaries;

  public SplignCompartPipeline() {}

  public SplignCompartPipeline(
    BedToolsBinariesExecutor btBinaries,
    SplignCompartBinariesExecutor sBinaries,
    BlastBinariesExecutor bBinaries
  ) throws IOException {
    this(btBinaries, sBinaries, bBinaries, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SplignCompartPipeline(
    BedToolsBinariesExecutor btBinaries,
    SplignCompartBinariesExecutor sBinaries,
    BlastBinariesExecutor bBinaries,
    DatatypeFactory factory
  ) throws IOException {
    this.bedToolsBinaries = btBinaries;
    this.splignCompartBinaries = sBinaries;
    this.blastBinaries = bBinaries;
    this.factory = factory;
  }

  public void splignCompart(
    File targetFileFasta,
    File cdsQueryFileFasta,
    File outputFasta,
    boolean concatenateExons
  ) throws InterruptedException, ExecutionException, IOException {
    try (
      final OperationDirectoryManager operationDirectoryManager =
        new OperationDirectoryManager(
          cdsQueryFileFasta, outputFasta
        )
    ) {
      // Reversing and appending genome
      reverseAndMerge(
        targetFileFasta,
        operationDirectoryManager.getReversedGenomeFile(),
        operationDirectoryManager.getRenamedReversedGenomeFile(),
        operationDirectoryManager.getBidirectionalGenomeFile()
      );

      this.checkReverseAndMergeOutput(operationDirectoryManager.getBidirectionalGenomeFile());
      
      Files.copy(operationDirectoryManager.getBidirectionalGenomeFile().toPath(), operationDirectoryManager.getBidirectionalGenomeFile().toPath());

      // Genome and genes database creation
      makeBlastDB(
        operationDirectoryManager.getBidirectionalGenomeFile(),
        new File(
          operationDirectoryManager.getWorkingDirectory(),
          operationDirectoryManager.getBidirectionalGenomeFile().getName()
        )
      );

      makeBlastDB(
        operationDirectoryManager.getCdsQueryFastaFile(),
        new File(
          operationDirectoryManager.getWorkingDirectory(), operationDirectoryManager.getCdsQueryFastaFile().getName()
        )
      );

      // SPLIGN-COMPART
      this.mklds(operationDirectoryManager.getWorkingDirectoryPath());

      this.compart(
        operationDirectoryManager.getCdsQueryFastaFile(),
        operationDirectoryManager.getBidirectionalGenomeFile(),
        operationDirectoryManager.getCompartmentsFile()
      );

      this.ldsdir(
        operationDirectoryManager.getWorkingDirectoryPath().toFile(),
        operationDirectoryManager.getCompartmentsFile(),
        operationDirectoryManager.getLdsdirFile()
      );

      ldsdirToBed(operationDirectoryManager.getLdsdirFile(), operationDirectoryManager.getBedtoolsFile());

      this.bedtools(
        operationDirectoryManager.getBidirectionalGenomeFile(),
        operationDirectoryManager.getBedtoolsFile(),
        operationDirectoryManager.getBedtoolsOutputFile()
      );

      this.mergeSequences(
        operationDirectoryManager.getBedtoolsOutputFile(),
        operationDirectoryManager.getBedtoolsMergedOutputFile(),
        concatenateExons
      );

      Files.move(operationDirectoryManager.getBedtoolsMergedOutputFile().toPath(), outputFasta.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
  }

  protected void reverseAndMerge(
    File fasta,
    File reversedFastaFile,
    File renamedReversedFastaFile,
    File bidirectionalFastaFile
  ) {
    SequencesGroup fastaSequencesGroup = factory.newSequencesGroup(fasta.toPath());

    List<Sequence> reversedFastaSequences = reverseComplement(fastaSequencesGroup.getSequences());
    SequencesGroup reversedFastaSequencesGroup =
      factory
        .newSequencesGroup(fastaSequencesGroup.getName(), fastaSequencesGroup.getProperties(), reversedFastaSequences);

    writeFasta(reversedFastaFile.toPath(), reversedFastaSequences.stream());

    AddStringHeaderRenamer renamer =
      new AddStringHeaderRenamer(factory, HeaderTarget.ALL, "Reversed", "_", Position.PREFIX);

    SequencesGroup renamedReversedFastaSequencesGroup = renamer.rename(reversedFastaSequencesGroup);
    writeFasta(renamedReversedFastaFile.toPath(), renamedReversedFastaSequencesGroup.getSequences());

    SequencesGroupDataset dataset =
      factory.newSequencesGroupDataset(fastaSequencesGroup, renamedReversedFastaSequencesGroup);
    SequencesGroupDataset bidirectionalDataset =
      new MergeSequencesGroupDatasetTransformation(factory, "Bidirectional").transform(dataset);
    writeFasta(
      bidirectionalFastaFile.toPath(), bidirectionalDataset.getSequencesGroups().findFirst().get().getSequences()
    );
  }

  protected void checkReverseAndMergeOutput(File fastaFile)
    throws IOException {
    if (!fastaFile.exists()) {
      throw new IOException("Bidirectional genome file does not exists");
    } else if (fastaFile.length() == 0) {
      throw new IOException("Bidirectional genome file is empty");
    }
  }

  protected void makeBlastDB(
    final File fastaFile,
    final File dbFile
  ) throws InterruptedException, IOException {
    this.blastBinaries.makeBlastDb(fastaFile, SequenceType.NUCLEOTIDES.getBlastName(), dbFile, true);
  }

  protected void mklds(
    Path directoryPath
  ) throws IOException, InterruptedException {
    this.splignCompartBinaries.mklds(directoryPath.toFile());
  }

  protected void compart(
    File genesFastaPath,
    File genomeFastaPath,
    File compartmentsFile
  ) throws ExecutionException, InterruptedException, IOException {
    this.splignCompartBinaries.compart(genesFastaPath, genomeFastaPath, compartmentsFile);
  }

  protected void ldsdir(
    File workingDirectoryPath,
    File compartmentsPath,
    File ldsdirFile
  ) throws InterruptedException, IOException {
    this.splignCompartBinaries.ldsdir(
      workingDirectoryPath,
      compartmentsPath,
      ldsdirFile
    );
  }

  protected void ldsdirToBed(File input, File output) throws IOException {
    try (final BufferedReader reader = new BufferedReader(new FileReader(input));
      final PrintWriter pw = new PrintWriter(output)
    ) {
      String line;

      while ((line = reader.readLine()) != null && !line.equals("# END")) {
        final String[] split = line.split("\t");

        if (split.length == 11) {
          if (split[7].equals("-")) {
            final String info = split[2];
            pw.append(info).append("\t2\t3\t")
              .println(TO_BE_ERASED_SEQUENCE_NAME);
          } else {
            final Integer start = safeParseInt(split[7]) - 1;
            final Integer end = safeParseInt(split[8]);

            if (start < end) {
              final String name = split[1];
              final String info = split[2];
              pw.append(info).append('\t')
                .append(Integer.toString(start)).append('\t')
                .append(Integer.toString(end)).append('\t')
                .append(name).append('\n');
            }
          }
        }
      }
    }
  }

  protected void bedtools(
    File genomeFasta,
    File bedtoolsFasta,
    File bedtoolsOutputFile
  ) throws IOException, InterruptedException {
    this.bedToolsBinaries.getFasta(
      genomeFasta,
      bedtoolsFasta,
      bedtoolsOutputFile,
      "-name"
    );
  }

  protected void mergeSequences(
    File inputFasta,
    File outputFasta,
    boolean concatenateExons
  ) throws ExecutionException, IOException {
    SequencesGroup inputFastaSequencesGroup = this.factory.newSequencesGroup(inputFasta.toPath());

    if (concatenateExons) {
      ConcatenateSequencesGroupDatasetTransformation concatenate =
        new ConcatenateSequencesGroupDatasetTransformation("Concatenated", new SequenceNameHeaderMatcher());

      inputFastaSequencesGroup =
        concatenate.transform(this.factory.newSequencesGroupDataset(inputFastaSequencesGroup)).getSequencesGroups().findFirst().get();
    }

    PatternFilteringSequencesGroupTransformation filtering =
      new PatternFilteringSequencesGroupTransformation(
        new SequencePattern(TO_BE_ERASED_SEQUENCE_NAME, 1, false, false), SequenceTarget.HEADER
      );

    SequencesGroup filteredFastaSequencesGroup = filtering.transform(inputFastaSequencesGroup);

    AddStringHeaderRenamer renamer = new AddStringHeaderRenamer(factory, HeaderTarget.ALL, "", "-", Position.PREFIX, true, "");

    SequencesGroup outputFastaSequencesGroup = renamer.rename(filteredFastaSequencesGroup);

    writeFasta(outputFasta.toPath(), outputFastaSequencesGroup.getSequences());
  }

  private static Integer safeParseInt(String value) {
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  protected static class OperationDirectoryManager implements AutoCloseable {
    private final Path workingDirectory;
    private final Path cdsQueryFastaFile;

    private final Path bidirectionalGenomeFile;
    private final Path reversedGenomeFile;
    private final Path renamedReversedGenomeFile;
    private final Path compartmentsFile;
    private final Path ldsdirFile;
    private final Path bedtoolsFile;
    private final Path bedtoolsOutputFile;
    private final Path bedtoolsMergedOutputFile;

    public OperationDirectoryManager(
      File cdsQueryFastaFile,
      File outputFasta
    ) throws IOException {
      this.workingDirectory = Files.createTempDirectory("seda-spligncompart-");

      this.cdsQueryFastaFile = new File(this.workingDirectory.toFile(), cdsQueryFastaFile.getName()).toPath();
      Files.copy(cdsQueryFastaFile.toPath(), this.cdsQueryFastaFile);

      this.bidirectionalGenomeFile = this.workingDirectory.resolve("genome_bidirectional");
      this.reversedGenomeFile = this.workingDirectory.resolve("genome_reversed");
      this.renamedReversedGenomeFile = this.workingDirectory.resolve("genome_reversed_renamed");
      this.compartmentsFile = this.workingDirectory.resolve("compartments");
      this.ldsdirFile = this.workingDirectory.resolve("ldsdir");
      this.bedtoolsFile = this.workingDirectory.resolve("bedtools");
      this.bedtoolsOutputFile = this.workingDirectory.resolve("bedtools_output");
      this.bedtoolsMergedOutputFile = this.workingDirectory.resolve("bedtools_merged_output");
    }
    
    public File getBidirectionalGenomeFile() {
      return this.bidirectionalGenomeFile.toFile();
    }

    public File getReversedGenomeFile() {
      return this.reversedGenomeFile.toFile();
    }

    public File getRenamedReversedGenomeFile() {
      return this.renamedReversedGenomeFile.toFile();
    }
    
    public File getWorkingDirectory() {
      return this.workingDirectory.toFile();
    }

    public File getBedtoolsMergedOutputFile() {
      return this.bedtoolsMergedOutputFile.toFile();
    }

    public File getBedtoolsOutputFile() {
      return this.bedtoolsOutputFile.toFile();
    }

    public File getBedtoolsFile() {
      return this.bedtoolsFile.toFile();
    }

    public String getBedtoolsOutputPath() {
      return this.bedtoolsOutputFile.toAbsolutePath().toString();
    }

    public String getBedtoolsPath() {
      return this.bedtoolsFile.toAbsolutePath().toString();
    }

    public File getLdsdirFile() {
      return this.ldsdirFile.toFile();
    }

    public File getCompartmentsFile() {
      return this.compartmentsFile.toFile();
    }

    public String getCompartmentsPath() {
      return this.compartmentsFile.toAbsolutePath().toString();
    }

    public File getCdsQueryFastaFile() {
      return this.cdsQueryFastaFile.toFile();
    }

    public Path getWorkingDirectoryPath() {
      return this.workingDirectory;
    }

    public File getCdsQueryDatabaseDirectory() {
      return this.getCdsQueryFastaFile();
    }

    @Override
    public void close() throws IOException {
      if (!Boolean.valueOf(System.getProperty(SEDA_SPLIGNCOMPART_KEEPTEMPORARYFILES, "false"))) {
        FileUtils.deleteIfExists(this.workingDirectory);
      }
    }
  }
}
