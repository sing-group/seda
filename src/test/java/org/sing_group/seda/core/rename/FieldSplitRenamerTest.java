package org.sing_group.seda.core.rename;

import static java.util.Arrays.asList;
import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Collection;
import java.util.HashSet;

import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.rename.FieldSplitRenamer.Mode;
import org.sing_group.seda.datatype.SequencesGroup;

public class FieldSplitRenamerTest extends AbstractRenamerTest {


  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.ALL, " ", "", Mode.KEEP, new HashSet<>(asList(0))), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.ALL, " ", " ", Mode.REMOVE, new HashSet<>(asList(0))), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("[gen", "= A] [Other = 1]"),
            newSequence("[gen", "= B] [Other = 2]"),
            newSequence("[gen", "= C] [Other = 3]")
          )
        },        
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.DESCRIPTION, "[", "[", Mode.KEEP, new HashSet<>(asList(0,1))), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[gen = A] "),
            newSequence("SequenceB", "[gen = B] "),
            newSequence("SequenceC", "[gen = C] ")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.DESCRIPTION, "[", "[", Mode.KEEP, new HashSet<>(asList(0,2))), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[Other = 1]"),
            newSequence("SequenceB", "[Other = 2]"),
            newSequence("SequenceC", "[Other = 3]")
          )
        }
      }
    );
  }

  public FieldSplitRenamerTest(FieldSplitRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    super(renamer, input, expected);
  }
}
