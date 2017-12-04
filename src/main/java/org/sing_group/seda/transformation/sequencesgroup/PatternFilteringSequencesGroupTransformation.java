package org.sing_group.seda.transformation.sequencesgroup;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;

public class PatternFilteringSequencesGroupTransformation extends FilterSequencesGroupTransformation {

  public PatternFilteringSequencesGroupTransformation(EvaluableSequencePattern pattern, SequenceTarget sequenceTarget) {
    super(sequence -> pattern.eval(getEvaluablePart(sequence, sequenceTarget)));
  }

  public PatternFilteringSequencesGroupTransformation(EvaluableSequencePattern pattern, SequenceTarget sequenceTarget, DatatypeFactory factory) {
    super(sequence -> pattern.eval(getEvaluablePart(sequence, sequenceTarget)), factory);
  }

  public PatternFilteringSequencesGroupTransformation(
    EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration
  ) {
    super(sequence -> evalTranslatedSequence(sequence.getChain(), pattern, configuration));
  }

  public PatternFilteringSequencesGroupTransformation(
    EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration, DatatypeFactory factory
  ) {
    super(sequence -> evalTranslatedSequence(sequence.getChain(), pattern, configuration), factory);
  }

  private static String getEvaluablePart(Sequence sequence, SequenceTarget patternTarget) {
    return patternTarget.isSequence() ? sequence.getChain() : sequence.getHeader();
  }

  private static boolean evalTranslatedSequence(
    String chain, EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration
  ) {
    if (configuration.isJoinFrames()) {
      StringBuilder translatedChain = new StringBuilder();
      for (int frame : configuration.getFrames()) {
        translatedChain.append(SequenceUtils.translate(chain, configuration.isReverseComplement(), frame, configuration.getCodonTable())).append("-");
      }

      return pattern.eval(translatedChain.toString());
    } else {
      for (int frame : configuration.getFrames()) {
        String translatedChain = SequenceUtils.translate(chain, configuration.isReverseComplement(), frame, configuration.getCodonTable());
        if (pattern.eval(translatedChain)) {
          return true;
        }
      }
      return false;
    }
  }
}