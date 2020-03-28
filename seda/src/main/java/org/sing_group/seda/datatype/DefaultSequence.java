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
package org.sing_group.seda.datatype;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultSequence implements Sequence, Serializable {
  private static final long serialVersionUID = 1L;

  private final String name;
  private final String description;
  private final String chain;
  private final Map<String, Object> properties;

  public DefaultSequence(String name, String description, String chain, Map<String, Object> properties) {
    this.name = requireNonNull(name, "name can't be null");
    this.description = description == null ? "" : description;
    this.chain = requireNonNull(chain, "chain can't be null");
    this.properties = unmodifiableMap(new HashMap<>(requireNonNull(properties, "properties can't be null")));
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
  public String getHeader() {
    final StringBuilder header = new StringBuilder(">");
    header.append(this.name);
    
    if (this.description != null && !this.description.isEmpty()) {
      header.append(" ").append(this.description);
    }
    
    return header.toString();
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

  @Override
  public String toString() {
    return this.getHeader() + "[" + this.getChain() + "]";
  }
}
