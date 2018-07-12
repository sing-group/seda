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
package org.sing_group.seda.core.filtering;

import static java.util.Collections.emptyMap;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.datatype.Sequence.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.Sequence;


@RunWith(Parameterized.class)
public class RegexHeaderMatcherTest {

	private RegexHeaderMatcher headerMatcher;
	private Sequence sequence;
	private Optional<String> expectedResult;
	
	@Parameters()
	public static Collection<Object[]> parameters() {
		return Arrays.asList(
				new Object[][] {
					{
						new RegexHeaderMatcher("(.*)_[0-9]*", HeaderTarget.NAME, new RegexConfiguration(true, 1)),
						of("Homo_Sapiens_1", "", "ACTG", emptyMap()),
						of("Homo_Sapiens")
					},
					{
						new RegexHeaderMatcher("(.*)_[0-9]*", HeaderTarget.NAME, new RegexConfiguration(true, 0)),
						of("Homo_Sapiens_1", "", "ACTG", emptyMap()),
						of("Homo_Sapiens_1")
					}
				}
		);
	}

	public RegexHeaderMatcherTest(RegexHeaderMatcher headerMatcher, Sequence sequence, Optional<String> expectedResult) {
		this.headerMatcher = headerMatcher;
		this.sequence = sequence;
		this.expectedResult = expectedResult;
	}
	
	@Test
	public void test() {
		Optional<String> actualMatch = this.headerMatcher.match(this.sequence);
		assertEquals(this.expectedResult, actualMatch);
	}
}
