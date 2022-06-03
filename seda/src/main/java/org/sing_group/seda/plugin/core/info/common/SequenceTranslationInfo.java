/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.plugin.core.info.common;

import static java.util.stream.Collectors.joining;

import java.util.Map;

import org.sing_group.seda.core.ncbi.codes.NcbiCodonTables;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class SequenceTranslationInfo extends AbstractInfo {
  public static final String PARAM_CONVERT_AMINO_ACID_NAME = "convert-amino-acid";
  public static final String PARAM_CONVERT_AMINO_ACID_SHORT_NAME = "caa";
  public static final String PARAM_CONVERT_AMINO_ACID_DESCRIPTION =
    "Convert to amino acid sequence before pattern matching";
  public static final String PARAM_CONVERT_AMINO_ACID_HELP =
    "If this option is selected, then input nucleic acid sequences are translated into amino acid sequences before applying the pattern matching. "
      + "If a translated sequence matches the defined pattern, then the input nucleic acid sequence is reported.";
  public static final String PARAM_CONVERT_AMINO_ACID_HELP_GUI = toHtml(PARAM_CONVERT_AMINO_ACID_HELP);

  public static final String PARAM_FRAME_NAME = "frame";
  public static final String PARAM_FRAME_SHORT_NAME = "f";
  public static final String PARAM_FRAME_DESCRIPTION = "Starting at fixed frame";
  public static final String PARAM_FRAME_HELP = "Translate sequences starting at a fixed frame.";
  public static final String PARAM_FRAME_HELP_GUI = toHtml(PARAM_FRAME_HELP);

  public static final String PARAM_ALL_FRAME_NAME = "all-frames";
  public static final String PARAM_ALL_FRAME_SHORT_NAME = "af";
  public static final String PARAM_ALL_FRAME_DESCRIPTION = "Considering frames 1, 2 and 3";
  public static final String PARAM_ALL_FRAME_HELP = "Translate sequences using frames 1, 2 and 3.";
  public static final String PARAM_ALL_FRAME_HELP_GUI = toHtml(PARAM_ALL_FRAME_HELP);

  public static final String PARAM_JOIN_FRAME_NAME = "join-frames";
  public static final String PARAM_JOIN_FRAME_SHORT_NAME = "jf";
  public static final String PARAM_JOIN_FRAME_DESCRIPTION = "Join frames";
  public static final String PARAM_JOIN_FRAME_HELP = "When frames 1, 2 and 3 are considered, this option allows indicating whether translated frames must be considered together or separately.";
  public static final String PARAM_JOIN_FRAME_HELP_GUI = toHtml(PARAM_JOIN_FRAME_HELP);

  public static final String PARAM_REVERSE_COMPLEMENT_NAME = "reverse-complement";
  public static final String PARAM_REVERSE_COMPLEMENT_SHORT_NAME = "rc";
  public static final String PARAM_REVERSE_COMPLEMENT_DESCRIPTION = "Use reverse complement sequences";
  public static final String PARAM_REVERSE_COMPLEMENT_HELP = "Calculate the reverse complement of sequences before translation.";
  public static final String PARAM_REVERSE_COMPLEMENT_HELP_GUI = "Whether reverse complement of sequences must be calculated before translation or not. If not selected, sequences are used as they are introduced.";

  public static final String PARAM_CODON_TABLE_NAME = "codon-table";
  public static final String PARAM_CODON_TABLE_SHORT_NAME = "ct";
  public static final String PARAM_CODON_TABLE_DESCRIPTION = "Codon table";
  public static final String PARAM_CODON_TABLE_HELP;
  static {
    Map<Integer, String> tables = new NcbiCodonTables().listTables();
    PARAM_CODON_TABLE_HELP = "The codon table to use. One of: \n\t\t\t"
      + tables.keySet().stream().map(key -> key + " = " + tables.get(key)).collect(joining("\n\t\t\t", "", "\n\t\t"));
  }
  public static final String PARAM_CODON_TABLE_HELP_GUI = toHtml(PARAM_CODON_TABLE_HELP);

  public static final String PARAM_CODON_TABLE_CUSTOM_NAME = "codon-table-custom";
  public static final String PARAM_CODON_TABLE_CUSTOM_SHORT_NAME = "ctc";
  public static final String PARAM_CODON_TABLE_CUSTOM_DESCRIPTION = "Custom";
  public static final String PARAM_CODON_TABLE_CUSTOM_HELP = "This option allows using a custom codon conversion table. If not selected, the prefedined codon table selected is used.\n"
    + "\t\tThe custom codon table must be a text file with the following format:\n" + "\t\t\tTTT=T\n" + "\t\t\tCTT=C\n"
    + "\t\t\tGCA=A";
  public static final String PARAM_CODON_TABLE_CUSTOM_HELP_GUI = toHtml(PARAM_CODON_TABLE_CUSTOM_HELP);
}
