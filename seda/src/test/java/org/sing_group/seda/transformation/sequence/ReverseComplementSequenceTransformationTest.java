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
package org.sing_group.seda.transformation.sequence;

import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.TestUtils.sequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class ReverseComplementSequenceTransformationTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          sequence("-ACTG"), true, false, sequence("GTCA-")
        },
        {
          sequence("ATUGCYRSWKMBDHVN"), true, false, sequence("NVHDBMKWSRYCGUTA")
        },

        {
          sequence("-ACTG"), false, true, sequence("-TGAC")
        },
        {
          sequence("ATUGCYRSWKMBDHVN"), false, true, sequence("TAACGRYSWMKVHDBN")
        },

        {
          sequence("-ACTG"), true, true, sequence("CAGT-")
        },
        {
          sequence("ATUGCYRSWKMBDHVN"), true, true, sequence("NBDHVKMWSYRGCAAT")
        },
      }
    );
  }

  private Sequence sequence;
  private Sequence expectedSequence;
  private boolean reverse;
  private boolean complement;

  public ReverseComplementSequenceTransformationTest(
    Sequence sequence, boolean reverse, boolean complement, Sequence expectedSequence
  ) {
    this.sequence = sequence;
    this.expectedSequence = expectedSequence;
    this.reverse = reverse;
    this.complement = complement;
  }

  @Test
  public void removeStopCodonsTest() {
    Sequence transformed =
      new ReverseComplementSequenceTransformation(this.reverse, this.complement)
        .transform(this.sequence);

    assertEquals(this.expectedSequence, transformed);
  }
}