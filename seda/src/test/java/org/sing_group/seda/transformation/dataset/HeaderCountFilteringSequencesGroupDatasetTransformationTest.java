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
package org.sing_group.seda.transformation.dataset;

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
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.matcher.EqualSequencesGroupDatasetMatcher;

@RunWith(Parameterized.class)
public class HeaderCountFilteringSequencesGroupDatasetTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SA_1 = Sequence.of("A", "1", "", PROPERTIES);
  private static final Sequence SA_2 = Sequence.of("A", "2", "", PROPERTIES);
  private static final Sequence SB_1 = Sequence.of("B", "1", "", PROPERTIES);
  private static final Sequence SB_2 = Sequence.of("B", "2", "", PROPERTIES);
  private static final Sequence SC_1 = Sequence.of("C", "1", "", PROPERTIES);
  private static final Sequence SC_2 = Sequence.of("C_2", "1", "", PROPERTIES);
  private static final Sequence SC_3 = Sequence.of("C_3", "1", "", PROPERTIES);

  private static final SequencesGroup SEQUENCES_1 = SequencesGroup.of("Group1", SA_1, SA_2, SB_1, SB_2);
  private static final SequencesGroup SEQUENCES_2 = SequencesGroup.of("Group2", SA_1, SA_2, SB_1, SB_2, SC_1);
  private static final SequencesGroup SEQUENCES_3 = SequencesGroup.of("Group3", SC_2, SC_3);

  private static final SequencesGroupDataset DATASET_1 = SequencesGroupDataset.of(SEQUENCES_1, SEQUENCES_2);
  private static final SequencesGroupDataset DATASET_2 = SequencesGroupDataset.of(SEQUENCES_1, SEQUENCES_2, SEQUENCES_3);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { DATASET_1, DATASET_1, new SequenceNameHeaderMatcher(), 1, 2, true },
        { DATASET_1, SequencesGroupDataset.of(SEQUENCES_1), new SequenceNameHeaderMatcher(), 2, 2, true },
        { DATASET_1, SequencesGroupDataset.of(SEQUENCES_2), new RegexHeaderMatcher("^C$", HeaderTarget.NAME, new RegexConfiguration(true, 0, false)), 1, 1, true },
        { DATASET_1, SequencesGroupDataset.of(SEQUENCES_1), new RegexHeaderMatcher("^C$", HeaderTarget.NAME, new RegexConfiguration(true, 0, false)), 1, 1, false },
        { DATASET_2, SequencesGroupDataset.of(SEQUENCES_3), new RegexHeaderMatcher("(C)_.*", HeaderTarget.NAME, new RegexConfiguration(true, 1, false)), 2, 2, true }
      }
    );
  }

  private SequencesGroupDataset dataset;
  private SequencesGroupDataset expectedDataset;
  private HeaderCountFilteringSequencesGroupDatasetTransformation transformation;

  public HeaderCountFilteringSequencesGroupDatasetTransformationTest(
    SequencesGroupDataset dataset, SequencesGroupDataset expectedDataset, HeaderMatcher matcher, int min, int max,
    boolean include
  ) {
    this.dataset = dataset;
    this.expectedDataset = expectedDataset;
    this.transformation = new HeaderCountFilteringSequencesGroupDatasetTransformation(matcher, min, max, include);
  }

  @Test
  public void filterBySequenceLengthTransformationTest() {
    SequencesGroupDataset transformed = transformation.transform(this.dataset);

    assertThat(
      transformed,
      EqualSequencesGroupDatasetMatcher.hasEqualSequenceGroups(expectedDataset)
    );
  }
}
