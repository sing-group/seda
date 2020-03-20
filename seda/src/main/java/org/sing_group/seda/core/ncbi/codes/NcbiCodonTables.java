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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class NcbiCodonTables {

  private static final String LIST = "list.txt";

  private static InputStream getResource(String resource) {
    return NcbiCodonTables.class.getResourceAsStream(resource);
  }

  private Map<Integer, String> tablesList;
  private Map<Integer, Map<String, String>> tables = new HashMap<>();

  public Map<Integer, String> listTables() {
    if (tablesList == null) {
      this.loadTables();
    }
    return tablesList;
  }

  private void loadTables() {
    this.tablesList = new HashMap<>();
    try (
      BufferedReader br =
        new BufferedReader(new InputStreamReader(getResource(LIST)))
    ) {

      br.lines().forEach(line -> {
        String[] split = line.split(Pattern.quote("."));
        this.tablesList.put(Integer.valueOf(split[0]), split[1].trim());
      });
    } catch (IOException e) {
      throw new RuntimeException("An error ocurred while reading the tables list");
    }
  }

  public Map<String, String> getCodonTable(Integer tableId) {
    if (!tables.containsKey(isValidTableId(tableId))) {
      this.loadTable(tableId);
    }
    return tables.get(tableId);
  }

  private Integer isValidTableId(Integer tableId) {
    if (!this.listTables().containsKey(tableId)) {
      throw new IllegalArgumentException(
        "The requested table ID (" + tableId + ") does not correspond to a known identifier"
      );
    }
    return tableId;
  }

  private void loadTable(Integer tableId) {
    Map<String, String> codonTable = new HashMap<>();
    try (
      BufferedReader br =
        new BufferedReader(new InputStreamReader(getResource("maps/" + tableId.toString())))
    ) {

      br.lines().forEach(line -> {
        String[] split = line.split(Pattern.quote(" "));
        codonTable.put(split[0], split[1]);
      });

      codonTable.put("---", "-");
    } catch (IOException e) {
      throw new RuntimeException("An error ocurred while reading the codon table");
    }

    this.tables.put(tableId, codonTable);
  }

  public Optional<Integer> findIdentifier(Map<String, String> standardCodonTable) {
    for (int id : this.listTables().keySet()) {
      if (standardCodonTable.equals(this.getCodonTable(id))) {
        return Optional.of(id);
      }
    }

    return Optional.empty();
  }
}
