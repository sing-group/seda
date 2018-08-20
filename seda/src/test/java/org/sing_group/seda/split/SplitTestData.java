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
package org.sing_group.seda.split;

import static java.util.Collections.emptyMap;
import static org.sing_group.seda.datatype.Sequence.of;

import java.util.Collections;
import java.util.Map;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SplitTestData {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();
  private static final Sequence[] SEQUENCES_ARRAY;

  static {
    SEQUENCES_ARRAY = new Sequence[20];
    for (int i = 0; i < 20; i++) {
      SEQUENCES_ARRAY[i] = of(Integer.toString(i), "", "A", PROPERTIES);
    }
  }

  public static final SequencesGroup SEQUENCES = SequencesGroup.of("Group", emptyMap(), SEQUENCES_ARRAY);
}
