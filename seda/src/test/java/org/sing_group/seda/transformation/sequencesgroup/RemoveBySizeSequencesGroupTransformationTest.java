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
import org.sing_group.seda.core.selection.LargestSequenceSelector;
import org.sing_group.seda.core.selection.SequenceIndexSelector;
import org.sing_group.seda.core.selection.SequenceSelector;
import org.sing_group.seda.core.selection.SmallestSequenceSelector;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.TransformationException;

@RunWith(Parameterized.class)
public class RemoveBySizeSequencesGroupTransformationTest {

  private static final SequencesGroup SEQUENCES =
    of("Group", emptyMap(), sequenceLength(10), sequenceLength(9), sequenceLength(11));

  @Parameters(name = "{index}: sequence difference = {2}")
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
          { SEQUENCES, new SequenceIndexSelector(0), 0.10, of("Group", emptyMap(), sequenceLength(10), sequenceLength(9), sequenceLength(11)) },
          { SEQUENCES, new SequenceIndexSelector(0), 0.09, of("Group", emptyMap(), sequenceLength(10)) },
          { SEQUENCES, new SequenceIndexSelector(0), 0.01, of("Group", emptyMap(), sequenceLength(10)) },
          { SEQUENCES, new LargestSequenceSelector(), 0.1, of("Group", emptyMap(), sequenceLength(10), sequenceLength(11)) },
          { SEQUENCES, new LargestSequenceSelector(), 0.09, of("Group", emptyMap(), sequenceLength(11)) },
          { SEQUENCES, new SmallestSequenceSelector(), 0.12, of("Group", emptyMap(), sequenceLength(10), sequenceLength(9)) },
          { SEQUENCES, new SmallestSequenceSelector(), 0.09, of("Group", emptyMap(), sequenceLength(9)) }
      }
    );
  }

  private SequencesGroup group;
  private SequencesGroup expectedGroup;
  private RemoveBySizeSequencesGroupTransformation transformation;

  public RemoveBySizeSequencesGroupTransformationTest(
    SequencesGroup group, SequenceSelector selector, double maxSizeDifference, SequencesGroup expectedGroup
  ) {
    this.group = group;
    this.expectedGroup = expectedGroup;
    this.transformation = new RemoveBySizeSequencesGroupTransformation(selector, maxSizeDifference);
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
