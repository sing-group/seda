package org.sing_group.seda.core.rename;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class AddStringHeaderRenamer extends AbstractHeaderRenamer {

  public enum Position { PREFIX, SUFFIX, OVERRIDE }

  private String string;
  private String delimiter;
  private Position position;
  private boolean addIndex;
  private String indexDelimiter;

  public AddStringHeaderRenamer(
    DatatypeFactory factory, HeaderTarget target, String string, String delimiter, Position position
  ) {
    this(factory, target, string, delimiter, position, false, "");
  }

  public AddStringHeaderRenamer(
    DatatypeFactory factory, HeaderTarget target, String string, String delimiter, Position position, boolean addIndex,
    String indexDelimiter
  ) {
    super(target, factory);

    this.string = string;
    this.delimiter = delimiter;
    this.position = position;
    this.addIndex = addIndex;
    this.indexDelimiter = indexDelimiter;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      String partToAdd = string;
      if (addIndex) {
        partToAdd = partToAdd + this.indexDelimiter + String.valueOf(i + 1);
      }

      String renamedPart;
      if (this.position.equals(Position.PREFIX)) {
        renamedPart = partToAdd + this.delimiter + partToRename;
      } else if (this.position.equals(Position.SUFFIX)) {
        renamedPart = partToRename + this.delimiter + partToAdd;
      } else {
        renamedPart = partToAdd;
      }

      renamedSequences.add(renameSequence(original, renamedPart));
    }

    return buildSequencesGroup(sequences.getName(), renamedSequences);
  }
}
