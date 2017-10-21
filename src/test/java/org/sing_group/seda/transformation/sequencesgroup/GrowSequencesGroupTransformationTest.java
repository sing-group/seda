package org.sing_group.seda.transformation.sequencesgroup;

import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class GrowSequencesGroupTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = Sequence.of("1", "", "ACT", PROPERTIES);
  private static final Sequence SEQ_2 = Sequence.of("2", "", "CTAC", PROPERTIES);
  private static final Sequence SEQ_3 = Sequence.of("3", "", "ACCCCCCTGAC", PROPERTIES);
  private static final Sequence SEQ_4 = Sequence.of("4", "", "TGACGGGGGGGGAAA", PROPERTIES);

  private static final SequencesGroup SEQUENCES =
    SequencesGroup.of("Group", SEQ_1, SEQ_2, SEQ_3, SEQ_4);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          new GrowSequencesGroupTransformation(FACTORY, 2),
          SEQUENCES,
          Arrays.asList("CTACT", "ACCCCCCTGACGGGGGGGGAAA")
        },
        {
          new GrowSequencesGroupTransformation(FACTORY, 3),
          SEQUENCES,
          Arrays.asList("ACT", "CTAC", "ACCCCCCTGACGGGGGGGGAAA")
        },
        {
          new GrowSequencesGroupTransformation(FACTORY, 4),
          SEQUENCES,
          Arrays.asList("ACT", "CTAC", "ACCCCCCTGACGGGGGGGGAAA")
        },
        {
          new GrowSequencesGroupTransformation(FACTORY, 5),
          SEQUENCES,
          Arrays.asList("ACT", "CTAC", "ACCCCCCTGAC", "TGACGGGGGGGGAAA")
        }
      }
    );
  }

  private GrowSequencesGroupTransformation transformation;
  private SequencesGroup input;
  private Set<String> expectedChains;

  public GrowSequencesGroupTransformationTest(
    GrowSequencesGroupTransformation transformation, SequencesGroup input, List<String> expectedChains
  ) {
    this.transformation = transformation;
    this.input = input;
    this.expectedChains = new HashSet<>(expectedChains);
  }

  @Test
  public void test() {
    SequencesGroup actualResult = this.transformation.transform(this.input);

    final Set<String> actualChains = actualResult.getSequences().map(Sequence::getChain).collect(Collectors.toSet());

    Assert.assertEquals(expectedChains, actualChains);
  }
}
