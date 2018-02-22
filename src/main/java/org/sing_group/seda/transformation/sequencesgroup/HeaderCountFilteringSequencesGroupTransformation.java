package org.sing_group.seda.transformation.sequencesgroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.HeaderMatcherCount;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;

public class HeaderCountFilteringSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequencesGroupBuilder builder;
  private HeaderMatcher matcher;
  private int min;
  private int max;
  private boolean include;

  public HeaderCountFilteringSequencesGroupTransformation(HeaderMatcher matcher, int min, int max, boolean include) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), matcher, min, max, include);
  }

  public HeaderCountFilteringSequencesGroupTransformation(
    DatatypeFactory factory, HeaderMatcher matcher, int min, int max, boolean include
  ) {
    this.builder = factory::newSequencesGroup;
    this.matcher = matcher;
    this.min = min;
    this.max = max;
    this.include = include;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<Sequence> newSequences = new LinkedList<>();

    Map<String, Integer> counts = new HeaderMatcherCount(matcher).count(sequencesGroup);

    sequencesGroup.getSequences().forEach(
      s -> {
        Optional<String> match = matcher.match(s);
        if (match.isPresent()) {
          int count = counts.get(match.get());
          if (count >= min && count <= max) {
            if (include) {
              newSequences.add(s);
            }
          } else {
            if (!include) {
              newSequences.add(s);
            }
          }
        }
      }
    );

    return builder.of(
      sequencesGroup.getName(), sequencesGroup.getProperties(), newSequences.toArray(new Sequence[newSequences.size()])
    );
  }
}
