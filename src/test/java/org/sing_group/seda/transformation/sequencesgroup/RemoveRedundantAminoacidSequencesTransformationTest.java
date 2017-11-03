package org.sing_group.seda.transformation.sequencesgroup;

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
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

@RunWith(Parameterized.class)
public class RemoveRedundantAminoacidSequencesTransformationTest {

  private static final SequenceTranslationConfiguration TRANSLATION_CONFIGURATION =
    new SequenceTranslationConfiguration(SequenceUtils.STANDARD_CODON_TABLE, 1); 
  
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence A1 = Sequence.of("A1", "Sequence A1", "CTCCTCCTACTACTG", PROPERTIES);
  private static final Sequence A2 = Sequence.of("A2", "Sequence A2", "TTGTTG", PROPERTIES);
  private static final Sequence A3 = Sequence.of("A3", "Sequence A3", "TTATTG", PROPERTIES);
  private static final Sequence B1 = Sequence.of("B1", "Sequence B1", "CAT", PROPERTIES);
  private static final Sequence B2 = Sequence.of("B2", "Sequence B2", "CAC", PROPERTIES);
  private static final Sequence C = Sequence.of("C", "Sequence C", "TGT", PROPERTIES);

  private static final SequencesGroup GROUP = SequencesGroup.of("Group", A1, A2, A3, B1, B2, C);
  private static final SequencesGroup WITHOUT_DUPLICATES = SequencesGroup.of("Group", A1, B1, C);
  private static final SequencesGroup WITHOUT_EXACT_DUPLICATES = SequencesGroup.of("Group", A1, A2, B1, C);

  private static final Sequence A1_MERGED =
    Sequence.of("A1", "Sequence A1 [A2 Sequence A2] [A3 Sequence A3]", "CTCCTCCTACTACTG", PROPERTIES);
  private static final Sequence B1_MERGED =
    Sequence.of("B1", "Sequence B1 [B2 Sequence B2]", "CAT", PROPERTIES);
  private static final SequencesGroup WITHOUT_DUPLICATES_MERGE_HEADERS =
    SequencesGroup.of("Group", A1_MERGED, B1_MERGED, C);

  private static final Sequence A2_MERGED =
    Sequence.of("A2", "Sequence A2 [A3 Sequence A3]", "TTGTTG", PROPERTIES);
  private static final SequencesGroup WITHOUT_EXACT_DUPLICATES_MERGE_HEADERS =
    SequencesGroup.of("Group", A1, A2_MERGED, B1_MERGED, C);

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
