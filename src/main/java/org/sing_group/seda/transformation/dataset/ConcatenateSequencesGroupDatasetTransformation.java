/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class ConcatenateSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {

  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final BiFunction<String, List<Sequence>, SequencesGroup> groupBuilder;
  private final SequenceBuilder sequenceBuilder;
  private final String mergeName;
  private final HeaderTarget headerTarget;

  public ConcatenateSequencesGroupDatasetTransformation(String mergeName, HeaderTarget headerTarget) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), mergeName, headerTarget);
  }

  public ConcatenateSequencesGroupDatasetTransformation(
    DatatypeFactory factory, String mergeName, HeaderTarget headerTarget
  ) {
    this.builder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
    this.mergeName = mergeName;
    this.headerTarget = headerTarget;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    Map<String, Sequence> concatenatedSequences = new HashMap<>();

    dataset.getSequencesGroups().forEach(
      g -> {
        g.getSequences().forEach(s -> {
          String headerMatch = getHeaderMatch(s);
          if(concatenatedSequences.containsKey(headerMatch)) {
            Sequence toMerge = concatenatedSequences.get(headerMatch);
            concatenatedSequences.put(
              headerMatch,
              this.sequenceBuilder.of(headerMatch, "", toMerge.getChain() + s.getChain(), toMerge.getProperties())
            );
          } else {
              concatenatedSequences.put(
                headerMatch,
                this.sequenceBuilder.of(headerMatch, "", s.getChain(), s.getProperties())
              );
          }
        });
      }
    );

    return this.builder.apply(
      new SequencesGroup[] {
        this.groupBuilder.apply(mergeName, new LinkedList<>(concatenatedSequences.values()))
      }
    );
  }

  private String getHeaderMatch(Sequence s) {
    switch (this.headerTarget) {
      case ALL:
        return s.getName() + " " + s.getDescription();
      case DESCRIPTION:
        return s.getDescription();
      case NAME:
        return s.getName();
    }
    throw new IllegalStateException();
  }
}
