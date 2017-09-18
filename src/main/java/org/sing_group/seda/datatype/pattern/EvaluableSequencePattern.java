package org.sing_group.seda.datatype.pattern;

public interface EvaluableSequencePattern {

  public enum GroupMode {
    ANY, ALL
  };

  public boolean eval(String sequence);
}
