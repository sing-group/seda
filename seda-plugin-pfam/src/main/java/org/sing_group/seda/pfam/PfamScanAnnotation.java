/*
 * #%L
 * SEquence DAtaset builder PfamScan plugin
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
package org.sing_group.seda.pfam;

public class PfamScanAnnotation {

  private String alignStart;
  private String alignEnd;
  private String hmmAcc;
  private String hmmName;
  private String type;

  public PfamScanAnnotation(String alignStart, String alignEnd, String hmmAcc, String hmmName, String type) {
    this.alignStart = alignStart;
    this.alignEnd = alignEnd;
    this.hmmAcc = hmmAcc;
    this.hmmName = hmmName;
    this.type = type;
  }

  public String getAlignStart() {
    return alignStart;
  }

  public String getAlignEnd() {
    return alignEnd;
  }

  public String getHmmAcc() {
    return hmmAcc;
  }

  public String getHmmName() {
    return hmmName;
  }

  public String getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((alignEnd == null) ? 0 : alignEnd.hashCode());
    result = prime * result + ((alignStart == null) ? 0 : alignStart.hashCode());
    result = prime * result + ((hmmAcc == null) ? 0 : hmmAcc.hashCode());
    result = prime * result + ((hmmName == null) ? 0 : hmmName.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    PfamScanAnnotation other = (PfamScanAnnotation) obj;
    if (alignEnd == null) {
      if (other.alignEnd != null)
        return false;
    } else if (!alignEnd.equals(other.alignEnd))
      return false;
    if (alignStart == null) {
      if (other.alignStart != null)
        return false;
    } else if (!alignStart.equals(other.alignStart))
      return false;
    if (hmmAcc == null) {
      if (other.hmmAcc != null)
        return false;
    } else if (!hmmAcc.equals(other.hmmAcc))
      return false;
    if (hmmName == null) {
      if (other.hmmName != null)
        return false;
    } else if (!hmmName.equals(other.hmmName))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    return new StringBuilder("PfamScan annotation: ")
      .append("\tAlignment start: ")
      .append(this.alignStart)
      .append("\tAlignment end: ")
      .append(this.alignEnd)
      .append("\tHMM acc.: ")
      .append(this.hmmAcc)
      .append("\tHMM name: ")
      .append(this.hmmName)
      .append("\tType: ")
      .append(this.type)
      .toString();
  }
}
