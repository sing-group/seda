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

public class RegexConfiguration {
  private int group;
  private boolean quotePattern;
  private boolean caseSensitive;

  public RegexConfiguration(boolean caseSensitive, int group, boolean quotePattern) {
    this.caseSensitive = caseSensitive;
    this.group = group;
    this.quotePattern = quotePattern;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public int getGroup() {
    return group;
  }

  public boolean isQuotePattern() {
    return quotePattern;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (caseSensitive ? 1231 : 1237);
    result = prime * result + (quotePattern ? 1231 : 1237);
    result = prime * result + group;
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
    RegexConfiguration other = (RegexConfiguration) obj;
    if (caseSensitive != other.caseSensitive)
      return false;
    if (group != other.group)
      return false;
    if (quotePattern != other.quotePattern)
      return false;
    return true;
  }
}
