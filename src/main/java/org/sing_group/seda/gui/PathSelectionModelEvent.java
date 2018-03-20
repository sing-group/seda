/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import java.util.Optional;

public class PathSelectionModelEvent {
  public static enum FileSelectionEventType {
    ADD_AVAILABLE, ADD_SELECTED,
    REMOVE_AVAILABLE, REMOVE_SELECTED,
    CLEAR_AVAILABLE, CLEAR_SELECTED;
    
    public boolean shouldHavePath() {
      return this == ADD_AVAILABLE ||
        this == ADD_SELECTED ||
        this == REMOVE_AVAILABLE ||
        this == REMOVE_SELECTED;
    }
    
    public boolean isSelectedEvent() {
      return this == ADD_SELECTED ||
        this == REMOVE_SELECTED ||
        this == CLEAR_SELECTED;
    }
    
    public boolean isAvailableEvent() {
      return this == ADD_AVAILABLE||
        this == REMOVE_AVAILABLE ||
        this == CLEAR_AVAILABLE;
    }
  }

  private final FileSelectionEventType type;
  private final String path;
  private final int index;
  
  public static PathSelectionModelEvent of(FileSelectionEventType type, int lastIndex) {
    return new PathSelectionModelEvent(type, lastIndex);
  }
  
  public static PathSelectionModelEvent of(FileSelectionEventType type, String path, int index) {
    return new PathSelectionModelEvent(type, path, index);
  }
  
  protected PathSelectionModelEvent(FileSelectionEventType type, int lastIndex) {
    this(type, null, lastIndex);
  }

  protected PathSelectionModelEvent(FileSelectionEventType type, String path, int index) {
    requireNonNull(type);
    
    if (type.shouldHavePath() && path == null)
      throw new IllegalArgumentException("Event type requires non-null path value: " + type);
    
    if (index < 0)
      throw new IllegalArgumentException("Index should be greater or equals to 0");
    
    this.type = type;
    this.path = path;
    this.index = index;
  }
  
  public FileSelectionEventType getType() {
    return type;
  }

  public Optional<String> getPath() {
    return Optional.ofNullable(this.path);
  }
  
  public int getIndex() {
    return this.index;
  }
}
