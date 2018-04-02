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

import static org.sing_group.seda.bio.SequenceType.NUCLEOTIDE;
import static org.sing_group.seda.bio.SequenceType.PROTEIN;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class GenerateConsensusSequencesGroupTransformationTest {

  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();

  private static final Sequence N1 = Sequence.of("1", "", "ATCGCAAGGCAAAC-A", PROPERTIES);
  private static final Sequence N2 = Sequence.of("2", "", "ATCGCAAGGCAAAC-A", PROPERTIES);
  private static final Sequence N3 = Sequence.of("3", "", "ATCGCAAGGCGCCG-A", PROPERTIES);
  private static final Sequence N4 = Sequence.of("4", "", "ATCGTGTCTAGCCG-C", PROPERTIES);
  private static final Sequence N5 = Sequence.of("5", "", "ATCGTGTCTATGTT-T", PROPERTIES);
  private static final Sequence N6 = Sequence.of("6", "", "ATCGTGTCTATGTT-G", PROPERTIES);

  private static final SequencesGroup NUCLEOTIDE_SEQUENCES = SequencesGroup.of("Group", N1, N2, N3, N4, N5, N6);

  private static final Sequence P1 = Sequence.of("1", "", "AAA-L", PROPERTIES);
  private static final Sequence P2 = Sequence.of("2", "", "AAA-K", PROPERTIES);
  private static final Sequence P3 = Sequence.of("3", "", "AAN-F", PROPERTIES);
  private static final Sequence P4 = Sequence.of("4", "", "ATC-P", PROPERTIES);
  private static final Sequence P5 = Sequence.of("5", "", "ATK-M", PROPERTIES);
  private static final Sequence P6 = Sequence.of("6", "", "ATW-S", PROPERTIES);

  private static final SequencesGroup PROTEIN_SEQUENCES = SequencesGroup.of("Group", P1, P2, P3, P4, P5, P6);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          NUCLEOTIDE_SEQUENCES, Sequence.of("consensus_Group", "", "ATCGYRWSKMDVHB-A", PROPERTIES), NUCLEOTIDE, 0d, false
        },
        {
          NUCLEOTIDE_SEQUENCES, Sequence.of("consensus_Group", "", "ATCGYRWSKMNNNN-A", PROPERTIES), NUCLEOTIDE, 0.5d, false
        },
        {
          NUCLEOTIDE_SEQUENCES, Sequence.of("consensus_Group", "", "ATCGNNNNNNNNNN-N", PROPERTIES), NUCLEOTIDE, 0.75d, false
        },
        {
          PROTEIN_SEQUENCES, Sequence.of("consensus_Group", "", "A[AT]X-X", PROPERTIES), PROTEIN, 0.5d, true
        },
        {
          PROTEIN_SEQUENCES, Sequence.of("consensus_Group", "", "AXX-X", PROPERTIES), PROTEIN, 0.5d, false
        },
        {
          PROTEIN_SEQUENCES, Sequence.of("consensus_Group", "", "A[AT]A-[PSFKLM]", PROPERTIES), PROTEIN, 0d, true
        },
        {
          PROTEIN_SEQUENCES, Sequence.of("consensus_Group", "", "AXA-X", PROPERTIES), PROTEIN, 0d, false
        },
      }
    );
  }

  private SequencesGroup input;
  private Sequence consensus;
  private SequenceType sequenceType;
  private double minimumPresence;
  private boolean verbose;

  public GenerateConsensusSequencesGroupTransformationTest(
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
      new GenerateConsensusSequencesGroupTransformation(sequenceType, minimumPresence, verbose).transform(input);
    Assert.assertEquals(this.consensus.getChain(), result.getSequence(0).getChain());
  }
}
