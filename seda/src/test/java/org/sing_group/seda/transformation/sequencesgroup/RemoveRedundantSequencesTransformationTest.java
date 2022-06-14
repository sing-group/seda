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
import static org.junit.Assert.assertThat;
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
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

@RunWith(Parameterized.class)
public class RemoveRedundantSequencesTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  /*
   * First set of nucleic acid sequences with full headers (sequence IDs and
   * descriptions).
   */
  private static final Sequence A1 = of("A1", "Sequence A1", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence A2 = of("A2", "Sequence A2", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence A3 = of("A3", "Sequence A3", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence B = of("B", "Sequence B", "CCCTTT", PROPERTIES);
  private static final Sequence C = of("C", "Sequence C", "CTTTG", PROPERTIES);
  private static final Sequence D = of("D", "Sequence D", "AGGGTTTCCCAAA", PROPERTIES);
  private static final Sequence E = of("E", "Sequence E", "GGGTA", PROPERTIES);

  private static final SequencesGroup GROUP = of("Group", emptyMap(), A1, A2, A3, B, C, D, E);
  private static final SequencesGroup WITHOUT_DUPLICATES = of("Group", emptyMap(), D, A1, B, C, E);

  private static final Sequence A1_MERGED =
    of("A1", "Sequence A1 [A2 Sequence A2] [A3 Sequence A3]", "AAACCCTTTGGG", PROPERTIES);
  private static final SequencesGroup WITHOUT_DUPLICATES_MERGED_HEADERS =
    of("Group", emptyMap(), D, A1_MERGED, B, C, E);

  private static final SequencesGroup WITHOUT_CONTAINED = of("Group", emptyMap(), D, A1, E);
  private static final Sequence A1_MERGED_2 =
    of(
      "A1", "Sequence A1 [A2 Sequence A2] [A3 Sequence A3] [B Sequence B] [C Sequence C]", "AAACCCTTTGGG", PROPERTIES
    );
  private static final SequencesGroup WITHOUT_CONTAINED_MERGED_HEADERS = of("Group", emptyMap(), D, A1_MERGED_2, E);
  
  /*
   * Second set of nucleic acid sequences with simple headers (i.e. only sequence
   * IDs).
   */
  private static final Sequence A1_NO_DESCRIPTION = of("A1", "", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence A2_NO_DESCRIPTION = of("A2", "", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence B_NO_DESCRIPTION = of("B", "", "CCCTTT", PROPERTIES);

  private static final SequencesGroup GROUP_NO_DESCRIPTION = of(
    "Group", emptyMap(), A1_NO_DESCRIPTION, A2_NO_DESCRIPTION, B_NO_DESCRIPTION
  );

  private static final Sequence A1_NO_DESCRIPTION_MERGED = of("A1", "[A2]", "AAACCCTTTGGG", PROPERTIES);
  private static final SequencesGroup GROUP_NO_DESCRIPTION_WITHOUT_DUPLICATES_MERGED_HEADERS = of(
    "Group", emptyMap(), A1_NO_DESCRIPTION_MERGED, B_NO_DESCRIPTION
  );
  private static final Sequence A1_NO_DESCRIPTION_MERGED_WITHOUT_CONTAINED = of(
    "A1", "[A2] [B]", "AAACCCTTTGGG", PROPERTIES
  );
  private static final SequencesGroup GROUP_NO_DESCRIPTION_WITHOUT_CONTAINED_MERGED_HEADERS = of(
    "Group", emptyMap(), A1_NO_DESCRIPTION_MERGED_WITHOUT_CONTAINED
  );

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          configuration(Mode.EXACT_DUPLICATES, false), GROUP, WITHOUT_DUPLICATES
        },
        {
          configuration(Mode.EXACT_DUPLICATES, true), GROUP, WITHOUT_DUPLICATES_MERGED_HEADERS
        },
        {
          configuration(Mode.CONTAINED_SEQUENCES, false), GROUP, WITHOUT_CONTAINED
        },
        {
          configuration(Mode.CONTAINED_SEQUENCES, true), GROUP, WITHOUT_CONTAINED_MERGED_HEADERS
        },
        {
          configuration(Mode.EXACT_DUPLICATES, true), GROUP_NO_DESCRIPTION, GROUP_NO_DESCRIPTION_WITHOUT_DUPLICATES_MERGED_HEADERS
        },
        {
          configuration(Mode.CONTAINED_SEQUENCES, true), GROUP_NO_DESCRIPTION, GROUP_NO_DESCRIPTION_WITHOUT_CONTAINED_MERGED_HEADERS
        }
      }
    );
  }

  private static RemoveRedundantSequencesTransformationConfiguration configuration(Mode mode, boolean mergeHeaders) {
    return new RemoveRedundantSequencesTransformationConfiguration(mode, mergeHeaders);
  }

  private RemoveRedundantSequencesTransformationConfiguration configuration;
  private SequencesGroup input;
  private SequencesGroup expected;


  public RemoveRedundantSequencesTransformationTest(
    RemoveRedundantSequencesTransformationConfiguration configuration, SequencesGroup input, SequencesGroup expected
  ) {
    this.configuration = configuration;
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void removeRedundantSequencesTest() {
    RemoveRedundantSequencesTransformation transformation =
      new RemoveRedundantSequencesTransformation(
        this.configuration
      );
    SequencesGroup transformed = transformation.transform(this.input);

    assertThat(
      transformed,
      ContainsSameSequencesMatcher.containsSameSequencesThat(expected)
    );
  }
}