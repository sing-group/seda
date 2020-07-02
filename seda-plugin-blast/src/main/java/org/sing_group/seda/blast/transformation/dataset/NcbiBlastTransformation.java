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

import static java.nio.file.Files.createTempFile;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.blast.ncbi.NcbiEutils.downloadFasta;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.ncbi.NcbiBlastDatabase;
import org.sing_group.seda.blast.ncbi.NcbiBlastHit;
import org.sing_group.seda.blast.ncbi.NcbiBlastHits;
import org.sing_group.seda.blast.ncbi.NcbiBlastRequest;
import org.sing_group.seda.blast.ncbi.NcbiBlastRequestStatus;
import org.sing_group.seda.blast.ncbi.NcbiBlastType;
import org.sing_group.seda.blast.ncbi.parameters.NcbiBlastParameter;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class NcbiBlastTransformation implements SequencesGroupDatasetTransformation {

  private static final String CONCURRENT_NCBI_BLAST_MESSAGE =
    "It seems that an NCBI BLAST operation is already running. "
      + "SEDA only allows to execute one NCBI BLAST operation at a time in order respect NCBI policies regarding the "
      + "usage of resources and avoid problems.";

  private NcbiBlastType blastType;
  private NcbiBlastDatabase blastDatabase;
  private OutputTypeParameter outputType;
  private List<NcbiBlastParameter> blastParameters;
  private DatatypeFactory factory;
  private File lockFile;

  public NcbiBlastTransformation(
    NcbiBlastType blastType, NcbiBlastDatabase blastDatabase, OutputTypeParameter outputType,
    List<NcbiBlastParameter> blastParameters, DatatypeFactory factory
  ) {
    this.blastType = blastType;
    this.blastDatabase = blastDatabase;
    this.outputType = outputType;
    this.blastParameters = blastParameters;
    this.factory = factory;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) throws TransformationException {
    Sequence sequence = this.getSequence(dataset);

    this.createLockFile();

    NcbiBlastRequest request =
      new NcbiBlastRequest(this.blastType, this.blastDatabase, sequence.getChain(), this.blastParameters);

    try {
      request.query();
    } catch (IOException | URISyntaxException e) {
      this.lockFile.delete();
      throw new TransformationException(e);
    }

    Optional<String> requestId = request.getRequestId();

    if (requestId.isPresent()) {
      try {
        int nTry = 1;
        NcbiBlastRequestStatus status = request.getStatus();

        while (status == NcbiBlastRequestStatus.WAITING) {
          Thread.sleep(TimeUnit.SECONDS.toMillis(nTry++ * 15));
          status = request.getStatus();
        }

        if (status.equals(NcbiBlastRequestStatus.READY_WITH_HITS)) {
          NcbiBlastHits blastHits = request.getBlastHits();

          return this.factory.newSequencesGroupDataset(this.getSequencesGroupResult(sequence, blastHits));
        } else {
          throw new TransformationException("No hits found for request ID: " + requestId.get());
        }
      } catch (URISyntaxException | IOException | InterruptedException e) {
        throw new TransformationException(e);
      } finally {
        this.lockFile.delete();
      }
    } else {
      this.lockFile.delete();
      throw new TransformationException("The query failed on submission");
    }
  }

  private void createLockFile() {
    this.lockFile = new File(System.getProperty("user.home"), ".seda-ncbi.lock");
    if (this.lockFile.exists()) {
      throw new TransformationException(CONCURRENT_NCBI_BLAST_MESSAGE);
    } else {
      try {
        this.lockFile.createNewFile();
      } catch (IOException e) {
        throw new TransformationException("Could not create lock file", e);
      }
    }
  }

  private Sequence getSequence(SequencesGroupDataset dataset) {
    if (
      dataset.getSequencesGroupsCount() > 1 || dataset.getSequencesGroups().findFirst().get().getSequenceCount() > 1
    ) {
      throw new IllegalArgumentException(
        "The NCBI blast operation only accepts 1 FASTA file containing exactly 1 sequence"
      );
    }

    return dataset.getSequencesGroups().findFirst().get().getSequence(0);
  }

  private SequencesGroup getSequencesGroupResult(Sequence sequence, NcbiBlastHits blastHits)
    throws ClientProtocolException, URISyntaxException, IOException {

    if (this.outputType.equals(OutputTypeParameter.ALIGNED)) {
      List<Sequence> alignedSequences = new LinkedList<>();

      for (NcbiBlastHit h : blastHits) {
        if (h.getHspHseqs().size() == 1) {
          alignedSequences.add(
            this.factory
              .newSequence(h.getAccession(), h.getDescription(), h.getHspHseqs().get(0).replace("-", ""), emptyMap())
          );
        } else {
          for (int i = 0; i < h.getHspHseqs().size(); i++) {
            alignedSequences.add(
              this.factory
                .newSequence(h.getAccession() + "_" + (i + 1), h.getDescription(), h.getHspHseqs().get(i).replace("-", ""), emptyMap())
            );
          }
        }
      }

      return this.factory
        .newSequencesGroup(sequence.getName() + "_aligned_sequences.fasta", emptyMap(), alignedSequences);
    } else {
      Path outputFasta = createTempFile("seda-ncbi-blast", "fasta");

      String database = this.blastType.getDatabaseType().equals(SequenceType.NUCLEOTIDES) ? "nuccore" : "protein";

      downloadFasta(
        outputFasta.toFile(), blastHits.stream().map(NcbiBlastHit::getAccession).collect(toList()), database
      );

      SequencesGroup outputSequencesGroup = this.factory.newSequencesGroup(outputFasta);
      return this.factory.newSequencesGroup(
        sequence.getName() + "_complete_sequences.fasta", emptyMap(),
        outputSequencesGroup.getSequences().collect(toList())
      );
    }
  }
}
