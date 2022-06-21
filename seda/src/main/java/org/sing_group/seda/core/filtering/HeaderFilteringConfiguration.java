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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.util.StringUtils;

@XmlRootElement
public class HeaderFilteringConfiguration {
  public enum Mode {
    KEEP, REMOVE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  public enum FilterType {
    SEQUENCE_NAME("Sequence name"),
    REGEX("Regular expression");

    private String description;

    FilterType(String description) {
      this.description = description;
    }

    @Override
    public String toString() {
      return description;
    }
  }

  public enum Level {
    SEQUENCE, FILE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  @XmlElement
  private boolean useFilter;

  @XmlElement
  private Mode mode;

  @XmlElement
  private Level level;

  @XmlElement
  private int min;

  @XmlElement
  private int max;

  @XmlElement
  private FilterType filterType;

  @XmlElement
  private String filterString;

  @XmlElement
  private boolean quotePattern;

  @XmlElement
  private int regexGroup;

  @XmlElement
  private boolean caseSensitive;

  @XmlElement
  private HeaderTarget headerTarget;

  public HeaderFilteringConfiguration() {
    this(false, Mode.KEEP, Level.SEQUENCE, 0, 0, FilterType.SEQUENCE_NAME, null, false, 0, false, HeaderTarget.ALL);
  }

  public HeaderFilteringConfiguration(boolean useFilter, Mode mode, Level level, int min, int max,
      FilterType filterType, String filterString, boolean quotePattern, int regexGroup, boolean caseSensitive,
      HeaderTarget headerTarget
  ) {
    this.useFilter = useFilter;
    this.mode = mode;
    this.level = level;
    this.min = min;
    this.max = max;
    this.filterType = filterType;
    this.filterString = filterString;
    this.quotePattern = quotePattern;
    this.regexGroup = regexGroup;
    this.caseSensitive = caseSensitive;
    this.headerTarget = headerTarget;
  }

  public Validation validate() {
    if (!useFilter) {
      return new DefaultValidation();
    }

    List<String> errors = new ArrayList<String>();

    if (!isValidRange()) {
      errors.add("The specified range is not valid (min > max)");
    }
    if (this.mode == null) {
      errors.add("The mode can't be null.");
    }
    if (this.level == null) {
      errors.add("The level can't be null.");
    }
    if (this.filterType == null) {
      errors.add("The filter type can't be null.");
    }
    if (this.headerTarget == null) {
      errors.add("The header target can't be null.");
    }
    if (this.filterType == null) {
      errors.add("The filter type can't be null.");
    }
    try {
      getHeaderMatcher();
    } catch (PatternSyntaxException e) {
      errors.add("The regular expression configuration is not valid.");
    }

    return new DefaultValidation(errors);
  }

  private boolean isValidRange() {
    return min <= max;
  }

  public boolean isUseFilter() {
    return useFilter;
  }

  public boolean isQuotePattern() {
    return quotePattern;
  }

  public int getRegexGroup() {
    return regexGroup;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  public String getFilterString() {
    return filterString;
  }

  public FilterType getFilterType() {
    return filterType;
  }

  public Level getLevel() {
    return level;
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public Mode getMode() {
    return mode;
  }

  public HeaderTarget getHeaderTarget() {
    return headerTarget;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (caseSensitive ? 1231 : 1237);
    result = prime * result + ((filterString == null) ? 0 : filterString.hashCode());
    result = prime * result + ((filterType == null) ? 0 : filterType.hashCode());
    result = prime * result + ((headerTarget == null) ? 0 : headerTarget.hashCode());
    result = prime * result + ((level == null) ? 0 : level.hashCode());
    result = prime * result + max;
    result = prime * result + min;
    result = prime * result + ((mode == null) ? 0 : mode.hashCode());
    result = prime * result + regexGroup;
    result = prime * result + (useFilter ? 1231 : 1237);
    result = prime * result + (quotePattern ? 1231 : 1237);
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
    HeaderFilteringConfiguration other = (HeaderFilteringConfiguration) obj;
    if (caseSensitive != other.caseSensitive)
      return false;
    if (filterString == null) {
      if (other.filterString != null)
        return false;
    } else if (!filterString.equals(other.filterString))
      return false;
    if (filterType != other.filterType)
      return false;
    if (headerTarget != other.headerTarget)
      return false;
    if (level != other.level)
      return false;
    if (max != other.max)
      return false;
    if (min != other.min)
      return false;
    if (mode != other.mode)
      return false;
    if (regexGroup != other.regexGroup)
      return false;
    if (useFilter != other.useFilter)
      return false;
    if (quotePattern != other.quotePattern)
      return false;
    return true;
  }

  public HeaderMatcher getHeaderMatcher() {
    if (getFilterType().equals(FilterType.SEQUENCE_NAME)) {
      return new SequenceNameHeaderMatcher();
    } else {
      RegexConfiguration regexConfiguration = new RegexConfiguration(
        isCaseSensitive(), getRegexGroup(), isQuotePattern()
      );

      return new RegexHeaderMatcher(getFilterString(), getHeaderTarget(), regexConfiguration);
    }
  }
}
