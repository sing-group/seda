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

import static java.lang.String.join;
import static java.util.Collections.emptyMap;
import static java.util.Collections.nCopies;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.TransformationException;

@RunWith(Parameterized.class)
public class RemoveBySizeSequencesGroupTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence sequenceLength(int length) {
    return of(String.valueOf(length), "", join("", nCopies(length, "A")), PROPERTIES);
  }

  private static final SequencesGroup SEQUENCES =
    of("Group", emptyMap(), sequenceLength(10), sequenceLength(9), sequenceLength(11));

  @Parameters(name = "{index}: reference sequence = {1}; sequence difference = {2}")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, 0, 0.1, of("Group", emptyMap(), sequenceLength(10), sequenceLength(9), sequenceLength(11))
        },
        {
          SEQUENCES, 0, 0.09, of("Group", emptyMap(), sequenceLength(10))
        },
        {
          SEQUENCES, 0, 0.01, of("Group", emptyMap(), sequenceLength(10))
        }
      }
    );
  }

  private SequencesGroup group;
  private SequencesGroup expectedGroup;
  private RemoveBySizeSequencesGroupTransformation transformation;

  public RemoveBySizeSequencesGroupTransformationTest(
    SequencesGroup group, int referenceSequenceIndex, double maxSizeDifference, SequencesGroup expectedGroup
  ) {
    this.group = group;
    this.expectedGroup = expectedGroup;
    this.transformation = new RemoveBySizeSequencesGroupTransformation(referenceSequenceIndex, maxSizeDifference);
  }

  @Test
  public void filterBySequenceLengthTransformationTest() {
    try {
      SequencesGroup transformed = transformation.transform(group);
      assertThat(
        transformed,
    	ContainsSameSequencesMatcher.containsSameSequencesThat(expectedGroup)
      );
    } catch (TransformationException ex) {
      assertTrue(expectedGroup.getSequenceCount() == 0);
    }
  }
}
