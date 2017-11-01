package org.sing_group.seda.core.ncbi;

import org.sing_group.seda.util.StringUtils;

public enum NcbiTaxonomyFields {
  SUPERKINGDOM, KINGDOM, PHYLUM, SUBPHYLUM, CLASS, SUPERORDER, ORDER, 
  SUBORDER, INFRAORDER, PARVORDER, SUPERFAMILY, FAMILY, SUBFAMILY, GENUS;

  public String nodeTitle() {
    return this.toString().toLowerCase();
  }
  
  @Override
  public String toString() {
    return StringUtils.capitalize(super.toString());
  }
}
