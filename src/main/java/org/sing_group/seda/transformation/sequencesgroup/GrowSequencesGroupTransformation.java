package org.sing_group.seda.transformation.sequencesgroup;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class GrowSequencesGroupTransformation implements SequencesGroupTransformation {
  private final SequenceBuilder seqBuilder;
  private final BiFunction<String, List<Sequence>, SequencesGroup> builder;
  private int minimumOverlapping;

  public GrowSequencesGroupTransformation(int minimumOverlapping) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), minimumOverlapping);
  }

  public GrowSequencesGroupTransformation(
    DatatypeFactory factory, int minimumOverlapping
  ) {
    this.seqBuilder = factory::newSequence;
    this.builder = factory::newSequencesGroup;
    this.minimumOverlapping = minimumOverlapping;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    return builder.apply(sequencesGroup.getName(), mergeOverlappingSequences(sequencesGroup));
  }

  private List<Sequence> mergeOverlappingSequences(SequencesGroup sequencesGroup) {
    List<Sequence> sequences = sequencesGroup.getSequences().collect(Collectors.toList());

    boolean shouldIterate = false;
    do {
      shouldIterate = false;
      for (int i = 0; i < sequences.size(); i++) {
        Sequence reference = sequences.get(i);

        if (reference.getChain().length() < this.minimumOverlapping) {
          continue;
        }

        String referenceChain = reference.getChain();

        for (int j = 0; j < sequences.size(); j++) {
          if (i == j) {
            continue;
          }
          Sequence compare = sequences.get(j);

          if (compare.getChain().length() < this.minimumOverlapping) {
            continue;
          }

          String compareChain = compare.getChain();

          String overlappingPart = computeDifferentialOverlapping(referenceChain, compareChain, minimumOverlapping);

          if (overlappingPart.length() >= this.minimumOverlapping) {
            String mergedChain =
              compareChain.substring(0, compareChain.length() - overlappingPart.length()) + referenceChain;

            String mergedDescription =
              (reference.getDescription().isEmpty() ? "[" : (reference.getDescription() + " [")) +
              compare.getName() +
              (compare.getDescription().isEmpty() ? "" : " ") + compare.getDescription() + "]";

            Sequence merged =
              seqBuilder.of(
                reference.getName(),
                mergedDescription,
                mergedChain, reference.getProperties()
              );

            sequences.remove(compare);
            sequences.remove(reference);
            sequences.add(merged);

            shouldIterate = true;
            break;
          }
        }

        if (shouldIterate) {
          break;
        }
      }
    } while (shouldIterate);

    return sequences;
  }

  private static String computeDifferentialOverlapping(String atBeginning, String atEnding, int minimumSize) {
    int size = minimumSize;
    String overlapping = "";
    do {
      String subA = atBeginning.substring(0, size);
      String subB = atEnding.substring(atEnding.length() - size, atEnding.length());
      if (subA.equals(subB)) {
        overlapping = subA;
      }
      size++;
    } while (size <= atBeginning.length() && size <= atEnding.length());

    return overlapping;
  }

}
