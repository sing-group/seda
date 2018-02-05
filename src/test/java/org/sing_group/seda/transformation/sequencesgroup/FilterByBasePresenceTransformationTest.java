package org.sing_group.seda.transformation.sequencesgroup;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
import org.sing_group.seda.transformation.TransformationException;

@RunWith(Parameterized.class)
public class FilterByBasePresenceTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence S1 = Sequence.of("1", "", "AAAAAACCCCCTTTGGGA", PROPERTIES);
  private static final Sequence S2 = Sequence.of("2", "", "AAAAAACCCTGGNNNNNN", PROPERTIES);

  private static final SequencesGroup SEQUENCES =
    SequencesGroup.of("Group", S1, S2);

  @Parameters()
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, SequencesGroup.of("Group", S1),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.35d, 0.40d, 'A')
          }
        },
        {
          SEQUENCES, SequencesGroup.of("Group", S1, S2),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.30d, 0.40d, 'A')
          }
        },
        {
          SEQUENCES, SequencesGroup.of("Group", S2),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.30d, 0.35d, 'A')
          }
        },
        {
          SEQUENCES, SequencesGroup.of("Group", S1),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.30d, 0.40d, 'A'),
            filter(0.20d, 0.30d, 'C')
          }
        },
        {
          SEQUENCES, SequencesGroup.of("Group", S2),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.30d, 0.40d, 'A'),
            filter(0.10d, 0.20d, 'C')
          }
        },
        {
          SEQUENCES, SequencesGroup.of("Group", S1),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.66d, 0.67d, 'A', 'C'),
          }
        },
        {
          SEQUENCES, SequencesGroup.of("Group"),
          new FilterByBasePresenceTransformation.BasePresence[] {
            filter(0.65d, 0.66d, 'A', 'C'),
          }
        }
      }
    );
  }

  private static FilterByBasePresenceTransformation.BasePresence filter(double d, double e, char...c) {
    return new FilterByBasePresenceTransformation.BasePresence(d, e, c);
  }

  private SequencesGroup group;
  private SequencesGroup expectedSequencesGroup;
  private FilterByBasePresenceTransformation.BasePresence[] basePresences;

  public FilterByBasePresenceTransformationTest(
    SequencesGroup group, SequencesGroup expectedSequencesGroup, FilterByBasePresenceTransformation.BasePresence...basePresences
  ) {
    this.group = group;
    this.expectedSequencesGroup = expectedSequencesGroup;
    this.basePresences = basePresences;
  }

  @Test
  public void filterBySequenceLengthTransformationTest() {
    try {
      SequencesGroup transformed = new FilterByBasePresenceTransformation(basePresences).transform(group);

      assertThat(
        transformed,
        ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
      );
    } catch (TransformationException e) {
      assertTrue(expectedSequencesGroup.getSequenceCount() == 0);
    }
  }
}
