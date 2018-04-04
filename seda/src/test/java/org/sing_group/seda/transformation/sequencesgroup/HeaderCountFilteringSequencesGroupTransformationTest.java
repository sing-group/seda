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

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.core.filtering.StringHeaderMatcher;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;

@RunWith(Parameterized.class)
public class HeaderCountFilteringSequencesGroupTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SA_1 = Sequence.of("A", "1", "", PROPERTIES);
  private static final Sequence SA_2 = Sequence.of("A", "2", "", PROPERTIES);
  private static final Sequence SB_1 = Sequence.of("B", "1", "", PROPERTIES);
  private static final Sequence SB_2 = Sequence.of("B", "2", "", PROPERTIES);
  private static final Sequence SC_1 = Sequence.of("C", "1", "", PROPERTIES);

  private static final SequencesGroup EMPTY_SEQUENCES = SequencesGroup.of("");
  private static final SequencesGroup SEQUENCES_1 = SequencesGroup.of("Group", SA_1, SA_2, SB_1, SB_2);
  private static final SequencesGroup SEQUENCES_2 = SequencesGroup.of("Group", SA_1, SA_2, SB_1, SB_2, SC_1);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { SEQUENCES_1, SEQUENCES_1, new SequenceNameHeaderMatcher(), 2, 2, true },
        { SEQUENCES_1, EMPTY_SEQUENCES, new SequenceNameHeaderMatcher(), 2, 2, false },
        { SEQUENCES_1, EMPTY_SEQUENCES, new SequenceNameHeaderMatcher(), 1, 1, true },
        { SEQUENCES_2, SEQUENCES_1, new SequenceNameHeaderMatcher(), 2, 2, true },
        { SEQUENCES_2, SequencesGroup.of("Group", SC_1), new SequenceNameHeaderMatcher(), 2, 2, false },
        { SEQUENCES_2, SequencesGroup.of("Group", SC_1), new SequenceNameHeaderMatcher(), 1, 1, true },
        { SEQUENCES_1, SequencesGroup.of("Group", SB_1, SB_2), new StringHeaderMatcher("B", false, true), 2, 2, true },
        { SEQUENCES_2, SequencesGroup.of("Group", SC_1), new StringHeaderMatcher("C", false, true), 1, 1, true }
      }
    );
  }

  private SequencesGroup group;
  private SequencesGroup expectedGroup;
  private HeaderCountFilteringSequencesGroupTransformation transformation;

  public HeaderCountFilteringSequencesGroupTransformationTest(
    SequencesGroup group, SequencesGroup expectedGroup, HeaderMatcher matcher, int min, int max, boolean include
  ) {
    this.group = group;
    this.expectedGroup = expectedGroup;
    this.transformation = new HeaderCountFilteringSequencesGroupTransformation(matcher, min, max, include);
  }

  @Test
  public void filterBySequenceLengthTransformationTest() {
    SequencesGroup transformed = transformation.transform(this.group);

    assertThat(
      transformed,
      ContainsSameSequencesMatcher.containsSameSequencesThat(this.expectedGroup)
    );
  }
}
