package org.sing_group.seda.core.rename;

import org.sing_group.seda.util.StringUtils;

public enum HeaderTarget {
  ALL, NAME, DESCRIPTION;

  @Override
  public String toString() {
    return StringUtils.capitalize(super.toString());
  }
}
