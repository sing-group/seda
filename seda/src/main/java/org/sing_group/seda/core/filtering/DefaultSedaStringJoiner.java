package org.sing_group.seda.core.filtering;

public class DefaultSedaStringJoiner implements SedaStringJoiner {

  private final String delimiter;
  private final String prefix;
  private final String suffix;

  public DefaultSedaStringJoiner() {
    this(", ", "[", "]");
  }

  public DefaultSedaStringJoiner(String delimiter, String prefix, String suffix) {
    this.delimiter = delimiter;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public String merge(String string, String part) {
    if (!string.endsWith(suffix)) {
      return prefix + string + (string.isEmpty() ? "" : delimiter) + part + suffix;
    } else {
      return string.replaceAll(suffix + "$", delimiter + part + suffix);
    }
  }
}
