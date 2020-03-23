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
package org.sing_group.seda.core.trim;

import static org.sing_group.seda.TestUtils.sequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class SequenceAlignmentGapsBoundariesTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          sequence("ACTG"), 0, 0
        },
        {
          sequence("-ACTG"), 1, 0
        },
        {
          sequence("ACTG-"), 0, 1
        },
        {
          sequence("-ACTG-"), 1, 1
        },
        {
          sequence("AC-TG"), 0, 0
        },
        {
          sequence("-AC-TG"), 1, 0
        },
        {
          sequence("AC-TG-"), 0, 1
        },
        {
          sequence("-AC-TG-"), 1, 1
        },
        {
          sequence("--ACTG"), 2, 0
        },
        {
          sequence("ACTG--"), 0, 2
        },
        {
          sequence("--ACTG--"), 2, 2
        },
        {
          sequence("AC--TG"), 0, 0
        },
        {
          sequence("--AC--TG"), 2, 0
        },
        {
          sequence("AC--TG--"), 0, 2
        },
        {
          sequence("--AC--TG--"), 2, 2
        }
      }
    );
  }

  private Sequence sequence;
  private int leading;
  private int trailing;

  public SequenceAlignmentGapsBoundariesTest(
    Sequence sequence, int start, int end
  ) {
    this.sequence = sequence;
    this.leading = start;
    this.trailing = end;
  }

  @Test
  public void test() {
    SequenceAlignmentGapsBoundaries boundaries = new SequenceAlignmentGapsBoundaries(sequence);
    Assert.assertEquals(this.leading, boundaries.leadingBases());
    Assert.assertEquals(this.trailing, boundaries.trailingBases());
  }
}
