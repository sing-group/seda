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
package org.sing_group.seda.core.ncbi;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public class NcbiTaxonomyInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  private Map<NcbiTaxonomyFields, String> values;

  public NcbiTaxonomyInfo(Map<NcbiTaxonomyFields, String> values) {
    this.values = values;
  }

  public Map<NcbiTaxonomyFields, String> getValues() {
    return values;
  }

  public Optional<String> getValue(NcbiTaxonomyFields field) {
    return Optional.ofNullable(this.values.get(field));
  }
}