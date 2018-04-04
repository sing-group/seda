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
package org.sing_group.seda.core.ncbi;

import java.net.URL;

public class NcbiAssemblyAccession {

  private String accession;
  private String organismName;
  private URL taxonomyUrl;

  public NcbiAssemblyAccession(String accession, String organismName, URL taxonomyUrl) {
    this.accession = accession;
    this.organismName = organismName;
    this.taxonomyUrl = taxonomyUrl;
  }

  public String getAccession() {
    return accession;
  }

  public String getOrganismName() {
    return organismName;
  }

  public URL getTaxonomyUrl() {
    return taxonomyUrl;
  }

  @Override
  public String toString() {
    return this.accession + " [Organism name = " + this.organismName + "]" + " [" + this.taxonomyUrl + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accession == null) ? 0 : accession.hashCode());
    result = prime * result + ((organismName == null) ? 0 : organismName.hashCode());
    result = prime * result + ((taxonomyUrl == null) ? 0 : taxonomyUrl.hashCode());

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
    NcbiAssemblyAccession other = (NcbiAssemblyAccession) obj;
    if (accession == null) {
      if (other.accession != null)
        return false;
    } else if (!accession.equals(other.accession))
      return false;
    if (organismName == null) {
      if (other.organismName != null)
        return false;
    } else if (!organismName.equals(other.organismName))
      return false;
    if (taxonomyUrl == null) {
      if (other.taxonomyUrl != null)
        return false;
    } else if (!taxonomyUrl.equals(other.taxonomyUrl))
      return false;
    return true;
  }
}
