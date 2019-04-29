/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
public class NumberOfSequencesSplitterTest {

  @Parameters(name = "{index}: Split in files with {1} sequences each")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { SEQUENCES, 21, 1, new int[]{ 20 } },
        { SEQUENCES, 20, 1, new int[]{ 20 } },
        { SEQUENCES, 19, 2, new int[]{ 19, 1 } },
        { SEQUENCES, 18, 2, new int[]{ 18, 2 } },
        { SEQUENCES, 10, 2, new int[]{ 10, 10 } },
        { SEQUENCES, 9, 3, new int[]{ 9, 9, 2 } },
        { SEQUENCES, 5, 4, new int[]{ 5, 5, 5, 5 } },
        { SEQUENCES, 4, 5, new int[]{ 4, 4, 4, 4, 4 } }
      }
    );
  }

  private SequencesGroup input;
  private NumberOfSequencesSplitter splitter;
  private int expectedResultSize;
  private int[] expectedGroupSizes;

  public NumberOfSequencesSplitterTest(
    SequencesGroup input, int numSequences, int expectedResultSize, int[] expectedGroupSizes
  ) {
    this.input = input;
    this.splitter = new NumberOfSequencesSplitter(numSequences);
    this.expectedResultSize = expectedResultSize;
    this.expectedGroupSizes = expectedGroupSizes;
  }

  @Test
  public void fixedNumberOfSequencesSplitterTest() {
    List<SequencesGroup> result = splitter.split(input);

    Assert.assertEquals(this.expectedResultSize, result.size());

    for (int i = 0; i < result.size(); i++) {
      Assert.assertEquals(this.expectedGroupSizes[i], result.get(i).getSequenceCount());
    }
  }
}
