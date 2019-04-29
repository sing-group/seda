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
package org.sing_group.seda;

import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyMap;
import static java.util.Collections.nCopies;
import static java.util.stream.IntStream.rangeClosed;
import static org.sing_group.seda.datatype.Sequence.of;

import org.sing_group.seda.datatype.Sequence;

public class TestUtils {
  
  public static final Sequence TEST_SEQUENCE = of("Sequence", "[Test]", "ACTG", emptyMap());

  public static final Sequence sequence(String chain) {
    return of("Sequence", "", chain, emptyMap());
  }
  
  public static final Sequence sequenceLength(int length) {
    return of(valueOf(length), "", join("", nCopies(length, "A")), emptyMap());
  }

  public static final Sequence[] testSequences(int n) {
    return rangeClosed(1, n).mapToObj(i -> TEST_SEQUENCE).toArray(Sequence[]::new);
  }
}
