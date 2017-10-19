package org.sing_group.seda.core.rename;

import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.SequencesGroup;

public class IntervalReplaceRenamerTest extends AbstractRenamerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { 
          new IntervalReplaceRenamer(FACTORY, HeaderTarget.ALL, "[", "]", ""), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        { 
          new IntervalReplaceRenamer(FACTORY, HeaderTarget.DESCRIPTION, "[", "]", ""), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        { 
          new IntervalReplaceRenamer(FACTORY, HeaderTarget.NAME, "[", "]", ""), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[gen = A] [Other = 1]"),
            newSequence("SequenceB", "[gen = B] [Other = 2]"),
            newSequence("SequenceC", "[gen = C] [Other = 3]")
          )
        }
      }
    );
  }

  public IntervalReplaceRenamerTest(HeaderRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    super(renamer, input, expected);
  }
}
