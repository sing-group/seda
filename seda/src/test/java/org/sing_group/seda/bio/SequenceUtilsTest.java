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
package org.sing_group.seda.bio;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SequenceUtilsTest {

  @Test
  public void convertToCodons() {
    String sequence = new String("ACTACTACT");

    List<String> codons = SequenceUtils.toCodons(sequence).collect(toList());
    assertEquals(asList("ACT", "ACT", "ACT"), codons);
  }

  @Test(expected = IllegalArgumentException.class)
  public void convertToCodonsNonMultipleOf3WithError() {
    String sequence = new String("ACTACTACTG");

    SequenceUtils.toCodons(sequence);
  }

  @Test
  public void convertToCodonsNonMultipleOf3WithoutError() {
    String sequence = new String("ACTACTACTG");

    List<String> codons = SequenceUtils.toCodons(sequence, true).collect(toList());
    assertEquals(asList("ACT", "ACT", "ACT"), codons);
  }

  @Test
  public void convertToCodonsNonMultipleOf3WithoutErrorLowerCase() {
    String sequence = new String("actactact");

    List<String> codons = SequenceUtils.toCodons(sequence, true).collect(toList());
    assertEquals(asList("act", "act", "act"), codons);
  }

  @Test
  public void translateSequence() {
    String sequence = "ATGGAGGAT---";
    String expectedTranslation = "MED-";

    String translationAtFrame1 = SequenceUtils.translate(sequence, false, SequenceUtils.STANDARD_CODON_TABLE);
    assertEquals(expectedTranslation, translationAtFrame1);
  }
  
  @Test
  public void translateSequenceAtMultipleFrames() {
    String sequence = "ATGGAGGATCAAGTTGGGTTTGGGTTCCGTCCGAACGACGAGGAGCTCGTTGGTCACTATCTCCGTAACAAAATCGAAGGAAACACTAG"
      + "CCGCGACGTTGAAGTAGCCATCAGCGAGGTCAACATCTGTAGCTACGATCCTTGGAACTTGCGCTTCCAGT";
    String expectedTranslationAtFrame1 = "MEDQVGFGFRPNDEELVGHYLRNKIEGNTSRDVEVAISEVNICSYDPWNLRFQ";
    String expectedTranslationAtFrame2 = "WRIKLGLGSVRTTRSSLVTISVTKSKETLAATLK" + SequenceUtils.STOP_CODON +
      "PSARSTSVATILGTCASS";
    String expectedTranslationAtFrame3 = "GGSSWVWVPSERRGARWSLSP" + SequenceUtils.STOP_CODON + "QNRRKH" +
      SequenceUtils.STOP_CODON + "PRR" + SequenceUtils.STOP_CODON + "SSHQRGQHL" + SequenceUtils.STOP_CODON + "LRSLELALP";

    String translationAtFrame1 = SequenceUtils.translate(sequence, false, SequenceUtils.STANDARD_CODON_TABLE);
    String translationAtFrame2 = SequenceUtils.translate(sequence, false, 2, SequenceUtils.STANDARD_CODON_TABLE);
    String translationAtFrame3 = SequenceUtils.translate(sequence, false, 3, SequenceUtils.STANDARD_CODON_TABLE);

    assertEquals(expectedTranslationAtFrame1, translationAtFrame1);
    assertEquals(expectedTranslationAtFrame2, translationAtFrame2);
    assertEquals(expectedTranslationAtFrame3, translationAtFrame3);
  }

  @Test
  public void reverseSequence() {
    assertEquals("GTCA-", SequenceUtils.reverse("-ACTG"));
    assertEquals("NVHDBMKWSRYCGUTA", SequenceUtils.reverse("ATUGCYRSWKMBDHVN"));
  }

  @Test
  public void reverseSequenceLowerCase() {
    assertEquals("gtca-", SequenceUtils.reverse("-actg"));
    assertEquals("nvhdbmkwsrycguta", SequenceUtils.reverse("atugcyrswkmbdhvn"));
  }

  @Test
  public void complementSequence() {
    assertEquals("-TGAC", SequenceUtils.complement("-ACTG"));
    assertEquals("TAACGRYSWMKVHDBN", SequenceUtils.complement("ATUGCYRSWKMBDHVN"));
  }

  @Test
  public void complementSequenceLowerCase() {
    assertEquals("-tgac", SequenceUtils.complement("-actg"));
    assertEquals("taacgryswmkvhdbn", SequenceUtils.complement("atugcyrswkmbdhvn"));
  }

  @Test
  public void reverseComplementSequence() {
    assertEquals("CAGT-", SequenceUtils.reverseComplement("-ACTG"));
    assertEquals("NBDHVKMWSYRGCAAT", SequenceUtils.reverseComplement("ATUGCYRSWKMBDHVN"));
  }

  @Test
  public void reverseComplementSequenceLowerCase() {
    assertEquals("cagt-", SequenceUtils.reverseComplement("-actg"));
    assertEquals("nbdhvkmwsyrgcaat", SequenceUtils.reverseComplement("atugcyrswkmbdhvn"));
  }

  @Test
  public void countBases() {
    Map<Character, Integer> counts = SequenceUtils.countBases("ACTGACTACA");

    assertEquals(4, counts.get('A').intValue());
    assertEquals(3, counts.get('C').intValue());
    assertEquals(2, counts.get('T').intValue());
    assertEquals(1, counts.get('G').intValue());
  }
}
