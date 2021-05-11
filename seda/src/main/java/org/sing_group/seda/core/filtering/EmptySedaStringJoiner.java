package org.sing_group.seda.core.filtering;

public class EmptySedaStringJoiner implements SedaStringJoiner {

  @Override
  public String merge(String string, String part) {
    return "";
  }
}
