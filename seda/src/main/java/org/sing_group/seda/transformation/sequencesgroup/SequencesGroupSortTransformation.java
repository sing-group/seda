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
package org.sing_group.seda.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;

public class SequencesGroupSortTransformation implements SequencesGroupTransformation {
  private final boolean descending;
  private final Comparator<? super Sequence> sequencesComparator;
  private final SequencesGroupBuilder builder;

  public SequencesGroupSortTransformation(Comparator<? super Sequence> sequencesComparator) {
    this(sequencesComparator, false);
  }
  
  public SequencesGroupSortTransformation(
    Comparator<? super Sequence> sequencesComparator, boolean descending
  ) {
    this(sequencesComparator, descending, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SequencesGroupSortTransformation(
    Comparator<? super Sequence> sequencesComparator, boolean descending, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroup;
    this.sequencesComparator = sequencesComparator;
    this.descending = descending;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<Sequence> newSequences =
      sequencesGroup.getSequences().sorted(descending ? sequencesComparator.reversed() : sequencesComparator)
        .collect(toList());
    
    return this.builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), newSequences);
  }
}
