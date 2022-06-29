/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.Arrays.asList;
import static org.sing_group.seda.split.SplitTestData.sequencesGroup;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.split.NumberOfSequencesSplitter;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class NumberOfSequencesSplitterTest {

  @Parameters(name = "{index}: Split in files with {1} sequences each")
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        { sequencesGroup(20), 21, 1, new int[]{ 20 } },
        { sequencesGroup(20), 20, 1, new int[]{ 20 } },
        { sequencesGroup(20), 19, 2, new int[]{ 19, 1 } },
        { sequencesGroup(20), 18, 2, new int[]{ 18, 2 } },
        { sequencesGroup(20), 10, 2, new int[]{ 10, 10 } },
        { sequencesGroup(20), 9, 3, new int[]{ 9, 9, 2 } },
        { sequencesGroup(20), 5, 4, new int[]{ 5, 5, 5, 5 } },
        { sequencesGroup(20), 4, 5, new int[]{ 4, 4, 4, 4, 4 } }
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
