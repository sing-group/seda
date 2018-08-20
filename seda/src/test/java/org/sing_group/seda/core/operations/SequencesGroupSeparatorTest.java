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
package org.sing_group.seda.core.operations;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.core.operations.SequencesGroupSeparator.GROUP_UNMATCHED_SEQUENCES;
import static org.sing_group.seda.core.rename.HeaderTarget.NAME;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequencesGroupSeparatorTest {

  @Test
  public void testSeparateGroupWithRegex() {
    Sequence S1 = of("Homo_Sapiens_1", "", "ACTG", Collections.emptyMap());
    Sequence S2 = of("Homo_Sapiens_2", "", "GTCA", Collections.emptyMap());
    Sequence S3 = of("Mus_Musculus_1", "", "TTAA", Collections.emptyMap());
    Sequence S4 = of("Mus_Musculus_2", "", "GGCC", Collections.emptyMap());
    Sequence S5 = of("Mus_Musculus_3", "", "GGCC", Collections.emptyMap());
    Sequence S6 = of("Other", "", "-C-T", Collections.emptyMap());

    SequencesGroup group = of("Group", emptyMap(), S1, S2, S3, S4, S5, S6);

    HeaderMatcher matcher = new RegexHeaderMatcher("(.*)_[0-9]", NAME, new RegexConfiguration(true, 1, false));

    Map<String, List<Sequence>> result = new SequencesGroupSeparator(matcher).separate(group);

    assertEquals(3, result.size());
    assertEquals(new HashSet<>(asList("Homo_Sapiens", "Mus_Musculus", GROUP_UNMATCHED_SEQUENCES)), result.keySet());
    assertEquals(asList(S1, S2), result.get("Homo_Sapiens"));
    assertEquals(asList(S3, S4, S5), result.get("Mus_Musculus"));
    assertEquals(asList(S6), result.get(GROUP_UNMATCHED_SEQUENCES));
  }
}
