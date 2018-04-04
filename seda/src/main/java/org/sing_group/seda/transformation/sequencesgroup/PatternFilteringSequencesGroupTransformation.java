/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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