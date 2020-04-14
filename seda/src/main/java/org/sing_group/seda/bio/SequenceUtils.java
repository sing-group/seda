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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.InMemorySequence;
import org.sing_group.seda.datatype.Sequence;

public final class SequenceUtils {

  /**
   * Standard DNA codon table. Source:
   * https://en.wikipedia.org/wiki/DNA_codon_table
   */
  public static final String STOP_CODON = "*";
  public static final String NON_CODON = "1";

  public static final Map<String, String> STANDARD_CODON_TABLE;

  static {
    Map<String, String> standardCodonTable = new HashMap<>();

    standardCodonTable.put("---", "-");

    standardCodonTable.put("TTT", "F");
    standardCodonTable.put("TTC", "F");

    standardCodonTable.put("TTA", "L");
    standardCodonTable.put("TTG", "L");
    standardCodonTable.put("CTT", "L");
    standardCodonTable.put("CTC", "L");
    standardCodonTable.put("CTA", "L");
    standardCodonTable.put("CTG", "L");

    standardCodonTable.put("ATT", "I");
    standardCodonTable.put("ATC", "I");
    standardCodonTable.put("ATA", "I");

    standardCodonTable.put("ATG", "M");

    standardCodonTable.put("GTT", "V");
    standardCodonTable.put("GTC", "V");
    standardCodonTable.put("GTA", "V");
    standardCodonTable.put("GTG", "V");

    standardCodonTable.put("TCT", "S");
    standardCodonTable.put("TCC", "S");
    standardCodonTable.put("TCA", "S");
    standardCodonTable.put("TCG", "S");

    standardCodonTable.put("CCT", "P");
    standardCodonTable.put("CCC", "P");
    standardCodonTable.put("CCA", "P");
    standardCodonTable.put("CCG", "P");

    standardCodonTable.put("ACT", "T");
    standardCodonTable.put("ACC", "T");
    standardCodonTable.put("ACA", "T");
    standardCodonTable.put("ACG", "T");

    standardCodonTable.put("GCT", "A");
    standardCodonTable.put("GCC", "A");
    standardCodonTable.put("GCA", "A");
    standardCodonTable.put("GCG", "A");

    standardCodonTable.put("TAT", "Y");
    standardCodonTable.put("TAC", "Y");

    standardCodonTable.put("TAA", STOP_CODON);
    standardCodonTable.put("TAG", STOP_CODON);

    standardCodonTable.put("CAT", "H");
    standardCodonTable.put("CAC", "H");

    standardCodonTable.put("CAA", "Q");
    standardCodonTable.put("CAG", "Q");

    standardCodonTable.put("AAT", "N");
    standardCodonTable.put("AAC", "N");

    standardCodonTable.put("AAA", "K");
    standardCodonTable.put("AAG", "K");

    standardCodonTable.put("GAT", "D");
    standardCodonTable.put("GAC", "D");

    standardCodonTable.put("GAA", "E");
    standardCodonTable.put("GAG", "E");

    standardCodonTable.put("TGT", "C");
    standardCodonTable.put("TGC", "C");

    standardCodonTable.put("TGA", STOP_CODON);

    standardCodonTable.put("TGG", "W");

    standardCodonTable.put("CGT", "R");
    standardCodonTable.put("CGC", "R");
    standardCodonTable.put("CGA", "R");
    standardCodonTable.put("CGG", "R");

    standardCodonTable.put("AGT", "S");
    standardCodonTable.put("AGC", "S");

    standardCodonTable.put("AGA", "R");
    standardCodonTable.put("AGG", "R");

    standardCodonTable.put("GGT", "G");
    standardCodonTable.put("GGC", "G");
    standardCodonTable.put("GGA", "G");
    standardCodonTable.put("GGG", "G");

    STANDARD_CODON_TABLE = Collections.unmodifiableMap(standardCodonTable);
  }

  private SequenceUtils() {}

  public static Stream<String> toCodons(Sequence sequence) {
    return toCodons(sequence.getChain(), false, false);
  }

  public static Stream<String> toCodons(Sequence sequence, boolean ignoreExtraNucleotides) {
    return toCodons(sequence.getChain(), ignoreExtraNucleotides, true);
  }
  
  public static Stream<String> toCodons(Sequence sequence, boolean ignoreExtraNucleotides, boolean checkGeneSequence) {
    return toCodons(sequence.getChain(), ignoreExtraNucleotides, checkGeneSequence);
  }

  public static Stream<String> toCodons(String chain) {
    return toCodons(chain, false, false);
  }

  public static Stream<String> toCodons(String chain, boolean ignoreExtraNucleotides) {
    return toCodons(chain, ignoreExtraNucleotides, true);
  }
  
  public static Stream<String> toCodons(String chain, boolean ignoreExtraNucleotides, boolean checkGeneSequence) {
    if (chain.length() % 3 != 0 && !ignoreExtraNucleotides) {
      throw new IllegalArgumentException("Sequence length must be multiple of 3");
    } else if (checkGeneSequence && !isGene(chain)) {
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

  public static boolean isGene(Sequence sequence) {
    return isGene(sequence.getChain());
  }

  public static boolean isGene(String chain) {
    return chain.toUpperCase().replaceAll("[ACGTU-]", "").isEmpty();
  }

  public static String translate(String chain, boolean reverseComplement, Map<String, String> codonTable) {
    return translate(chain, reverseComplement, 1, codonTable);
  }

  public static String translate(String chain, boolean reverseComplement, int frame, Map<String, String> codonTable) {
    if (frame < 1 || frame > 3) {
      throw new IllegalArgumentException("Starting frame must be 1, 2 or 3");
    }

    chain = chain.toUpperCase();

    if (reverseComplement) {
      chain = reverseComplement(chain);
    }

    String effectiveChain = chain.substring(frame - 1);
    Stream<String> codons = toCodons(effectiveChain, true);

    return codons.map(c -> codonTable.getOrDefault(c, NON_CODON)).collect(joining());
  }

  public static List<Sequence> reverseComplement(List<Sequence> sequences) {
    return reverseComplement(sequences.stream());
  }

  public static List<Sequence> reverseComplement(Stream<Sequence> sequences) {
    return sequences.map(SequenceUtils::reverseComplement).collect(toList());
  }

  public static Sequence reverseComplement(Sequence sequence) {
    return new InMemorySequence(
      sequence.getName(), sequence.getDescription(), reverseComplement(sequence.getChain()), sequence.getProperties()
    );
  }

  public static String reverse(String chain) {
    return new StringBuilder(chain).reverse().toString();
  }

  public static String reverseComplement(String chain) {
    return reverse(complement(chain));
  }

  public static String complement(String chain) {
    StringBuilder reversed = new StringBuilder();
    for (int i = 0; i < chain.length(); i++) {
      switch (chain.charAt(i)) {
        case 'A':
          reversed.append("T");
          break;
        case 'a':
          reversed.append("t");
          break;
        case 'T':
          reversed.append("A");
          break;
        case 't':
          reversed.append("a");
          break;
        case 'U':
          reversed.append("A");
          break;
        case 'u':
          reversed.append("a");
          break;
        case 'C':
          reversed.append("G");
          break;
        case 'c':
          reversed.append("g");
          break;
        case 'G':
          reversed.append("C");
          break;
        case 'g':
          reversed.append("c");
          break;
        case 'Y':
          reversed.append("R");
          break;
        case 'y':
          reversed.append("r");
          break;
        case 'R':
          reversed.append("Y");
          break;
        case 'r':
          reversed.append("y");
          break;
        case 'S':
          reversed.append("S");
          break;
        case 's':
          reversed.append("s");
          break;
        case 'W':
          reversed.append("W");
          break;
        case 'w':
          reversed.append("w");
          break;
        case 'K':
          reversed.append("M");
          break;
        case 'k':
          reversed.append("m");
          break;
        case 'M':
          reversed.append("K");
          break;
        case 'm':
          reversed.append("k");
          break;
        case 'B':
          reversed.append("V");
          break;
        case 'b':
          reversed.append("v");
          break;
        case 'D':
          reversed.append("H");
          break;
        case 'd':
          reversed.append("h");
          break;
        case 'H':
          reversed.append("D");
          break;
        case 'h':
          reversed.append("d");
          break;
        case 'V':
          reversed.append("B");
          break;
        case 'v':
          reversed.append("b");
          break;
        case 'N':
          reversed.append("N");
          break;
        case 'n':
          reversed.append("n");
          break;
        default:
          reversed.append(chain.charAt(i));
      }
    }
    return reversed.toString();
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
