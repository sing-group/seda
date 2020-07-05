/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.transformation.dataset;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.blast.uniprot.EbiFastaDownloader.downloadFasta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.uniprot.UniProtBlastHit;
import org.sing_group.seda.blast.uniprot.UniProtBlastHits;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.BlastInput;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.BlastResult;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.BlastSummary;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.UniParcBlastService;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.UniParcHit;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.UniProtBlastService;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.UniProtHit;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.UniRefBlastService;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.UniRefHit;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.AlignmentCutoffOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.FilterOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.MatrixOption;

public class UniProtBlastTransformation implements SequencesGroupDatasetTransformation {

  private static final String CONCURRENT_BLAST_MESSAGE =
    "It seems that an UniProt BLAST operation is already running. "
      + "SEDA only allows to execute one UniProt BLAST operation at a time in order respect EMBL-EBI policies regarding the "
      + "usage of resources and avoid problems.";

  private File lockFile;
  private DatatypeFactory factory;
  private DatabaseOption database;
  private OutputTypeParameter outputType;
  private ExpectationOption expectValue;
  private MatrixOption matrix;
  private FilterOption filterOption;
  private boolean gapped;
  private AlignmentCutoffOption hits;

  public UniProtBlastTransformation(
    DatabaseOption database, OutputTypeParameter outputType, ExpectationOption expectValue,
    MatrixOption matrix, FilterOption filterOption, boolean gapped,
    AlignmentCutoffOption hits, DatatypeFactory factory
  ) {
    this.database = database;
    this.outputType = outputType;
    this.expectValue = expectValue;
    this.matrix = matrix;
    this.filterOption = filterOption;
    this.gapped = gapped;
    this.hits = hits;
    this.factory = factory;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) throws TransformationException {
    Sequence sequence = this.getSequence(dataset);

    this.createLockFile();

    UniProtBlastHits blastHits = this.getUniProtBlastHits(sequence);

    if (blastHits.isEmpty()) {
      this.lockFile.delete();
      throw new TransformationException("No hits found");
    }

    SequencesGroup result = this.getSequencesGroupResult(sequence, blastHits);

    return this.factory.newSequencesGroupDataset(result);
  }

  private void createLockFile() {
    this.lockFile = new File(System.getProperty("user.home"), ".seda-uniprot.lock");
    if (this.lockFile.exists()) {
      throw new TransformationException(CONCURRENT_BLAST_MESSAGE);
    } else {
      try {
        this.lockFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
        throw new TransformationException("Could not create lock file");
      }
    }
  }

  private Sequence getSequence(SequencesGroupDataset dataset) {
    if (
      dataset.getSequencesGroupsCount() > 1 || dataset.getSequencesGroups().findFirst().get().getSequenceCount() > 1
    ) {
      throw new IllegalArgumentException(
        "The UniProt BLAST operation only accepts 1 FASTA file containing exactly 1 sequence"
      );
    }

    return dataset.getSequencesGroups().findFirst().get().getSequence(0);
  }

  private UniProtBlastHits getUniProtBlastHits(Sequence sequence) {
    ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();

    UniProtBlastHits blastHits = new UniProtBlastHits();

    if (database.equals(DatabaseOption.UNIPARC)) {
      UniParcBlastService blastService = serviceFactoryInstance.getUniParcBlastService();
      blastService.start();

      CompletableFuture<BlastResult<UniParcHit>> resultFuture =
        blastService.runBlast(getBlastInput(sequence.getChain()));

      try {
        BlastResult<UniParcHit> blastResult = resultFuture.get();

        blastHits =
          getUniProtBlastHits(asStream(blastResult.hits().iterator(), false).map(UniParcHit::getSummary).collect(toList()));

      } catch (InterruptedException | ExecutionException e) {
        this.lockFile.delete();
        throw new TransformationException(e);
      } finally {
        blastService.stop();
      }
    } else if (database.getDisplayName().toUpperCase().contains("UNIREF")) {
      UniRefBlastService blastService = serviceFactoryInstance.getUniRefBlastService();
      blastService.start();

      CompletableFuture<BlastResult<UniRefHit>> resultFuture =
        blastService.runBlast(getBlastInput(sequence.getChain()));

      try {
        BlastResult<UniRefHit> blastResult = resultFuture.get();

        blastHits =
          getUniProtBlastHits(asStream(blastResult.hits().iterator(), false).map(UniRefHit::getSummary).collect(toList()));

      } catch (InterruptedException | ExecutionException e) {
        this.lockFile.delete();
        throw new TransformationException(e);
      } finally {
        blastService.stop();
      }
    } else {
      UniProtBlastService blastService = serviceFactoryInstance.getUniProtBlastService();
      blastService.start();

      CompletableFuture<BlastResult<UniProtHit>> resultFuture =
        blastService.runBlast(getBlastInput(sequence.getChain()));

      try {
        BlastResult<UniProtHit> blastResult = resultFuture.get();

        blastHits =
          getUniProtBlastHits(asStream(blastResult.hits().iterator(), false).map(UniProtHit::getSummary).collect(toList()));

      } catch (InterruptedException | ExecutionException e) {
        this.lockFile.delete();
        throw new TransformationException(e);
      } finally {
        blastService.stop();
      }
    }

    return blastHits;
  }

  public static <T> Stream<T> asStream(Iterator<T> sourceIterator, boolean parallel) {
    Iterable<T> iterable = () -> sourceIterator;
    return StreamSupport.stream(iterable.spliterator(), parallel);
  }

  private static UniProtBlastHits getUniProtBlastHits(List<BlastSummary> summaries) {
    UniProtBlastHits blastHits = new UniProtBlastHits();

    for (BlastSummary summary : summaries) {
      List<String> alignmments = new LinkedList<>();
      summary.getAlignments().forEach(a -> {
        alignmments.add(a.getMatchSeq());
      });

      blastHits.add(new UniProtBlastHit(summary.getEntryId(), summary.getDescription(), alignmments));
    }

    return blastHits;
  }

  private BlastInput getBlastInput(String sequence) {
    return new BlastInput.Builder(this.database, sequence)
      .withMaximumNumberOfAlignments(this.hits)
      .withGapAlign(this.gapped)
      .withExpectation(this.expectValue)
      .withFilter(this.filterOption)
      .withMatrix(this.matrix)
      .build();
  }

  private SequencesGroup getSequencesGroupResult(Sequence sequence, UniProtBlastHits blastHits) {
    if (this.outputType.equals(OutputTypeParameter.ALIGNED)) {
      List<Sequence> alignedSequences = new LinkedList<>();
      for (UniProtBlastHit h : blastHits) {
        if (h.getAlignments().size() == 1) {
          alignedSequences.add(
            this.factory
              .newSequence(h.getEntryId(), h.getDescription(), h.getAlignments().get(0).replace("-", ""), emptyMap())
          );
        } else {
          for (int i = 0; i < h.getAlignments().size(); i++) {
            alignedSequences.add(
              this.factory
                .newSequence(h.getEntryId() + "_" + (i + 1), h.getDescription(), h.getAlignments().get(i).replace("-", ""), emptyMap())
            );
          }
        }
      }

      this.lockFile.delete();

      return this.factory
        .newSequencesGroup(sequence.getName() + "_aligned_sequences.fasta", emptyMap(), alignedSequences);
    } else {
      Path outputFasta;
      try {
        outputFasta = Files.createTempFile("seda-uniprot-blast", "fasta");

        downloadFasta(outputFasta.toFile(), blastHits.stream().map(UniProtBlastHit::getEntryId).collect(toList()));

        SequencesGroup outputSequencesGroup = this.factory.newSequencesGroup(outputFasta);

        return this.factory.newSequencesGroup(
          sequence.getName() + "_complete_sequences.fasta", emptyMap(),
          outputSequencesGroup.getSequences().collect(toList())
        );
      } catch (IOException e) {
        throw new TransformationException(e);
      } finally {
        this.lockFile.delete();
      }
    }
  }
}
