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
package org.sing_group.seda.core.rename;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;

@Ignore
@RunWith(Parameterized.class)
public class AbstractRenamerTest {

  private DatatypeFactory factory;
  private HeaderRenamer renamer;
  private SequencesGroup input;
  private SequencesGroup expected;

  public AbstractRenamerTest(
    DatatypeFactory factory, HeaderRenamer renamer, SequencesGroup input, SequencesGroup expected
  ) {
    this.renamer = renamer;
    this.input = input;
    this.expected = expected;
    this.factory = factory;
  }

  @Test
  public void renamerTest() {
    SequencesGroup actual = this.renamer.rename(this.input, factory);
    assertEquals(expected, actual);
  }
}
