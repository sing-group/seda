package org.sing_group.seda.transformation.sequence;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class UndoAlignmentSequenceTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence SEQ_1 = Sequence.of("Sequence1", "", "ACTG-", PROPERTIES);
  private static final Sequence SEQ_2 = Sequence.of("Sequence1", "", "ACTG------", PROPERTIES);
  private static final Sequence SEQ_3 = Sequence.of("Sequence1", "", "------ACTG", PROPERTIES);
  private static final Sequence SEQ_4 = Sequence.of("Sequence1", "", "-----AC--TG------", PROPERTIES);

  @Parameters(name = "{index}: sequence target = {1}")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { SEQ_1, "ACTG" },
        { SEQ_2, "ACTG" },
        { SEQ_3, "ACTG" },
        { SEQ_4, "ACTG" },
      }
    );
  }

  private Sequence sequence;
  private String expectedChain;
  private UndoAlignmentSequenceTransformation transformation;

  public UndoAlignmentSequenceTransformationTest(
    Sequence sequence, String expectedChain
  ) {
    this.sequence = sequence;
    this.expectedChain = expectedChain;
    this.transformation = new UndoAlignmentSequenceTransformation();
  }

  @Test
  public void undoAlignmentSequenceTransformationTest() {
      Sequence transformed = transformation.transform(this.sequence);
      Assert.assertEquals(this.expectedChain, transformed.getChain());
  }
}
