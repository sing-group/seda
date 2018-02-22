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
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.core.filtering.StringHeaderMatcher;
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

  private static final SequencesGroup SEQUENCES_1 = SequencesGroup.of("Group1", SA_1, SA_2, SB_1, SB_2);
  private static final SequencesGroup SEQUENCES_2 = SequencesGroup.of("Group2", SA_1, SA_2, SB_1, SB_2, SC_1);

  private static final SequencesGroupDataset DATASET_1 = SequencesGroupDataset.of(SEQUENCES_1, SEQUENCES_2);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { DATASET_1, DATASET_1, new SequenceNameHeaderMatcher(), 1, 2, true },
        { DATASET_1, SequencesGroupDataset.of(SEQUENCES_1), new SequenceNameHeaderMatcher(), 2, 2, true },
        { DATASET_1, SequencesGroupDataset.of(SEQUENCES_2), new StringHeaderMatcher("C", false, true), 1, 1, true },
        { DATASET_1, SequencesGroupDataset.of(SEQUENCES_1), new StringHeaderMatcher("C", false, true), 1, 1, false }
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
