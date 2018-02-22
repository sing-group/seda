package org.sing_group.seda.core.filtering;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sing_group.seda.datatype.Sequence;

public class StringHeaderMatcher implements HeaderMatcher {
  private Pattern pattern;

  public StringHeaderMatcher(String string, boolean regex, boolean caseSensitive) {
    String effectiveString = regex ? string : Pattern.quote(string);

    if (caseSensitive) {
      this.pattern = Pattern.compile(effectiveString);
    } else {
      this.pattern = Pattern.compile(effectiveString, Pattern.CASE_INSENSITIVE);
    }
  }

  @Override
  public Optional<String> match(Sequence sequence) {
    Matcher matcher = this.pattern.matcher(sequence.getName() + " " + sequence.getDescription());

    if (!matcher.find()) {

      return Optional.empty();
    }
    return Optional.of(matcher.group(0));
  }
}
