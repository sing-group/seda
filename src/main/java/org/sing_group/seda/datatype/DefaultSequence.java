package org.sing_group.seda.datatype;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public class DefaultSequence implements Sequence, Serializable {
  private static final long serialVersionUID = 1L;
  
  private final String name;
  private final String description;
  private final String chain;
  private Map<String, Object> properties;

  public DefaultSequence(String name, String description, String chain, Map<String, Object> properties) {
    this.name = name;
    this.description = description;
    this.chain = chain;
    this.properties = properties;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public String getChain() {
    return this.chain;
  }

  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Optional<T> getProperty(String key) {
    return Optional.ofNullable((T) this.getProperties().get(key));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((properties == null) ? 0 : properties.hashCode());
    result = prime * result + ((chain == null) ? 0 : chain.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    DefaultSequence other = (DefaultSequence) obj;
    if (properties == null) {
      if (other.properties != null)
        return false;
    } else if (!properties.equals(other.properties))
      return false;
    if (chain == null) {
      if (other.chain != null)
        return false;
    } else if (!chain.equals(other.chain))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }
}
