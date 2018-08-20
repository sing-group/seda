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
package org.sing_group.seda.transformation.sequencesgroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.HeaderMatcherCount;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;

public class HeaderCountFilteringSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
  private HeaderMatcher matcher;
  private int min;
  private int max;
  private boolean include;

  public HeaderCountFilteringSequencesGroupTransformation(HeaderMatcher matcher, int min, int max, boolean include) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), matcher, min, max, include);
  }

  public HeaderCountFilteringSequencesGroupTransformation(
    DatatypeFactory factory, HeaderMatcher matcher, int min, int max, boolean include
  ) {
    this.builder = factory::newSequencesGroup;
    this.matcher = matcher;
    this.min = min;
    this.max = max;
    this.include = include;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<Sequence> newSequences = new LinkedList<>();

    Map<String, Integer> counts = new HeaderMatcherCount(matcher).count(sequencesGroup);

    sequencesGroup.getSequences().forEach(s -> {
      Optional<String> match = matcher.match(s);
      if (match.isPresent()) {
        int count = counts.get(match.get());
        if (count >= min && count <= max) {
          if (include) {
            newSequences.add(s);
          }
        } else {
          if (!include) {
            newSequences.add(s);
          }
        }
      }
    });

    return builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), newSequences);
  }
}
