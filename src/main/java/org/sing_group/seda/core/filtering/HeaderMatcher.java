package org.sing_group.seda.core.filtering;

import java.util.Optional;

import org.sing_group.seda.datatype.Sequence;

public interface HeaderMatcher {
  public Optional<String> match(Sequence sequence);
}
