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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.datatype.SequencesGroupDataset;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;
import static org.sing_group.seda.datatype.SequencesGroupDataset.of;
import static org.sing_group.seda.matcher.EqualSequencesGroupDatasetMatcher.hasEqualSequenceGroups;

@RunWith(Parameterized.class)
public class CompareSequencesGroupDatasetTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final SequencesGroupDataset DATASET_1 = of(
    of("Homo_Sapiens", emptyMap(),
      of("H1", "[Sequence 1.1]", "A", PROPERTIES),
      of("H2", "[Sequence 1.2]", "T", PROPERTIES),
      of("H3", "[Sequence 1.3]", "G", PROPERTIES)
    ),
    of("Mus_Musculus", emptyMap(),
      of("M1", "[Sequence 2.1]", "A", PROPERTIES),
      of("M2", "[Sequence 2.2]", "T", PROPERTIES),
      of("M3", "[Sequence 2.3]", "C", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_1_COMPARED = of(
    of("Homo_Sapiens_vs_Mus_Musculus_only_Homo_Sapiens.fasta", emptyMap(),
      of("H3", "[Sequence 1.3]", "G", PROPERTIES)
    ),
    of("Homo_Sapiens_vs_Mus_Musculus_only_Mus_Musculus.fasta", emptyMap(),
      of("M3", "[Sequence 2.3]", "C", PROPERTIES)
    ),
    of("Homo_Sapiens_vs_Mus_Musculus_both.fasta", emptyMap(),
      of("H1", "[Sequence 1.1]", "A", PROPERTIES),
      of("H2", "[Sequence 1.2]", "T", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_2 = of(
    of("Homo_Sapiens", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    ),
    of("Mus_Musculus", emptyMap(),
      of("M1", "[Sequence 2.1]", "A", PROPERTIES),
      of("M2", "[Sequence 2.2]", "T", PROPERTIES),
      of("M3", "[Sequence 2.3]", "C", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_2_COMPARED = of(
    of("Homo_Sapiens_vs_Mus_Musculus_only_Homo_Sapiens.fasta", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    ),
    of("Homo_Sapiens_vs_Mus_Musculus_only_Mus_Musculus.fasta", emptyMap(),
      of("M1", "[Sequence 2.1]", "A", PROPERTIES),
      of("M2", "[Sequence 2.2]", "T", PROPERTIES),
      of("M3", "[Sequence 2.3]", "C", PROPERTIES)
    ),
    of("Homo_Sapiens_vs_Mus_Musculus_both.fasta", emptyMap())
  );

  private static final SequencesGroupDataset DATASET_3 = of(
    of("Homo_Sapiens", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    ),
    of("Mus_Musculus", emptyMap(),
      of("M1", "[Sequence 2.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("M2", "[Sequence 2.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("M3", "[Sequence 2.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_3_COMPARED = of(
    of("Homo_Sapiens_vs_Mus_Musculus_only_Homo_Sapiens.fasta", emptyMap()),
    of("Homo_Sapiens_vs_Mus_Musculus_only_Mus_Musculus.fasta", emptyMap()),
    of("Homo_Sapiens_vs_Mus_Musculus_both.fasta", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_4 = of(
    of("Homo_Sapiens", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    ),
    of("Mus_Musculus", emptyMap(),
      of("M1", "[Sequence 2.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("M2", "[Sequence 2.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("M3", "[Sequence 2.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES),
      of("M4", "[Sequence 2.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_4_COMPARED = of(
    of("Homo_Sapiens_vs_Mus_Musculus_only_Homo_Sapiens.fasta", emptyMap()),
    of("Homo_Sapiens_vs_Mus_Musculus_only_Mus_Musculus.fasta", emptyMap()),
    of("Homo_Sapiens_vs_Mus_Musculus_both.fasta", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_5 = of(
    of("Homo_Sapiens", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES),
      of("H4", "[Sequence 1.4]", "TTTGCACGCAACACCGTCGTTGAGGACTCGAAAAATT", PROPERTIES)
    ),
    of("Mus_Musculus", emptyMap(),
      of("M1", "[Sequence 2.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("M2", "[Sequence 2.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("M3", "[Sequence 2.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    )
  );

  private static final SequencesGroupDataset DATASET_5_COMPARED = of(
    of("Homo_Sapiens_vs_Mus_Musculus_only_Homo_Sapiens.fasta", emptyMap(),
      of("H4", "[Sequence 1.4]", "TTTGCACGCAACACCGTCGTTGAGGACTCGAAAAATT", PROPERTIES)
    ),
    of("Homo_Sapiens_vs_Mus_Musculus_only_Mus_Musculus.fasta", emptyMap()),
    of("Homo_Sapiens_vs_Mus_Musculus_both.fasta", emptyMap(),
      of("H1", "[Sequence 1.1]", "ATGTCGACAGTCGATAAGGAAGAGCTGGTCCAGAAGG", PROPERTIES),
      of("H2", "[Sequence 1.2]", "AAAACAGCAGCTCGCCCGTGAGTACAGTGAGTACACG", PROPERTIES),
      of("H3", "[Sequence 1.3]", "GACGCACGCAACACCGTCGTTGAGGACTCGAAAAAAG", PROPERTIES)
    )
  );

  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][]{
        {DATASET_1, DATASET_1_COMPARED, new SequenceNameHeaderMatcher()},
        {DATASET_2, DATASET_2_COMPARED, new SequenceNameHeaderMatcher()},
        {DATASET_3, DATASET_3_COMPARED, new SequenceNameHeaderMatcher()},
        {DATASET_4, DATASET_4_COMPARED, new SequenceNameHeaderMatcher()},
        {DATASET_5, DATASET_5_COMPARED, new SequenceNameHeaderMatcher()}
      }
    );
  }

  private SequencesGroupDataset dataset;
  private SequencesGroupDataset expectedDataset;
  private HeaderMatcher headerMatcher;

  public CompareSequencesGroupDatasetTransformationTest(
    SequencesGroupDataset group, SequencesGroupDataset expectedDataset, HeaderMatcher headerMatcher
  ) {
    this.dataset = group;
    this.expectedDataset = expectedDataset;
    this.headerMatcher = headerMatcher;
  }

  @Test
  public void translateSequences() {
    CompareSequencesGroupDatasetTransformation transformer = new CompareSequencesGroupDatasetTransformation();
    SequencesGroupDataset transformedDataset = transformer.transform(this.dataset);

    assertThat(transformedDataset, hasEqualSequenceGroups(expectedDataset));
  }
}
