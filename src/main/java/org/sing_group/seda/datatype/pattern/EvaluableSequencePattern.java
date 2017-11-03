package org.sing_group.seda.datatype.pattern;

import org.sing_group.seda.util.StringUtils;

public interface EvaluableSequencePattern {

  public enum GroupMode {
    ANY, ALL;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  };

  public boolean eval(String sequence);
}
