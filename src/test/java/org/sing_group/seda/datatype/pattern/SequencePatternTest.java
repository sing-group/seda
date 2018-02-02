package org.sing_group.seda.datatype.pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern.GroupMode;

public class SequencePatternTest {

  @Test
  public void testSingleSequencePattern() {
    SequencePattern pattern = new SequencePattern("[AB]CD", 1);

    assertTrue(pattern.eval("NNACDNN"));
    assertTrue(pattern.eval("ACD"));
    assertTrue(pattern.eval("NNBCDNN"));
    assertTrue(pattern.eval("BCD"));
    assertFalse(pattern.eval("NNCCDNN"));
    assertFalse(pattern.eval("CCD"));
  }

  @Test
  public void testSingleSequencePatternWithMultipleMatches() {
    SequencePattern pattern = new SequencePattern("ACD", 2);

    assertFalse(pattern.eval("NNACDNN"));
    assertTrue(pattern.eval("NNACDNNACD"));
    assertTrue(pattern.eval("ACDNNACDNN"));
    assertTrue(pattern.eval("NNACDNNACDNNACD"));
  }

  @Test
  public void testSingleNotSequencePattern() {
    SequencePattern pattern = new SequencePattern("[AB]CD", 1, false);

    assertFalse(pattern.eval("NNACDNN"));
    assertFalse(pattern.eval("ACD"));
    assertFalse(pattern.eval("NNBCDNN"));
    assertFalse(pattern.eval("BCD"));
    assertTrue(pattern.eval("NNCCDNN"));
    assertTrue(pattern.eval("CCD"));
  }

  @Test
  public void testGroupSequencePatternAllMode() {
    SequencePattern pattern1 = new SequencePattern("[AB]CD", 1);
    SequencePattern pattern2 = new SequencePattern("[XY]Z", 1);
    SequencePatternGroup groupPattern = new SequencePatternGroup(GroupMode.ALL, pattern1, pattern2);

    assertFalse(groupPattern.eval("NNACDNN"));
    assertFalse(groupPattern.eval("NNXZNN"));
    assertTrue(groupPattern.eval("NNACDNNXZNN"));
    assertTrue(groupPattern.eval("NNBCDNNYZNN"));
  }

  @Test
  public void testGroupSequencePatternAnyMode() {
    SequencePattern pattern1 = new SequencePattern("[AB]CD", 1);
    SequencePattern pattern2 = new SequencePattern("[XY]Z", 1);
    SequencePatternGroup groupPattern = new SequencePatternGroup(GroupMode.ANY, pattern1, pattern2);

    assertFalse(groupPattern.eval("NNCDNZN"));
    assertTrue(groupPattern.eval("NNACDNN"));
    assertTrue(groupPattern.eval("NNXZNN"));
    assertTrue(groupPattern.eval("NNACDNNXZNN"));
    assertTrue(groupPattern.eval("NNBCDNNYZNN"));
  }

  @Test
  public void testGroupOfGroupsPattern() {
    SequencePattern pattern1 = new SequencePattern("AB", 1);
    SequencePattern pattern2 = new SequencePattern("XY", 1);
    SequencePatternGroup groupPattern1 = new SequencePatternGroup(GroupMode.ANY, pattern1, pattern2);

    SequencePattern pattern3 = new SequencePattern("EF", 1);
    SequencePatternGroup groupPattern2 = new SequencePatternGroup(GroupMode.ALL, pattern3);

    SequencePatternGroup groupPattern3 = new SequencePatternGroup(GroupMode.ALL, groupPattern1, groupPattern2);
    assertFalse(groupPattern3.eval("NNEFN"));
    assertFalse(groupPattern3.eval("NNABN"));
    assertFalse(groupPattern3.eval("NNXYN"));
    assertTrue(groupPattern3.eval("NNABNEFN"));
    assertTrue(groupPattern3.eval("NNXYNEFN"));
    assertTrue(groupPattern3.eval("NNABNXYNEFN"));
  }

  @Test
  public void testGroupOfGroupsPatternWithNot() {
    SequencePattern pattern1 = new SequencePattern("AB", 1);
    SequencePattern pattern2 = new SequencePattern("XY", 1);
    SequencePatternGroup groupPattern1 = new SequencePatternGroup(GroupMode.ANY, pattern1, pattern2);

    SequencePattern pattern3 = new SequencePattern("EF", 1, false);
    SequencePatternGroup groupPattern2 = new SequencePatternGroup(GroupMode.ALL, pattern3);

    SequencePatternGroup groupPattern3 = new SequencePatternGroup(GroupMode.ALL, groupPattern1, groupPattern2);
    assertFalse(groupPattern3.eval("NNEFN"));
    assertTrue(groupPattern3.eval("NNABN"));
    assertTrue(groupPattern3.eval("NNXYN"));
    assertFalse(groupPattern3.eval("NNABNEFN"));
    assertFalse(groupPattern3.eval("NNXYNEFN"));
    assertFalse(groupPattern3.eval("NNABNXYNEFN"));
  }
}
