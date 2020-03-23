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

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class StringTrimmerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          "ACTG", 0, 0, "ACTG"
        },
        {
          "ACTG", 0, 1, "ACT"
        },
        {
          "ACTG", 1, 0, "CTG"
        },
        {
          "ACTG", 1, 1, "CT"
        },
        {
          "ACTG", 0, 2, "AC"
        },
        {
          "ACTG", 2, 0, "TG"
        },
        {
          "ACTG", 2, 2, ""
        },
        {
          "ACTG", 2, 3, ""
        },
        {
          "ACTG", 3, 2, ""
        },
        {
          "ACTG", 4, 0, ""
        },
        {
          "ACTG", 0, 4, ""
        },
        {
          "ACTG", 4, 1, ""
        },
        {
          "ACTG", 1, 4, ""
        },
      }
    );
  }

  private String input;
  private int leading;
  private int trailing;
  private String output;

  public StringTrimmerTest(String input, int leading, int trailing, String output) {
    this.input = input;
    this.output = output;
    this.leading = leading;
    this.trailing = trailing;
  }

  @Test
  public void test() {
    StringTrimmer trimmer = new StringTrimmer(this.leading, this.trailing);
    Assert.assertEquals(this.output, trimmer.trim(this.input));
  }
}
