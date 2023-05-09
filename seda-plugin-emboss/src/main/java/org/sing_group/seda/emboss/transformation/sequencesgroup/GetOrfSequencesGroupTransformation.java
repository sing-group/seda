/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.emboss.execution.EmbossBinariesExecutor;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class GetOrfSequencesGroupTransformation implements SequencesGroupTransformation {
  public static final int DEFAULT_MIN_SIZE = 30;
  public static final int DEFAULT_MAX_SIZE = 10000;

  private int table;
  private int minSize;
  private int maxSize;
  private int find;
  private final DatatypeFactory factory;
  private String additionalParameters;
  private EmbossBinariesExecutor embossBinariesExecutor;

  public GetOrfSequencesGroupTransformation(
    EmbossBinariesExecutor embossBinariesExecutor, int table, int minSize, int maxSize, int find,
    String additionalParameters
  ) {
    this(getDefaultDatatypeFactory(), embossBinariesExecutor, table, minSize, maxSize, find, additionalParameters);
  }

  public GetOrfSequencesGroupTransformation(
    DatatypeFactory factory, EmbossBinariesExecutor embossBinariesExecutor, int table, int minSize, int maxSize,
    int find, String additionalParameters
  ) {
    this.embossBinariesExecutor = embossBinariesExecutor;
    this.factory = factory;
    this.table = table;
    this.minSize = minSize;
    this.maxSize = maxSize;
    this.find = find;
    this.additionalParameters = additionalParameters;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    try {
      return getOrf(sequencesGroup, factory);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("Error while running EMBOSS getorf." + e.getMessage());
    }
  }

  private SequencesGroup getOrf(SequencesGroup sequencesGroup, DatatypeFactory factory)
    throws IOException, InterruptedException {
    final Path fastaFile = Files.createTempFile(sequencesGroup.getName(), "fasta");
    writeFasta(fastaFile, sequencesGroup.getSequences());

    final Path alignedFile = Files.createTempFile(sequencesGroup.getName() + "_aligned", ".fasta");

    this.embossBinariesExecutor
      .getOrf(fastaFile.toFile(), alignedFile.toFile(), table, minSize, maxSize, find, additionalParameters);

    List<Sequence> alignedSequences = factory.newSequencesGroup(alignedFile).getSequences().collect(toList());

    return factory.newSequencesGroup(sequencesGroup.getName(), sequencesGroup.getProperties(), alignedSequences);
  }
}
