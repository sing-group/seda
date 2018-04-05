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
package org.sing_group.seda.core.filtering;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sing_group.seda.datatype.Sequence;

public class StringHeaderMatcher implements HeaderMatcher {
  private Pattern pattern;

  public StringHeaderMatcher(String string, boolean regex, boolean caseSensitive) {
    String effectiveString = regex ? string : Pattern.quote(string);

    if (caseSensitive) {
      this.pattern = Pattern.compile(effectiveString);
    } else {
      this.pattern = Pattern.compile(effectiveString, Pattern.CASE_INSENSITIVE);
    }
  }

  @Override
  public Optional<String> match(Sequence sequence) {
    Matcher matcher = this.pattern.matcher(sequence.getName() + " " + sequence.getDescription());

    if (!matcher.find()) {

      return Optional.empty();
    }
    return Optional.of(matcher.group(0));
  }
}