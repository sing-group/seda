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
import org.sing_group.seda.transformation.sequence.TrimSequenceTransformation;

@RunWith(Parameterized.class)
public class TrimSequenceTransformationTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          sequence("ACTG"), 0, 0, sequence("ACTG")
        },
        {
          sequence("ACTG"), 0, 1, sequence("ACT")
        },
        {
          sequence("ACTG"), 1, 0, sequence("CTG")
        },
        {
          sequence("ACTG"), 1, 1, sequence("CT")
        },
        {
          sequence("ACTG"), 0, 2, sequence("AC")
        },
        {
          sequence("ACTG"), 2, 0, sequence("TG")
        },
        {
          sequence("ACTG"), 2, 2, sequence("")
        },
        {
          sequence("ACTG"), 2, 3, sequence("")
        },
        {
          sequence("ACTG"), 3, 2, sequence("")
        },
        {
          sequence("ACTG"), 4, 0, sequence("")
        },
        {
          sequence("ACTG"), 0, 4, sequence("")
        },
        {
          sequence("ACTG"), 4, 1, sequence("")
        },
        {
          sequence("ACTG"), 1, 4, sequence("")
        },
      }
    );
  }

  private Sequence sequence;
  private int leading;
  private int trailing;
  private Sequence expectedSequence;

  public TrimSequenceTransformationTest(Sequence sequence, int leading, int trailing, Sequence expectedSequence) {
    this.sequence = sequence;
    this.leading = leading;
    this.trailing = trailing;
    this.expectedSequence = expectedSequence;
  }

  @Test
  public void removeStopCodonsTest() {
    Sequence transformed = new TrimSequenceTransformation(this.leading, this.trailing).transform(this.sequence);
    assertEquals(this.expectedSequence, transformed);
  }
}