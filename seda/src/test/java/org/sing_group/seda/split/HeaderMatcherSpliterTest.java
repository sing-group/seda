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
package org.sing_group.seda.split;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.core.rename.HeaderTarget.NAME;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class HeaderMatcherSpliterTest {
  private static final Map<String, Object> PROPERTIES = emptyMap();

  private static final Sequence HS_1 = of("Homo_sapiens_1", "", "", PROPERTIES);
  private static final Sequence HS_2 = of("Homo_sapiens_2", "", "", PROPERTIES);
  private static final Sequence HS_3 = of("Homo_sapiens_3", "", "", PROPERTIES);
  private static final Sequence MM_1 = of("Mus_musculus_1", "", "", PROPERTIES);
  private static final Sequence MM_2 = of("Mus_musculus_2", "", "", PROPERTIES);
  private static final Sequence GG_1 = of("Gallus_gallus_1", "", "", PROPERTIES);

  private static final SequencesGroup SEQUENCES = of("Group", HS_1, HS_2, HS_3, MM_1, MM_2, GG_1);

  @Test
  public void testSplitRegex() {
    HeaderMatcher matcher = new RegexHeaderMatcher("^[^_]*_[^_]*", NAME, new RegexConfiguration(true, 0, false));

    List<SequencesGroup> result = new HeaderMatcherSplitter(matcher).split(SEQUENCES);

    assertEquals("Group_Homo_sapiens", result.get(0).getName());
    assertEquals(3, result.get(0).getSequenceCount());

    assertEquals("Group_Mus_musculus", result.get(1).getName());
    assertEquals(2, result.get(1).getSequenceCount());

    assertEquals("Group_Gallus_gallus", result.get(2).getName());
    assertEquals(1, result.get(2).getSequenceCount());
  }
}
