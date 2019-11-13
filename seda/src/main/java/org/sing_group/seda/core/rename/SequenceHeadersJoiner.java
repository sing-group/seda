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

import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.Sequence;

public class SequenceHeadersJoiner {
  private HeaderTarget target;
  private CharSequence delimiter;
  private CharSequence prefix;
  private CharSequence suffix;

  public SequenceHeadersJoiner(
    HeaderTarget target, CharSequence delimiter,
    CharSequence prefix,
    CharSequence suffix
  ) {
    this.target = target;
    this.delimiter = delimiter;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  public String join(List<Sequence> sequences) {
    return sequences.stream()
      .map(this::mapSequence)
      .collect(Collectors.joining(delimiter, prefix, suffix));
  }

  private String mapSequence(Sequence s) {
    switch (this.target) {
      case ALL:
        return s.getName() + " " + s.getDescription();
      case NAME:
        return s.getName();
      case DESCRIPTION:
        return s.getName();
      default:
        throw new IllegalArgumentException("Unknown HeaderTarget value: " + this.target);
    }
  }
}
