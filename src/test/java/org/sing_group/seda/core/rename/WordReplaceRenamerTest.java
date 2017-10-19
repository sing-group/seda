package org.sing_group.seda.core.rename;

import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.SequencesGroup;

public class WordReplaceRenamerTest extends AbstractRenamerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.ALL, "INFO", false, Arrays.asList("gen", "Other")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[INFO = A] [INFO = 1]"),
            newSequence("SequenceB", "[INFO = B] [INFO = 2]"),
            newSequence("SequenceC", "[INFO = C] [INFO = 3]")
          )
        },
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.NAME, "INFO", false, Arrays.asList("gen", "Other")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[gen = A] [Other = 1]"),
            newSequence("SequenceB", "[gen = B] [Other = 2]"),
            newSequence("SequenceC", "[gen = C] [Other = 3]")
          )
        },
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.DESCRIPTION, "INFO", false, Arrays.asList("gen", "Other")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[INFO = A] [INFO = 1]"),
            newSequence("SequenceB", "[INFO = B] [INFO = 2]"),
            newSequence("SequenceC", "[INFO = C] [INFO = 3]")
          )
        },
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.ALL, "X", true, Arrays.asList("[123]")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), 
            newSequence("SequenceA", "[gen = A] [Other = X]"),
            newSequence("SequenceB", "[gen = B] [Other = X]"),
            newSequence("SequenceC", "[gen = C] [Other = X]")
          )
        }
      }
    );
  }
  
  public WordReplaceRenamerTest(WordReplaceRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    super(renamer, input, expected);
  }
}
