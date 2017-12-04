package org.sing_group.seda.bio;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SequenceUtilsTest {

  @Test
  public void convertToCodons() {
    String sequence = new String("ACTACTACT");

    List<String> codons = SequenceUtils.toCodons(sequence).collect(toList());
    Assert.assertEquals(asList("ACT", "ACT", "ACT"), codons);
  }

  @Test(expected = IllegalArgumentException.class)
  public void convertToCodonsNonMultipleOf3WithError() {
    String sequence = new String("ACTACTACTG");

    SequenceUtils.toCodons(sequence).collect(toList());
  }

  @Test
  public void convertToCodonsNonMultipleOf3WithoutError() {
    String sequence = new String("ACTACTACTG");

    List<String> codons = SequenceUtils.toCodons(sequence, true).collect(toList());
    Assert.assertEquals(asList("ACT", "ACT", "ACT"), codons);
  }

  @Test
  public void translateSequence() {
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

    Assert.assertEquals(expectedTranslationAtFrame1, translationAtFrame1);
    Assert.assertEquals(expectedTranslationAtFrame2, translationAtFrame2);
    Assert.assertEquals(expectedTranslationAtFrame3, translationAtFrame3);
  }

  @Test
  public void reverseSequence() {
    String sequence = "ACTG";
    Assert.assertEquals("CAGT", SequenceUtils.reverseComplement(sequence));
  }
}
