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
package org.sing_group.seda.core.operations;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.datatype.Sequence.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector.TieBreakOption;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class SequenceIsoformSelectorTest {

	  @Parameters()
	  public static Collection<Object[]> parameters() {
	    return Arrays.asList(
	      new Object[][] {
	        { asList(
	        	of("A", "Length 11", 	"CCCCCCCCCCC", emptyMap()),
	        	of("B", "Length 9", 	"CCCCCCCCC", emptyMap())
	        ),
	        	of("B", "Length 9", 	"CCCCCCCCC", emptyMap()),
	        	9, TieBreakOption.SHORTEST
	        },
	        { asList(
	        	of("A", "Length 11", 	"CCCCCCCCCCC", emptyMap()),
	        	of("B", "Length 9", 	"CCCCCCCCC", emptyMap())
	        ),
	        	of("B", "Length 9", 	"CCCCCCCCC", emptyMap()),
	        	10, TieBreakOption.SHORTEST
	        },
	        { asList(
	        		of("A", "Length 11", 	"CCCCCCCCCCC", emptyMap()),
	        		of("B", "Length 9", 	"CCCCCCCCC", emptyMap())
        		),
	        	of("A", "Length 11", 	"CCCCCCCCCCC", emptyMap()),
	        	10, TieBreakOption.LONGEST
	        },
	        { asList(
	        	of("A", "Length 11", 	"CCCCCCCCCCC", emptyMap()),
	        	of("B", "Length 9", 	"CCCCCCCCC", emptyMap())
	        ),
	        	of("A", "Length 11", 	"CCCCCCCCCCC", emptyMap()),
	        	11, TieBreakOption.SHORTEST
	        },
	      }
	    );
	  }

	private List<Sequence> isoforms;
	private Sequence selection;
	private int referenceSize;
	private TieBreakOption tieBreak;

	public SequenceIsoformSelectorTest(List<Sequence> isoforms, Sequence selection, int referenceSize, TieBreakOption tieBreak) {
		this.isoforms = isoforms;
		this.selection = selection;
		this.referenceSize = referenceSize;
		this.tieBreak = tieBreak;
	  }

	@Test
	public void testIsoforms() {
		Sequence actualSelection = new DefaultSequenceIsoformSelector(referenceSize, tieBreak).selectSequence(this.isoforms);
		assertEquals(this.selection, actualSelection);
	}
}
