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

import static java.util.Arrays.asList;

import java.util.Collections;

import org.sing_group.seda.core.split.SequencesGroupSplitMode;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class SplitSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Split";
  public static final String SHORT_NAME = "split";
  public static final String DESCRIPTION =
    "Split each input FASTA file into several FASTA files (with the possibilty of random sequence sampling).";
  public static final String GROUP = Group.GROUP_GENERAL.getName();

  public static final String PARAM_RANDOMIZE_NAME = "randomize";
  public static final String PARAM_RANDOMIZE_SHORT_NAME = "r";
  public static final String PARAM_RANDOMIZE_DESCRIPTION = "Randomize";
  public static final String PARAM_RANDOMIZE_HELP = "Whether sequences must be randomized or not.";
  public static final String PARAM_RANDOMIZE_HELP_GUI = toHtml(PARAM_RANDOMIZE_HELP);

  public static final String PARAM_SEED_NAME = "seed";
  public static final String PARAM_SEED_SHORT_NAME = "s";
  public static final String PARAM_SEED_DESCRIPTION = "Seed";
  public static final String PARAM_SEED_HELP =
    "The random seed to randomize the sequences. This allows the same result to be reproduced in different runs and environments with same random seed.";
  public static final String PARAM_SEED_HELP_GUI = toHtml(PARAM_SEED_HELP);

  public static final String PARAM_SPLIT_MODE_NAME = "split-mode";
  public static final String PARAM_SPLIT_MODE_SHORT_NAME = "sm";
  public static final String PARAM_SPLIT_MODE_DESCRIPTION = "Split mode";
  public static final String PARAM_SPLIT_MODE_HELP = shortEnumString("Split mode.", SequencesGroupSplitMode.class);
  public static final String PARAM_SPLIT_MODE_HELP_GUI = toHtml(PARAM_SPLIT_MODE_HELP);

  public static final String PARAM_NUM_FILES_NAME = "num-files";
  public static final String PARAM_NUM_FILES_SHORT_NAME = "nf";
  public static final String PARAM_NUM_FILES_DESCRIPTION = "Number of files";
  public static final String PARAM_NUM_FILES_HELP = "The desired number of files.";
  public static final String PARAM_NUM_FILES_HELP_GUI = toHtml(PARAM_NUM_FILES_HELP);

  public static final String PARAM_NUM_SEQUENCES_NAME = "num-sequences";
  public static final String PARAM_NUM_SEQUENCES_SHORT_NAME = "ns";
  public static final String PARAM_NUM_SEQUENCES_DESCRIPTION = "Number of sequences";
  public static final String PARAM_NUM_SEQUENCES_HELP = "The desired number of sequences.";
  public static final String PARAM_NUM_SEQUENCES_HELP_GUI = toHtml(PARAM_NUM_SEQUENCES_HELP);

  public static final String PARAM_INDEPENDENT_EXTRACTIONS_NAME = "independent";
  public static final String PARAM_INDEPENDENT_EXTRACTIONS_SHORT_NAME = "i";
  public static final String PARAM_INDEPENDENT_EXTRACTIONS_DESCRIPTION = "Independent extractions";
  public static final String PARAM_INDEPENDENT_EXTRACTIONS_HELP =
    "Whether independent extractions should be made or not. This option can only be used with the "
      + SequencesGroupSplitMode.SEQUENCES_PER_FILE_AND_FILES.toString() + ". It is useful in combination with "
      + "the randomization in order to obtain different random subsets from the same input file.";
  public static final String PARAM_INDEPENDENT_EXTRACTIONS_HELP_GUI =
    toHtml(
      PARAM_INDEPENDENT_EXTRACTIONS_HELP,
      asList(SequencesGroupSplitMode.SEQUENCES_PER_FILE_AND_FILES.toString(), "randomization"), Collections.emptyList(),
      true
    );
}
