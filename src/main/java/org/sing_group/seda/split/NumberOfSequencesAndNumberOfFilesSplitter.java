package org.sing_group.seda.split;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class NumberOfSequencesAndNumberOfFilesSplitter extends AbstractSequencesGroupSplitter {
  private int numFiles;
  private int numSequences;

  public NumberOfSequencesAndNumberOfFilesSplitter(int numFiles, int numSequences) {
    this(numFiles, numSequences, false, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public NumberOfSequencesAndNumberOfFilesSplitter(
    int numFiles, int numSequences, boolean randomize, DatatypeFactory factory
  ) {
    super(randomize, factory);
    this.numFiles = numFiles;
    this.numSequences = numSequences;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    if (this.numFiles * this.numSequences > group.getSequenceCount()) {
      throw new TransformationException("Not enough sequences");
    }

    List<Sequence> input = getInputSequencesGroup(group);

    List<SequencesGroup> toret = new LinkedList<>();

    for (int file = 0; file < this.numFiles; file++) {
      int startIndex = file * numSequences;
      int endIndex = (file + 1) * numSequences;
      List<Sequence> currentSubList = input.subList(startIndex, endIndex);

      toret.add(createGroup(group.getName() + "_" + (file + 1), currentSubList));
    }
    
    return toret;
  }
}
