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
package org.sing_group.seda.blast.ncbi;

import java.util.List;

public class NcbiBlastHit {
  private String accession;
  private String description;
  private List<String> hspHseqs;

  public NcbiBlastHit(String accession, String description, List<String> hspHseqs) {
    this.accession = accession;
    this.description = description;
    this.hspHseqs = hspHseqs;
  }

  public String getAccession() {
    return accession;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getHspHseqs() {
    return hspHseqs;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accession == null) ? 0 : accession.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((hspHseqs == null) ? 0 : hspHseqs.hashCode());
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
    NcbiBlastHit other = (NcbiBlastHit) obj;
    if (accession == null) {
      if (other.accession != null)
        return false;
    } else if (!accession.equals(other.accession))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (hspHseqs == null) {
      if (other.hspHseqs != null)
        return false;
    } else if (!hspHseqs.equals(other.hspHseqs))
      return false;
    return true;
  }
}
