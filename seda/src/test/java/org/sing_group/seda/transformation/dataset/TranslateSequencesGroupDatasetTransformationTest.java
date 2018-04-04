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
import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.matcher.EqualSequencesGroupDatasetMatcher;

@RunWith(Parameterized.class)
public class TranslateSequencesGroupDatasetTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ = Sequence.of("D", "", "CTCATTCCC", PROPERTIES);
  private static final Sequence SEQ_FRAME_1 = Sequence.of("D", "", "LIP", PROPERTIES);
  private static final Sequence SEQ_FRAME_2 = Sequence.of("D", "", "SF", PROPERTIES);
  private static final Sequence SEQ_FRAME_3 = Sequence.of("D", "", "HS", PROPERTIES);
  private static final Sequence SEQ_REVERSED_FRAME_1 = Sequence.of("D", "", "GNE", PROPERTIES);
  private static final Sequence SEQ_REVERSED_FRAME_2 = Sequence.of("D", "", "GM", PROPERTIES);
  private static final Sequence SEQ_REVERSED_FRAME_3 = Sequence.of("D", "", "E" + SequenceUtils.STOP_CODON, PROPERTIES);

  private static final SequencesGroupDataset DATASET =
    SequencesGroupDataset.of(SequencesGroup.of("Group", SEQ));
  private static final SequencesGroupDataset DATASET_TRANSLATION =
    SequencesGroupDataset.of(
      SequencesGroup.of("Group_frame_1", SEQ_FRAME_1),
      SequencesGroup.of("Group_frame_2", SEQ_FRAME_2),
      SequencesGroup.of("Group_frame_3", SEQ_FRAME_3)
    );
  private static final SequencesGroupDataset DATASET_REVERSE_TRANSLATION =
    SequencesGroupDataset.of(
      SequencesGroup.of("Group_frame_1", SEQ_REVERSED_FRAME_1),
      SequencesGroup.of("Group_frame_2", SEQ_REVERSED_FRAME_2),
      SequencesGroup.of("Group_frame_3", SEQ_REVERSED_FRAME_3)
    );

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          DATASET, DATASET_TRANSLATION,
          new SequenceTranslationConfiguration(SequenceUtils.STANDARD_CODON_TABLE, false, 1, 2, 3)
        },
        {
          DATASET, DATASET_REVERSE_TRANSLATION,
          new SequenceTranslationConfiguration(SequenceUtils.STANDARD_CODON_TABLE, true, 1, 2, 3)
        }
      }
    );
  }

  private SequencesGroupDataset dataset;
  private SequencesGroupDataset expectedDataset;
  private SequenceTranslationConfiguration configuration;

  public TranslateSequencesGroupDatasetTransformationTest(
    SequencesGroupDataset group, SequencesGroupDataset expectedDataset, SequenceTranslationConfiguration configuration
  ) {
    this.dataset = group;
    this.expectedDataset = expectedDataset;
    this.configuration = configuration;
  }

  @Test
  public void translateSequences() {
    TranslateSequencesGroupDatasetTransformation transformer = new TranslateSequencesGroupDatasetTransformation(configuration);
    SequencesGroupDataset translatedDataset = transformer.transform(this.dataset);

    assertThat(
      translatedDataset,
      EqualSequencesGroupDatasetMatcher.hasEqualSequenceGroups(expectedDataset)
    );
  }
}
