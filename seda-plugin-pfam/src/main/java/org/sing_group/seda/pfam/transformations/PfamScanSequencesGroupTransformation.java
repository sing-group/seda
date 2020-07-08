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
package org.sing_group.seda.pfam.transformations;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.pfam.PfamScanSequenceErrorPolicy.MARK_ERROR;
import static org.sing_group.seda.pfam.PfamScanSequenceErrorPolicy.PRODUCE_ERROR;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.pfam.PfamScanAnnotations;
import org.sing_group.seda.pfam.PfamScanBatch;
import org.sing_group.seda.pfam.PfamScanBatchResult;
import org.sing_group.seda.pfam.PfamScanRequestConfiguration;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class PfamScanSequencesGroupTransformation implements SequencesGroupTransformation {

  private static final int BATCH_SIZE = 30;

  private PfamScanRequestConfiguration configuration;
  private long firstBatchDuration = -1;
  private DatatypeFactory factory;

  public PfamScanSequencesGroupTransformation(PfamScanRequestConfiguration configuration) {
    this(configuration, getDefaultDatatypeFactory());
  }

  public PfamScanSequencesGroupTransformation(PfamScanRequestConfiguration configuration, DatatypeFactory factory) {
    this.factory = factory;
    this.configuration = configuration;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {

    List<Sequence> sequences = sequencesGroup.getSequences().collect(toList());
    List<Sequence> annotatedSequences = new LinkedList<>();

    for (int i = 0; i < sequencesGroup.getSequenceCount(); i = i + BATCH_SIZE) {
      if (firstBatchDuration > 0) {
        try {
          TimeUnit.NANOSECONDS.sleep(firstBatchDuration * this.configuration.getBatchDelayFactor());
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }

      int start = i;
      int end = Math.min(i + BATCH_SIZE, sequencesGroup.getSequenceCount());
      List<Sequence> currentBatch = sequences.subList(start, end);

      long startTime = System.nanoTime();
      PfamScanBatch pfam = new PfamScanBatch(this.configuration, currentBatch);

      try {
        PfamScanBatchResult pfamResults = pfam.query().get();

        if (
          pfamResults.getFailures().size() > 0
            && this.configuration.getErrorPolicy().equals(PRODUCE_ERROR)
        ) {
          throw new TransformationException(
            "The following sequences could not be annotated with PfamScan"
              + pfamResults.getFailures().stream().map(Sequence::getName).collect(joining(", "))
          );
        }

        for (Sequence sequence : currentBatch) {
          if (pfamResults.getAnnotations().containsKey(sequence)) {
            String description =
              getAnnotatedDescription(sequence.getDescription(), pfamResults.getAnnotations().get(sequence));

            annotatedSequences.add(
              this.factory.newSequence(sequence.getName(), description, sequence.getChain(), sequence.getProperties())
            );
          } else if (this.configuration.getErrorPolicy().equals(MARK_ERROR)) {
            StringBuilder description = new StringBuilder(sequence.getDescription());
            description.append("(PfamScan error)");

            annotatedSequences.add(
              this.factory
                .newSequence(sequence.getName(), description.toString(), sequence.getChain(), sequence.getProperties())
            );
          }
        }
      } catch (InterruptedException | ExecutionException e) {
        throw new TransformationException(e);
      }

      long endTime = System.nanoTime();
      if (firstBatchDuration < 0) {
        firstBatchDuration = endTime - startTime;
      }
    }

    return this.factory.newSequencesGroup(sequencesGroup.getName(), sequencesGroup.getProperties(), annotatedSequences);
  }

  private String getAnnotatedDescription(String sequenceDescription, PfamScanAnnotations pfamScanAnnotations) {
    StringBuilder description = new StringBuilder(sequenceDescription);
    if (!sequenceDescription.isEmpty() && !pfamScanAnnotations.isEmpty()) {
      description.append(" ");
    }

    description.append(pfamScanAnnotations.stream().map(a -> {
      return new StringBuilder("(")
        .append(a.getAlignStart())
        .append("-")
        .append(a.getAlignEnd())
        .append(" ")
        .append(a.getHmmAcc())
        .append(" ")
        .append(a.getHmmName())
        .append(" - ")
        .append(a.getType())
        .append(")")
        .toString();
    }).collect(joining(" ")));

    return description.toString();
  }
}
