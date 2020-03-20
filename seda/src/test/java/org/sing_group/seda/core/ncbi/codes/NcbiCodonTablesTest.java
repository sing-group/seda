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
package org.sing_group.seda.core.ncbi.codes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.sing_group.seda.bio.SequenceUtils;

public class NcbiCodonTablesTest {

  @Test
  public void listTablesTest() {
    Map<Integer, String> tables = new NcbiCodonTables().listTables();
    Assert.assertEquals(25, tables.size());
    Assert.assertEquals("Standard", tables.get(1));
    Assert.assertEquals("Cephalodiscidae Mitochondrial UAA-Tyr", tables.get(33));
  }

  @Test
  public void loadStandardTableTest() {
    Assert.assertEquals(SequenceUtils.STANDARD_CODON_TABLE, new NcbiCodonTables().getCodonTable(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void loadInvalidIdentifierTableTest() {
    new NcbiCodonTables().getCodonTable(0);
  }

  @Test
  public void findCodonTableIdentifier() {
    Optional<Integer> id = new NcbiCodonTables().findIdentifier(SequenceUtils.STANDARD_CODON_TABLE);
    Assert.assertEquals(1, id.get().intValue());
  }

  @Test
  public void findCodonTableIdentifierNotFound() {
    Map<String, String> codonTable = new HashMap<>();
    codonTable.put("ABC", "D");
    Optional<Integer> id = new NcbiCodonTables().findIdentifier(codonTable);
    Assert.assertFalse(id.isPresent());
  }
}
