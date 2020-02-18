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
package org.sing_group.seda.core.split;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;

public abstract class AbstractSequencesGroupSplitter implements SequencesGroupSplitter {
  private SequencesSort sequencesSort;
  private final SequencesGroupBuilder builder;

  public AbstractSequencesGroupSplitter(SequencesSort sequencesSort, DatatypeFactory factory) {
    this.sequencesSort = sequencesSort;
    this.builder = factory::newSequencesGroup;
  }

  protected List<Sequence> getInputSequencesGroup(SequencesGroup group) {
    return sequencesSort.sort(group.getSequences().collect(toList()));
  }

  protected SequencesGroup createGroup(String name, Map<String, Object> properties, List<Sequence> sequences) {
    return builder.of(name, properties, sequences);
  }
}
