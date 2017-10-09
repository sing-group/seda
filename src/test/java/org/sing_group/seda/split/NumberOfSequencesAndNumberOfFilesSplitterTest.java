package org.sing_group.seda.split;

import static org.sing_group.seda.split.SplitTestData.SEQUENCES;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class NumberOfSequencesAndNumberOfFilesSplitterTest {

  @Parameters(name = "{index}: Split in {1} files with {2} sequences each")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { SEQUENCES, 1, 1 , false },
        { SEQUENCES, 2, 2 , false },
        { SEQUENCES, 3, 3 , false },
        { SEQUENCES, 4, 5 , false },
        { SEQUENCES, 5, 4 , false },
        { SEQUENCES, 2, 20, true }
      }
    );
  }

  private SequencesGroup input;
  private NumberOfSequencesAndNumberOfFilesSplitter splitter;
  private int expectedNumFiles;
  private int expectedGroupSize;

  public NumberOfSequencesAndNumberOfFilesSplitterTest(
    SequencesGroup input, int numFiles, int numSequences, boolean independentExtractions
  ) {
    this.input = input;
    this.splitter = new NumberOfSequencesAndNumberOfFilesSplitter(numFiles, numSequences, independentExtractions);
    this.expectedNumFiles = numFiles;
    this.expectedGroupSize = numSequences;
  }

  @Test
  public void fixedNumberOfSequencesSplitterTest() {
    List<SequencesGroup> result = splitter.split(input);

    Assert.assertEquals(this.expectedNumFiles, result.size());

    for (int i = 0; i < result.size(); i++) {
      Assert.assertEquals(this.expectedGroupSize, result.get(i).getSequenceCount());
    }
  }
}
