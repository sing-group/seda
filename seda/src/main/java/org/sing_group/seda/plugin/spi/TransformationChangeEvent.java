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
package org.sing_group.seda.plugin.spi;

import java.io.Serializable;

public class TransformationChangeEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private final TransformationProvider provider;
  private final TransformationChangeType type;
  private final Object oldValue;
  private final Object newValue;
  
  public static TransformationChangeEvent of(
    TransformationProvider provider,
    TransformationChangeType type,
    Object oldValue,
    Object newValue
  ) {
    return new TransformationChangeEvent(provider, type, oldValue, newValue);
  }

  protected TransformationChangeEvent(
    TransformationProvider provider,
    TransformationChangeType type,
    Object oldValue,
    Object newValue
  ) {
    this.provider = provider;
    this.type = type;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  public TransformationProvider getProvider() {
    return this.provider;
  }

  public TransformationChangeType getType() {
    return this.type;
  }

  public Object getOldValue() {
    return this.oldValue;
  }

  public Object getNewValue() {
    return this.newValue;
  }
}
