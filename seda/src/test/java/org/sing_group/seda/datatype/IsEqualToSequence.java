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
package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.collection.IsIterableContainingInOrder;

public class IsEqualToSequence extends TypeSafeMatcher<Sequence> {
  private final Sequence expected;
  
  private String lastCheckError;
  
  public IsEqualToSequence(Sequence sequence) {
    this.expected = sequence;
    this.lastCheckError = null;
  }

  @Override
  public void describeTo(Description description) {
    if (this.lastCheckError != null) {
      description.appendText(this.lastCheckError);
    }
  }

  @Override
  protected boolean matchesSafely(Sequence actual) {
    this.lastCheckError = null;
    
    if (!this.expected.getName().equals(actual.getName())) {
      this.lastCheckError = String.format("Expected sequence with name '%s' but was '%s'",
        this.expected.getName(), actual.getName()
      );
      
      return false;
    } else if (!this.expected.getDescription().equals(actual.getDescription())) {
      this.lastCheckError = String.format("Expected sequence with description '%s' but was '%s'",
        this.expected.getDescription(), actual.getDescription()
      );
      
      return false;
    } else if (!this.expected.getHeader().equals(actual.getHeader())) {
      this.lastCheckError = String.format("Expected sequence with header '%s' but was '%s'",
        this.expected.getHeader(), actual.getHeader()
      );
      
      return false;
    } else if (!this.expected.getChain().equals(actual.getChain())) {
      this.lastCheckError = String.format("Expected sequence with chain '%s' but was '%s'",
        this.expected.getChain(), actual.getChain()
      );
      
      return false;
    } else if (!this.expected.getProperties().equals(actual.getProperties())) {
      this.lastCheckError = "Sequences have different properties";
      
      return false;
    } else {
      return true;
    }
  }
  
  @Factory
  public static IsEqualToSequence equalToSequence(Sequence expected) {
    return new IsEqualToSequence(expected);
  }
  
  @Factory
  public static Matcher<Iterable<? extends Sequence>> containsSequencesInOrder(Sequence ... expected) {
    final IsEqualToSequence[] matchers = stream(expected)
      .map(IsEqualToSequence::equalToSequence)
    .toArray(IsEqualToSequence[]::new);
    
    return IsIterableContainingInOrder.contains(matchers);
  }
}
