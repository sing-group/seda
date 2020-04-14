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

import static java.util.Collections.emptyMap;
import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer.Position;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class AddStringRenamerTest extends AbstractRenamerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.ALL, "ADD", "_", Position.PREFIX),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("ADD_SequenceA", "[gen = A] [Other = 1]"),
            newSequence("ADD_SequenceB", "[gen = B] [Other = 2]"),
            newSequence("ADD_SequenceC", "[gen = C] [Other = 3]")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.ALL, "ADD", "_", Position.SUFFIX),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[gen = A] [Other = 1]_ADD"),
            newSequence("SequenceB", "[gen = B] [Other = 2]_ADD"),
            newSequence("SequenceC", "[gen = C] [Other = 3]_ADD")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.NAME, "ADD", "_", Position.SUFFIX),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA_ADD", "[gen = A] [Other = 1]"),
            newSequence("SequenceB_ADD", "[gen = B] [Other = 2]"),
            newSequence("SequenceC_ADD", "[gen = C] [Other = 3]")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.DESCRIPTION, "ADD", "_", Position.PREFIX),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "ADD_[gen = A] [Other = 1]"),
            newSequence("SequenceB", "ADD_[gen = B] [Other = 2]"),
            newSequence("SequenceC", "ADD_[gen = C] [Other = 3]")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.ALL, "ADD", "", Position.OVERRIDE, true, "_"),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("ADD_1", ""),
            newSequence("ADD_2", ""),
            newSequence("ADD_3", "")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.NAME, "ADD", "", Position.OVERRIDE, true, "_"),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("ADD_1", "[gen = A] [Other = 1]"),
            newSequence("ADD_2", "[gen = B] [Other = 2]"),
            newSequence("ADD_3", "[gen = C] [Other = 3]")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.DESCRIPTION, "ADD", "", Position.OVERRIDE, true, "_"),
          GROUP,
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "ADD_1"),
            newSequence("SequenceB", "ADD_2"),
            newSequence("SequenceC", "ADD_3")
          )
        },
        {
          FACTORY,
          new AddStringHeaderRenamer(HeaderTarget.ALL, "SUFFIX", "_", Position.SUFFIX),
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", ""),
            newSequence("SequenceB", ""),
            newSequence("SequenceC", "")
          ),
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA_SUFFIX", ""),
            newSequence("SequenceB_SUFFIX", ""),
            newSequence("SequenceC_SUFFIX", "")
          )
        }
      }
    );
  }

  public AddStringRenamerTest(
    DatatypeFactory factory, AddStringHeaderRenamer renamer, SequencesGroup input, SequencesGroup expected
  ) {
    super(factory, renamer, input, expected);
  }
}
