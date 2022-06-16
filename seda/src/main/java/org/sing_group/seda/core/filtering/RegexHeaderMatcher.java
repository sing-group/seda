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
package org.sing_group.seda.core.filtering;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.regex.Pattern.compile;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.Sequence;

@XmlRootElement
public class RegexHeaderMatcher implements HeaderMatcher {
  @XmlElement
  private String string;
  @XmlElement
  private HeaderTarget headerTarget;
  @XmlElement
  private RegexConfiguration regexConfig;

  @XmlTransient
  private Pattern pattern;

  public RegexHeaderMatcher() {}

  public RegexHeaderMatcher(String string, HeaderTarget headerTarget, RegexConfiguration regexConfig) {
    this.string = string;
    this.regexConfig = regexConfig;
    this.headerTarget = headerTarget;
    this.getPattern();
  }

  @Override
  public Optional<String> match(Sequence sequence) {
    Matcher matcher = getPattern().matcher(this.headerTarget.partToMatch(sequence));

    try {
      if (matcher.find()) {
        return of(matcher.group(this.regexConfig.getGroup()));
      } else {
        return empty();
      }
    } catch (IndexOutOfBoundsException | IllegalStateException e) {
      return empty();
    }
  }

  private Pattern getPattern() {
    if (this.pattern == null) {
      String effectiveString = regexConfig.isQuotePattern() ? Pattern.quote(string) : string;

      if (regexConfig.isCaseSensitive()) {
        this.pattern = compile(effectiveString);
      } else {
        this.pattern = compile(effectiveString, Pattern.CASE_INSENSITIVE);
      }
    }

    return this.pattern;
  }

  public String getString() {
    return string;
  }

  public HeaderTarget getHeaderTarget() {
    return headerTarget;
  }

  public RegexConfiguration getRegexConfig() {
    return regexConfig;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((headerTarget == null) ? 0 : headerTarget.hashCode());
    result = prime * result + ((regexConfig == null) ? 0 : regexConfig.hashCode());
    result = prime * result + ((string == null) ? 0 : string.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RegexHeaderMatcher other = (RegexHeaderMatcher) obj;
    if (headerTarget != other.headerTarget)
      return false;
    if (regexConfig == null) {
      if (other.regexConfig != null)
        return false;
    } else if (!regexConfig.equals(other.regexConfig))
      return false;
    if (string == null) {
      if (other.string != null)
        return false;
    } else if (!string.equals(other.string))
      return false;
    return true;
  }
}
