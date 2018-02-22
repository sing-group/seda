package org.sing_group.seda.core.filtering;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.datatype.SequencesGroup;

public class HeaderMatcherCount {
  private HeaderMatcher matcher;

  public HeaderMatcherCount(HeaderMatcher matcher) {
    this.matcher = matcher;
  }

  public Map<String, Integer> count(SequencesGroup sequences) {
    Map<String, Integer> toret = new HashMap<>();

    sequences.getSequences().forEach(
      s -> {
        Optional<String> match = this.matcher.match(s);
        if (match.isPresent()) {
          toret.putIfAbsent(match.get(), 0);
          toret.put(match.get(), toret.get(match.get()) + 1);
        }
      }
    );

    return toret;
  }
}
