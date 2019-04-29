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
package org.sing_group.seda.transformation.dataset;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class MergeSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final SequencesGroupBuilder groupBuilder;
  private final String mergeName;

  public MergeSequencesGroupDatasetTransformation(String mergeName) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), mergeName);
  }

  public MergeSequencesGroupDatasetTransformation(DatatypeFactory factory, String mergeName) {
    this.builder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.mergeName = mergeName;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    List<Sequence> sequences = new LinkedList<>();

    dataset.getSequencesGroups().forEach(
      g -> {
        sequences.addAll(g.getSequences().collect(Collectors.toList()));
      }
    );

    return this.builder.apply(
      new SequencesGroup[] {
        this.groupBuilder.of(mergeName, getDatasetPropertiesMap(dataset), sequences)
      }
    );
  }
}
