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
package org.sing_group.seda.core.rename;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP_2;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.rename.FieldSplitRenamer.Mode;
import org.sing_group.seda.datatype.SequencesGroup;

public class FieldSplitRenamerTest extends AbstractRenamerTest {


  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(
      new Object[][] {
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.ALL, " ", "", Mode.KEEP, asList(0)), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.ALL, " ", " ", Mode.REMOVE, asList(0)), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("[gen", "= A] [Other = 1]"),
            newSequence("[gen", "= B] [Other = 2]"),
            newSequence("[gen", "= C] [Other = 3]")
          )
        },        
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.DESCRIPTION, "[", "[", Mode.KEEP, asList(0,1)), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[gen = A] "),
            newSequence("SequenceB", "[gen = B] "),
            newSequence("SequenceC", "[gen = C] ")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.DESCRIPTION, "[", "[", Mode.KEEP, asList(0,2)), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[Other = 1]"),
            newSequence("SequenceB", "[Other = 2]"),
            newSequence("SequenceC", "[Other = 3]")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.NAME, "_", "|", Mode.KEEP, asList(2,1,0)), 
          GROUP_2, 
          FACTORY.newSequencesGroup(GROUP_2.getName(), emptyMap(),
            newSequence("C|B|A", ""),
            newSequence("F|E|D", ""),
            newSequence("I|H|G", "")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.NAME, "_", "|", Mode.KEEP, asList(2,0)), 
          GROUP_2, 
          FACTORY.newSequencesGroup(GROUP_2.getName(), emptyMap(),
            newSequence("C|A", ""),
            newSequence("F|D", ""),
            newSequence("I|G", "")
          )
        },
        { 
          new FieldSplitRenamer(FACTORY, HeaderTarget.NAME, "_", "|", Mode.REMOVE, asList(2,0)), 
          GROUP_2, 
          FACTORY.newSequencesGroup(GROUP_2.getName(), emptyMap(),
            newSequence("B", ""),
            newSequence("E", ""),
            newSequence("H", "")
          )
        }
      }
    );
  }

  public FieldSplitRenamerTest(FieldSplitRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    super(renamer, input, expected);
  }
}
