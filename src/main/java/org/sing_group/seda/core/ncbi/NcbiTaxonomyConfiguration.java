package org.sing_group.seda.core.ncbi;

public class NcbiTaxonomyConfiguration {
  private String delimiter = "";
  private NcbiTaxonomyFields[] fields = new NcbiTaxonomyFields[0];

  public NcbiTaxonomyConfiguration() {
    this("");
  }

  public NcbiTaxonomyConfiguration(String delimiter, NcbiTaxonomyFields... fields) {
    this.delimiter = delimiter;
    this.fields = fields;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public NcbiTaxonomyFields[] getFields() {
    return fields;
  }
}