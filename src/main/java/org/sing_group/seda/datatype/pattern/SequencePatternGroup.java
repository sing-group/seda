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
