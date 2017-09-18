package org.sing_group.seda.datatype.pattern;

import java.util.regex.Pattern;

public class SequencePattern implements EvaluableSequencePattern {

  private Pattern pattern;
  private boolean containsRegex;

  public SequencePattern(String regex) {
    this(regex, true);
  }

  public SequencePattern(String regex, boolean containsRegex) {
    this.pattern = Pattern.compile(regex);
    this.containsRegex = containsRegex;
  }

  @Override
  public boolean eval(String sequence) {
    boolean patternEvaluation = this.pattern.matcher(sequence).find();
    return containsRegex ? patternEvaluation : !patternEvaluation;
  }

  @Override
  public String toString() {
    return (this.containsRegex == true ? "" : "NOT(") + this.pattern + (this.containsRegex == true ? "" : ")");
  }
}
