/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.selection;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.TestUtils.sequenceLength;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import org.junit.Test;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequenceSelectionTest {

  private static final SequencesGroup SEQUENCES = of(
    "Group", emptyMap(), sequenceLength(10), sequenceLength(9), sequenceLength(11)
  );

  @Test
  public void testSelectLargest() {
    SequenceSelector selector = new LargestSequenceSelector();
    assertEquals(sequenceLength(11), selector.select(SEQUENCES));
  }

  @Test
  public void testSelectSmallest() {
    SequenceSelector selector = new SmallestSequenceSelector();
    assertEquals(sequenceLength(9), selector.select(SEQUENCES));
  }
}
