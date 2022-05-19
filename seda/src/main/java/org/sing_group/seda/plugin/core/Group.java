package org.sing_group.seda.plugin.core;

public enum Group {
  GROUP_GENERAL("General"), GROUP_ALIGNMENT("Alignment-related"), GROUP_REFORMATTING("Reformatting"), GROUP_FILTERING(
    "Filtering"
  ), GROUP_BLAST("BLAST"), GROUP_GENE_ANNOTATION("Gene Annotation");

  private final String group;

  Group(String group) {
    this.group = group;
  }

  public String getName() {
    return this.group;
  }
}
