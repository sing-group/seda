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
package org.sing_group.seda.transformation.sequencesgroup;

import static java.util.Collections.emptyMap;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

@RunWith(Parameterized.class)
public class FilterBySequenceLengthTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence LENGTH_1 = of("1", "", "A", PROPERTIES);
  private static final Sequence LENGTH_2 = of("2", "", "AC", PROPERTIES);
  private static final Sequence LENGTH_10 = of("10", "", "ACTGACTGAC", PROPERTIES);
  private static final Sequence LENGTH_20 = of("20", "", "ACTGACTGACACTGACTGAC", PROPERTIES);

  private static final SequencesGroup SEQUENCES =
    of("Group", emptyMap(), LENGTH_1, LENGTH_2, LENGTH_10, LENGTH_20);

  @Parameters(name = "{index}: minimum sequence length = {1}; maximum sequence length = {2}")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, 0, 0, 4
        },
        {
          SEQUENCES, 1, 0, 4
        },
        {
          SEQUENCES, 2, 0, 3
        },
        {
          SEQUENCES, 3, 0, 2
        },
        {
          SEQUENCES, 5, 15, 1
        },
        {
          SEQUENCES, 10, 10, 1
        },
        {
          SEQUENCES, 10, 0, 2
        },
        {
          SEQUENCES, 11, 0, 1
        },
        {
          SEQUENCES, 20, 0, 1
        },
        {
          SEQUENCES, 21, 0, 0
        },
        {
          SEQUENCES, 100, 0, 0
        }
      }
    );
  }

  private SequencesGroup group;
  private int expectedSequenceGroupSize;
  private FilterBySequenceLengthTransformation transformation;

  public FilterBySequenceLengthTransformationTest(
    SequencesGroup group, int minSequenceLength, int maxSequenceLength, int expectedSequenceGroupSize
  ) {
    this.group = group;
    this.expectedSequenceGroupSize = expectedSequenceGroupSize;
    this.transformation = new FilterBySequenceLengthTransformation(minSequenceLength, maxSequenceLength);
  }

  @Test
  public void filterBySequenceLengthTransformationTest() {
    try {
      SequencesGroup transformed = transformation.transform(group);
      Assert.assertEquals(this.expectedSequenceGroupSize, transformed.getSequenceCount());
    } catch (TransformationException ex) {
      Assert.assertEquals(0, this.expectedSequenceGroupSize);
    }
  }
}
