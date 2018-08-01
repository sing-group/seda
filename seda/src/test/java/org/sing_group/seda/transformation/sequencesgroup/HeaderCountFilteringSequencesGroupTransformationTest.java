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
import static org.sing_group.seda.core.rename.HeaderTarget.NAME;
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
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;

@RunWith(Parameterized.class)
public class HeaderCountFilteringSequencesGroupTransformationTest {

	private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

	private static final Sequence SA_1 = of("A", "1", "", PROPERTIES);
	private static final Sequence SA_2 = of("A", "2", "", PROPERTIES);
	private static final Sequence SB_1 = of("B", "1", "", PROPERTIES);
	private static final Sequence SB_2 = of("B", "2", "", PROPERTIES);
	private static final Sequence SC_1 = of("C", "1", "", PROPERTIES);

	private static final SequencesGroup EMPTY_SEQUENCES = of("");
	private static final SequencesGroup SEQUENCES_1 = of("Group", SA_1, SA_2, SB_1, SB_2);
	private static final SequencesGroup SEQUENCES_2 = of("Group", SA_1, SA_2, SB_1, SB_2, SC_1);

	private static final Sequence HS_1 = of("Homo_sapiens_1", "", "", PROPERTIES);
	private static final Sequence HS_2 = of("Homo_sapiens_2", "", "", PROPERTIES);
	private static final Sequence MM_1 = of("Mus_musculus_1", "", "", PROPERTIES);
	private static final Sequence GG_1 = of("Gallus_gallus_1", "", "", PROPERTIES);

	private static final SequencesGroup SEQUENCES_3 = of("Group", HS_1, HS_2, MM_1, GG_1);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { SEQUENCES_1, SEQUENCES_1, 				new SequenceNameHeaderMatcher(), 2, 2, true 	},
        { SEQUENCES_1, EMPTY_SEQUENCES, 		new SequenceNameHeaderMatcher(), 2, 2, false 	},
        { SEQUENCES_1, EMPTY_SEQUENCES, 		new SequenceNameHeaderMatcher(), 1, 1, true 	},
        { SEQUENCES_2, SEQUENCES_1, 				new SequenceNameHeaderMatcher(), 2, 2, true 	},
        { SEQUENCES_2, of("Group", SC_1), 	new SequenceNameHeaderMatcher(), 2, 2, false 	},
        { SEQUENCES_2, of("Group", SC_1), 	new SequenceNameHeaderMatcher(), 1, 1, true 	},

        { SEQUENCES_1, of("Group", SB_1, SB_2),
        		new RegexHeaderMatcher("^B$", NAME, new RegexConfiguration(true, 0, false)), 2, 2, true
        },
        { SEQUENCES_2, of("Group", SC_1),
        		new RegexHeaderMatcher("^C$", NAME, new RegexConfiguration(true, 0, false)), 1, 1, true
        },
        { SEQUENCES_3, of("Group", MM_1, GG_1),
        		new RegexHeaderMatcher("^[^_]*_[^_]*", NAME, new RegexConfiguration(true, 0, false)), 1, 1, true },
        { SEQUENCES_3, of("Group", HS_1, HS_2),
        		new RegexHeaderMatcher("^[^_]*_[^_]*", NAME, new RegexConfiguration(true, 0, false)), 1, 1, false
        },
        { SEQUENCES_3, of("Group", HS_1, HS_1),
        		new RegexHeaderMatcher("^[^_]*_[^_]*", NAME, new RegexConfiguration(true, 0, false)), 2, 2, true
      	},
        { SEQUENCES_3, of("Group", MM_1, GG_1),
      			new RegexHeaderMatcher("^[^_]*_[^_]*", NAME, new RegexConfiguration(true, 0, false)), 2, 2, false
        },
        { SEQUENCES_3, SEQUENCES_3,
        		new RegexHeaderMatcher("^[^_]*_[^_]*", NAME, new RegexConfiguration(true, 0, false)), 1, 2, true
        }
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
