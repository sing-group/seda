package org.sing_group.seda.core.ncbi;

import java.util.Map;
import java.util.Optional;

public class NcbiTaxonomyInfo {

  private Map<NcbiTaxonomyFields, String> values;

  public NcbiTaxonomyInfo(Map<NcbiTaxonomyFields, String> values) {
    this.values = values;
  }

  public Map<NcbiTaxonomyFields, String> getValues() {
    return values;
  }

  public Optional<String> getValue(NcbiTaxonomyFields field) {
    return Optional.ofNullable(this.values.get(field));
  }
}
