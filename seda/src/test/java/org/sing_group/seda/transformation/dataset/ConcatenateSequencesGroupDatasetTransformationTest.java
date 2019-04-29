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
package org.sing_group.seda.transformation.dataset;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;
import static org.sing_group.seda.datatype.SequencesGroupDataset.of;
import static org.sing_group.seda.matcher.EqualSequencesGroupDatasetMatcher.hasEqualSequenceGroups;

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
import org.sing_group.seda.datatype.SequencesGroupDataset;

@RunWith(Parameterized.class)
public class ConcatenateSequencesGroupDatasetTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final SequencesGroupDataset DATASET_1 = of(
      of("Group_1", emptyMap(), 
          of("Homo_Sapiens", "[Sequence 1.1]", "A", PROPERTIES), 
          of("Mus_Musculus", "[Sequence 1.2]", "T", PROPERTIES)
      ),
      of("Group_2", emptyMap(), 
          of("Homo_Sapiens", "[Sequence 2.1]", "A", PROPERTIES), 
          of("Mus_Musculus", "[Sequence 2.2]", "T", PROPERTIES)
      )
  );

  private static final SequencesGroupDataset DATASET_1_MERGED = of(
      of("merged_by_name", emptyMap(), 
          of("Mus_Musculus", "", "TT", PROPERTIES), 
          of("Homo_Sapiens", "", "AA", PROPERTIES)
      )
  );

  private static final SequencesGroupDataset DATASET_2 = of(
      of("Group_1", emptyMap(), 
          of("Homo_Sapiens_1", "[Sequence 1.1]", "A", PROPERTIES), 
          of("Mus_Musculus_1", "[Sequence 1.2]", "T", PROPERTIES),
          of("Gallus_Gallus_1", "[Sequence 1.3]", "C", PROPERTIES)
      ),
      of("Group_1", emptyMap(), 
          of("Homo_Sapiens_2", "[Sequence 2.1]", "C", PROPERTIES), 
          of("Mus_Musculus_2", "[Sequence 2.2]", "G", PROPERTIES),
          of("Gallus_Gallus_2", "[Sequence 3.3]", "T", PROPERTIES)
      )
   );

  private static final SequencesGroupDataset DATASET_2_MERGED = of(
      of("merged_by_regex_matcher", emptyMap(), 
          of("Gallus_Gallus", "", "CT", PROPERTIES),
          of("Mus_Musculus", "", "TG", PROPERTIES), 
          of("Homo_Sapiens", "", "AC", PROPERTIES)
      )
  );

  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        { DATASET_1, DATASET_1_MERGED, "merged_by_name", new SequenceNameHeaderMatcher() },
        { DATASET_2, DATASET_2_MERGED, "merged_by_regex_matcher", 
          new RegexHeaderMatcher("^[^_]*_[^_]*", HeaderTarget.NAME, new RegexConfiguration(true, 0, false)) 
        },
      }
    );
  }

  private SequencesGroupDataset dataset;
  private SequencesGroupDataset expectedDataset;
  private String mergeName;
  private HeaderMatcher headerMatcher;

  public ConcatenateSequencesGroupDatasetTransformationTest(
    SequencesGroupDataset group, SequencesGroupDataset expectedDataset, String mergeName, HeaderMatcher headerMatcher
  ) {
    this.dataset = group;
    this.expectedDataset = expectedDataset;
    this.mergeName = mergeName;
    this.headerMatcher = headerMatcher;
  }

  @Test
  public void translateSequences() {
    ConcatenateSequencesGroupDatasetTransformation transformer = new ConcatenateSequencesGroupDatasetTransformation(mergeName, headerMatcher);
    SequencesGroupDataset transformedDataset = transformer.transform(this.dataset);

    assertThat(transformedDataset, hasEqualSequenceGroups(expectedDataset));
  }
}
