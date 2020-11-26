/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.bio.SequenceType.NUCLEOTIDE;
import static org.sing_group.seda.bio.SequenceType.PROTEIN;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.bio.consensus.ConsensusBaseStrategy;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class GenerateAboveThresholdConsensusSequencesGroupTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence S1 = of("1", "", "AAAA", PROPERTIES);
  private static final Sequence S2 = of("2", "", "AAAA", PROPERTIES);
  private static final Sequence S3 = of("3", "", "AACT", PROPERTIES);
  private static final Sequence S4 = of("4", "", "ACCT", PROPERTIES);
  private static final Sequence S5 = of("5", "", "ACTC", PROPERTIES);
  private static final Sequence S6 = of("6", "", "ACTG", PROPERTIES);

  private static final SequencesGroup SEQUENCES = of("Group", emptyMap(), S1, S2, S3, S4, S5, S6);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          SEQUENCES, of("consensus_Group", "", "AMHN", PROPERTIES), NUCLEOTIDE, 0d, false
        },
        {
          SEQUENCES, of("consensus_Group", "", "AMNN", PROPERTIES), NUCLEOTIDE, 0.5d, false
        },
        {
          SEQUENCES, of("consensus_Group", "", "AXXX", PROPERTIES), PROTEIN, 0d, false
        },
        {
          SEQUENCES, of("consensus_Group", "", "A[AC][ACT][ACTG]", PROPERTIES), PROTEIN, 0d, true
        },
        {
          SEQUENCES, of("consensus_Group", "", "AXXX", PROPERTIES), PROTEIN, 0.5d, false
        },
        {
          SEQUENCES, of("consensus_Group", "", "A[AC]XX", PROPERTIES), PROTEIN, 0.5d, true
        }
      }
    );
  }

  private SequencesGroup input;
  private Sequence consensus;
  private SequenceType sequenceType;
  private double minimumPresence;
  private boolean verbose;

  public GenerateAboveThresholdConsensusSequencesGroupTransformationTest(
    SequencesGroup input, Sequence consensus, SequenceType sequenceType, double minimumPresence, boolean verbose
  ) {
    this.sequenceType = sequenceType;
    this.consensus = consensus;
    this.input = input;
    this.minimumPresence = minimumPresence;
    this.verbose = verbose;
  }

  @Test
  public void test() {
    SequencesGroup result =
      new GenerateConsensusSequencesGroupTransformation(
        sequenceType, ConsensusBaseStrategy.ABOVE_THRESHOLD, minimumPresence, verbose
      ).transform(input);

    assertEquals(this.consensus.getChain(), result.getSequence(0).getChain());
  }
}
