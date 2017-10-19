package org.sing_group.seda.core.rename;

import java.util.List;
import java.util.function.BiFunction;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;

public abstract class AbstractHeaderRenamer implements HeaderRenamer {
  private HeaderTarget target;
  private BiFunction<String, List<Sequence>, SequencesGroup> groupBuilder;
  private SequenceBuilder sequenceBuilder;

  public AbstractHeaderRenamer(HeaderTarget target, DatatypeFactory factory) {
    this.target = target;
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
  }

  protected SequencesGroup buildSequencesGroup(String name, List<Sequence> renamedSequences) {
    return this.groupBuilder.apply(name, renamedSequences);
  }

  protected Sequence renameSequence(Sequence sequence, String renamedPart) {
    String newHeader;
    switch (this.target) {
      case ALL:
        newHeader = renamedPart;
        break;
      case DESCRIPTION:
        newHeader = sequence.getName() + " " + renamedPart;
        break;
      case NAME:
        newHeader = renamedPart + " " + sequence.getDescription();
        break;
      default:
        throw new IllegalStateException("Unknown header target");
    }

    int firstBlankSpace = newHeader.indexOf(" ");
    if (firstBlankSpace == -1) {
      return this.sequenceBuilder.of(newHeader, "", sequence.getChain(), sequence.getProperties());
    } else {
      String newName = newHeader.substring(0, firstBlankSpace);
      String newDescription = newHeader.substring(firstBlankSpace + 1);

      return this.sequenceBuilder.of(newName, newDescription, sequence.getChain(), sequence.getProperties());
    }
  }

  protected String getRenamePart(Sequence sequence) {
    switch (this.target) {
      case ALL:
        return sequence.getName() + " " + sequence.getDescription();
      case DESCRIPTION:
        return sequence.getDescription();
      case NAME:
        return sequence.getName();
      default:
        break;
    }
    throw new IllegalStateException("Unknown header target");
  }
}
