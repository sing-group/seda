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
package org.sing_group.seda.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;

public abstract class AbstractTargetedSequenceComparator implements Comparator<Sequence>, Serializable {
  private static final long serialVersionUID = 1L;

  private final SequenceTarget sequenceTarget;

  public AbstractTargetedSequenceComparator(SequenceTarget sequenceTarget) {
    this.sequenceTarget = sequenceTarget;
  }

  @Override
  public int compare(Sequence o1, Sequence o2) {
    if (sequenceTarget.isSequence()) {
      return compare(o1.getChain(), o2.getChain());
    } else {
      return compare(o1.getHeader(), o2.getHeader());
    }
  }

  protected abstract int compare(String o1, String o2);
}
