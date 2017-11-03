/*
 * #%L
 * S2P Core
 * %%
 * Copyright (C) 2016 - 2017 José Luis Capelo Martínez, José Eduardo Araújo, Florentino Fdez-Riverola, Miguel
 * 			Reboiro-Jato, Hugo López-Fernández, and Daniel Glez-Peña
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
package org.sing_group.seda.matcher;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class ContainsSameSequencesMatcher extends TypeSafeDiagnosingMatcher<SequencesGroup> {
	private SequencesGroup expected;

	public ContainsSameSequencesMatcher(SequencesGroup expected) {
		this.expected = expected;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Sequences group with ").appendText(String.valueOf(this.expected.getSequenceCount() + " sequences."));
	}

	@Override
	protected boolean matchesSafely(SequencesGroup item, Description mismatchDescription) {
    if (item.getSequenceCount() != this.expected.getSequenceCount()) {
      mismatchDescription.appendText("Different number of Sequences");
      return false;
    }
    
    Set<Sequence> expectedSet = expected.getSequences().collect(Collectors.toSet());
    Set<Sequence> itemSet = item.getSequences().collect(Collectors.toSet());
    Iterator<Sequence> expectedSetIterator = expectedSet.iterator();
    Iterator<Sequence> itemSetIterator = itemSet.iterator();
    int count = 0;
    
    while (expectedSetIterator.hasNext() && itemSetIterator.hasNext()) {
      Sequence expectedSequence = expectedSetIterator.next();
      Sequence itemSequence = itemSetIterator.next();
      if (!Objects.equals(expectedSequence, itemSequence)) {
        mismatchDescription.appendText("Different Sequences at position " + (count++));
        mismatchDescription.appendText(". Expected: " + expectedSequence.toString());
        mismatchDescription.appendText(". Actual: " + itemSequence.toString());

        return false;
      }
    }


    return true;
  }

  @Factory
  public static ContainsSameSequencesMatcher containsSameSequencesThat(SequencesGroup expectedSequencesGroup) {
    return new ContainsSameSequencesMatcher(expectedSequencesGroup);
  }
}
