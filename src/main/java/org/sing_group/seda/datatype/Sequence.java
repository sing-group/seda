package org.sing_group.seda.datatype;

import java.util.Map;
import java.util.Optional;

public interface Sequence {

  public static final String PROPERTY_CHAIN_COLUMNS = "chain.columns";

  public static Sequence of(String name, String description, String chain, Map<String, Object> properties) {
    return DatatypeFactory.getDefaultDatatypeFactory().newSequence(name, description, chain, properties);
  }

  public String getName();

  public String getDescription();

  public String getChain();

  public String getHeader();

  public Map<String, Object> getProperties();

  public <T> Optional<T> getProperty(String key);

  public default int getLength() {
    return this.getChain().length();
  }
}
