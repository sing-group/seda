/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.uniprot;

import java.util.List;

public class UniProtBlastHit {

  private String entryId;
  private String description;
  private List<String> alignmments;

  public UniProtBlastHit(String entryId, String description, List<String> alignmments) {
    this.entryId = entryId;
    this.description = description;
    this.alignmments = alignmments;
  }

  public String getEntryId() {
    return entryId;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getAlignments() {
    return alignmments;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((alignmments == null) ? 0 : alignmments.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((entryId == null) ? 0 : entryId.hashCode());
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
    UniProtBlastHit other = (UniProtBlastHit) obj;
    if (alignmments == null) {
      if (other.alignmments != null)
        return false;
    } else if (!alignmments.equals(other.alignmments))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (entryId == null) {
      if (other.entryId != null)
        return false;
    } else if (!entryId.equals(other.entryId))
      return false;
    return true;
  }
}
