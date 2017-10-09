package org.sing_group.seda.split;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class NumberOfSequencesAndNumberOfFilesSplitter extends AbstractSequencesGroupSplitter {
  private int numFiles;
  private int numSequences;
  private boolean independentExtractions;

  public NumberOfSequencesAndNumberOfFilesSplitter(int numFiles, int numSequences, boolean independentExtractions) {
    this(numFiles, numSequences, false, independentExtractions, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public NumberOfSequencesAndNumberOfFilesSplitter(
    int numFiles, int numSequences, boolean randomize, boolean independentExtractions, DatatypeFactory factory
  ) {
    super(randomize, factory);
    this.numFiles = numFiles;
    this.numSequences = numSequences;
    this.independentExtractions = independentExtractions;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    if (this.independentExtractions) {
      return independentExtractions(group);
    } else {
      return splitGroup(group);
    }
  }

  private List<SequencesGroup> independentExtractions(SequencesGroup group) {
    List<SequencesGroup> toret = new LinkedList<>();
    for (int file = 0; file < this.numFiles; file++) {
      List<Sequence> input = getInputSequencesGroup(group);
      Iterator<Sequence> inputIterator = input.iterator();
      List<Sequence> currentSubList = new LinkedList<>();
      for (int i = 0; i < this.numSequences; i++) {
        if (!inputIterator.hasNext()) {
          inputIterator = input.iterator();
        }
        currentSubList.add(inputIterator.next());
      }
      toret.add(createGroup(group.getName() + "_" + (file + 1), currentSubList));
    }

    return toret;
  }

  private List<SequencesGroup> splitGroup(SequencesGroup group) {
    if (this.numFiles * this.numSequences > group.getSequenceCount()) {
      throw new TransformationException("Not enough sequences");
    }

    List<SequencesGroup> toret = new LinkedList<>();
    List<Sequence> input = getInputSequencesGroup(group);
    for (int file = 0; file < this.numFiles; file++) {
      int startIndex = file * this.numSequences;
      int endIndex = (file + 1) * this.numSequences;
      List<Sequence> currentSubList = input.subList(startIndex, endIndex);

      toret.add(createGroup(group.getName() + "_" + (file + 1), currentSubList));
    }
    return toret;
  }
}
