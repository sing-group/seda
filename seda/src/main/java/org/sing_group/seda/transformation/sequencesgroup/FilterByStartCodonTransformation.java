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

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.transformation.TransformationException;

public class FilterByStartCodonTransformation extends FilterSequencesGroupTransformation {

  public FilterByStartCodonTransformation(String[] validStartCodons) {
    this(validStartCodons, false);
  }

  public FilterByStartCodonTransformation(String[] validStartCodons, boolean checkGeneSequence) {
    this(asList(validStartCodons), checkGeneSequence);
  }

  public FilterByStartCodonTransformation(Collection<String> validStartCodons) {
    this(validStartCodons, false);
  }

  public FilterByStartCodonTransformation(Collection<String> validStartCodons, boolean checkGeneSequence) {
    super(buildPredicate(validStartCodons, checkGeneSequence));
  }

  public FilterByStartCodonTransformation(String[] validStartCodons, DatatypeFactory factory) {
    this(asList(validStartCodons), false, factory);
  }

  public FilterByStartCodonTransformation(
    String[] validStartCodons, boolean checkGeneSequence, DatatypeFactory factory
  ) {
    this(asList(validStartCodons), checkGeneSequence, factory);
  }

  public FilterByStartCodonTransformation(Collection<String> validStartCodons, DatatypeFactory factory) {
    this(validStartCodons, false, factory);
  }

  public FilterByStartCodonTransformation(
    Collection<String> validStartCodons, boolean checkGeneSequence, DatatypeFactory factory
  ) {
    super(buildPredicate(validStartCodons, checkGeneSequence), factory);
  }

  private final static Predicate<Sequence> buildPredicate(
    Collection<String> validStartCodons, boolean checkGeneSequence
  ) {
    final Set<String> validStartCodonsSet = new HashSet<>(validStartCodons);

    return (sequence) -> {
      final String startCodon =
        SequenceUtils.toCodons(sequence, true, checkGeneSequence)
          .findFirst()
          .orElseThrow(() -> new TransformationException("Invalid sequence: " + sequence.getChain()));

      return validStartCodonsSet.contains(startCodon);
    };
  }
}
