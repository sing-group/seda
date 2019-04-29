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
package org.sing_group.seda.comparator;

import java.util.Comparator;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;

public enum SequenceComparator {
  LENGTH("Length", LengthComparator::new),
  ALPHABPETICAL("Alphabetical", AlphabeticalComparator::new);
  
  private String description;
  private SequenceComparatorBuilder builder;

  SequenceComparator(String description, SequenceComparatorBuilder builder) {
    this.description = description;
    this.builder = builder;
  }
  
  @Override
  public String toString() {
    return description;
  }
  
  public Comparator<Sequence> getComparator(SequenceTarget target) {
    return this.builder.of(target);
  }
}
