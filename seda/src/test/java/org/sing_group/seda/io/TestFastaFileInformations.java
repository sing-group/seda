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
package org.sing_group.seda.io;

import static java.util.Collections.singletonMap;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.io.FastaReader.SequenceTextInfo;
import org.sing_group.seda.io.FastaReader.SequenceLocationsInfo;

public final class TestFastaFileInformations {
  private TestFastaFileInformations() {}
  
  private static Sequence[] getFnaSequences() {
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      )
    };
  }
  
  private static SequenceTextInfo[] getFnaSequenceTextInfos(Path file) {
    return new SequenceTextInfo[] {
      new SequenceTextInfo(
        "lcl|NC_003070.9_cds_NP_171609.1_1",
        "[gene=NAC001] [locus_tag=AT1G01010] [db_xref=Araport:AT1G01010,TAIR:AT1G01010,GeneID:839580] [protein=NAC domain containing protein 1] [protein_id=NP_171609.1] [location=join(3760..3913,3996..4276,4486..4605,4706..5095,5174..5326,5439..5630)]",
        ">lcl|NC_003070.9_cds_NP_171609.1_1 [gene=NAC001] [locus_tag=AT1G01010] [db_xref=Araport:AT1G01010,TAIR:AT1G01010,GeneID:839580] [protein=NAC domain containing protein 1] [protein_id=NP_171609.1] [location=join(3760..3913,3996..4276,4486..4605,4706..5095,5174..5326,5439..5630)]",
        "ATGGAGGATCAAGTTGGGTTTGGGTTCCGTCCGAACGACGAGGAGCTCGTTGGTCACTATCTCCGTAACAAAATCGAAGGAAACACTAGCCGCGACGTTGAAGTAGCCATCAGCGAGGTCAACATCTGTAGCTACGATCCTTGGAACTTGCGCTTCCAGTCAAAGTACAAATCGAGAGATGCTATGTGGTACTTCTTCTCTCGTAGAGAAAACAACAAAGGGAATCGACAGAGCAGGACAACGGTTTCTGGTAAATGGAAGCTTACCGGAGAATCTGTTGAGGTCAAGGACCAGTGGGGATTTTGTAGTGAGGGCTTTCGTGGTAAGATTGGTCATAAAAGGGTTTTGGTGTTCCTCGATGGAAGATACCCTGACAAAACCAAATCTGATTGGGTTATCCACGAGTTCCACTACGACCTCTTACCAGAACATCAGAGGACATATGTCATCTGCAGACTTGAGTACAAGGGTGATGATGCGGACATTCTATCTGCTTATGCAATAGATCCCACTCCCGCTTTTGTCCCCAATATGACTAGTAGTGCAGGTTCTGTGGTCAACCAATCACGTCAACGAAATTCAGGATCTTACAACACTTACTCTGAGTATGATTCAGCAAATCATGGCCAGCAGTTTAATGAAAACTCTAACATTATGCAGCAGCAACCACTTCAAGGATCATTCAACCCTCTCCTTGAGTATGATTTTGCAAATCACGGCGGTCAGTGGCTGAGTGACTATATCGACCTGCAACAGCAAGTTCCTTACTTGGCACCTTATGAAAATGAGTCGGAGATGATTTGGAAGCATGTGATTGAAGAAAATTTTGAGTTTTTGGTAGATGAAAGGACATCTATGCAACAGCATTACAGTGATCACCGGCCCAAAAAACCTGTGTCTGGGGTTTTGCCTGATGATAGCAGTGATACTGAAACTGGATCAATGATTTTCGAAGACACTTCGAGCTCCACTGATAGTGTTGGTAGTTCAGATGAACCGGGCCATACTCGTATAGATGATATTCCATCATTGAACATTATTGAGCCTTTGCACAATTATAAGGCACAAGAGCAACCAAAGCAGCAGAGCAAAGAAAAGGTGATAAGTTCGCAGAAAAGCGAATGCGAGTGGAAAATGGCTGAAGACTCGATCAAGATACCTCCATCCACCAACACGGTGAAGCAGAGCTGGATTGTTTTGGAGAATGCACAGTGGAACTATCTCAAGAACATGATCATTGGTGTCTTGTTGTTCATCTCCGTCATTAGTTGGATCATTCTTGTTGGTTAA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lcl|NC_003070.9_cds_NP_001318899.1_2",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,TAIR:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001318899.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8464,8571..8666))]",
        ">lcl|NC_003070.9_cds_NP_001318899.1_2 [gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,TAIR:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001318899.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8464,8571..8666))]",
        "ATGGCGGCGAGTGAACACAGATGCGTGGGATGTGGTTTTAGGGTAAAGTCATTGTTCATTCAATACTCTCCGGGTAACATTCGTCTCATGAAATGCGGAAATTGCAAGGAAGTAGCAGATGAGTACATCGAGTGTGAACGCATGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lcl|NC_003070.9_cds_NP_001321777.1_3",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001321777.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        ">lcl|NC_003070.9_cds_NP_001321777.1_3 [gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001321777.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        "ATGAGTACATCGAGTGTGAACGCATGGTCTGTTTTAATCACTTTCTTTCCCTTTTTGGGTGTTTCCCTATTTCTTTCCAATTTTGGTTTTTGGTTCTTTAATTGTTTTCTTTGGCAGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lcl|NC_003070.9_cds_NP_001321775.1_4",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001321775.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        ">lcl|NC_003070.9_cds_NP_001321775.1_4 [gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001321775.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        "ATGAGTACATCGAGTGTGAACGCATGGTCTGTTTTAATCACTTTCTTTCCCTTTTTGGGTGTTTCCCTATTTCTTTCCAATTTTGGTTTTTGGTTCTTTAATTGTTTTCTTTGGCAGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lcl|NC_003070.9_cds_NP_001321776.1_5",
        "[gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001321776.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8419))]",
        ">lcl|NC_003070.9_cds_NP_001321776.1_5 [gene=ARV1] [locus_tag=AT1G01020] [db_xref=Araport:AT1G01020,GeneID:839569] [protein=ARV1 family protein] [protein_id=NP_001321776.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8419))]",
        "ATGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      )
    };
  }
  
  private static SequenceLocationsInfo[] getFnaSequenceLocationInfos(Path file) {
    return new SequenceLocationsInfo[] {
      new SequenceLocationsInfo(
        file, Charset.forName("US-ASCII"),
        1, 33,
        35, 242,
        0, 277,
        278, 1306,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("US-ASCII"),
        1588, 36,
        1625, 276,
        1587, 314,
        1902, 747,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("US-ASCII"),
        2651, 36,
        2688, 239,
        2650, 277,
        2928, 719,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("US-ASCII"),
        3649, 36,
        3686, 239,
        3648, 277,
        3926, 719,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("US-ASCII"),
        4647, 36,
        4684, 250,
        4646, 288,
        4935, 604,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),

    };
  }
  
  private static Sequence[] getFnaMultibyteCharsSequences() {
    return new Sequence[] {
      Sequence.of(
        "lĉl|NC_003070.9_cds_NP_171609.1_1",
        "[gené=NAC001] [lócus_tag=AT1G01010] [db_xref=Araport:AT1G01010,TAIR:AT1G01010,genéID:839580]" +
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lĉl|NC_003070.9_cds_NP_001318899.1_2",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,TAIR:AT1G01020,genéID:839569]" +
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lĉl|NC_003070.9_cds_NP_001321777.1_3",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] " +
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lĉl|NC_003070.9_cds_NP_001321775.1_4",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] " +
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      Sequence.of(
        "lĉl|NC_003070.9_cds_NP_001321776.1_5",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] " +
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
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      )
    };
  }
  
  private static SequenceTextInfo[] getFnaMultibyteCharsSequenceTextInfos(Path file) {
    return new SequenceTextInfo[] {
      new SequenceTextInfo(
        "lĉl|NC_003070.9_cds_NP_171609.1_1",
        "[gené=NAC001] [lócus_tag=AT1G01010] [db_xref=Araport:AT1G01010,TAIR:AT1G01010,genéID:839580] [protein=NAC domain containing protein 1] [protein_id=NP_171609.1] [location=join(3760..3913,3996..4276,4486..4605,4706..5095,5174..5326,5439..5630)]",
        ">lĉl|NC_003070.9_cds_NP_171609.1_1 [gené=NAC001] [lócus_tag=AT1G01010] [db_xref=Araport:AT1G01010,TAIR:AT1G01010,genéID:839580] [protein=NAC domain containing protein 1] [protein_id=NP_171609.1] [location=join(3760..3913,3996..4276,4486..4605,4706..5095,5174..5326,5439..5630)]",
        "ATGGAGGATCAAGTTGGGTTTGGGTTCCGTCCGAACGACGAGGAGCTCGTTGGTCACTATCTCCGTAACAAAATCGAAGGAAACACTAGCCGCGACGTTGAAGTAGCCATCAGCGAGGTCAACATCTGTAGCTACGATCCTTGGAACTTGCGCTTCCAGTCAAAGTACAAATCGAGAGATGCTATGTGGTACTTCTTCTCTCGTAGAGAAAACAACAAAGGGAATCGACAGAGCAGGACAACGGTTTCTGGTAAATGGAAGCTTACCGGAGAATCTGTTGAGGTCAAGGACCAGTGGGGATTTTGTAGTGAGGGCTTTCGTGGTAAGATTGGTCATAAAAGGGTTTTGGTGTTCCTCGATGGAAGATACCCTGACAAAACCAAATCTGATTGGGTTATCCACGAGTTCCACTACGACCTCTTACCAGAACATCAGAGGACATATGTCATCTGCAGACTTGAGTACAAGGGTGATGATGCGGACATTCTATCTGCTTATGCAATAGATCCCACTCCCGCTTTTGTCCCCAATATGACTAGTAGTGCAGGTTCTGTGGTCAACCAATCACGTCAACGAAATTCAGGATCTTACAACACTTACTCTGAGTATGATTCAGCAAATCATGGCCAGCAGTTTAATGAAAACTCTAACATTATGCAGCAGCAACCACTTCAAGGATCATTCAACCCTCTCCTTGAGTATGATTTTGCAAATCACGGCGGTCAGTGGCTGAGTGACTATATCGACCTGCAACAGCAAGTTCCTTACTTGGCACCTTATGAAAATGAGTCGGAGATGATTTGGAAGCATGTGATTGAAGAAAATTTTGAGTTTTTGGTAGATGAAAGGACATCTATGCAACAGCATTACAGTGATCACCGGCCCAAAAAACCTGTGTCTGGGGTTTTGCCTGATGATAGCAGTGATACTGAAACTGGATCAATGATTTTCGAAGACACTTCGAGCTCCACTGATAGTGTTGGTAGTTCAGATGAACCGGGCCATACTCGTATAGATGATATTCCATCATTGAACATTATTGAGCCTTTGCACAATTATAAGGCACAAGAGCAACCAAAGCAGCAGAGCAAAGAAAAGGTGATAAGTTCGCAGAAAAGCGAATGCGAGTGGAAAATGGCTGAAGACTCGATCAAGATACCTCCATCCACCAACACGGTGAAGCAGAGCTGGATTGTTTTGGAGAATGCACAGTGGAACTATCTCAAGAACATGATCATTGGTGTCTTGTTGTTCATCTCCGTCATTAGTTGGATCATTCTTGTTGGTTAA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lĉl|NC_003070.9_cds_NP_001318899.1_2",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,TAIR:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001318899.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8464,8571..8666))]",
        ">lĉl|NC_003070.9_cds_NP_001318899.1_2 [gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,TAIR:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001318899.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8464,8571..8666))]",
        "ATGGCGGCGAGTGAACACAGATGCGTGGGATGTGGTTTTAGGGTAAAGTCATTGTTCATTCAATACTCTCCGGGTAACATTCGTCTCATGAAATGCGGAAATTGCAAGGAAGTAGCAGATGAGTACATCGAGTGTGAACGCATGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lĉl|NC_003070.9_cds_NP_001321777.1_3",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001321777.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        ">lĉl|NC_003070.9_cds_NP_001321777.1_3 [gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001321777.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        "ATGAGTACATCGAGTGTGAACGCATGGTCTGTTTTAATCACTTTCTTTCCCTTTTTGGGTGTTTCCCTATTTCTTTCCAATTTTGGTTTTTGGTTCTTTAATTGTTTTCTTTGGCAGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lĉl|NC_003070.9_cds_NP_001321775.1_4",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001321775.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        ">lĉl|NC_003070.9_cds_NP_001321775.1_4 [gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001321775.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8442))]",
        "ATGAGTACATCGAGTGTGAACGCATGGTCTGTTTTAATCACTTTCTTTCCCTTTTTGGGTGTTTCCCTATTTCTTTCCAATTTTGGTTTTTGGTTCTTTAATTGTTTTCTTTGGCAGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceTextInfo(
        "lĉl|NC_003070.9_cds_NP_001321776.1_5",
        "[gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001321776.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8419))]",
        ">lĉl|NC_003070.9_cds_NP_001321776.1_5 [gené=ARV1] [lócus_tag=AT1G01020] [db_xref=Araport:AT1G01020,genéID:839569] [protein=ARV1 family protein] [protein_id=NP_001321776.1] [location=complement(join(6915..7069,7157..7232,7384..7450,7564..7649,7762..7835,7942..7987,8236..8325,8417..8419))]",
        "ATGATTATTTTCATCGATTTAATCCTTCACAGACCAAAGGTATATAGACACGTCCTCTACAATGCAATTAATCCAGCAACTGTCAATATTCAGCATCTGTTGTGGAAGTTGGTCTTCGCCTATCTTCTTCTAGACTGTTATAGAAGCTTGCTACTGAGAAAAAGTGATGAAGAATCGAGCTTTTCTGATAGCCCTGTTCTTCTATCTATAAAGGTTCTGATTGGTGTCTTATCTGCAAACGCTGCATTTATCATCTCTTTTGCCATTGCGACTAAGGGTTTGCTAAATGAAGTTTCCAGAAGAAGAGAGATTATGTTGGGGATATTCATCTCTAGTTACTTCAAGATATTTCTGCTTGCGATGTTGGTATGGGAATTCCCAATGTCAGTGATTTTTTTTGTCGATATACTTCTCTTAACATCAAACTCCATGGCTCTTAAAGTGATGACTGAATCAACAATGACCAGATGCATAGCCGTATGCTTAATCGCGCACTTGATTAGATTCTTGGTGGGTCAGATTTTTGAGCCGACAATATTTTTGATACAAATTGGATCTCTGTTGCAATATATGTCTTATTTTTTCAGAATCGTATGA",
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      )
    };
  }
  
  private static SequenceLocationsInfo[] getFnaMultibyteCharsSequenceLocationInfos(Path file) {
    return new SequenceLocationsInfo[] {
      new SequenceLocationsInfo(
        file, Charset.forName("UTF-8"),
        1, 34,
        36, 245,
        0, 281,
        282, 1306,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("UTF-8"),
        1592, 37,
        1630, 279,
        1591, 318,
        1910, 747,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("UTF-8"),
        2659, 37,
        2697, 242,
        2658, 281,
        2940, 719,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("UTF-8"),
        3661, 37,
        3699, 242,
        3660, 281,
        3942, 719,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      ),
      new SequenceLocationsInfo(
        file, Charset.forName("UTF-8"),
        4663, 37,
        4701, 253,
        4662, 292,
        4955, 604,
        80, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 80)
      )
    };
  }

  private static Sequence[] getSingleSequenceFileSequences() {
    return new Sequence[] {
      Sequence.of(
        "1",
        null,
        "ACTGACTGACTGACTG",
        singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 16)
      )
    };
  }

  private static SequenceTextInfo[] getSingleSequenceFileSequenceTextInfos(final Path path) {
    return new SequenceTextInfo[] {
      new SequenceTextInfo(
        "1",
        null,
        ">1",
        "ACTGACTGACTGACTG",
        16, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 16)
      )
    };
  }

  private static SequenceLocationsInfo[] getSingleSequenceFileSequenceLocationInfos(final Path path) {
    return new SequenceLocationsInfo[] {
      new SequenceLocationsInfo(
        path, Charset.forName("US-ASCII"),
        1, 1,
        -1, -1,
        0, 2,
        3, 16,
        16, singletonMap(Sequence.PROPERTY_CHAIN_COLUMNS, 16)
      )
    };
  }
  
  public static FastaFileInformation getFnaFileInformation() {
    final Path path = Paths.get("src/test/resources/fasta/test.fna");
    final Sequence[] sequences = getFnaSequences();
    final SequenceTextInfo[] sequenceTextInfos = getFnaSequenceTextInfos(path);
    final SequenceLocationsInfo[] sequenceLocationInfos = getFnaSequenceLocationInfos(path);

    return FastaFileInformation.of(path, sequences, sequenceTextInfos, sequenceLocationInfos);
  }
  
  public static FastaFileInformation getFnaGZipFileInformation() {
    final Path path = Paths.get("src/test/resources/fasta/test.fna.gz");
    final Sequence[] sequences = getFnaSequences();
    final SequenceTextInfo[] sequenceTextInfos = getFnaSequenceTextInfos(path);
    final SequenceLocationsInfo[] sequenceLocationInfos = getFnaSequenceLocationInfos(path);

    return FastaFileInformation.of(path, sequences, sequenceTextInfos, sequenceLocationInfos);
  }
  
  public static FastaFileInformation getFnaMultibyteCharsFileInformation() {
    final Path path = Paths.get("src/test/resources/fasta/test-multibyte-chars.fna");
    final Sequence[] sequences = getFnaMultibyteCharsSequences();
    final SequenceTextInfo[] sequenceTextInfos = getFnaMultibyteCharsSequenceTextInfos(path);
    final SequenceLocationsInfo[] sequenceLocationInfos = getFnaMultibyteCharsSequenceLocationInfos(path);

    return FastaFileInformation.of(path, sequences, sequenceTextInfos, sequenceLocationInfos);
  }
  
  public static FastaFileInformation getFnaMultibyteCharsGZipFileInformation() {
    final Path path = Paths.get("src/test/resources/fasta/test-multibyte-chars.fna.gz");
    final Sequence[] sequences = getFnaMultibyteCharsSequences();
    final SequenceTextInfo[] sequenceTextInfos = getFnaMultibyteCharsSequenceTextInfos(path);
    final SequenceLocationsInfo[] sequenceLocationInfos = getFnaMultibyteCharsSequenceLocationInfos(path);

    return FastaFileInformation.of(path, sequences, sequenceTextInfos, sequenceLocationInfos);
  }
  
  public static FastaFileInformation getSingleSequenceFileInformation() {
    final Path path = Paths.get("src/test/resources/fasta/single-sequence.fasta");
    final Sequence[] sequences = getSingleSequenceFileSequences();
    final SequenceTextInfo[] sequenceTextInfos = getSingleSequenceFileSequenceTextInfos(path);
    final SequenceLocationsInfo[] sequenceLocationInfos = getSingleSequenceFileSequenceLocationInfos(path);

    return FastaFileInformation.of(path, sequences, sequenceTextInfos, sequenceLocationInfos);
  }
  
  public static FastaFileInformation getSingleSequenceGZipFileInformation() {
    final Path path = Paths.get("src/test/resources/fasta/single-sequence.fasta.gz");
    final Sequence[] sequences = getSingleSequenceFileSequences();
    final SequenceTextInfo[] sequenceTextInfos = getSingleSequenceFileSequenceTextInfos(path);
    final SequenceLocationsInfo[] sequenceLocationInfos = getSingleSequenceFileSequenceLocationInfos(path);

    return FastaFileInformation.of(path, sequences, sequenceTextInfos, sequenceLocationInfos);
  }
  
  public static Map<String, FastaFileInformation> getFileInformations() {
    final Map<String, FastaFileInformation> informations = new HashMap<>();
    
    informations.put("Regular fna file", getFnaFileInformation());
    informations.put("GZip fna file", getFnaGZipFileInformation());
    informations.put("Multibyte char fna file", getFnaMultibyteCharsFileInformation());
    informations.put("Multibyte char gZip fna file", getFnaMultibyteCharsGZipFileInformation());
    informations.put("Single sequence file", getSingleSequenceFileInformation());
    informations.put("Single sequence gzip file", getSingleSequenceGZipFileInformation());
    
    return informations;
  }
}
