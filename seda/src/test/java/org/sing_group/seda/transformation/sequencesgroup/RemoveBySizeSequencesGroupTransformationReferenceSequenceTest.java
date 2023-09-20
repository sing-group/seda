/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.sing_group.seda.TestUtils.sequenceLength;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.TransformationException;

@RunWith(Parameterized.class)
public class RemoveBySizeSequencesGroupTransformationReferenceSequenceTest {

  private static final SequencesGroup SEQUENCES =
    of("Group", emptyMap(), sequenceLength(85), sequenceLength(95), sequenceLength(105), sequenceLength(115));

  @Parameters(name = "{index}: sequence difference = {2}")
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
          { SEQUENCES, sequenceLength(100), 0.01, of("Group", emptyMap()) },
          { SEQUENCES, sequenceLength(100), 0.05, of("Group", emptyMap(), sequenceLength(95), sequenceLength(105)) },
          { SEQUENCES, sequenceLength(100), 0.15, of("Group", emptyMap(), sequenceLength(85), sequenceLength(95), sequenceLength(105), sequenceLength(115)) },
      }
    );
  }

  private SequencesGroup group;
  private SequencesGroup expectedGroup;
  private RemoveBySizeSequencesGroupTransformation transformation;

  public RemoveBySizeSequencesGroupTransformationReferenceSequenceTest(
    SequencesGroup group, Sequence referenceSequence, double maxSizeDifference, SequencesGroup expectedGroup
  ) {
    this.group = group;
    this.expectedGroup = expectedGroup;
    this.transformation = new RemoveBySizeSequencesGroupTransformation(referenceSequence, maxSizeDifference);
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