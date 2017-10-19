package org.sing_group.seda.core.rename;

import static java.util.Arrays.asList;
import static java.util.regex.Pattern.quote;

import org.sing_group.seda.datatype.DatatypeFactory;

public class IntervalReplaceRenamer extends WordReplaceRenamer {

  public IntervalReplaceRenamer(
    DatatypeFactory factory, HeaderTarget target, String from, String to, String replacement
  ) {
    super(factory, target, replacement, true, asList(new String(quote(from) + ".*" + quote(to))));
  }
}
