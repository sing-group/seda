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
