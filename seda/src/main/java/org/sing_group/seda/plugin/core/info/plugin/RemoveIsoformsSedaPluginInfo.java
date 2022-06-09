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

import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector.TieBreakOption;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class RemoveIsoformsSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Remove isoforms";
  public static final String SHORT_NAME = "remove-isoforms";
  public static final String DESCRIPTION = "Keep the isoform with the size closest to that specified, and remove all other isoforms, identified based on a shared word of specified length.";
  public static final String GROUP = Group.GROUP_FILTERING.getName();

  public static final String PARAM_MINIMUM_WORD_LENGTH_NAME = "minimum-word-length";
  public static final String PARAM_MINIMUM_WORD_LENGTH_SHORT_NAME = "mwl";
  public static final String PARAM_MINIMUM_WORD_LENGTH_DESCRIPTION = "Minimum word length";
  public static final String PARAM_MINIMUM_WORD_LENGTH_HELP =
    "The minimum length of word to consider that two sequences are isoforms.";
  public static final String PARAM_MINIMUM_WORD_LENGTH_HELP_GUI = toHtml(PARAM_MINIMUM_WORD_LENGTH_HELP);

  public static final String PARAM_REFERENCE_SIZE_NAME = "reference-size";
  public static final String PARAM_REFERENCE_SIZE_SHORT_NAME = "rs";
  public static final String PARAM_REFERENCE_SIZE_DESCRIPTION = "Reference size";
  public static final String PARAM_REFERENCE_SIZE_HELP =
    "The isoform with the length closest to this reference size will be selected. "
      + "In case of having two isoforms that are at the same distance, the tie break mode option allows specifying which one should be selected.";
  public static final String PARAM_REFERENCE_SIZE_HELP_GUI =
    toHtml(PARAM_REFERENCE_SIZE_HELP, asList("tie break mode option"), Collections.emptyList(), true);

  public static final String PARAM_TIE_BREAK_NAME = "tie-break-mode";
  public static final String PARAM_TIE_BREAK_SHORT_NAME = "tbm";
  public static final String PARAM_TIE_BREAK_DESCRIPTION = "Tie break mode";
  private static final String[] PARAM_TIE_BREAK_ENUM = { 
    "The sequence with less bases will be selected as isoform.",
    "The sequence with more bases will be selected as isoform."
  };
  public static final String PARAM_TIE_BREAK_HELP = longEnumStringForCli(
    "The criterion to select between isoforms at the same distance.",
    cliMap(TieBreakOption.values(), PARAM_TIE_BREAK_ENUM)
  );
  public static final String PARAM_TIE_BREAK_HELP_GUI = toHtml(
    longEnumStringForGui(
      "The criterion to select between isoforms at the same distance.",
      guiMap(TieBreakOption.values(), PARAM_TIE_BREAK_ENUM)
    )
  );

  public static final String PARAM_REMOVE_ISOFORM_HEADER_NAME = "add-removed-isoform-headers";
  public static final String PARAM_REMOVE_ISOFORM_HEADER_SHORT_NAME = "arih";
  public static final String PARAM_REMOVE_ISOFORM_HEADER_DESCRIPTION = "Add removed isoform headers";
  public static final String PARAM_REMOVE_ISOFORM_HEADER_HELP =
    "Whether the removed isoform headers should be added to the header of the selected isoform.";
  public static final String PARAM_REMOVE_ISOFORM_HEADER_HELP_GUI = toHtml(PARAM_REMOVE_ISOFORM_HEADER_HELP);

  public static final String PARAM_HEADER_TARGET_NAME = "header-target";
  public static final String PARAM_HEADER_TARGET_SHORT_NAME = "ht";
  public static final String PARAM_HEADER_TARGET_DESCRIPTION = "Header target";
  public static final String PARAM_HEADER_TARGET_HELP = "The part of the removed isoform headers that should be added.";
  public static final String PARAM_HEADER_TARGET_HELP_GUI = toHtml(PARAM_HEADER_TARGET_HELP);

  public static final String PARAM_ISOFORM_FILE_NAME = "isoform-files-directory";
  public static final String PARAM_ISOFORM_FILE_SHORT_NAME = "ifd";
  public static final String PARAM_ISOFORM_FILE_DESCRIPTION = "Isoform files directory";
  public static final String PARAM_ISOFORM_FILE_HELP =
    "Whether the removed isoform names should be saved into a CSV file or not. "
      + "This allows an easy identification of those sequences that had isoforms in the output files. "
      + "If you do not want to save them, leave this file empty. "
      + "Otherwise, choose the directory where such files should be created.";
  public static final String PARAM_ISOFORM_FILE_HELP_GUI = toHtml(PARAM_ISOFORM_FILE_HELP);

  public static final String PARAM_GROUP_SEQUENCES_REGEX_NAME = "group-sequences-regex";
  public static final String PARAM_GROUP_SEQUENCES_REGEX_SHORT_NAME = "gsr";
  public static final String PARAM_GROUP_SEQUENCES_REGEX_DESCRIPTION = "Group Sequences Regex";
  public static final String PARAM_GROUP_SEQUENCES_REGEX_HELP = "This option allows to specify whether sequences must be grouped before "
    + "the identification of the isoforms. Don't use this flag if isoforms must be removed at a file level. In contrast, if you want to "
    + "make groups of sequences before the identification of the isoforms, this flag allows regex options to configure how sequence headers "
    + "must be matched in order to group sequences. Check the manual for examples.";
  public static final String PARAM_GROUP_SEQUENCES_REGEX_HELP_GUI = toHtml(PARAM_GROUP_SEQUENCES_REGEX_HELP);

  public static final String DEFAULT_REMOVE_ISOFORM_HEADER_DELIMITER = ", ";
  public static final String DEFAULT_REMOVE_ISOFORM_HEADER_PREFIX = "[";
  public static final String DEFAULT_REMOVE_ISOFORM_HEADER_SUFFIX = "]";

  public static final HeaderTarget DEFAULT_REMOVE_ISOFORM_HEADER_TARGET = HeaderTarget.NAME;

  public static final DefaultSequenceIsoformSelector.TieBreakOption DEFAULT_REMOVE_ISOFORM_TIE_BREAK_OPTION =
    DefaultSequenceIsoformSelector.TieBreakOption.SHORTEST;
}
