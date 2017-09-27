package org.sing_group.seda.split;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class NumberOfFilesSplitter extends AbstractSequencesGroupSplitter {

  private int numFiles;

  public NumberOfFilesSplitter(int numFiles) {
    this(numFiles, false);
  }

  public NumberOfFilesSplitter(int numFiles, boolean randomize) {
    super(randomize);
    this.numFiles = numFiles;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup group) {
    if (this.numFiles > group.getSequenceCount()) {
      throw new TransformationException(
        "The required number of files must be lesser than or equal to the actual number of sequences."
      );
    }

    List<SequencesGroup> toret = new LinkedList<>();
    List<Sequence> input = getInputSequencesGroup(group);

    int partitionSize = Math.round(group.getSequenceCount() / this.numFiles);
    
    for(int i = 0; i < this.numFiles; i++) {
      int startIndex = i * partitionSize;
      int endIndex = i == this.numFiles-1 ? input.size() : (i+1) * partitionSize;
      List<Sequence> currentSubList = input.subList(startIndex, endIndex);

      toret.add(
        SequencesGroup.of(group.getName() + "_" + (i+1), currentSubList.toArray(new Sequence[currentSubList.size()]))
       );
    }

    return toret;
  }
}
