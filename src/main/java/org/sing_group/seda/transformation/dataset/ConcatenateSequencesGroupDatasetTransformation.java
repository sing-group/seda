package org.sing_group.seda.transformation.dataset;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;

public class ConcatenateSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {

  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final BiFunction<String, List<Sequence>, SequencesGroup> groupBuilder;
  private final SequenceBuilder sequenceBuilder;
  private final String mergeName;
  private final HeaderTarget headerTarget;

  public ConcatenateSequencesGroupDatasetTransformation(String mergeName, HeaderTarget headerTarget) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), mergeName, headerTarget);
  }

  public ConcatenateSequencesGroupDatasetTransformation(
    DatatypeFactory factory, String mergeName, HeaderTarget headerTarget
  ) {
    this.builder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
    this.mergeName = mergeName;
    this.headerTarget = headerTarget;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    Map<String, Sequence> concatenatedSequences = new HashMap<>();

    dataset.getSequencesGroups().forEach(
      g -> {
        g.getSequences().forEach(s -> {
          String headerMatch = getHeaderMatch(s);
          if(concatenatedSequences.containsKey(headerMatch)) {
            Sequence toMerge = concatenatedSequences.get(headerMatch);
            concatenatedSequences.put(
              headerMatch,
              this.sequenceBuilder.of(headerMatch, "", toMerge.getChain() + s.getChain(), toMerge.getProperties())
            );
          } else {
              concatenatedSequences.put(
                headerMatch,
                this.sequenceBuilder.of(headerMatch, "", s.getChain(), s.getProperties())
              );
          }
        });
      }
    );

    return this.builder.apply(
      new SequencesGroup[] {
        this.groupBuilder.apply(mergeName, new LinkedList<>(concatenatedSequences.values()))
      }
    );
  }

  private String getHeaderMatch(Sequence s) {
    switch (this.headerTarget) {
      case ALL:
        return s.getName() + " " + s.getDescription();
      case DESCRIPTION:
        return s.getDescription();
      case NAME:
        return s.getName();
    }
    throw new IllegalStateException();
  }
}
