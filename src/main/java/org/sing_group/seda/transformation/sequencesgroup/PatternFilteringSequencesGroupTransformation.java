package org.sing_group.seda.transformation.sequencesgroup;

import java.util.Map;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;

public class PatternFilteringSequencesGroupTransformation extends FilterSequencesGroupTransformation {

  public enum PatternTarget {
    HEADER("Header"), SEQUENCE("Sequence");

    private String description;

    PatternTarget(String description) {
      this.description = description;
    }

    @Override
    public String toString() {
      return this.description;
    }

    public boolean isSequence() {
      return this.equals(SEQUENCE);
    }
  }

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

  public PatternFilteringSequencesGroupTransformation(EvaluableSequencePattern pattern, PatternTarget patternTarget) {
    super(sequence -> pattern.eval(getEvaluablePart(sequence, patternTarget)));
  }

  public PatternFilteringSequencesGroupTransformation(EvaluableSequencePattern pattern, PatternTarget patternTarget, DatatypeFactory factory) {
    super(sequence -> pattern.eval(getEvaluablePart(sequence, patternTarget)), factory);
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

  private static String getEvaluablePart(Sequence sequence, PatternTarget patternTarget) {
    return patternTarget.isSequence() ? sequence.getChain() : sequence.getHeader();
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
