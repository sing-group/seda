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
import org.sing_group.seda.core.split.NumberOfSequencesAndNumberOfFilesSplitter;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class NumberOfSequencesAndNumberOfFilesSplitterTest {

  @Parameters(name = "{index}: Split in {1} files with {2} sequences each")
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        { sequencesGroup(20), 1, 1 },
        { sequencesGroup(20), 2, 2 },
        { sequencesGroup(20), 3, 3 },
        { sequencesGroup(20), 4, 5 },
        { sequencesGroup(20), 5, 4 },
      }
    );
  }

  private SequencesGroup input;
  private NumberOfSequencesAndNumberOfFilesSplitter splitter;
  private int expectedNumFiles;
  private int expectedGroupSize;

  public NumberOfSequencesAndNumberOfFilesSplitterTest(SequencesGroup input, int numFiles, int numSequences) {
    this.input = input;
    this.splitter = new NumberOfSequencesAndNumberOfFilesSplitter(numFiles, numSequences);
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
