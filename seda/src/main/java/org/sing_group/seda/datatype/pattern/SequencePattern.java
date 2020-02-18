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

  public boolean isContainsRegex() {
    return containsRegex;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public int getRequiredNumberOfMatches() {
    return requiredNumberOfMatches;
  }

  @Override
  public String toString() {
    return (this.containsRegex == true ? "" : "NOT(") + this.pattern + (this.containsRegex == true ? "" : ")")
      + " [CS = " + (this.caseSensitive? "yes" : "no") + "]";
  }
}
