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

import java.util.Collections;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class RenameTestUtils {
  public static final DatatypeFactory FACTORY = DatatypeFactory.getDefaultDatatypeFactory();

  public static final Sequence TEST_1 = newSequence("SequenceA", "[gen = A] [Other = 1]");
  public static final Sequence TEST_2 = newSequence("SequenceB", "[gen = B] [Other = 2]");
  public static final Sequence TEST_3 = newSequence("SequenceC", "[gen = C] [Other = 3]");

  public static final SequencesGroup GROUP = FACTORY.newSequencesGroup("Group", emptyMap(), TEST_1, TEST_2, TEST_3);

  public static Sequence newSequence(String name, String description) {
    return FACTORY.newSequence(name, description, "", Collections.emptyMap());
  }
}
