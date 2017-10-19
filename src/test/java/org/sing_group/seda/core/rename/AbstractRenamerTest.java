package org.sing_group.seda.core.rename;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sing_group.seda.datatype.SequencesGroup;

@Ignore
@RunWith(Parameterized.class)
public class AbstractRenamerTest {

  private HeaderRenamer renamer;
  private SequencesGroup input;
  private SequencesGroup expected;

  public AbstractRenamerTest(HeaderRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    this.renamer = renamer;
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void renamerTest() {
    SequencesGroup actual = this.renamer.rename(this.input);
    assertEquals(actual, expected);
  }
}
