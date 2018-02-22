package org.sing_group.seda.core.filtering;

import org.sing_group.seda.util.StringUtils;

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
    STRING("String");

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

  private boolean useFilter;
  private Mode mode;
  private Level level;
  private int min;
  private int max;
  private FilterType filterType;
  private String filterString;
  private boolean useRegex;
  private boolean caseSensitive;

  public HeaderFilteringConfiguration() {
    this(false, null, null, 0, 0, null, null, false, false);
  }

  public HeaderFilteringConfiguration(
    boolean useFilter, Mode mode, Level level, int min, int max, FilterType filterType, String filterString,
    boolean useRegex, boolean caseSensitive
  ) {
    this.useFilter = useFilter;
    this.mode = mode;
    this.level = level;
    this.min = min;
    this.max = max;
    this.filterType = filterType;
    this.filterString = filterString;
    this.useRegex = useRegex;
    this.caseSensitive = caseSensitive;
  }

  public boolean isValidConfiguration() {
    return !useFilter || (isValidFilterType() && isValidRange());
  }

  private boolean isValidRange() {
    return min <= max;
  }

  private boolean isValidFilterType() {
    return filterType != null && (isValidFilterTypeConfiguration());
  }

  private boolean isValidFilterTypeConfiguration() {
    return filterType.equals(FilterType.SEQUENCE_NAME) || isValidStringConfiguration();
  }

  private boolean isValidStringConfiguration() {
    return this.filterString != null && !this.filterString.isEmpty();
  }

  public boolean isUseFilter() {
    return useFilter;
  }

  public boolean isUseRegex() {
    return useRegex;
  }

  public boolean isCaseSensitive() {
    return caseSensitive;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (caseSensitive ? 1231 : 1237);
    result = prime * result + ((filterString == null) ? 0 : filterString.hashCode());
    result = prime * result + ((filterType == null) ? 0 : filterType.hashCode());
    result = prime * result + ((level == null) ? 0 : level.hashCode());
    result = prime * result + max;
    result = prime * result + min;
    result = prime * result + ((mode == null) ? 0 : mode.hashCode());
    result = prime * result + (useFilter ? 1231 : 1237);
    result = prime * result + (useRegex ? 1231 : 1237);
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
    if (level != other.level)
      return false;
    if (max != other.max)
      return false;
    if (min != other.min)
      return false;
    if (mode != other.mode)
      return false;
    if (useFilter != other.useFilter)
      return false;
    if (useRegex != other.useRegex)
      return false;
    return true;
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
}
