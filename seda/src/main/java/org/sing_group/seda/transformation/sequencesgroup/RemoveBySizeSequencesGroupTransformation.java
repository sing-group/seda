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
package org.sing_group.seda.transformation.sequencesgroup;

import java.util.function.BiPredicate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class RemoveBySizeSequencesGroupTransformation extends FilterSequencesGroupTransformation {
  public RemoveBySizeSequencesGroupTransformation(Sequence referenceSequence, double maxSizeDifference) {
    super(buildPredicate(referenceSequence, maxSizeDifference));
  }

  public RemoveBySizeSequencesGroupTransformation(Sequence referenceSequence, double maxSizeDifference, DatatypeFactory factory) {
    super(buildPredicate(referenceSequence, maxSizeDifference), factory);
  }

  private final static BiPredicate<SequencesGroup, Sequence> buildPredicate(
    Sequence referenceSequence, double maxSizeDifference
  ) {
    return (sequencesGroup, sequence) -> {
      return filter(sequence, referenceSequence, maxSizeDifference);
    };
  }

  public RemoveBySizeSequencesGroupTransformation(int referenceSequenceIndex, double maxSizeDifference) {
    super(buildPredicate(referenceSequenceIndex, maxSizeDifference));
  }

  public RemoveBySizeSequencesGroupTransformation(int referenceSequenceIndex, double maxSizeDifference, DatatypeFactory factory) {
    super(buildPredicate(referenceSequenceIndex, maxSizeDifference), factory);
  }

  private final static BiPredicate<SequencesGroup, Sequence> buildPredicate(
    int referenceSequenceIndex, double maxSizeDifference
  ) {
    return (sequencesGroup, sequence) -> {
      final Sequence referenceSequence = sequencesGroup.getSequence(referenceSequenceIndex);

      return filter(referenceSequence, sequence, maxSizeDifference);
    };
  }

  private final static boolean filter(Sequence reference, Sequence sequence, double maxSizeDifference) {
    final int referenceLength = reference.getLength();

    final double minLength = referenceLength * (1d - maxSizeDifference);
    final double maxLength = referenceLength * (1d + maxSizeDifference);

    final int sequenceLength = sequence.getLength();

    return sequenceLength >= minLength && sequenceLength <= maxLength;
  }
}
