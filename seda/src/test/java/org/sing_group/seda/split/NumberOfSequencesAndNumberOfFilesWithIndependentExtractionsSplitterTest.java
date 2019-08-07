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
import org.sing_group.seda.core.split.NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitterTest {

  @Parameters(name = "{index}: Split in {1} files with {2} sequences each")
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { SEQUENCES, 2, 20 },
        { SEQUENCES, 4, 30 },
      }
    );
  }

  private SequencesGroup input;
  private NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter splitter;
  private int expectedNumFiles;
  private int expectedGroupSize;

  public NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitterTest(
    SequencesGroup input, int numFiles, int numSequences
  ) {
    this.input = input;
    this.splitter = new NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter(numFiles, numSequences);
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
