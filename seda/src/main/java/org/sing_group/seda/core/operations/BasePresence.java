/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.stream.Collectors.joining;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class BasePresence {
  @XmlElement
  protected double minimumPresence;
  @XmlElement
  protected double maximumPresence;
  @XmlElement
  protected List<Character> bases;

  public BasePresence() {}

  public BasePresence(double minimumPresence, double maximumPresence, char... bases) {
    this.minimumPresence = minimumPresence;
    this.maximumPresence = maximumPresence;
    this.checkRange();
    this.bases = requireNonEmptyList(getBasesList(bases));
  }

  private void checkRange() {
    if (this.maximumPresence < this.minimumPresence) {
      throw new IllegalArgumentException("The maximum value should be equal or higher than the minimum value");
    }
  }

  protected static <T> List<T> requireNonEmptyList(List<T> bases) {
    if (bases.isEmpty()) {
      throw new IllegalArgumentException("The list can't be empty");
    }
    return bases;
  }

  protected static List<Character> getBasesList(char[] bases) {
    List<Character> toret = new LinkedList<>();
    new LinkedList<>();
    for (char b : bases) {
      toret.add(b);
    }
    return toret;
  }

  public double getMaximumPresence() {
    return maximumPresence;
  }

  public double getMinimumPresence() {
    return minimumPresence;
  }

  public List<Character> getBases() {
    return bases;
  }

  @Override
  public String toString() {
    return "Base(s): " + bases.stream().map(Object::toString).collect(joining()) +
      " [" + this.minimumPresence + " ," + this.maximumPresence + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bases == null) ? 0 : bases.hashCode());
    long temp;
    temp = Double.doubleToLongBits(maximumPresence);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(minimumPresence);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    BasePresence other = (BasePresence) obj;
    if (bases == null) {
      if (other.bases != null)
        return false;
    } else if (!bases.equals(other.bases))
      return false;
    if (Double.doubleToLongBits(maximumPresence) != Double.doubleToLongBits(other.maximumPresence))
      return false;
    if (Double.doubleToLongBits(minimumPresence) != Double.doubleToLongBits(other.minimumPresence))
      return false;
    return true;
  }
}
