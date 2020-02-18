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
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
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
import org.sing_group.seda.transformation.TransformationException;

@RunWith(Parameterized.class)
public class FilterByStartCodonTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence S1 = of("1", "", "ACTGGT", PROPERTIES);
  private static final Sequence S2 = of("2", "", "GGTACT", PROPERTIES);
  private static final Sequence S3 = of("3", "", "GGTACTY", PROPERTIES);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return asList(
        new Object[][] { 
          { of("Group", emptyMap(), S1, S2, S3), of("Group", emptyMap()), emptyList() }, 
          { of("Group", emptyMap(), S1, S2, S3), of("Group", emptyMap(), S1), asList("ACT") }, 
          { of("Group", emptyMap(), S1, S2, S3), of("Group", emptyMap(), S2, S3), asList("GGT") }, 
          { of("Group", emptyMap(), S1, S2, S3), of("Group", emptyMap(), S1, S2, S3), asList("ACT", "GGT") }, 
        });
  }

  private SequencesGroup group;
  private SequencesGroup expectedSequencesGroup;
  private Collection<String> validStartCodons;

  public FilterByStartCodonTransformationTest(SequencesGroup group, SequencesGroup expectedSequencesGroup,
      Collection<String> validStartCodons
  ) {
    this.group = group;
    this.expectedSequencesGroup = expectedSequencesGroup;
    this.validStartCodons = validStartCodons;
  }

  @Test
  public void removeInFramestopCodonsSequencesGroupTransformationTest() {
    try {
      SequencesGroup transformed = new FilterByStartCodonTransformation(validStartCodons).transform(group);

      assertThat(
        transformed,
        ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
      );
    } catch (TransformationException e) {
      assertTrue(expectedSequencesGroup.getSequenceCount() == 0);
    }
  }
}
