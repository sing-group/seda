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
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

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

@RunWith(Parameterized.class)
public class TrimAlignmentSequencesGroupTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence S1 = of("1", "", "----TGCTAGCTAGTGATCGCATGCT", PROPERTIES);
  private static final Sequence S2 = of("2", "", "GCTAGCTAGTGATCGCATGCTC----", PROPERTIES);
  private static final Sequence S3 = of("3", "", "-CTAGCTAGTGATCGCATGCTCAG--", PROPERTIES);
  private static final Sequence S4 = of("4", "", "----GCTAGTGATCGCATGCTCA---", PROPERTIES);
  private static final Sequence S5 = of("5", "", "--GCTAGTGATCGCATGCTCAGGAA-", PROPERTIES);
  private static final Sequence S6 = of("6", "", "ATGGCTAGTGATCGCATGCTCAGGAA", PROPERTIES);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        {
          of("Group", emptyMap(), S1, S2, S3, S4, S5, S6),
          of(
            "Group", emptyMap(),
            of("1", "", "TGCTAGCTAGTGATCGCA", PROPERTIES),
            of("2", "", "GCTAGTGATCGCATGCTC", PROPERTIES),
            of("3", "", "GCTAGTGATCGCATGCTC", PROPERTIES),
            of("4", "", "GCTAGTGATCGCATGCTC", PROPERTIES),
            of("5", "", "TAGTGATCGCATGCTCAG", PROPERTIES),
            of("6", "", "CTAGTGATCGCATGCTCA", PROPERTIES)
          )
        },
        {
          of("Group", emptyMap(), S1, S6),
          of(
            "Group", emptyMap(),
            of("1", "", "TGCTAGCTAGTGATCGCATGCT", PROPERTIES),
            of("6", "", "CTAGTGATCGCATGCTCAGGAA", PROPERTIES)
          )
        },
        {
          of("Group", emptyMap(), S3, S5),
          of(
            "Group", emptyMap(),
            of("3", "", "TAGCTAGTGATCGCATGCTCAG", PROPERTIES),
            of("5", "", "GCTAGTGATCGCATGCTCAGGA", PROPERTIES)
          )
        }
      }
    );
  }

  private SequencesGroup group;
  private SequencesGroup expectedSequencesGroup;

  public TrimAlignmentSequencesGroupTransformationTest(
    SequencesGroup group, SequencesGroup expectedSequencesGroup
  ) {
    this.group = group;
    this.expectedSequencesGroup = expectedSequencesGroup;
  }

  @Test
  public void trimAlignmentSequencesGroupTransformationTest() {
    SequencesGroup transformed = new TrimAlignmentSequencesGroupTransformation().transform(group);

    assertThat(
      transformed,
      ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
    );
  }
}
