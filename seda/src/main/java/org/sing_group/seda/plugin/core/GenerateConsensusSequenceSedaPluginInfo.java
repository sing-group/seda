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
package org.sing_group.seda.plugin.core;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.bio.consensus.ConsensusBaseStrategy;

public class GenerateConsensusSequenceSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Consensus sequence";
  public static final String SHORT_NAME = "consensus";
  public static final String DESCRIPTION =
    "Create a consensus sequence using the set of aligned sequences present in the selected FASTA file(s).";

  public static final String PARAM_SEQUENCE_TYPE_NAME = "sequence-type";
  public static final String PARAM_SEQUENCE_TYPE_SHORT_NAME = "st";
  public static final String PARAM_SEQUENCE_TYPE_DESCRIPTION = "Sequence type";
  public static final String PARAM_SEQUENCE_TYPE_HELP =
    "The type of the sequences in the selected files.\n"
      + "\t\t\tIn nucleotide sequences, ambiguous positions are indicated by using the IUPAC ambiguity codes.\n"
      + "\t\t\tIn protein sequences, ambiguous positions are indicated as the Verbose option explains.";
  public static final String PARAM_SEQUENCE_TYPE_HELP_GUI =
    toHtml(
      PARAM_SEQUENCE_TYPE_HELP, asList("nucleotide", "protein", "IUPAC", "Verbose"), Collections.emptyList(),
      true
    );

  public static final String PARAM_CONSENSUS_BASE_NAME = "consensus-bases";
  public static final String PARAM_CONSENSUS_BASE_SHORT_NAME = "cb";
  public static final String PARAM_CONSENSUS_BASE_DESCRIPTION = "Consensus bases";
  public static final String PARAM_CONSENSUS_BASE_HELP =
    "The strategy for selecting the bases at each position that should be considered to create the consensus.\n"
      + "\t\tIt can be one of:\n"
      + Stream.of(ConsensusBaseStrategy.values())
        .map(strategy -> strategy.name().toLowerCase() + ": " + strategy.getDescription())
        .collect(joining("\n\t\t\t", "\t\t\t", ""));
  public static final String PARAM_CONSENSUS_BASE_HELP_GUI =
    toHtml(
      PARAM_CONSENSUS_BASE_HELP,
      Stream.of(ConsensusBaseStrategy.values()).map(ConsensusBaseStrategy::name).map(String::toLowerCase).collect(
        Collectors.toList()
      ), Collections.emptyList(), false
    );

  public static final String PARAM_MINIMUM_PRESENCE_NAME = "minimum-presence";
  public static final String PARAM_MINIMUM_PRESENCE_SHORT_NAME = "mp";
  public static final String PARAM_MINIMUM_PRESENCE_DESCRIPTION = "Minimum presence";
  public static final String PARAM_MINIMUM_PRESENCE_HELP =
    "The minimum presence for a given nucleotide or amino acid in order to be part of the consensus sequence.\n" +
      "\t\tRead the Consensus bases description to understand how this option is used in each case.";
  public static final String PARAM_MINIMUM_PRESENCE_HELP_GUI =
    toHtml(PARAM_MINIMUM_PRESENCE_HELP, asList("Consensus bases"), Collections.emptyList(), true);

  public static final String PARAM_VERBOSE_NAME = "verbose";
  public static final String PARAM_VERBOSE_SHORT_NAME = "v";
  public static final String PARAM_VERBOSE_DESCRIPTION = "Verbose";
  public static final String PARAM_VERBOSE_HELP =
    "In protein sequences, when this option is unselected then X is used for ambiguous positions in the consensus sequence.\n "
      + "\t\tOn the other hand, when this option is selected, then all amino acids in such positions are reported (e.g. [HWY]).";
  public static final String PARAM_VERBOSE_HELP_GUI = toHtml(PARAM_VERBOSE_HELP);
}