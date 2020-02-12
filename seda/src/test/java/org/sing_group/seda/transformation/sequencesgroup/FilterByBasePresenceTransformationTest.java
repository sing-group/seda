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
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation.BasePresence;

@RunWith(Parameterized.class)
public class FilterByBasePresenceTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence S1 = of("1", "", "AAAAAACCCCCTTTGGGA", PROPERTIES);
  private static final Sequence S2 = of("2", "", "AAAAAACCCTGGNNNNNN", PROPERTIES);

  private static final SequencesGroup SEQUENCES =
    of("Group", emptyMap(), S1, S2);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, of("Group", emptyMap(), S1),
          new BasePresence[] { filter(0.35d, 0.40d, 'A') }
        },
        {
          SEQUENCES, of("Group", emptyMap(), S1, S2),
          new BasePresence[] { filter(0.30d, 0.40d, 'A') }
        },
        {
          SEQUENCES, of("Group", emptyMap(), S2),
          new BasePresence[] { filter(0.30d, 0.35d, 'A') }
        },
        {
          SEQUENCES, of("Group", emptyMap(), S1),
          new BasePresence[] {
            filter(0.30d, 0.40d, 'A'),
            filter(0.20d, 0.30d, 'C')
          }
        },
        {
          SEQUENCES, of("Group", emptyMap(), S2),
          new BasePresence[] {
            filter(0.30d, 0.40d, 'A'),
            filter(0.10d, 0.20d, 'C')
          }
        },
        {
          SEQUENCES, of("Group", emptyMap(), S1),
          new BasePresence[] { filter(0.66d, 0.67d, 'A', 'C') }
        },
        {
          SEQUENCES, of("Group", emptyMap()),
          new BasePresence[] { filter(0.65d, 0.66d, 'A', 'C') }
        }
      }
    );
  }

  private static BasePresence filter(double d, double e, char...c) {
    return new BasePresence(d, e, c);
  }

  private SequencesGroup group;
  private SequencesGroup expectedSequencesGroup;
  private BasePresence[] basePresences;

  public FilterByBasePresenceTransformationTest(
    SequencesGroup group, SequencesGroup expectedSequencesGroup, BasePresence...basePresences
  ) {
    this.group = group;
    this.expectedSequencesGroup = expectedSequencesGroup;
    this.basePresences = basePresences;
  }

  @Test
  public void filterBySequenceLengthTransformationTest() {
    try {
      SequencesGroup transformed = new FilterByBasePresenceTransformation(basePresences).transform(group);

      assertThat(
        transformed,
        ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
      );
    } catch (TransformationException e) {
      assertTrue(expectedSequencesGroup.getSequenceCount() == 0);
    }
  }
}
