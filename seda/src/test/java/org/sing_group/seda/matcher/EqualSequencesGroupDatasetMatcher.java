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

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class EqualSequencesGroupDatasetMatcher extends TypeSafeDiagnosingMatcher<SequencesGroupDataset> {
	private SequencesGroupDataset expected;

	public EqualSequencesGroupDatasetMatcher(SequencesGroupDataset expected) {
		this.expected = expected;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Sequences group dataset with ").appendText(String.valueOf(this.expected.getSequencesGroupsCount() + " groups."));
	}

	@Override
	protected boolean matchesSafely(SequencesGroupDataset item, Description mismatchDescription) {
    if (item.getSequencesGroupsCount() != this.expected.getSequencesGroupsCount()) {
      mismatchDescription.appendText("Different number of SequenceGroup");
      return false;
    }
    
    SequencesGroup[] expectedSequenceGroups = expected.getSequencesGroups().toArray(size -> new SequencesGroup[size]);
    SequencesGroup[] itemSequenceGroups = item.getSequencesGroups().toArray(size -> new SequencesGroup[size]);
    for (int i = 0; i < item.getSequencesGroupsCount(); i++) {
      SequencesGroup expectedSg = expectedSequenceGroups[i];
      SequencesGroup itemSg = itemSequenceGroups[i];
      
      if (!Objects.equals(expectedSg, itemSg)) {
        mismatchDescription.appendText("Different SequenceGroup at position " + i);
        return false;
      }
    }
    
    return true;
	}

  @Factory
  public static EqualSequencesGroupDatasetMatcher hasEqualSequenceGroups(SequencesGroupDataset expectedGroupDataset) {
    return new EqualSequencesGroupDatasetMatcher(expectedGroupDataset);
  }
}
