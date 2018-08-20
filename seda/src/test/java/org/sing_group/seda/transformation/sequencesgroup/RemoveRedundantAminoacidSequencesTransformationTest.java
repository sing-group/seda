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

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertThat;
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
import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

@RunWith(Parameterized.class)
public class RemoveRedundantAminoacidSequencesTransformationTest {

  private static final SequenceTranslationConfiguration TRANSLATION_CONFIGURATION = new SequenceTranslationConfiguration(
      SequenceUtils.STANDARD_CODON_TABLE, false, 1);

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence A1 = of("A1", "Sequence A1", "CTCCTCCTACTACTG", PROPERTIES);
  private static final Sequence A2 = of("A2", "Sequence A2", "TTGTTG", PROPERTIES);
  private static final Sequence A3 = of("A3", "Sequence A3", "TTATTG", PROPERTIES);
  private static final Sequence B1 = of("B1", "Sequence B1", "CAT", PROPERTIES);
  private static final Sequence B2 = of("B2", "Sequence B2", "CAC", PROPERTIES);
  private static final Sequence C = of("C", "Sequence C", "TGT", PROPERTIES);

  private static final SequencesGroup GROUP = of("Group", emptyMap(), A1, A2, A3, B1, B2, C);
  private static final SequencesGroup WITHOUT_DUPLICATES = of("Group", emptyMap(), A1, B1, C);
  private static final SequencesGroup WITHOUT_EXACT_DUPLICATES = of("Group", emptyMap(), A1, A2, B1, C);

  private static final Sequence A1_MERGED = 
      of("A1", "Sequence A1 [A2 Sequence A2] [A3 Sequence A3]", "CTCCTCCTACTACTG", PROPERTIES);
  private static final Sequence B1_MERGED = of("B1", "Sequence B1 [B2 Sequence B2]", "CAT", PROPERTIES);
  private static final SequencesGroup WITHOUT_DUPLICATES_MERGE_HEADERS = 
      of("Group", emptyMap(), A1_MERGED, B1_MERGED, C);

  private static final Sequence A2_MERGED = of("A2", "Sequence A2 [A3 Sequence A3]", "TTGTTG", PROPERTIES);
  private static final SequencesGroup WITHOUT_EXACT_DUPLICATES_MERGE_HEADERS = 
      of("Group", emptyMap(), A1, A2_MERGED, B1_MERGED, C);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          configuration(Mode.CONTAINED_SEQUENCES, false), TRANSLATION_CONFIGURATION, GROUP, WITHOUT_DUPLICATES
        },
        {
          configuration(Mode.EXACT_DUPLICATES, false), TRANSLATION_CONFIGURATION, GROUP, WITHOUT_EXACT_DUPLICATES
        },
        {
          configuration(Mode.CONTAINED_SEQUENCES, true), TRANSLATION_CONFIGURATION, GROUP, WITHOUT_DUPLICATES_MERGE_HEADERS
        },
        {
          configuration(Mode.EXACT_DUPLICATES, true), TRANSLATION_CONFIGURATION, GROUP, WITHOUT_EXACT_DUPLICATES_MERGE_HEADERS
        },
      }
    );
  }

  private static RemoveRedundantSequencesTransformationConfiguration configuration(Mode mode, boolean mergeHeaders) {
    return new RemoveRedundantSequencesTransformationConfiguration(mode, mergeHeaders);
  }

  private SequenceTranslationConfiguration translationConfiguration;
  private RemoveRedundantSequencesTransformationConfiguration configuration;
  private SequencesGroup input;
  private SequencesGroup expected;

  public RemoveRedundantAminoacidSequencesTransformationTest(
    RemoveRedundantSequencesTransformationConfiguration configuration,
    SequenceTranslationConfiguration translationConfiguration,
    SequencesGroup input, SequencesGroup expected
  ) {
    this.configuration = configuration;
    this.input = input;
    this.expected = expected;
    this.translationConfiguration = translationConfiguration;
  }

  @Test
  public void removeRedundantSequencesTest() {
    RemoveRedundantSequencesTransformation transformation =
      new RemoveRedundantSequencesTransformation(
        this.configuration,
        this.translationConfiguration
      );
    SequencesGroup transformed = transformation.transform(this.input);

    assertThat(
      transformed,
      ContainsSameSequencesMatcher.containsSameSequencesThat(expected)
    );
  }
}
