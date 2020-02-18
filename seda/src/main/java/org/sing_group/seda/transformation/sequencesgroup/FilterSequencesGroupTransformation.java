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

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;

public abstract class FilterSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
  private final BiPredicate<SequencesGroup, Sequence> filter;

  public FilterSequencesGroupTransformation(Predicate<Sequence> filter) {
    this((sequencesGroup, sequence) -> filter.test(sequence));
  }

  public FilterSequencesGroupTransformation(BiPredicate<SequencesGroup, Sequence> filter) {
    this(filter, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public FilterSequencesGroupTransformation(Predicate<Sequence> filter, DatatypeFactory factory) {
    this((sequencesGroup, sequence) -> filter.test(sequence), factory);
  }
  
  public FilterSequencesGroupTransformation(BiPredicate<SequencesGroup, Sequence> filter, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroup;
    this.filter = filter;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) {
    try {
      final List<Sequence> sequences = sequencesGroup.getSequences()
        .filter(sequence -> filter.test(sequencesGroup, sequence))
      .collect(toList());

      if (sequences.size() == 0) {
        throw new TransformationException("Empty sequences after filtering");
      }

      return this.builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), sequences);
    } catch (RuntimeException e) {
      if (e instanceof TransformationException)
        throw e;
      else
        throw new TransformationException("Unexpected error during transformation", e);
    }
  }

}
