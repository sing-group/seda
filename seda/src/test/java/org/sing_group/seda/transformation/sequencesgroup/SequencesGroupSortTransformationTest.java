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

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.IsEqualToSequence;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class SequencesGroupSortTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = of("D", "", "A", PROPERTIES);
  private static final Sequence SEQ_2 = of("C", "", "AC", PROPERTIES);
  private static final Sequence SEQ_3 = of("B", "", "ACTG", PROPERTIES);
  private static final Sequence SEQ_4 = of("A", "", "ACTGA", PROPERTIES);

  private static final SequencesGroup SEQUENCES = of("Group", emptyMap(), SEQ_2, SEQ_1, SEQ_4, SEQ_3);
  
  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, SequenceTarget.SEQUENCE, SequenceComparator.LENGTH, false, new Sequence[] {
            SEQ_1, SEQ_2, SEQ_3, SEQ_4
          }
        },
        {
          SEQUENCES, SequenceTarget.SEQUENCE, SequenceComparator.LENGTH, true, new Sequence[] {
            SEQ_4, SEQ_3, SEQ_2, SEQ_1
          }
        },
        {
          SEQUENCES, SequenceTarget.HEADER, SequenceComparator.ALPHABETICAL, false, new Sequence[] {
            SEQ_4, SEQ_3, SEQ_2, SEQ_1
          }
        },
        {
          SEQUENCES, SequenceTarget.HEADER, SequenceComparator.ALPHABETICAL, true, new Sequence[] {
            SEQ_1, SEQ_2, SEQ_3, SEQ_4
          }
        }        
      }
    );
  }

  private SequencesGroup group;
  private Comparator<Sequence> comparator;
  private boolean descending;
  private Sequence[] expectedResult;

  public SequencesGroupSortTransformationTest(
    SequencesGroup group, SequenceTarget target, SequenceComparator comparator, boolean descending,
    Sequence[] expectedResult
  ) {
    this.group = group;
    this.comparator = comparator.getComparator(target);
    this.descending = descending;
    this.expectedResult = expectedResult;
  }

  @Test
  public void sortBySequenceLength() {
    SequencesGroupTransformation transformer = new SequencesGroupSortTransformation(this.comparator, this.descending);
    SequencesGroup sortedSequences = transformer.transform(this.group);

    final List<Sequence> actual = sortedSequences.getSequences().collect(Collectors.toList());

    assertThat(actual, IsEqualToSequence.containsSequencesInOrder(expectedResult));
  }
}
