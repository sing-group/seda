package org.sing_group.seda.bio;

import static java.util.Arrays.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.Sequence;

public final class SequenceUtils {

  /**
   * Standard DNA codon table. Source:
   * https://en.wikipedia.org/wiki/DNA_codon_table
   */
  public static final String STOP_CODON = "*";
  public static final String NON_CODON = "1";
  public static final Map<String, String> STANDARD_CODON_TABLE = new HashMap<>();

  static {
    STANDARD_CODON_TABLE.put("TTT", "F");
    STANDARD_CODON_TABLE.put("TTC", "F");

    STANDARD_CODON_TABLE.put("TTA", "L");
    STANDARD_CODON_TABLE.put("TTG", "L");
    STANDARD_CODON_TABLE.put("CTT", "L");
    STANDARD_CODON_TABLE.put("CTC", "L");
    STANDARD_CODON_TABLE.put("CTA", "L");
    STANDARD_CODON_TABLE.put("CTG", "L");

    STANDARD_CODON_TABLE.put("ATT", "I");
    STANDARD_CODON_TABLE.put("ATC", "I");
    STANDARD_CODON_TABLE.put("ATA", "I");

    STANDARD_CODON_TABLE.put("ATG", "M");

    STANDARD_CODON_TABLE.put("GTT", "V");
    STANDARD_CODON_TABLE.put("GTC", "V");
    STANDARD_CODON_TABLE.put("GTA", "V");
    STANDARD_CODON_TABLE.put("GTG", "V");

    STANDARD_CODON_TABLE.put("TCT", "S");
    STANDARD_CODON_TABLE.put("TCC", "S");
    STANDARD_CODON_TABLE.put("TCA", "S");
    STANDARD_CODON_TABLE.put("TCG", "S");

    STANDARD_CODON_TABLE.put("CCT", "P");
    STANDARD_CODON_TABLE.put("CCC", "P");
    STANDARD_CODON_TABLE.put("CCA", "P");
    STANDARD_CODON_TABLE.put("CCG", "P");

    STANDARD_CODON_TABLE.put("ACT", "T");
    STANDARD_CODON_TABLE.put("ACC", "T");
    STANDARD_CODON_TABLE.put("ACA", "T");
    STANDARD_CODON_TABLE.put("ACG", "T");

    STANDARD_CODON_TABLE.put("GCT", "A");
    STANDARD_CODON_TABLE.put("GCC", "A");
    STANDARD_CODON_TABLE.put("GCA", "A");
    STANDARD_CODON_TABLE.put("GCG", "A");

    STANDARD_CODON_TABLE.put("TAT", "Y");
    STANDARD_CODON_TABLE.put("TAC", "Y");

    STANDARD_CODON_TABLE.put("TAA", STOP_CODON);
    STANDARD_CODON_TABLE.put("TAG", STOP_CODON);

    STANDARD_CODON_TABLE.put("CAT", "H");
    STANDARD_CODON_TABLE.put("CAC", "H");

    STANDARD_CODON_TABLE.put("CAA", "Q");
    STANDARD_CODON_TABLE.put("CAG", "Q");

    STANDARD_CODON_TABLE.put("AAT", "N");
    STANDARD_CODON_TABLE.put("AAC", "N");

    STANDARD_CODON_TABLE.put("AAA", "K");
    STANDARD_CODON_TABLE.put("AAG", "K");

    STANDARD_CODON_TABLE.put("GAT", "D");
    STANDARD_CODON_TABLE.put("GAC", "D");

    STANDARD_CODON_TABLE.put("GAA", "E");
    STANDARD_CODON_TABLE.put("GAG", "E");

    STANDARD_CODON_TABLE.put("TGT", "C");
    STANDARD_CODON_TABLE.put("TGC", "C");

    STANDARD_CODON_TABLE.put("TGA", STOP_CODON);

    STANDARD_CODON_TABLE.put("TGG", "W");

    STANDARD_CODON_TABLE.put("CGT", "R");
    STANDARD_CODON_TABLE.put("CGC", "R");
    STANDARD_CODON_TABLE.put("CGA", "R");
    STANDARD_CODON_TABLE.put("CGG", "R");

    STANDARD_CODON_TABLE.put("AGT", "S");
    STANDARD_CODON_TABLE.put("AGC", "S");

    STANDARD_CODON_TABLE.put("AGA", "R");
    STANDARD_CODON_TABLE.put("AGG", "R");

    STANDARD_CODON_TABLE.put("GGT", "G");
    STANDARD_CODON_TABLE.put("GGC", "G");
    STANDARD_CODON_TABLE.put("GGA", "G");
    STANDARD_CODON_TABLE.put("GGG", "G");
  }

  private SequenceUtils() {}

  public static Stream<String> toCodons(Sequence sequence) {
    return toCodons(sequence.getChain(), false);
  }

  public static Stream<String> toCodons(Sequence sequence, boolean ignoreExtraNucleotides) {
    return toCodons(sequence.getChain(), ignoreExtraNucleotides);
  }

  public static Stream<String> toCodons(String chain) {
    return toCodons(chain, false);
  }

  public static Stream<String> toCodons(String chain, boolean ignoreExtraNucleotides) {
    if (chain.length() % 3 != 0 && !ignoreExtraNucleotides) {
      throw new IllegalArgumentException("Sequence length must be multiple of 3");
    } else if (!isGene(chain)) {
      throw new IllegalArgumentException("Only gene sequences are allowed");
    } else {
      final int numCodons = chain.length() / 3;

      final String[] codons = new String[numCodons];
      for (int i = 0; i < numCodons; i++) {
        codons[i] = chain.substring(i * 3, (i + 1) * 3);
      }

      return stream(codons);
    }
  }

  public static boolean isDNA(Sequence sequence) {
    return isDNA(sequence.getChain());
  }

  public static boolean isDNA(String chain) {
    return chain.toUpperCase().replaceAll("[ACGT-]", "").isEmpty();
  }

  public static boolean isRNA(Sequence sequence) {
    return isRNA(sequence.getChain());
  }

  public static boolean isRNA(String chain) {
    return chain.toUpperCase().replaceAll("[ACGU-]", "").isEmpty();
  }

  public static boolean isGene(Sequence sequence) {
    return isGene(sequence.getChain());
  }

  public static boolean isGene(String chain) {
    return chain.toUpperCase().replaceAll("[ACGTU-]", "").isEmpty();
  }

  public static boolean isProtein(Sequence sequence) {
    return isProtein(sequence.getChain());
  }

  public static boolean isProtein(String chain) {
    return chain.toUpperCase().replaceAll("[ABCDEFGHIKLMNPQRSTVWYZ-]", "").isEmpty();
  }

  public static String translate(String chain, boolean reverseComplement, Map<String, String> codonTable) {
    return translate(chain, reverseComplement, 1, codonTable);
  }

  public static String translate(String chain, boolean reverseComplement, int frame, Map<String, String> codonTable) {
    if (frame < 1 || frame > 3) {
      throw new IllegalArgumentException("Starting frame must be 1, 2 or 3");
    }

    if(reverseComplement) {
      chain = reverseComplement(chain);
    }

    String effectiveChain = chain.substring(frame - 1);
    Stream<String> codons = toCodons(effectiveChain, true);

    return codons.map(c -> codonTable.getOrDefault(c, NON_CODON)).collect(Collectors.joining());
  }

  public static String reverseComplement(String chain) {
    StringBuilder reversed = new StringBuilder();
    for (int i = 0; i < chain.length(); i++) {
      switch (chain.charAt(i)) {
        case 'A':
          reversed.append("T");
          break;
        case 'T':
          reversed.append("A");
          break;
        case 'C':
          reversed.append("G");
          break;
        case 'G':
          reversed.append("C");
          break;
        default:
          reversed.append(chain.charAt(i));
      }
    }
    return new StringBuilder(reversed.toString()).reverse().toString();
  }

  public static Map<Character, Integer> countBases(String s) {
    Map<Character, Integer> toret = new HashMap<>();
    for (Character c : s.toCharArray()) {
      toret.putIfAbsent(c, 0);
      toret.put(c, toret.get(c) + 1);
    }
    return toret;
  }
}
