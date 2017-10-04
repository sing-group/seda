package org.sing_group.seda.datatype.statistics;

import java.util.List;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.SequencesGroupDataset;

public class SequencesGroupDatasetStatistics {

  private List<SequencesGroupStatistics> statistics;

  public SequencesGroupDatasetStatistics(SequencesGroupDataset dataset) {
    this.statistics = dataset.getSequencesGroups().map(SequencesGroupStatistics::new).collect(Collectors.toList());
  }

  public List<SequencesGroupStatistics> getStatistics() {
    return statistics;
  }
}
