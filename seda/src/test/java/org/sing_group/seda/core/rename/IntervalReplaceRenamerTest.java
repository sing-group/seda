/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.Collections.emptyMap;
import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.SequencesGroup;

public class IntervalReplaceRenamerTest extends AbstractRenamerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { 
          new IntervalReplaceRenamer(FACTORY, HeaderTarget.ALL, "[", "]", ""), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(), 
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        { 
          new IntervalReplaceRenamer(FACTORY, HeaderTarget.DESCRIPTION, "[", "]", ""), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        { 
          new IntervalReplaceRenamer(FACTORY, HeaderTarget.NAME, "[", "]", ""), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[gen = A] [Other = 1]"),
            newSequence("SequenceB", "[gen = B] [Other = 2]"),
            newSequence("SequenceC", "[gen = C] [Other = 3]")
          )
        }
      }
    );
  }

  public IntervalReplaceRenamerTest(HeaderRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    super(renamer, input, expected);
  }
}
