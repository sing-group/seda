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
package org.sing_group.seda.core.operations;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import org.junit.Test;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequencesGroupIsoformTesterTest {

  @Test
  public void selectIsoformsTest() {
    Sequence S1 = of("S1", "", "AAAATTTT", emptyMap());
    Sequence S2 = of("S2", "", "CGTACGTA", emptyMap());
    Sequence S3 = of("S3", "", "AAATTT", emptyMap());
    Sequence S4 = of("S4", "", "GGGGGGT", emptyMap());
    Sequence S5 = of("S5", "", "GGGGGT", emptyMap());
    Sequence S6 = of("S6", "", "CCCCCCCCCC", emptyMap());
    Sequence S7 = of("S7", "", "CCCCCCCC", emptyMap());
    Sequence S8 = of("S8", "", "CCCCCCC", emptyMap());
    SequencesGroup group = of("G", emptyMap(), S1, S2, S3, S4, S5, S6, S7, S8);

    SequencesGroupIsoformTesterResult result = new SequencesGroupIsoformTester(group).test(6);

    assertEquals(4, result.getIsoformsLists().size());

    assertEquals(asList(S1, S3), result.getIsoformsLists().get(0));
    assertEquals(asList(S2), result.getIsoformsLists().get(1));
    assertEquals(asList(S4, S5), result.getIsoformsLists().get(2));
    assertEquals(asList(S6, S7, S8), result.getIsoformsLists().get(3));
  }
}
