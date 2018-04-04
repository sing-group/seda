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
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.matcher.EqualSequencesGroupDatasetMatcher;

@RunWith(Parameterized.class)
public class ConcatenateSequencesGroupDatasetTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = Sequence.of("Homo_Sapiens", "[Sequence 1]", "A", PROPERTIES);
  private static final Sequence SEQ_2 = Sequence.of("Homo_Sapiens", "[Sequence 2]", "A", PROPERTIES);
  private static final Sequence SEQ_3 = Sequence.of("Mus_Musculus", "[Sequence 3]", "T", PROPERTIES);
  private static final Sequence SEQ_4 = Sequence.of("Mus_Musculus", "[Sequence 4]", "T", PROPERTIES);

  private static final SequencesGroupDataset DATASET =
    SequencesGroupDataset.of(
      SequencesGroup.of("Group_1", SEQ_1, SEQ_3),
      SequencesGroup.of("Group_2", SEQ_2, SEQ_4)
     );

  private static final Sequence SEQ_NAME_MERGED_1 = Sequence.of("Mus_Musculus", "", "TT", PROPERTIES);
  private static final Sequence SEQ_NAME_MERGED_2 = Sequence.of("Homo_Sapiens", "", "AA", PROPERTIES);

  private static final SequencesGroupDataset DATASET_SEQ_NAME_MERGED =
    SequencesGroupDataset.of(
      SequencesGroup.of("merged_by_name", SEQ_NAME_MERGED_1, SEQ_NAME_MERGED_2)
    );

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { DATASET, DATASET_SEQ_NAME_MERGED, "merged_by_name", HeaderTarget.NAME }
      }
    );
  }

  private SequencesGroupDataset dataset;
  private SequencesGroupDataset expectedDataset;
  private String mergeName;
  private HeaderTarget headerTarget;

  public ConcatenateSequencesGroupDatasetTransformationTest(
    SequencesGroupDataset group, SequencesGroupDataset expectedDataset, String mergeName, HeaderTarget headerTarget
  ) {
    this.dataset = group;
    this.expectedDataset = expectedDataset;
    this.mergeName = mergeName;
    this.headerTarget = headerTarget;
  }

  @Test
  public void translateSequences() {
    ConcatenateSequencesGroupDatasetTransformation transformer = new ConcatenateSequencesGroupDatasetTransformation(mergeName, headerTarget);
    SequencesGroupDataset transformedDataset = transformer.transform(this.dataset);

    assertThat(
      transformedDataset,
      EqualSequencesGroupDatasetMatcher.hasEqualSequenceGroups(expectedDataset)
    );
  }
}
