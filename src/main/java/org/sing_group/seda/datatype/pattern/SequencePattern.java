package org.sing_group.seda.datatype.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequencePattern implements EvaluableSequencePattern {

  private Pattern pattern;
  private boolean containsRegex;
  private int requiredNumberOfMatches;

  public SequencePattern(String regex, int requiredNumberOfMatches) {
    this(regex, requiredNumberOfMatches, true);
  }

  public SequencePattern(String regex, int requiredNumberOfMatches, boolean containsRegex) {
    this.pattern = Pattern.compile(regex);
    this.containsRegex = containsRegex;
    this.requiredNumberOfMatches = requiredNumberOfMatches;
  }

  @Override
  public boolean eval(String sequence) {
    int count = 0;
    Matcher matcher = this.pattern.matcher(sequence);
    while (matcher.find()) {
      count++;
    }

    boolean patternEvaluation = count >= requiredNumberOfMatches;

    return containsRegex ? patternEvaluation : !patternEvaluation;
  }

  @Override
  public String toString() {
    return (this.containsRegex == true ? "" : "NOT(") + this.pattern + (this.containsRegex == true ? "" : ")");
  }
}
