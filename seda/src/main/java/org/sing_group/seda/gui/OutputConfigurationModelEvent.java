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
package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;

public class OutputConfigurationModelEvent {
  public static enum OutputConfigurationModelEventType {
    OUTPUT_DIRECTORY_CHANGED,
    SPLIT_INTO_SUBDIRECTORIES_CHANGED,
    SUBDIRECTORIES_SIZE_CHANGED,
    IN_MEMORY_PROCESSING_ENABLED,
    WRITE_GZIP;
  }

  private final OutputConfigurationModelEventType type;
  private final Object newValue;
  
  public static OutputConfigurationModelEvent of(OutputConfigurationModelEventType type, Object value) {
    return new OutputConfigurationModelEvent(type, value);
  }
  
  protected OutputConfigurationModelEvent(OutputConfigurationModelEventType type, Object value) {
    this.type = requireNonNull(type);
    this.newValue = requireNonNull(value);
  }
  
  public OutputConfigurationModelEventType getType() {
    return type;
  }

  public Object getNewValue() {
    return newValue;
  }
}
