package org.sing_group.seda.transformation.dataset;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class MergeSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final BiFunction<String, List<Sequence>, SequencesGroup> groupBuilder;
  private final String mergeName;

  public MergeSequencesGroupDatasetTransformation(String mergeName) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), mergeName);
  }

  public MergeSequencesGroupDatasetTransformation(DatatypeFactory factory, String mergeName) {
    this.builder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.mergeName = mergeName;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    List<Sequence> sequences = new LinkedList<>();

    dataset.getSequencesGroups().forEach(
      g -> {
        sequences.addAll(g.getSequences().collect(Collectors.toList()));
      }
    );

    return this.builder.apply(
      new SequencesGroup[] {
        this.groupBuilder.apply(mergeName, sequences)
      }
    );
  }
}
