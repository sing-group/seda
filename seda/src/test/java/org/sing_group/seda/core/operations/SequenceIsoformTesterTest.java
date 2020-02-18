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

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.datatype.Sequence.of;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;


@RunWith(Parameterized.class)
public class SequenceIsoformTesterTest {

	  @Parameters()
	  public static Collection<Object[]> parameters() {
	    return Arrays.asList(
	      new Object[][] {
	        { of("A", "", "AAAACCCCTTTTGGGG", emptyMap()),  of("B", "", "AAACCCCTTTTGGG", emptyMap()), 10, true},
	        { of("A", "", "AAAACCCCTTTTGGGG", emptyMap()),  of("B", "", "AAACCCCTTTTGGG", emptyMap()), 12, true},
	        { of("A", "", "AAAACCCCTTTTGGGG", emptyMap()),  of("B", "", "AAACCCCTTTTGGG", emptyMap()), 14, true},
	        { of("A", "", "AAAACCCCTTTTGGGG", emptyMap()),  of("B", "", "AAACCCCTTTTGGG", emptyMap()), 15, false},
	        { of("A", "", "AAAACCCCTTTTGGGG", emptyMap()),  of("B", "", "AAACCCCTTTTGGG", emptyMap()), 16, false},

	        { of("A", "", "ACTGACTGACTGACTG", emptyMap()),  of("B", "", "CCCCCCCCCCCCCCCC", emptyMap()), 15, false},
	        { of("A", "", "ACTGACTGACTGACTG", emptyMap()),  of("B", "", "CCCCCCCCCCCCCCCC", emptyMap()), 16, false},
	        { of("A", "", "ACTGACTGACTGACTG", emptyMap()),  of("B", "", "CCCCCCCCCCCCCCCC", emptyMap()), 17, false},
	      }
	    );
	  }

	private Sequence a;
	private Sequence b;
	private int wordLength;
	private boolean isoformTest;

	public SequenceIsoformTesterTest(Sequence a, Sequence b, int wordLength, boolean isoformTest) {
		this.a = a;
		this.b = b;
		this.wordLength = wordLength;
		this.isoformTest = isoformTest;
	  }

	@Test
	public void testIsoforms() {
		SequenceIsoformTester isoformTester = new SequenceIsoformTester(a, b);
		assertEquals(this.isoformTest, isoformTester.test(wordLength));
	}
}
