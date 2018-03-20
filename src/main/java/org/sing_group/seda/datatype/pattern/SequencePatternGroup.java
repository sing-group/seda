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
package org.sing_group.seda.datatype.pattern;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SequencePatternGroup implements EvaluableSequencePattern {
  private GroupMode mode;
  private List<EvaluableSequencePattern> patterns;

  public SequencePatternGroup(GroupMode mode, EvaluableSequencePattern... patterns) {
    this.mode = mode;
    this.patterns = Arrays.asList(patterns);
  }

  @Override
  public boolean eval(String sequence) {
    if (patterns.size() > 0) {
      boolean eval = patterns.get(0).eval(sequence);
      for (EvaluableSequencePattern pattern : patterns.subList(1, patterns.size())) {
        boolean patternEval = pattern.eval(sequence);
        if (mode.equals(GroupMode.ANY)) {
          eval = eval || patternEval;
        } else {
          eval = eval && patternEval;
        }
      }

      return eval;
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Pattern group [Combination mode:")
      .append(this.mode)
      .append("]: ")
      .append(Stream.of(patterns).map(Object::toString).collect(Collectors.joining(",")));
    return sb.toString();
  }
}
