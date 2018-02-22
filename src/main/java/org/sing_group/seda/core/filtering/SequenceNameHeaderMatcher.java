package org.sing_group.seda.core.filtering;

import java.util.Optional;

import org.sing_group.seda.datatype.Sequence;

public class SequenceNameHeaderMatcher implements HeaderMatcher {

  @Override
  public Optional<String> match(Sequence sequence) {
    return Optional.of(sequence.getName());
  }
}
