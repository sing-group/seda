package org.sing_group.seda.core.rename;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class WordReplaceRenamer extends AbstractHeaderRenamer {

  private String replacement;
  private List<String> targets;
  private boolean regex;

  public WordReplaceRenamer(
    DatatypeFactory factory, HeaderTarget target, String replacement, boolean regex, List<String> targets
  ) {
    super(target, factory);

    this.replacement = replacement;
    this.targets = targets;
    this.regex = regex;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      for (String target : targets) {
        if (regex) {
          partToRename = partToRename.replaceAll(target, replacement);
        } else {
          partToRename = partToRename.replace(target, replacement);
        }
      }

      renamedSequences.add(renameSequence(original, partToRename));
    }

    return buildSequencesGroup(sequences.getName(), renamedSequences);
  }
}