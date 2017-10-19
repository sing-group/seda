package org.sing_group.seda.core.rename;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class FieldSplitRenamer extends AbstractHeaderRenamer {

  public enum Mode {
    KEEP, REMOVE
  }

  private String fieldDelimiter;
  private String joinDelimiter;
  private Mode mode;
  private Set<Integer> fields;;

  public FieldSplitRenamer(
    DatatypeFactory factory, HeaderTarget target, String fieldDelimiter, String joinDelimiter, Mode mode,
    Set<Integer> fields
  ) {
    super(target, factory);

    this.fieldDelimiter = fieldDelimiter;
    this.joinDelimiter = joinDelimiter;
    this.mode = mode;
    this.fields = fields;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      String[] parts = partToRename.split(Pattern.quote(fieldDelimiter));

      List<String> newFields = new LinkedList<>();

      for (int field = 0; field < parts.length; field++) {
        if (this.mode.equals(Mode.KEEP)) {
          if (fields.contains(field)) {
            newFields.add(parts[field]);
          }
        } else if (this.mode.equals(Mode.REMOVE)) {
          if (!fields.contains(field)) {
            newFields.add(parts[field]);
          }
        }
      }

      String renamedPart = newFields.stream().collect(Collectors.joining(joinDelimiter));

      renamedSequences.add(renameSequence(original, renamedPart));
    }

    return buildSequencesGroup(sequences.getName(), renamedSequences);
  }
}
