/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.clustalomega.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.sing_group.seda.clustalomega.execution.ClustalOmegaBinariesExecutor;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class ClustalOmegaAlignmentSequencesGroupTransformation implements SequencesGroupTransformation {
  private int numThreads;
  private final DatatypeFactory factory;
  private String additionalParameters;
  private ClustalOmegaBinariesExecutor clustalOmegaBinariesExecutor;

  public ClustalOmegaAlignmentSequencesGroupTransformation(
    ClustalOmegaBinariesExecutor clustalOmegaBinariesExecutor, int numThreads, String additionalParameters
  ) {
    this(getDefaultDatatypeFactory(), clustalOmegaBinariesExecutor, numThreads, additionalParameters);
  }

  public ClustalOmegaAlignmentSequencesGroupTransformation(
    DatatypeFactory factory, ClustalOmegaBinariesExecutor clustalOmegaBinariesExecutor, int numThreads,
    String additionalParameters
  ) {
    this.clustalOmegaBinariesExecutor = clustalOmegaBinariesExecutor;
    this.factory = factory;
    this.numThreads = numThreads;
    this.additionalParameters = additionalParameters;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    try {
      return alignSequences(sequencesGroup, factory);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("Error while running alignment." + e.getMessage());
    }
  }

  private SequencesGroup alignSequences(SequencesGroup sequencesGroup, DatatypeFactory factory)
    throws IOException, InterruptedException {
    final Path fastaFile = Files.createTempFile(sequencesGroup.getName(), "fasta");
    writeFasta(fastaFile, sequencesGroup.getSequences());

    final Path alignedFile = Files.createTempFile(sequencesGroup.getName() + "_aligned", ".fasta");

    this.clustalOmegaBinariesExecutor
      .executeAlignment(fastaFile.toFile(), alignedFile.toFile(), numThreads, additionalParameters);

    List<Sequence> alignedSequences = factory.newSequencesGroup(alignedFile).getSequences().collect(toList());

    return factory.newSequencesGroup(sequencesGroup.getName(), sequencesGroup.getProperties(), alignedSequences);
  }
}
