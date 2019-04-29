/*
 * #%L
 * SEquence DAtaset builder Splign/Compart plugin
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
package org.sing_group.seda.splign.transformation.dataset;

import static java.nio.file.Files.createTempFile;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.io.FastaWriter.writeFasta;
import static org.sing_group.seda.splign.execution.SplignCompartPipeline.SEDA_SPLIGNCOMPART_KEEPTEMPORARYFILES;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.splign.execution.SplignCompartBinariesExecutor;
import org.sing_group.seda.splign.execution.SplignCompartPipeline;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class SplignCompartPipelineSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {

  private boolean concatenateExons;
  private File genomeFasta;
  private final DatatypeFactory factory;
  private BlastBinariesExecutor blastBinariesExecutor;
  private BedToolsBinariesExecutor bedToolsBinariesExecutor;
  private SplignCompartBinariesExecutor splignCompartBinariesExecutor;

  public SplignCompartPipelineSequencesGroupDatasetTransformation(
    SplignCompartBinariesExecutor proSplignCompartBinariesExecutor, 
    BedToolsBinariesExecutor bedToolsBinariesExecutor,
    BlastBinariesExecutor blastBinariesExecutor,
    File nucleotideFasta, boolean concatenateExons
  ) {
    this(
      getDefaultDatatypeFactory(), proSplignCompartBinariesExecutor, bedToolsBinariesExecutor, blastBinariesExecutor, nucleotideFasta,
      concatenateExons
    );
  }

  public SplignCompartPipelineSequencesGroupDatasetTransformation(
    DatatypeFactory factory, SplignCompartBinariesExecutor proSplignCompartBinariesExecutor,
    BedToolsBinariesExecutor bedToolsBinariesExecutor,
    BlastBinariesExecutor blastBinariesExecutor,
    File genomeFasta, boolean concatenateExons
  ) {
    this.splignCompartBinariesExecutor = proSplignCompartBinariesExecutor;
    this.blastBinariesExecutor = blastBinariesExecutor;
    this.bedToolsBinariesExecutor = bedToolsBinariesExecutor;
    this.factory = factory;
    this.genomeFasta = genomeFasta;
    this.concatenateExons = concatenateExons;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset sequencesGroupDataset) throws TransformationException {
    try {
      return runPipeline(sequencesGroupDataset, factory);
    } catch (IOException | InterruptedException | ExecutionException e) {
      throw new TransformationException("Error while running Splign/Compart. " + e.getMessage());
    }
  }

  private SequencesGroupDataset runPipeline(SequencesGroupDataset sequencesGroupDataset, DatatypeFactory factory)
    throws IOException, InterruptedException, ExecutionException {
    SplignCompartPipeline pipeline =
      new SplignCompartPipeline(
        this.bedToolsBinariesExecutor, this.splignCompartBinariesExecutor, this.blastBinariesExecutor, this.genomeFasta,
        factory
      );

    List<SequencesGroup> results = new LinkedList<>();
    try {
      for (SequencesGroup sequencesGroup : sequencesGroupDataset.getSequencesGroups().collect(toList())) {
        final Path queryFasta = createTempFile(sequencesGroup.getName(), "fasta");
        writeFasta(queryFasta, sequencesGroup.getSequences());

        final Path outputFasta = createTempFile(sequencesGroup.getName() + "_splign_compart", ".fasta");

        pipeline.splignCompart(queryFasta.toFile(), outputFasta.toFile(), this.concatenateExons);
        List<Sequence> alignedSequences = factory.newSequencesGroup(outputFasta).getSequences().collect(toList());

        results
          .add(factory.newSequencesGroup(sequencesGroup.getName(), sequencesGroup.getProperties(), alignedSequences));
      }
    } catch (InterruptedException e) {
      throw e;
    } catch (ExecutionException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    } finally {
      if (!Boolean.valueOf(System.getProperty(SEDA_SPLIGNCOMPART_KEEPTEMPORARYFILES, "false"))) {
        pipeline.clearTemporaryFiles();
      }
    }

    return factory.newSequencesGroupDataset(results.toArray(new SequencesGroup[results.size()]));
  }
}
