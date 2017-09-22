package org.sing_group.seda.transformation.sequencesgroup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.DefaultSequence;
import org.sing_group.seda.datatype.DefaultSequencesGroup;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class SequencesGroupSortTransformationTest {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = new DefaultSequence(">D", "", "A", PROPERTIES);
  private static final Sequence SEQ_2 = new DefaultSequence(">C", "", "AC", PROPERTIES);
  private static final Sequence SEQ_3 = new DefaultSequence(">B", "", "ACTG", PROPERTIES);
  private static final Sequence SEQ_4 = new DefaultSequence(">A", "", "ACTGA", PROPERTIES);

  private static final SequencesGroup SEQUENCES =
    new DefaultSequencesGroup("Group", SEQ_2, SEQ_1, SEQ_4, SEQ_3);
  
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

    assertThat(sortedSequences.getSequences().collect(Collectors.toList())).containsExactly(expectedResult);
  }
}
