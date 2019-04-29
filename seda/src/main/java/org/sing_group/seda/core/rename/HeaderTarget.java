/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.rename;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.util.StringUtils;

public enum HeaderTarget {
  ALL, NAME, DESCRIPTION;

  @Override
  public String toString() {
    return StringUtils.capitalize(super.toString());
  }

  public String partToMatch(Sequence s) {
    switch (this) {
      case ALL:
        return s.getHeader();
      case NAME:
        return s.getName();
      case DESCRIPTION:
        return s.getDescription();
      default:
        throw new IllegalStateException();
    }
  }
}
