package org.sing_group.seda.transformation.sequencesgroup;

import java.util.Map;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;

public class PatternFilteringSequencesGroupTransformation extends FilterSequencesGroupTransformation {

  public static class SequenceTranslationConfiguration {

    private Map<String, String> codonTable;
    private int[] frames;

    public SequenceTranslationConfiguration(Map<String, String> codonTable, int... frames) {
      this.codonTable = codonTable;
      this.frames = frames;
    }

    public int[] getFrames() {
      return frames;
    }

    public Map<String, String> getCodonTable() {
      return codonTable;
    }
  }

  public PatternFilteringSequencesGroupTransformation(EvaluableSequencePattern pattern) {
    super(sequence -> pattern.eval(sequence.getChain()));
  }

  public PatternFilteringSequencesGroupTransformation(EvaluableSequencePattern pattern, DatatypeFactory factory) {
    super(sequence -> pattern.eval(sequence.getChain()), factory);
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

  private static boolean evalTranslatedSequence(
    String chain, EvaluableSequencePattern pattern, SequenceTranslationConfiguration configuration
  ) {
    for (int frame : configuration.getFrames()) {
      String translatedChain = SequenceUtils.translate(chain, frame, configuration.getCodonTable());
      if (pattern.eval(translatedChain)) {
        return true;
      }
    }

    return false;
  }
}
