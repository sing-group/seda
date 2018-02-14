package org.sing_group.seda.datatype.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequencePattern implements EvaluableSequencePattern {

  private Pattern pattern;
  private boolean containsRegex;
  private int requiredNumberOfMatches;
  private boolean caseSensitive;

  public SequencePattern(String regex, int requiredNumberOfMatches, boolean caseSensitive) {
    this(regex, requiredNumberOfMatches, caseSensitive, true);
  }

  public SequencePattern(String regex, int requiredNumberOfMatches, boolean caseSensitive, boolean containsRegex) {
    if (caseSensitive) {
      this.pattern = Pattern.compile(regex);
    } else {
      this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    this.containsRegex = containsRegex;
    this.requiredNumberOfMatches = requiredNumberOfMatches;
    this.caseSensitive = caseSensitive;
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
    return (this.containsRegex == true ? "" : "NOT(") + this.pattern + (this.containsRegex == true ? "" : ")")
      + " [CS = " + (this.caseSensitive? "yes" : "no") + "]";
  }
}
