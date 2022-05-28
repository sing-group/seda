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
package org.sing_group.seda.plugin.core.info.plugin;

import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class FilteringSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Filtering";
  public static final String SHORT_NAME = "filtering";
  public static final String DESCRIPTION =
    "Filter sequences based on different criteria (e.g. sequence length, non-multiple of three, or in-frame stop codons presence, among others).";

  public static final String PARAM_STARTING_CODON_NAME = "starting-codon";
  public static final String PARAM_STARTING_CODON_SHORT_NAME = "sc";
  public static final String PARAM_STARTING_CODON_DESCRIPTION = "Codons";
  public static final String PARAM_STARTING_CODON_HELP =
    "Filters sequences so that only those starting with the selected codons are kept. "
      + "Combinations of 3 characters using: 'A', 'C', 'T', 'G'. Example: ATG, CCA, AAA, GCT";
  public static final String PARAM_STARTING_CODON_HELP_GUI = toHtml(PARAM_STARTING_CODON_HELP);

  public static final String PARAM_NON_MULTIPLE_3_NAME = "remove-non-multiple-3";
  public static final String PARAM_NON_MULTIPLE_3_SHORT_NAME = "rnm3";
  public static final String PARAM_NON_MULTIPLE_3_DESCRIPTION = "Remove sequences with a non-multiple of three length";
  public static final String PARAM_NON_MULTIPLE_3_HELP =
    "Filters sequences so that only those having a length that is multiple of 3 are kept.";
  public static final String PARAM_NON_MULTIPLE_3_HELP_GUI = toHtml(PARAM_NON_MULTIPLE_3_HELP);

  public static final String PARAM_IN_FRAME_STOP_CODONS_NAME = "remove-with-in-frame-stop-codons";
  public static final String PARAM_IN_FRAME_STOP_CODONS_SHORT_NAME = "rwifsc";
  public static final String PARAM_IN_FRAME_STOP_CODONS_DESCRIPTION = "Remove sequences with in-frame stop codons";
  public static final String PARAM_IN_FRAME_STOP_CODONS_HELP =
    "Filters sequences so that only those without in-frame stop codons are kept.";
  public static final String PARAM_IN_FRAME_STOP_CODONS_HELP_GUI = toHtml(PARAM_IN_FRAME_STOP_CODONS_HELP);

  public static final String PARAM_MINIMUM_SEQUENCE_LENGTH_NAME = "minimum-sequence-length";
  public static final String PARAM_MINIMUM_SEQUENCE_LENGTH_SHORT_NAME = "minl";
  public static final String PARAM_MINIMUM_SEQUENCE_LENGTH_DESCRIPTION = "Minimum sequence length";
  public static final String PARAM_MINIMUM_SEQUENCE_LENGTH_HELP =
    "Filters sequences so that only those with the specified minimum sequence length are kept.";
  public static final String PARAM_MINIMUM_SEQUENCE_LENGTH_HELP_GUI =
    toHtml(
      PARAM_MINIMUM_SEQUENCE_LENGTH_HELP + " A value of 0 indicates that no minimum sequence length is required."
    );

  public static final String PARAM_MAXIMUM_SEQUENCE_LENGTH_NAME = "maximum-sequence-length";
  public static final String PARAM_MAXIMUM_SEQUENCE_LENGTH_SHORT_NAME = "maxl";
  public static final String PARAM_MAXIMUM_SEQUENCE_LENGTH_DESCRIPTION = "Maximum sequence length";
  public static final String PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP =
    "Filters sequences so that only those with the specified maximum sequence length are kept.";
  public static final String PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP_GUI =
    toHtml(
      PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP + " A value of 0 indicates that no maximum sequence length is required."
    );

  public static final String PARAM_MINIMUM_SEQUENCES_NAME = "minimum-sequences";
  public static final String PARAM_MINIMUM_SEQUENCES_SHORT_NAME = "mins";
  public static final String PARAM_MINIMUM_SEQUENCES_DESCRIPTION = "Minimum number of sequences";
  public static final String PARAM_MINIMUM_SEQUENCES_HELP =
    "Filters files so that only those with the specified minimum number of sequences are kept.";
  public static final String PARAM_MINIMUM_SEQUENCES_HELP_GUI = toHtml(PARAM_MINIMUM_SEQUENCES_HELP);

  public static final String PARAM_MAXIMUM_SEQUENCES_NAME = "maximum-sequences";
  public static final String PARAM_MAXIMUM_SEQUENCES_SHORT_NAME = "maxs";
  public static final String PARAM_MAXIMUM_SEQUENCES_DESCRIPTION = "Maximum number of sequences";
  public static final String PARAM_MAXIMUM_SEQUENCES_HELP =
    "Filters files so that only those with the specified maximum number of sequences are kept.";
  public static final String PARAM_MAXIMUM_SEQUENCES_HELP_GUI = toHtml(PARAM_MAXIMUM_SEQUENCES_HELP);

  public static final String PARAM_REMOVE_SIZE_DIFFERENCE_NAME = "remove-size-difference";
  public static final String PARAM_REMOVE_SIZE_DIFFERENCE_SHORT_NAME = "rsd";
  public static final String PARAM_REMOVE_SIZE_DIFFERENCE_DESCRIPTION = "Remove by sequence length difference";
  public static final String PARAM_REMOVE_SIZE_DIFFERENCE_HELP =
    "Filters sequences so that only those with the specified difference when compared to the reference sequence are kept.";
  public static final String PARAM_REMOVE_SIZE_DIFFERENCE_HELP_GUI = toHtml(PARAM_REMOVE_SIZE_DIFFERENCE_HELP);

  public static final String PARAM_MAX_SIZE_DIFFERENCE_NAME = "max-size-difference";
  public static final String PARAM_MAX_SIZE_DIFFERENCE_SHORT_NAME = "maxsd";
  public static final String PARAM_MAX_SIZE_DIFFERENCE_DESCRIPTION = "Maximum length difference (%)";
  public static final String PARAM_MAX_SIZE_DIFFERENCE_HELP =
    "The maximum sequence length difference allowed expressed as a percentage.";
  public static final String PARAM_MAX_SIZE_DIFFERENCE_HELP_GUI = toHtml(PARAM_MAX_SIZE_DIFFERENCE_HELP);

  public static final String PARAM_SEQUENCE_INDEX_NAME = "reference-sequence-index";
  public static final String PARAM_SEQUENCE_INDEX_SHORT_NAME = "rsi";
  public static final String PARAM_SEQUENCE_INDEX_DESCRIPTION = "Reference sequence index";
  public static final String PARAM_SEQUENCE_INDEX_HELP =
    "The index of the sequence to use as reference to compare others. The first sequence corresponds to index 1. "
      + "This option is ignored if a reference sequence file is selected.";
  public static final String PARAM_SEQUENCE_INDEX_HELP_GUI = toHtml(PARAM_SEQUENCE_INDEX_HELP);

  public static final String PARAM_SEQUENCE_FILE_NAME = "reference-sequence-file";
  public static final String PARAM_SEQUENCE_FILE_SHORT_NAME = "rsf";
  public static final String PARAM_SEQUENCE_FILE_DESCRIPTION = "Reference sequence file";
  public static final String PARAM_SEQUENCE_FILE_HELP =
    "The file containing the sequence to use as reference to compare others. "
      + "If a file is selected, then the reference sequence index is ignored.";
  public static final String PARAM_SEQUENCE_FILE_HELP_GUI = toHtml(PARAM_SEQUENCE_FILE_HELP);
}
