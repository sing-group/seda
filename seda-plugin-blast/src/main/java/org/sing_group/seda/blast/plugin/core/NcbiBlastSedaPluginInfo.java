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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import org.sing_group.seda.blast.ncbi.NcbiBlastDatabase;
import org.sing_group.seda.blast.ncbi.NcbiBlastType;
import org.sing_group.seda.blast.ncbi.parameters.MatrixParameter;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.transformation.dataset.NcbiBlastTransformation;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class NcbiBlastSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "NCBI BLAST";
  public static final String SHORT_NAME = "blast-ncbi";
  public static final String DESCRIPTION =
    "Perform a BLAST query through the NCBI web server (https://blast.ncbi.nlm.nih.gov/Blast.cgi).";
  public static final String GROUP = Group.GROUP_BLAST.getName();
  
  public static final String PARAM_NCBI_BLAST_TYPE_NAME = "blast-type";
  public static final String PARAM_NCBI_BLAST_TYPE_SHORT_NAME = "bt";
  public static final String PARAM_NCBI_BLAST_TYPE_DESCRIPTION = "BLAST type";
  public static final String PARAM_NCBI_BLAST_TYPE_HELP_BASE =
    "The BLAST program to execute.";
  public static final String PARAM_NCBI_BLAST_TYPE_HELP =
    shortEnumString(
      PARAM_NCBI_BLAST_TYPE_HELP_BASE, NcbiBlastType.class
    );
  public static final String PARAM_NCBI_BLAST_TYPE_HELP_GUI = PARAM_NCBI_BLAST_TYPE_HELP_BASE;

  public static final String PARAM_NCBI_BLAST_DATABASE_NAME = "blast-database";
  public static final String PARAM_NCBI_BLAST_DATABASE_SHORT_NAME = "bd";
  public static final String PARAM_NCBI_BLAST_DATABASE_DESCRIPTION = "BLAST database";
  public static final String PARAM_NCBI_BLAST_DATABASE_HELP_BASE =
    "The NCBI database to run BLAST against.";
  public static final String PARAM_NCBI_BLAST_DATABASE_HELP =
    longEnumStringForCli(
      PARAM_NCBI_BLAST_DATABASE_HELP_BASE,
      stream(NcbiBlastDatabase.values()).collect(toMap(NcbiBlastDatabase::getName, NcbiBlastDatabase::getTitle))
    );
  public static final String PARAM_NCBI_BLAST_DATABASE_HELP_GUI = PARAM_NCBI_BLAST_DATABASE_HELP_BASE;
  
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

  public static final String PARAM_MATRIX_NAME = "matrix";
  public static final String PARAM_MATRIX_SHORT_NAME = "m";
  public static final String PARAM_MATRIX_DESCRIPTION = "Matrix";
  public static final String PARAM_MATRIX_HELP_BASE =
    "The scoring matrix to use.";
  public static final String PARAM_MATRIX_HELP =
    shortEnumString(
      PARAM_MATRIX_HELP_BASE, MatrixParameter.class
    );
  public static final String PARAM_MATRIX_HELP_GUI = PARAM_MATRIX_HELP_BASE;
  
  public static final String PARAM_FILTER_APPLY_NAME = "filter";
  public static final String PARAM_FILTER_APPLY_SHORT_NAME = "f";
  public static final String PARAM_FILTER_APPLY_DESCRIPTION = "Filter";
  public static final String PARAM_FILTER_APPLY_HELP = "Low complexity filtering.";
  public static final String PARAM_FILTER_APPLY_HELP_GUI = PARAM_FILTER_APPLY_HELP;
  
  public static final String PARAM_FILTER_LOOKUP_NAME = "filter-lookup";
  public static final String PARAM_FILTER_LOOKUP_SHORT_NAME = "fl";
  public static final String PARAM_FILTER_LOOKUP_DESCRIPTION = "Mask at lookup.";
  public static final String PARAM_FILTER_LOOKUP_HELP = "Mask at lookup when applying the low complexity filtering.";
  
  public static final String PARAM_EVALUE_NAME = "evalue";
  public static final String PARAM_EVALUE_SHORT_NAME = "ev";
  public static final String PARAM_EVALUE_DESCRIPTION = "Expectation value";
  public static final String PARAM_EVALUE_HELP = "The expectation value (E) threshold for saving hits.";
  public static final String PARAM_EVALUE_HELP_GUI = PARAM_EVALUE_HELP;
  
  public static final String PARAM_HITS_LIST_SIZE_NAME = "hits-list-size";
  public static final String PARAM_HITS_LIST_SIZE_SHORT_NAME = "hls";
  public static final String PARAM_HITS_LIST_SIZE_DESCRIPTION = "Hit list size";
  public static final String PARAM_HITS_LIST_SIZE_HELP = "Number of databases sequences to keep.";
  public static final String PARAM_HITS_LIST_SIZE_HELP_GUI = PARAM_HITS_LIST_SIZE_HELP;
  
  public static final String PARAM_WORD_SIZE_NAME = "word-size";
  public static final String PARAM_WORD_SIZE_SHORT_NAME = "ws";
  public static final String PARAM_WORD_SIZE_DESCRIPTION = "Word size";
  public static final String PARAM_WORD_SIZE_HELP = "Size of word for initial matches.";
  public static final String PARAM_WORD_SIZE_HELP_GUI = PARAM_WORD_SIZE_HELP;
  
  public static final String PARAM_THRESHOLD_NAME = "threshold";
  public static final String PARAM_THRESHOLD_SHORT_NAME = "th";
  public static final String PARAM_THRESHOLD_DESCRIPTION = "Threshold";
  public static final String PARAM_THRESHOLD_HELP = "Neighboring score for initial words. Does not apply to BLASTN or MegaBLAST.";
  public static final String PARAM_THRESHOLD_HELP_GUI = toHtml(PARAM_THRESHOLD_HELP);
  
  public static final OutputTypeParameter DEFAULT_OUTPUT_TYPE = NcbiBlastTransformation.DEFAULT_OUTPUT_TYPE_PARAMETER;
}
