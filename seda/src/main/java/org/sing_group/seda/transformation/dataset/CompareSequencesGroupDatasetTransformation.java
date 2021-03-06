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
package org.sing_group.seda.transformation.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class CompareSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final SequencesGroupBuilder groupBuilder;
  private SequenceTarget sequenceTarget;

  public CompareSequencesGroupDatasetTransformation(SequenceTarget sequenceTarget) {
    this(sequenceTarget, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public CompareSequencesGroupDatasetTransformation(SequenceTarget sequenceTarget, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceTarget = sequenceTarget;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    ArrayList<SequencesGroup> result = new ArrayList<>();

    List<SequencesGroup> sequencesGroups = dataset.getSequencesGroups().collect(Collectors.toList());

    for ( int i = 0; i < sequencesGroups.size() - 1; i++) {
      SequencesGroup A = sequencesGroups.get(i);
      for (int j = i+1; j < sequencesGroups.size(); j++) {
        SequencesGroup B = sequencesGroups.get(j);

        Stream<Sequence> onlyA = A.getSequences()
          .filter(sequenceA -> B.getSequences()
            .noneMatch(sequenceB -> filter(sequenceA, sequenceB,sequenceTarget)));
        Stream<Sequence> onlyB = B.getSequences()
          .filter(sequenceB -> A.getSequences()
            .noneMatch(sequenceA -> filter(sequenceA, sequenceB,sequenceTarget)));
        Stream<Sequence> both = A.getSequences()
          .filter(sequenceA -> B.getSequences()
            .anyMatch(sequenceB -> filter(sequenceA, sequenceB,sequenceTarget)));

        result.add(this.groupBuilder.of(A.getName() + "_vs_" + B.getName() + "_only_" + A.getName() + ".fasta",
          getDatasetPropertiesMap(dataset), onlyA.collect(Collectors.toList())));
        result.add(this.groupBuilder.of(A.getName() + "_vs_" + B.getName() + "_only_" + B.getName() + ".fasta",
          getDatasetPropertiesMap(dataset), onlyB.collect(Collectors.toList())));
        result.add(this.groupBuilder.of(A.getName() + "_vs_" + B.getName() + "_both.fasta",
          getDatasetPropertiesMap(dataset), both.collect(Collectors.toList())));
      }
    }

    return this.builder.apply(
      result.toArray(new SequencesGroup[0])
    );
  }

  private static boolean filter(Sequence sequenceA, Sequence sequenceB, SequenceTarget sequenceTarget) {
    switch (sequenceTarget) {
    case HEADER:
      return sequenceA.getHeader().equals(sequenceB.getHeader());
    case SEQUENCE:
      return sequenceA.getChain().equals(sequenceB.getChain());
    }
    throw new IllegalStateException("Unknown SequenceTarget enum value.");
  }
}
