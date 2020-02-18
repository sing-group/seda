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
package org.sing_group.seda.core.rename;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class SequenceHeadersJoinerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        {
          new SequenceHeadersJoiner(HeaderTarget.NAME, ", ", "(", ")"),
          "(SequenceA, SequenceB, SequenceC)",
          Arrays.asList(
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        {
          new EmptySequenceHeadersJoiner(),
          "",
          Arrays.asList(
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        }
      }
    );
  }

  private SequenceHeadersJoiner joiner;
  private String expected;
  private List<Sequence> sequences;

  public SequenceHeadersJoinerTest(SequenceHeadersJoiner joiner, String expected, List<Sequence> sequences) {
    this.joiner = joiner;
    this.expected = expected;
    this.sequences = sequences;
  }

  @Test
  public void renamerTest() {
    assertEquals(expected, joiner.join(sequences));
  }
}
