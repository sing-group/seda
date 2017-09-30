package org.sing_group.seda.transformation.sequencesgroup;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.IsEqualToSequence;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class SequencesGroupSortTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = Sequence.of("D", "", "A", PROPERTIES);
  private static final Sequence SEQ_2 = Sequence.of("C", "", "AC", PROPERTIES);
  private static final Sequence SEQ_3 = Sequence.of("B", "", "ACTG", PROPERTIES);
  private static final Sequence SEQ_4 = Sequence.of("A", "", "ACTGA", PROPERTIES);

  private static final SequencesGroup SEQUENCES =
    SequencesGroup.of("Group", SEQ_2, SEQ_1, SEQ_4, SEQ_3);
  
  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, SequenceTarget.SEQUENCE, SequenceComparator.LENGTH, false, new Sequence[] {
            SEQ_1, SEQ_2, SEQ_3, SEQ_4
          }
        },
        {
          SEQUENCES, SequenceTarget.SEQUENCE, SequenceComparator.LENGTH, true, new Sequence[] {
            SEQ_4, SEQ_3, SEQ_2, SEQ_1
          }
        },
        {
          SEQUENCES, SequenceTarget.HEADER, SequenceComparator.ALPHABPETICAL, false, new Sequence[] {
            SEQ_4, SEQ_3, SEQ_2, SEQ_1
          }
        },
        {
          SEQUENCES, SequenceTarget.HEADER, SequenceComparator.ALPHABPETICAL, true, new Sequence[] {
            SEQ_1, SEQ_2, SEQ_3, SEQ_4
          }
        }        
      }
    );
  }

  private SequencesGroup group;
  private Comparator<Sequence> comparator;
  private boolean descending;
  private Sequence[] expectedResult;

  public SequencesGroupSortTransformationTest(
    SequencesGroup group, SequenceTarget target, SequenceComparator comparator, boolean descending,
    Sequence[] expectedResult
  ) {
    this.group = group;
    this.comparator = comparator.getComparator(target);
    this.descending = descending;
    this.expectedResult = expectedResult;
  }

  @Test
  public void sortBySequenceLength() {
    SequencesGroupTransformation transformer = new SequencesGroupSortTransformation(this.comparator, this.descending);
    SequencesGroup sortedSequences = transformer.transform(this.group);

    final List<Sequence> actual = sortedSequences.getSequences().collect(Collectors.toList());

    assertThat(actual, IsEqualToSequence.containsSequencesInOrder(expectedResult));
  }
}
