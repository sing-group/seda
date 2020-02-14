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
package org.sing_group.seda.core.operations;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.util.StringUtils;

@XmlRootElement
public class DefaultSequenceIsoformSelector implements SequenceIsoformSelector {
  public enum TieBreakOption {
    SHORTEST, LONGEST;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  @XmlElement
  private int referenceSize;
  @XmlElement
  private TieBreakOption tieBreak;

  public DefaultSequenceIsoformSelector() {}

  public DefaultSequenceIsoformSelector(int referenceSize, TieBreakOption tieBreak) {
    this.referenceSize = referenceSize;
    this.tieBreak = tieBreak;
  }

  @Override
  public Sequence selectSequence(List<Sequence> isoforms) {
    Sequence closestToReferenceSize = null;
    int minDistance = Integer.MAX_VALUE;
    for (Sequence s : isoforms) {
      int distance = Math.abs(s.getLength() - referenceSize);
      if (distance < minDistance) {
        minDistance = distance;
        closestToReferenceSize = s;
      } else if (distance == minDistance) {
        if (closestToReferenceSize != null) {
          if (tieBreak.equals(TieBreakOption.SHORTEST)) {
            if (s.getLength() < closestToReferenceSize.getLength()) {
              closestToReferenceSize = s;
            }
          } else {
            if (s.getLength() > closestToReferenceSize.getLength()) {
              closestToReferenceSize = s;
            }
          }
        }
      }
    }

    return closestToReferenceSize;
  }

  public int getReferenceSize() {
    return referenceSize;
  }

  public TieBreakOption getTieBreak() {
    return tieBreak;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + referenceSize;
    result = prime * result + ((tieBreak == null) ? 0 : tieBreak.hashCode());
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
    DefaultSequenceIsoformSelector other = (DefaultSequenceIsoformSelector) obj;
    if (referenceSize != other.referenceSize)
      return false;
    if (tieBreak != other.tieBreak)
      return false;
    return true;
  }
}
