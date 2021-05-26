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

import static java.lang.Integer.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer.Position;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.WordReplaceRenamer;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.ChangePropertiesSequenceTransformation;
import org.sing_group.seda.transformation.sequence.ReverseComplementSequenceTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;
import org.sing_group.seda.util.FileUtils;

public class SplignCompartPipeline {
  public static final String SEDA_SPLIGNCOMPART_KEEPTEMPORARYFILES = "seda.spligncompart.keeptemporaryfiles";

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
          targetFileFasta, cdsQueryFileFasta, outputFasta
        )
    ) {
      makeBlastDB(
        operationDirectoryManager.getTargetFastaFile(),
        new File(
          operationDirectoryManager.getWorkingDirectory(),
          operationDirectoryManager.getTargetFastaFile().getName()
        )
      );

      makeBlastDB(
        operationDirectoryManager.getCdsQueryFastaFile(),
        new File(
          operationDirectoryManager.getWorkingDirectory(), operationDirectoryManager.getCdsQueryFastaFile().getName()
        )
      );

      this.mklds(operationDirectoryManager.getWorkingDirectoryPath());

      this.compart(
        operationDirectoryManager.getCdsQueryFastaFile(),
        operationDirectoryManager.getTargetFastaFile(),
        operationDirectoryManager.getCompartmentsFile()
      );

      this.ldsdir(
        operationDirectoryManager.getWorkingDirectoryPath().toFile(),
        operationDirectoryManager.getCompartmentsFile(),
        operationDirectoryManager.getLdsdirFile()
      );

      this.ldsdirToBed(
        operationDirectoryManager.getLdsdirFile(),
        operationDirectoryManager.getBedtoolsCase1File(),
        operationDirectoryManager.getBedtoolsCase2File(),
        operationDirectoryManager.getBedtoolsCase3File(),
        operationDirectoryManager.getBedtoolsCase4File()
      );

      this.extractFastaFiles(operationDirectoryManager);

      this.mergeSequences(
        operationDirectoryManager.getBedtoolsOutputCase1File(),
        operationDirectoryManager.getBedtoolsOutputCase2File(),
        operationDirectoryManager.getBedtoolsOutputCase3File(),
        operationDirectoryManager.getBedtoolsOutputCase4File(),
        operationDirectoryManager.getBedtoolsMergedOutputFile(),
        concatenateExons
      );

      Files.move(operationDirectoryManager.getBedtoolsMergedOutputFile().toPath(), outputFasta.toPath(), StandardCopyOption.REPLACE_EXISTING);
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

  protected void ldsdirToBed(File input, File bedCase1, File bedCase2, File bedCase3, File bedCase4)
    throws IOException {
    try (
      final BufferedReader reader = new BufferedReader(new FileReader(input));
      final PrintWriter case1 = new PrintWriter(bedCase1);
      final PrintWriter case2 = new PrintWriter(bedCase2);
      final PrintWriter case3 = new PrintWriter(bedCase3);
      final PrintWriter case4 = new PrintWriter(bedCase4);
    ) {
      String line;

      while ((line = reader.readLine()) != null && !line.equals("# END")) {
        final String[] split = line.split("\t");

        if (split.length == 11) {
          if (!split[6].equals("-")) {
            final Integer queryStart = safeParseInt(split[5]);
            final Integer queryStop = safeParseInt(split[6]);
            final Integer subjectStart = safeParseInt(split[7]);
            final Integer subjectStop = safeParseInt(split[8]);
            final String name = split[1] + "_" + split[0];
            final String info = split[2];
            
            if (queryStart < queryStop && subjectStart < subjectStop) {
              // # Case 1: 6 < 7 e 8 < 9 => read from beginning to end.
              case1.append(info).append('\t')
                .append(Integer.toString(subjectStart-1)).append('\t')
                .append(Integer.toString(subjectStop)).append('\t')
                .append(name).append('\n');
            } else if (queryStart < queryStop && subjectStart > subjectStop) {
              // Case 2: 6 < 7 e 8 > 9: read from beginning to end and obtain the reverse-complement of the sequences.
              case2.append(info).append('\t')
                .append(Integer.toString(subjectStop - 1)).append('\t')
                .append(Integer.toString(subjectStart)).append('\t')
                .append(name).append('\n');
            } else if (queryStart > queryStop && subjectStart < subjectStop) {
              // # Case 3: 6 > 7 e 8 < 9 => read from end to beginning and obtain reverse-complement of the sequences.
              case3.append(info).append('\t')
                .append(Integer.toString(subjectStart - 1)).append('\t')
                .append(Integer.toString(subjectStop)).append('\t')
                .append(name).append('\n');
            } else if (queryStart > queryStop && subjectStart > subjectStop) {
              // Case 4: 6 > 7 e 8 > 9 => read from end to beginning.
              case4.append(info).append('\t')
                .append(Integer.toString(subjectStop - 1)).append('\t')
                .append(Integer.toString(subjectStart)).append('\t')
                .append(name).append('\n');
            }
          }
        }
      }
    }

    // Case 3 and case 4 files must be sorted in descending order by the second column (start) in order to make them to 
    // be read from end to beginning by bedtools.
    sortDescending(bedCase3);
    sortDescending(bedCase4);
  }

  private static Integer safeParseInt(String value) {
    try {
      return Integer.valueOf(value);
    } catch (NumberFormatException nfe) {
      return null;
    }
  }

  private void sortDescending(File bedfile) {
    if (bedfile.exists()) {
      try (Stream<String> fileStream = Files.lines(bedfile.toPath())) {
        final String[] sortedLines =
          fileStream
            .map(line -> line.split("\t"))
            .sorted((line1, line2) -> compareLines(line1, line2))
            .map(line -> String.join("\t", line))
            .toArray(String[]::new);

        Files.write(bedfile.toPath(), Arrays.asList(sortedLines));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private int compareLines(String[] line1, String[] line2) {
    return -1 * valueOf(line1[1]).compareTo(valueOf(line2[1]));
  }

  private void extractFastaFiles(OperationDirectoryManager operationDirectoryManager) throws IOException,
    InterruptedException {
    this.bedtools(
      operationDirectoryManager.getTargetFastaFile(),
      operationDirectoryManager.getBedtoolsCase1File(),
      operationDirectoryManager.getBedtoolsOutputCase1File()
    );
    this.bedtools(
      operationDirectoryManager.getTargetFastaFile(),
      operationDirectoryManager.getBedtoolsCase2File(),
      operationDirectoryManager.getBedtoolsOutputCase2File()
    );
    this.bedtools(
      operationDirectoryManager.getTargetFastaFile(),
      operationDirectoryManager.getBedtoolsCase3File(),
      operationDirectoryManager.getBedtoolsOutputCase3File()
    );
    this.bedtools(
      operationDirectoryManager.getTargetFastaFile(),
      operationDirectoryManager.getBedtoolsCase4File(),
      operationDirectoryManager.getBedtoolsOutputCase4File()
    );
  }

  protected void bedtools(
    File genomeFasta,
    File bedtoolsFile,
    File bedtoolsOutputFastaFile
  ) throws IOException, InterruptedException {
    if (bedtoolsFile.exists()) {
      this.bedToolsBinaries.getFasta(
        genomeFasta,
        bedtoolsFile,
        bedtoolsOutputFastaFile,
        "-name"
        );
    }
  }

  private static SequencesGroup loadFasta(File fasta, DatatypeFactory factory) {
    if (fasta.exists()) {
      return factory.newSequencesGroup(fasta.toPath());
    } else {
      return factory.newSequencesGroup(fasta.getName(), Collections.emptyMap());
    }
  }

  protected void mergeSequences(
    File case1Fasta,
    File case2Fasta,
    File case3Fasta,
    File case4Fasta,
    File outputFasta,
    boolean concatenateExons
  ) throws ExecutionException, IOException {
    SequencesGroup case1FastaSequencesGroup = loadFasta(case1Fasta, factory);
    SequencesGroup case2FastaSequencesGroup = loadFasta(case2Fasta, factory);
    SequencesGroup case3FastaSequencesGroup = loadFasta(case3Fasta, factory);
    SequencesGroup case4FastaSequencesGroup = loadFasta(case4Fasta, factory);

    // First, sequences in case 2 and case 3 files must be reverse-complemented
    SequencesGroupTransformation revComp =
      new ComposedSequencesGroupTransformation(new ReverseComplementSequenceTransformation(true, true));

    case2FastaSequencesGroup = revComp.transform(case2FastaSequencesGroup);
    case3FastaSequencesGroup = revComp.transform(case3FastaSequencesGroup);

    // Second, join all sequences into a single FASTA file (i.e. SequencesGroup)
    List<Sequence> allSequences = new ArrayList<>();
    allSequences.addAll(case1FastaSequencesGroup.getSequences().collect(toList()));
    allSequences.addAll(case2FastaSequencesGroup.getSequences().collect(toList()));
    allSequences.addAll(case3FastaSequencesGroup.getSequences().collect(toList()));
    allSequences.addAll(case4FastaSequencesGroup.getSequences().collect(toList()));

    SequencesGroup result =
      this.factory.newSequencesGroup("result", case1FastaSequencesGroup.getProperties(), allSequences);

    // Third, replace the "::" in the sequence coordinates generated by bedtools to have the sequence name
    // as sequence ID and the coordinates as sequence description.
    WordReplaceRenamer replaceRenamer = new WordReplaceRenamer(HeaderTarget.ALL, " ", false, asList("::"));
    result = replaceRenamer.rename(result, this.factory);

    if (concatenateExons) {
      ConcatenateSequencesGroupDatasetTransformation concatenate =
        new ConcatenateSequencesGroupDatasetTransformation("Concatenated", new SequenceNameHeaderMatcher(), true);

      result =
        concatenate.transform(this.factory.newSequencesGroupDataset(result)).getSequencesGroups().findFirst().get();

      SequencesGroupTransformation sortCoordinates =
        new ComposedSequencesGroupTransformation(new SortCoordinatesDescriptionsTransformation(factory));

      result = sortCoordinates.transform(result);
    } else {
      AddStringHeaderRenamer addPrefixRenamer =
        new AddStringHeaderRenamer(HeaderTarget.DESCRIPTION, "[", "", Position.PREFIX, false, "");

      AddStringHeaderRenamer addSuffixRenamer =
        new AddStringHeaderRenamer(HeaderTarget.DESCRIPTION, "]", "", Position.SUFFIX, false, "");

      result =
        addSuffixRenamer.rename(addPrefixRenamer.rename(result, factory), factory);
    }

    result = getRemoveLineBreaksTransformation(factory).transform(result);

    writeFasta(outputFasta.toPath(), result.getSequences());
  }

  /**
   * This transformation changes the header descriptions containing a list of coordinates to sort them in ascending
   * order by the start coordinates. An example of header descriptions is:
   * [Scf_3L:10847766-10848161;Scf_3L:10848862-10848971]
   *
   * Such headers are produced adjacent exons are concatenated.
   */
  private static class SortCoordinatesDescriptionsTransformation implements SequenceTransformation {

    private DatatypeFactory factory;

    public SortCoordinatesDescriptionsTransformation(DatatypeFactory factory) {
      this.factory = factory;
    }

    @Override
    public Sequence transform(Sequence sequence) throws TransformationException {
      if (sequence.getDescription().contains(";")) {
        String description = sequence.getDescription();
        List<String> split = Arrays.asList(description.substring(1, description.length() - 1).split(";"));
        split.sort(new Comparator<String>() {

          @Override
          public int compare(String o1, String o2) {
            Integer start1 = Integer.valueOf(o1.split(":|-")[1]);
            Integer start2 = Integer.valueOf(o2.split(":|-")[1]);
            return start1.compareTo(start2);
          }
        });

        description = split.stream().collect(Collectors.joining(";", "[", "]"));

        return factory.newSequence(sequence.getName(), description, sequence.getChain(), sequence.getProperties());
      } else {
        return sequence;
      }
    }
  }

  private static SequencesGroupTransformation getRemoveLineBreaksTransformation(DatatypeFactory factory) {
    SequenceTransformation transformation =
      new ChangePropertiesSequenceTransformation(factory, emptyMap());

    return new ComposedSequencesGroupTransformation(transformation);
  }

  protected static class OperationDirectoryManager implements AutoCloseable {
    private final Path targetFileFasta;
    private final Path workingDirectory;
    private final Path cdsQueryFastaFile;

    private final Path compartmentsFile;
    private final Path ldsdirFile;
    private final Path bedtoolsFileCase1;
    private final Path bedtoolsFileCase2;
    private final Path bedtoolsFileCase3;
    private final Path bedtoolsFileCase4;
    private final Path bedtoolsOutputFileCase1;
    private final Path bedtoolsOutputFileCase2;
    private final Path bedtoolsOutputFileCase3;
    private final Path bedtoolsOutputFileCase4;
    private final Path bedtoolsMergedOutputFile;

    public OperationDirectoryManager(
      File targetFileFasta,
      File cdsQueryFastaFile,
      File outputFasta
    ) throws IOException {
      this.workingDirectory = Files.createTempDirectory("seda-spligncompart-");

      this.targetFileFasta = new File(this.workingDirectory.toFile(), targetFileFasta.getName()).toPath();
      Files.copy(targetFileFasta.toPath(), this.targetFileFasta);

      this.cdsQueryFastaFile = new File(this.workingDirectory.toFile(), cdsQueryFastaFile.getName()).toPath();
      Files.copy(cdsQueryFastaFile.toPath(), this.cdsQueryFastaFile);

      this.compartmentsFile = this.workingDirectory.resolve("compartments");
      this.ldsdirFile = this.workingDirectory.resolve("ldsdir");
      this.bedtoolsFileCase1 = this.workingDirectory.resolve("bedtools_case1");
      this.bedtoolsFileCase2 = this.workingDirectory.resolve("bedtools_case2");
      this.bedtoolsFileCase3 = this.workingDirectory.resolve("bedtools_case3");
      this.bedtoolsFileCase4 = this.workingDirectory.resolve("bedtools_case4");
      this.bedtoolsOutputFileCase1 = this.workingDirectory.resolve("bedtools_output_case1");
      this.bedtoolsOutputFileCase2 = this.workingDirectory.resolve("bedtools_output_case2");
      this.bedtoolsOutputFileCase3 = this.workingDirectory.resolve("bedtools_output_case3");
      this.bedtoolsOutputFileCase4 = this.workingDirectory.resolve("bedtools_output_case4");
      this.bedtoolsMergedOutputFile = this.workingDirectory.resolve("bedtools_merged_output");
    }
    
    public File getWorkingDirectory() {
      return this.workingDirectory.toFile();
    }

    public File getBedtoolsMergedOutputFile() {
      return this.bedtoolsMergedOutputFile.toFile();
    }

    public File getBedtoolsOutputCase1File() {
      return this.bedtoolsOutputFileCase1.toFile();
    }
    
    public File getBedtoolsOutputCase2File() {
      return this.bedtoolsOutputFileCase2.toFile();
    }
    
    public File getBedtoolsOutputCase3File() {
      return this.bedtoolsOutputFileCase3.toFile();
    }

    public File getBedtoolsOutputCase4File() {
      return this.bedtoolsOutputFileCase4.toFile();
    }

    public File getBedtoolsCase1File() {
      return this.bedtoolsFileCase1.toFile();
    }

    public File getBedtoolsCase2File() {
      return this.bedtoolsFileCase2.toFile();
    }

    public File getBedtoolsCase3File() {
      return this.bedtoolsFileCase3.toFile();
    }

    public File getBedtoolsCase4File() {
      return this.bedtoolsFileCase4.toFile();
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

    public File getTargetFastaFile() {
      return this.targetFileFasta.toFile();
    }

    public File getCdsQueryFastaFile() {
      return this.cdsQueryFastaFile.toFile();
    }

    public Path getWorkingDirectoryPath() {
      return this.workingDirectory;
    }

    @Override
    public void close() throws IOException {
      if (!Boolean.valueOf(System.getProperty(SEDA_SPLIGNCOMPART_KEEPTEMPORARYFILES, "false"))) {
        FileUtils.deleteIfExists(this.workingDirectory);
      }
    }
  }
}
