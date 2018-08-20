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

import static java.util.Collections.emptyMap;
import static org.sing_group.seda.bio.SequenceType.NUCLEOTIDE;
import static org.sing_group.seda.bio.SequenceType.PROTEIN;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

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

  private static final Sequence N1 = of("1", "", "ATCGCAAGGCAAAC-A", PROPERTIES);
  private static final Sequence N2 = of("2", "", "ATCGCAAGGCAAAC-A", PROPERTIES);
  private static final Sequence N3 = of("3", "", "ATCGCAAGGCGCCG-A", PROPERTIES);
  private static final Sequence N4 = of("4", "", "ATCGTGTCTAGCCG-C", PROPERTIES);
  private static final Sequence N5 = of("5", "", "ATCGTGTCTATGTT-T", PROPERTIES);
  private static final Sequence N6 = of("6", "", "ATCGTGTCTATGTT-G", PROPERTIES);

  private static final SequencesGroup NUCLEOTIDE_SEQUENCES = of("Group", emptyMap(), N1, N2, N3, N4, N5, N6);

  private static final Sequence P1 = of("1", "", "AAA-L", PROPERTIES);
  private static final Sequence P2 = of("2", "", "AAA-K", PROPERTIES);
  private static final Sequence P3 = of("3", "", "AAN-F", PROPERTIES);
  private static final Sequence P4 = of("4", "", "ATC-P", PROPERTIES);
  private static final Sequence P5 = of("5", "", "ATK-M", PROPERTIES);
  private static final Sequence P6 = of("6", "", "ATW-S", PROPERTIES);

  private static final SequencesGroup PROTEIN_SEQUENCES = of("Group", emptyMap(), P1, P2, P3, P4, P5, P6);

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {
          NUCLEOTIDE_SEQUENCES, of("consensus_Group", "", "ATCGYRWSKMDVHB-A", PROPERTIES), NUCLEOTIDE, 0d, false
        },
        {
          NUCLEOTIDE_SEQUENCES, of("consensus_Group", "", "ATCGYRWSKMNNNN-A", PROPERTIES), NUCLEOTIDE, 0.5d, false
        },
        {
          NUCLEOTIDE_SEQUENCES, of("consensus_Group", "", "ATCGNNNNNNNNNN-N", PROPERTIES), NUCLEOTIDE, 0.75d, false
        },
        {
          PROTEIN_SEQUENCES, of("consensus_Group", "", "A[AT]X-X", PROPERTIES), PROTEIN, 0.5d, true
        },
        {
          PROTEIN_SEQUENCES, of("consensus_Group", "", "AXX-X", PROPERTIES), PROTEIN, 0.5d, false
        },
        {
          PROTEIN_SEQUENCES, of("consensus_Group", "", "A[AT]A-[PSFKLM]", PROPERTIES), PROTEIN, 0d, true
        },
        {
          PROTEIN_SEQUENCES, of("consensus_Group", "", "AXA-X", PROPERTIES), PROTEIN, 0d, false
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
