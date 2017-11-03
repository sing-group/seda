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
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.Mode;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;

@RunWith(Parameterized.class)
public class RemoveRedundantSequencesTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence A1 = Sequence.of("A1", "Sequence A1", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence A2 = Sequence.of("A2", "Sequence A2", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence A3 = Sequence.of("A3", "Sequence A3", "AAACCCTTTGGG", PROPERTIES);
  private static final Sequence B = Sequence.of("B", "Sequence B", "CCCTTT", PROPERTIES);
  private static final Sequence C = Sequence.of("C", "Sequence C", "CTTTG", PROPERTIES);
  private static final Sequence D = Sequence.of("D", "Sequence D", "AGGGTTTCCCAAA", PROPERTIES);
  private static final Sequence E = Sequence.of("E", "Sequence E", "GGGTA", PROPERTIES);

  private static final SequencesGroup GROUP = SequencesGroup.of("Group", A1, A2, A3, B, C, D, E);
  private static final SequencesGroup WITHOUT_DUPLICATES = SequencesGroup.of("Group", D, A1, B, C, E);

  private static final Sequence A1_MERGED =
    Sequence.of("A1", "Sequence A1 [A2 Sequence A2] [A3 Sequence A3]", "AAACCCTTTGGG", PROPERTIES);
  private static final SequencesGroup WITHOUT_DUPLICATES_MERGED_HEADERS =
    SequencesGroup.of("Group", D, A1_MERGED, B, C, E);

  private static final SequencesGroup WITHOUT_CONTAINED = SequencesGroup.of("Group", D, A1, E);
  private static final Sequence A1_MERGED_2 =
    Sequence.of(
      "A1", "Sequence A1 [A2 Sequence A2] [A3 Sequence A3] [B Sequence B] [C Sequence C]", "AAACCCTTTGGG", PROPERTIES
    );
  private static final SequencesGroup WITHOUT_CONTAINED_MERGED_HEADERS = SequencesGroup.of("Group", D, A1_MERGED_2, E);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          configuration(Mode.EXACT_DUPLICATES, false), GROUP, WITHOUT_DUPLICATES
        },
        {
          configuration(Mode.EXACT_DUPLICATES, true), GROUP, WITHOUT_DUPLICATES_MERGED_HEADERS
        },
        {
          configuration(Mode.CONTAINED_SEQUENCES, false), GROUP, WITHOUT_CONTAINED
        },
        {
          configuration(Mode.CONTAINED_SEQUENCES, true), GROUP, WITHOUT_CONTAINED_MERGED_HEADERS
        }
      }
    );
  }

  private static RemoveRedundantSequencesTransformationConfiguration configuration(Mode mode, boolean mergeHeaders) {
    return new RemoveRedundantSequencesTransformationConfiguration(mode, mergeHeaders);
  }

  private RemoveRedundantSequencesTransformationConfiguration configuration;
  private SequencesGroup input;
  private SequencesGroup expected;


  public RemoveRedundantSequencesTransformationTest(
    RemoveRedundantSequencesTransformationConfiguration configuration, SequencesGroup input, SequencesGroup expected
  ) {
    this.configuration = configuration;
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void removeRedundantSequencesTest() {
    RemoveRedundantSequencesTransformation transformation =
      new RemoveRedundantSequencesTransformation(
        this.configuration
      );
    SequencesGroup transformed = transformation.transform(this.input);

    assertThat(
      transformed,
      ContainsSameSequencesMatcher.containsSameSequencesThat(expected)
    );
  }
}