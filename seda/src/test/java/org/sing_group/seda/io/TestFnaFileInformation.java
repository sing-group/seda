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
package org.sing_group.seda.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.sing_group.seda.datatype.Sequence;

public final class TestFnaFileInformation {
  private TestFnaFileInformation() {}
  
  public static Path testFnaPath() {
    return Paths.get("src/test/resources/fasta/test.fna");
  }
  
  public static String sequenceGroupName() {
    return "test.fna";
  }

  public static int sequencesCount() {
    return 5;
  }
  
  public static Sequence[] getSequences() {
    return new Sequence[] {
      Sequence.of(
        "lcl|NC_003070.9_cds_NP_171609.1_1",
        "[gene=NAC001] [locus_tag=AT1G01010] [db_xref=Araport:AT1G01010,TAIR:AT1G01010,GeneID:839580]" +
        " [protein=NAC domain containing protein 1] [protein_id=NP_171609.1]" +
        " [location=join(3760..3913,3996..4276,4486..4605,4706..5095,5174..5326,5439..5630)]",
        "ATGGAGGATCAAGTTGGGTTTGGGTTCCGTCCGAACGACGAGGAGCTCGTTGGTCACTATCTCCGTAACAAAATCGAAGG" +
        "AAACACTAGCCGCGACGTTGAAGTAGCCATCAGCGAGGTCAACATCTGTAGCTACGATCCTTGGAACTTGCGCTTCCAGT" +
        "CAAAGTACAAATCGAGAGATGCTATGTGGTACTTCTTCTCTCGTAGAGAAAACAACAAAGGGAATCGACAGAGCAGGACA" +
        "ACGGTTTCTGGTAAATGGAAGCTTACCGGAGAATCTGTTGAGGTCAAGGACCAGTGGGGATTTTGTAGTGAGGGCTTTCG" +
        "TGGTAAGATTGGTCATAAAAGGGTTTTGGTGTTCCTCGATGGAAGATACCCTGACAAAACCAAATCTGATTGGGTTATCC" +
        "ACGAGTTCCACTACGACCTCTTACCAGAACATCAGAGGACATATGTCATCTGCAGACTTGAGTACAAGGGTGATGATGCG" +
        "GACATTCTATCTGCTTATGCAATAGATCCCACTCCCGCTTTTGTCCCCAATATGACTAGTAGTGCAGGTTCTGTGGTCAA" +
        "CCAATCACGTCAACGAAATTCAGGATCTTACAACACTTACTCTGAGTATGATTCAGCAAATCATGGCCAGCAGTTTAATG" +
        "AAAACTCTAACATTATGCAGCAGCAACCACTTCAAGGATCATTCAACCCTCTCCTTGAGTATGATTTTGCAAATCACGGC" +
        "GGTCAGTGGCTGAGTGACTATATCGACCTGCAACAGCAAGTTCCTTACTTGGCACCTTATGAAAATGAGTCGGAGATGAT" +
        "TTGGAAGCATGTGATTGAAGAAAATTTTGAGTTTTTGGTAGATGAAAGGACATCTATGCAACAGCATTACAGTGATCACC" +
        "GGCCCAAAAAACCTGTGTCTGGGGTTTTGCCTGATGATAGCAGTGATACTGAAACTGGATCAATGATTTTCGAAGACACT" +
        "TCGAGCTCCACTGATAGTGTTGGTAGTTCAGATGAACCGGGCCATACTCGTATAGATGATATTCCATCATTGAACATTAT" +
        "TGAGCCTTTGCACAATTATAAGGCACAAGAGCAACCAAAGCAGCAGAGCAAAGAAAAGGTGATAAGTTCGCAGAAAAGCG" +
        "AATGCGAGTGGAAAATGGCTGAAGACTCGATCAAGATACCTCCATCCACCAACACGGTGAAGCAGAGCTGGATTGTTTTG" +
        "GAGAATGCACAGTGGAACTATCTCAAGAACATGATCATTGGTGTCTTGTTGTTCATCTCCGTCATTAGTTGGATCATTCT" +
        "TGTTGGTTAA",
        Collections.singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lcl|NC_003070.9_cds_NP_001318899.1_2",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,TAIR:AT1G01020,GeneID:839569]" +
        " [protein=ARV1 family protein] [protein_id=NP_001318899.1]" +
        " [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8464,8571..8666))]",
        "ATGGCGGCGAGTGAACACAGATGCGTGGGATGTGGTTTTAGGGTAAAGTCATTGTTCATTCAATACTCTCCGGGTAACAT" +
        "TCGTCTCATGAAATGCGGAAATTGCAAGGAAGTAGCAGATGAGTACATCGAGTGTGAACGCATGATTATTTTCATCGATT" +
        "TAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTG" +
        "TTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAG" +
        "CTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTT" +
        "TTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTAC" +
        "TTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAAC" +
        "ATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGA" +
        "TTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTAT" +
        "TTTTTCAGAATCGTATGA",
        Collections.singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lcl|NC_003070.9_cds_NP_001321777.1_3",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] " +
        "[protein=ARV1 family protein] [protein_id=NP_001321777.1] " +
        "[location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        "ATGAGTACATCGAGTGTGAACGCATGGTCTGTTTTAATCACTTTCTTTCCCTTTTTGGGTGTTTCCCTATTTCTTTCCAA" +
        "TTTTGGTTTTTGGTTCTTTAATTGTTTTCTTTGGCAGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATA" +
        "GACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTT" +
        "CTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATC" +
        "TATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAA" +
        "ATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTG" +
        "GTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGAT" +
        "GACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTG" +
        "AGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        Collections.singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lcl|NC_003070.9_cds_NP_001321775.1_4",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] " +
        "[protein=ARV1 family protein] [protein_id=NP_001321775.1] " +
        "[location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        "ATGAGTACATCGAGTGTGAACGCATGGTCTGTTTTAATCACTTTCTTTCCCTTTTTGGGTGTTTCCCTATTTCTTTCCAA" +
        "TTTTGGTTTTTGGTTCTTTAATTGTTTTCTTTGGCAGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATA" +
        "GACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTT" +
        "CTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATC" +
        "TATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAA" +
        "ATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTG" +
        "GTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGAT" +
        "GACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTG" +
        "AGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        Collections.singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lcl|NC_003070.9_cds_NP_001321776.1_5",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] " +
        "[protein=ARV1 family protein] [protein_id=NP_001321776.1] " +
        "[location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8419))]",
        "ATGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAAC" +
        "TGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAA" +
        "AAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAAC" +
        "GCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGG" +
        "GATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTG" +
        "TCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTA" +
        "TGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCT" +
        "GTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        Collections.singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      )
    };
  }
  
}