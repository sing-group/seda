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

import static java.util.Collections.emptyMap;
import static org.sing_group.seda.core.rename.RenameTestUtils.FACTORY;
import static org.sing_group.seda.core.rename.RenameTestUtils.GROUP;
import static org.sing_group.seda.core.rename.RenameTestUtils.newSequence;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.SequencesGroup;

public class WordReplaceRenamerTest extends AbstractRenamerTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.ALL, "INFO", false, Arrays.asList("gen", "Other")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[INFO = A] [INFO = 1]"),
            newSequence("SequenceB", "[INFO = B] [INFO = 2]"),
            newSequence("SequenceC", "[INFO = C] [INFO = 3]")
          )
        },
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.NAME, "INFO", false, Arrays.asList("gen", "Other")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[gen = A] [Other = 1]"),
            newSequence("SequenceB", "[gen = B] [Other = 2]"),
            newSequence("SequenceC", "[gen = C] [Other = 3]")
          )
        },
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.DESCRIPTION, "INFO", false, Arrays.asList("gen", "Other")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[INFO = A] [INFO = 1]"),
            newSequence("SequenceB", "[INFO = B] [INFO = 2]"),
            newSequence("SequenceC", "[INFO = C] [INFO = 3]")
          )
        },
        { 
          new WordReplaceRenamer(FACTORY, HeaderTarget.ALL, "X", true, Arrays.asList("[123]")), 
          GROUP, 
          FACTORY.newSequencesGroup(GROUP.getName(), emptyMap(),
            newSequence("SequenceA", "[gen = A] [Other = X]"),
            newSequence("SequenceB", "[gen = B] [Other = X]"),
            newSequence("SequenceC", "[gen = C] [Other = X]")
          )
        }
      }
    );
  }
  
  public WordReplaceRenamerTest(WordReplaceRenamer renamer, SequencesGroup input, SequencesGroup expected) {
    super(renamer, input, expected);
  }
}
