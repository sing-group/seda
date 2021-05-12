/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2021 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

public class DefaultSedaStringJoiner implements SedaStringJoiner {

  private final String delimiter;
  private final String prefix;
  private final String suffix;

  public DefaultSedaStringJoiner() {
    this(";", "[", "]");
  }

  public DefaultSedaStringJoiner(String delimiter, String prefix, String suffix) {
    this.delimiter = delimiter;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Override
  public String merge(String string, String part) {
    if(part.isEmpty()) {
      return string;
    } else if (!string.endsWith(suffix)) {
      return prefix + string + (string.isEmpty() ? "" : delimiter) + part + suffix;
    } else {
      return string.replaceAll(suffix + "$", delimiter + part + suffix);
    }
  }
}
