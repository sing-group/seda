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
package org.sing_group.seda.core.operations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequencesGroupSeparator {
  public static final String GROUP_UNMATCHED_SEQUENCES = "sequences.group.separator.unmatched";

  private HeaderMatcher matcher;

  public SequencesGroupSeparator(HeaderMatcher matcher) {
    this.matcher = matcher;
  }

  public Map<String, List<Sequence>> separate(SequencesGroup group) {
    Map<String, List<Sequence>> groups = new HashMap<>();

    group.getSequences().forEach(s -> {
      String groupId = this.matcher.match(s).orElse(GROUP_UNMATCHED_SEQUENCES);
      groups.putIfAbsent(groupId, new LinkedList<>());
      groups.get(groupId).add(s);
    });

    return groups;
  }
}
