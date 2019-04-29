/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class GrowSequencesGroupTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = of("1", "", "ACT", PROPERTIES);
  private static final Sequence SEQ_2 = of("2", "", "CTAC", PROPERTIES);
  private static final Sequence SEQ_3 = of("3", "", "ACCCCCCTGAC", PROPERTIES);
  private static final Sequence SEQ_4 = of("4", "", "TGACGGGGGGGGAAA", PROPERTIES);

  private static final SequencesGroup SEQUENCES = of("Group", emptyMap(), SEQ_1, SEQ_2, SEQ_3, SEQ_4);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          new GrowSequencesGroupTransformation(FACTORY, 2),
          SEQUENCES,
          Arrays.asList("CTACT", "ACCCCCCTGACGGGGGGGGAAA")
        },
        {
          new GrowSequencesGroupTransformation(FACTORY, 3),
          SEQUENCES,
          Arrays.asList("ACT", "CTAC", "ACCCCCCTGACGGGGGGGGAAA")
        },
        {
          new GrowSequencesGroupTransformation(FACTORY, 4),
          SEQUENCES,
          Arrays.asList("ACT", "CTAC", "ACCCCCCTGACGGGGGGGGAAA")
        },
        {
          new GrowSequencesGroupTransformation(FACTORY, 5),
          SEQUENCES,
          Arrays.asList("ACT", "CTAC", "ACCCCCCTGAC", "TGACGGGGGGGGAAA")
        }
      }
    );
  }

  private GrowSequencesGroupTransformation transformation;
  private SequencesGroup input;
  private Set<String> expectedChains;

  public GrowSequencesGroupTransformationTest(
    GrowSequencesGroupTransformation transformation, SequencesGroup input, List<String> expectedChains
  ) {
    this.transformation = transformation;
    this.input = input;
    this.expectedChains = new HashSet<>(expectedChains);
  }

  @Test
  public void test() {
    SequencesGroup actualResult = this.transformation.transform(this.input);

    final Set<String> actualChains = actualResult.getSequences().map(Sequence::getChain).collect(Collectors.toSet());

    Assert.assertEquals(expectedChains, actualChains);
  }
}
