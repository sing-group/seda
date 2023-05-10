/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.blast.plugin.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.transformation.dataset.UniProtBlastTransformation;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.AlignmentCutoffOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.FilterOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.MatrixOption;

public class UniProtBlastSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "UniProt BLAST";
  public static final String SHORT_NAME = "blast-uniprot";
  public static final String DESCRIPTION =
    "Perform a BLAST query through the UniProt web server (https://www.uniprot.org/blast/).";
  public static final String GROUP = Group.GROUP_BLAST.getName();

  public static final String PARAM_DATABASE_NAME = "database";
  public static final String PARAM_DATABASE_SHORT_NAME = "db";
  public static final String PARAM_DATABASE_DESCRIPTION = "Database";
  public static final String PARAM_DATABASE_HELP =
    shortEnumString(
      "The target database.", DatabaseOption.class
    );
  public static final String PARAM_DATABASE_HELP_GUI = toHtml(PARAM_DATABASE_HELP);
  
  public static final String PARAM_OUTPUT_TYPE_NAME = "output-type";
  public static final String PARAM_OUTPUT_TYPE_SHORT_NAME = "ot";
  public static final String PARAM_OUTPUT_TYPE_DESCRIPTION = "Output type";
  private static final String[] PARAM_OUTPUT_TYPE_HELP_ENUM = {
    "Creates a FASTA file with the complete sequences of each sequence that has an alignment against the query sequence.",
    "Creates a FASTA file with the portions of the sequences aligned against the query." 
  };
  public static final String PARAM_OUTPUT_TYPE_HELP =
    longEnumStringForCli(
      "The output type.",
      cliMap(OutputTypeParameter.values(), PARAM_OUTPUT_TYPE_HELP_ENUM)
    );
  public static final String PARAM_OUTPUT_TYPE_HELP_GUI =
    toHtml(
      longEnumStringForGui(
        "The output type.",
        guiMap(OutputTypeParameter.values(), PARAM_OUTPUT_TYPE_HELP_ENUM)
      )
    );
  
  public static final String PARAM_EXPECTATION_VALUE_NAME = "expectation-value";
  public static final String PARAM_EXPECTATION_VALUE_SHORT_NAME = "e";
  public static final String PARAM_EXPECTATION_VALUE_DESCRIPTION = "E-Threshold";
  public static final String PARAM_EXPECTATION_VALUE_HELP_BASE =
    "The expectation value (E) threshold is a statistical measure of the number "
      + "of expected matches in a random database. The lower the e-value, the more likely the match is to be significant."
      + "E-values between 0.1 and 10 are generally dubious, and over 10 are unlikely to have biological significance. "
      + "In all cases, those matches need to be verified manually. You may need to increase the E threshold if you have a very "
      + "short query sequence, to detect very weak similarities, or similarities in a short region, or if your sequence has a "
      + "low complexity region and you use the filter option.";
  public static final String PARAM_EXPECTATION_VALUE_HELP =
    shortEnumString(
      PARAM_EXPECTATION_VALUE_HELP_BASE, ExpectationOption.class
    );
  public static final String PARAM_EXPECTATION_VALUE_HELP_GUI = toHtml(
    PARAM_EXPECTATION_VALUE_HELP_BASE, 
    emptyList(), asList(new String[]{"filter"}), true
  );
  
  public static final String PARAM_MATRIX_OPTION_NAME = "matrix";
  public static final String PARAM_MATRIX_OPTION_SHORT_NAME = "m";
  public static final String PARAM_MATRIX_OPTION_DESCRIPTION = "Matrix";
  public static final String PARAM_MATRIX_OPTION_HELP_BASE = 
    "The scoring matrix to use. The matrix assigns a probability score for each "
    + "position in an alignment. The BLOSUM matrix assigns a probability score for each position in an alignment that is based on "
    + "the frequency with which that substitution is known to occur among consensus blocks within related proteins. BLOSUM62 is among the best "
    + "of the available matrices for detecting weak protein similarities. The PAM set of matrices is also available.";
  public static final String PARAM_MATRIX_OPTION_HELP =
    shortEnumString(
      PARAM_MATRIX_OPTION_HELP_BASE, MatrixOption.class
    );
  public static final String PARAM_MATRIX_OPTION_HELP_GUI = toHtml(PARAM_MATRIX_OPTION_HELP_BASE);
  
  public static final String PARAM_FILTER_NAME = "filter";
  public static final String PARAM_FILTER_SHORT_NAME = "f";
  public static final String PARAM_FILTER_DESCRIPTION = "Filtering";
  public static final String PARAM_FILTER_HELP_BASE =
    "Low-complexity regions (for example: stretches of cysteine in Q03751, or hydrophobic regions in membrane proteins) tend to produce spurious, "
      + "insignificant matches with sequences in the database which have the same kind of low-complexity regions, but are unrelated biologically. "
      + "If filtering low complexity regions is selected, the query sequence will be run through the program SEG, and all amino acids in "
      + "low-complexity regions will be replaced by X's.";
  public static final String PARAM_FILTER_HELP =
    shortEnumString(
      PARAM_FILTER_HELP_BASE, FilterOption.class
    );
  public static final String PARAM_FILTER_HELP_GUI = toHtml(
    PARAM_FILTER_HELP_BASE, 
    emptyList(), asList(new String[] {"filtering low complexity regions"}), true
  );
  
  public static final String PARAM_GAPPED_NAME = "gapped";
  public static final String PARAM_GAPPED_SHORT_NAME = "g";
  public static final String PARAM_GAPPED_DESCRIPTION = "Gapped";
  public static final String PARAM_GAPPED_HELP =
    "Whether the query is gapped or not. This will allow gaps to be introduced in the sequences when the comparison is done.";
  public static final String PARAM_GAPPED_HELP_GUI = toHtml(PARAM_GAPPED_HELP);

  public static final String PARAM_HITS_NAME = "hits";
  public static final String PARAM_HITS_SHORT_NAME = "h";
  public static final String PARAM_HITS_DESCRIPTION = "Hits";
  public static final String PARAM_HITS_HELP_BASE = "Limits the number of returned alignments.";
  public static final String PARAM_HITS_HELP = 
    shortEnumString(
      PARAM_HITS_HELP_BASE, AlignmentCutoffOption.class
    );
  public static final String PARAM_HITS_HELP_GUI = toHtml(PARAM_HITS_HELP_BASE);
  
  public static final DatabaseOption DEFAULT_DATABASE_OPTION = UniProtBlastTransformation.DEFAULT_DATABASE;
  public static final OutputTypeParameter DEFAULT_OUTPUT_TYPE = UniProtBlastTransformation.DEFAULT_OUTPUT_TYPE_PARAMETER;
  public static final ExpectationOption DEFAULT_EXPECTATION_OPTION = UniProtBlastTransformation.DEFAULT_EXPECTATION_OPTION;
  public static final MatrixOption DEFAULT_MATRIX_OPTION = UniProtBlastTransformation.DEFAULT_MATRIX_OPTION;
  public static final FilterOption DEFAULT_FILTER_OPTION = UniProtBlastTransformation.DEFAULT_FILTER_OPTION;
  public static final AlignmentCutoffOption DEFAULT_ALIGNMENT_CUTOFF = UniProtBlastTransformation.DEFAULT_ALIGNMENT_CUTOFF;
}
