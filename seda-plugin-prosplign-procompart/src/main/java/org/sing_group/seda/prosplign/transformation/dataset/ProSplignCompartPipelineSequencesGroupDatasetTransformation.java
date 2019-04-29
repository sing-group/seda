/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
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
package org.sing_group.seda.prosplign.transformation.dataset;

import static java.nio.file.Files.createTempFile;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.io.FastaWriter.writeFasta;
import static org.sing_group.seda.prosplign.execution.ProSplignCompartPipeline.SEDA_PROSPLIGNCOMPART_KEEPTEMPORARYFILES;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.sing_group.seda.blast.execution.BlastBinariesExecutor;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.prosplign.execution.ProSplignCompartBinariesExecutor;
import org.sing_group.seda.prosplign.execution.ProSplignCompartPipeline;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ProSplignCompartPipelineSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {

  private int maxTargetSeqs;
  private File nucleotideFasta;
  private final DatatypeFactory factory;
  private BlastBinariesExecutor blastBinariesExecutor;
  private ProSplignCompartBinariesExecutor proSplignCompartBinariesExecutor;

  public ProSplignCompartPipelineSequencesGroupDatasetTransformation(
    ProSplignCompartBinariesExecutor proSplignCompartBinariesExecutor, BlastBinariesExecutor blastBinariesExecutor,
    File nucleotideFasta, int maxTargetSeqs
  ) {
    this(
      getDefaultDatatypeFactory(), proSplignCompartBinariesExecutor, blastBinariesExecutor, nucleotideFasta,
      maxTargetSeqs
    );
  }

  public ProSplignCompartPipelineSequencesGroupDatasetTransformation(
    DatatypeFactory factory, ProSplignCompartBinariesExecutor proSplignCompartBinariesExecutor,
    BlastBinariesExecutor blastBinariesExecutor,
    File nucleotideFasta, int maxTargetSeqs
  ) {
    this.proSplignCompartBinariesExecutor = proSplignCompartBinariesExecutor;
    this.blastBinariesExecutor = blastBinariesExecutor;
    this.factory = factory;
    this.nucleotideFasta = nucleotideFasta;
    this.maxTargetSeqs = maxTargetSeqs;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset sequencesGroupDataset) throws TransformationException {
    try {
      return runPipeline(sequencesGroupDataset, factory);
    } catch (IOException | InterruptedException | ExecutionException e) {
      throw new TransformationException("Error while running ProSplign/ProCompart. " + e.getMessage());
    }
  }

  private SequencesGroupDataset runPipeline(SequencesGroupDataset sequencesGroupDataset, DatatypeFactory factory)
    throws IOException, InterruptedException, ExecutionException {
    ProSplignCompartPipeline pipeline =
      new ProSplignCompartPipeline(
        this.proSplignCompartBinariesExecutor, this.blastBinariesExecutor, this.nucleotideFasta
      );

    List<SequencesGroup> results = new LinkedList<>();
    try {
      for (SequencesGroup sequencesGroup : sequencesGroupDataset.getSequencesGroups().collect(toList())) {
        final Path queryFasta = createTempFile(sequencesGroup.getName(), "fasta");
        writeFasta(queryFasta, sequencesGroup.getSequences());

        final Path outputFasta = createTempFile(sequencesGroup.getName() + "_prosplign_procompart", ".fasta");

        pipeline.proSplignCompart(queryFasta.toFile(), outputFasta.toFile(), this.maxTargetSeqs);
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
      if (!Boolean.valueOf(System.getProperty(SEDA_PROSPLIGNCOMPART_KEEPTEMPORARYFILES, "false"))) {
        pipeline.clearTemporaryFiles();
      }
    }

    return factory.newSequencesGroupDataset(results.toArray(new SequencesGroup[results.size()]));
  }
}
