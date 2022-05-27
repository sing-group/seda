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

import org.sing_group.seda.core.ncbi.NcbiTaxonomyFields;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation;
import org.sing_group.seda.util.OsUtils;

public class NcbiRenameSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "NCBI Rename";
  public static final String SHORT_NAME = "rename-ncbi";
  public static final String DESCRIPTION = "Modify sequence headers and/or file names replacing NCBI accession numbers with organism names and additional information from the NCBI Taxonomy Browser.";
  public static final String GROUP = Group.GROUP_REFORMATTING.getName();

  /* FILE NAME */

  public static final String PARAM_FILE_POSITION_NAME = "file-position";
  public static final String PARAM_FILE_POSITION_SHORT_NAME = "fp";
  public static final String PARAM_FILE_POSITION_DESCRIPTION = "Position";
  public static final String PARAM_FILE_POSITION_HELP =
    shortEnumString(
      "The position where the substitution must be placed.",
      MapRenameSequencesGroupDatasetTransformation.RenameMode.class
    );
  public static final String PARAM_FILE_POSITION_HELP_GUI = toHtml(PARAM_FILE_POSITION_HELP);

  public static final String PARAM_FILE_DELIMITER_NAME = "file-delimiter";
  public static final String PARAM_FILE_DELIMITER_SHORT_NAME = "fd";
  public static final String PARAM_FILE_DELIMITER_DESCRIPTION = "Delimiter";
  public static final String PARAM_FILE_DELIMITER_HELP =
    "The delimiter for the substitution (only applicable when prefix or suffix modes are used).";
  public static final String PARAM_FILE_DELIMITER_HELP_GUI = toHtml(PARAM_FILE_POSITION_HELP);

  /* SEQUENCE HEADERS */

  public static final String PARAM_HEADER_POSITION_NAME = "header-position";
  public static final String PARAM_HEADER_POSITION_SHORT_NAME = "hp";
  public static final String PARAM_HEADER_POSITION_DESCRIPTION = "Position";
  public static final String PARAM_HEADER_POSITION_HELP =
    shortEnumString(
      "The position where the substitution must be placed.",
      MapRenameSequencesGroupDatasetTransformation.RenameMode.class
    );
  public static final String PARAM_HEADER_POSITION_HELP_GUI = toHtml(PARAM_HEADER_POSITION_HELP);

  public static final String PARAM_HEADER_DELIMITER_NAME = "header-delimiter";
  public static final String PARAM_HEADER_DELIMITER_SHORT_NAME = "hd";
  public static final String PARAM_HEADER_DELIMITER_DESCRIPTION = "Delimiter";
  public static final String PARAM_HEADER_DELIMITER_HELP = "The delimiter for the substitution.";
  public static final String PARAM_HEADER_DELIMITER_HELP_GUI = toHtml(PARAM_HEADER_DELIMITER_HELP);

  public static final String PARAM_HEADER_INDEX_NAME = "add-index";
  public static final String PARAM_HEADER_INDEX_SHORT_NAME = "i";
  public static final String PARAM_HEADER_INDEX_DESCRIPTION = "Add index";
  public static final String PARAM_HEADER_INDEX_HELP = "Whether an index must be added or not.";
  public static final String PARAM_HEADER_INDEX_HELP_GUI = toHtml(PARAM_HEADER_INDEX_HELP);

  public static final String PARAM_HEADER_INDEX_DELIMITER_NAME = "delimiter-index";
  public static final String PARAM_HEADER_INDEX_DELIMITER_SHORT_NAME = "di";
  public static final String PARAM_HEADER_INDEX_DELIMITER_DESCRIPTION = "Index delimiter";
  public static final String PARAM_HEADER_INDEX_DELIMITER_HELP = "The delimiter for the index.";
  public static final String PARAM_HEADER_INDEX_DELIMITER_HELP_GUI = toHtml(PARAM_HEADER_INDEX_DELIMITER_HELP);

  /* CONFIGURATION */

  public static final String PARAM_CONFIG_REPLACE_BLANK_SPACES_NAME = "replace-blank-space";
  public static final String PARAM_CONFIG_REPLACE_BLANK_SPACES_SHORT_NAME = "rbs";
  public static final String PARAM_CONFIG_REPLACE_BLANK_SPACES_DESCRIPTION = "Replace blank spaces";
  public static final String PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP = "Whether blank spaces must be replaced or not.";
  public static final String PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP_GUI =
    toHtml(PARAM_CONFIG_REPLACE_BLANK_SPACES_HELP);

  public static final String PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_NAME = "replace-special-characters";
  public static final String PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_SHORT_NAME = "rsc";
  public static final String PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_DESCRIPTION = "Replace special characters";
  public static final String PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP =
    "Whether special characters must be replaced or not. Special characters are: "
      + OsUtils.getInvalidWindowsFileCharacters().toString().replace("]", ".").replace("[", "");;
  public static final String PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP_GUI =
    toHtml(PARAM_CONFIG_REPLACE_SPECIAL_CHARACTERS_HELP);

  public static final String PARAM_CONFIG_REPLACEMENT_NAME = "replacement";
  public static final String PARAM_CONFIG_REPLACEMENT_SHORT_NAME = "r";
  public static final String PARAM_CONFIG_REPLACEMENT_DESCRIPTION = "Replacement";
  public static final String PARAM_CONFIG_REPLACEMENT_HELP = "The replacement string for those special characters.";
  public static final String PARAM_CONFIG_REPLACEMENT_HELP_GUI = toHtml(PARAM_CONFIG_REPLACEMENT_HELP);

  public static final String PARAM_CONFIG_SAVE_REPLACEMENTS_NAME = "save-replacements-map";
  public static final String PARAM_CONFIG_SAVE_REPLACEMENTS_SHORT_NAME = "srm";
  public static final String PARAM_CONFIG_SAVE_REPLACEMENTS_DESCRIPTION = "Save replacements map";
  public static final String PARAM_CONFIG_SAVE_REPLACEMENTS_HELP = "Whether the replacements map must be saved or not.";
  public static final String PARAM_CONFIG_SAVE_REPLACEMENTS_HELP_GUI = toHtml(PARAM_CONFIG_SAVE_REPLACEMENTS_HELP);

  public static final String PARAM_CONFIG_REPLACEMENTS_MAP_FILE_NAME = "map-file";
  public static final String PARAM_CONFIG_REPLACEMENTS_MAP_FILE_SHORT_NAME = "mp";
  public static final String PARAM_CONFIG_REPLACEMENTS_MAP_FILE_DESCRIPTION = "File";
  public static final String PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP = "The file to save the replacements map.";
  public static final String PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP_GUI =
    toHtml(PARAM_CONFIG_REPLACEMENTS_MAP_FILE_HELP);

  /* NCBI TAXONOMY */

  public static final String PARAM_NCBI_DELIMITER_NAME = "ncbi-delimiter";
  public static final String PARAM_NCBI_DELIMITER_SHORT_NAME = "nd";
  public static final String PARAM_NCBI_DELIMITER_DESCRIPTION = "Delimiter";
  public static final String PARAM_NCBI_DELIMITER_HELP = "The fields delimiter.";
  public static final String PARAM_NCBI_DELIMITER_HELP_GUI = toHtml(PARAM_NCBI_DELIMITER_HELP);

  public static final String PARAM_NCBI_FIELDS_NAME = "include-taxonomy-field";
  public static final String PARAM_NCBI_FIELDS_SHORT_NAME = "itf";
  public static final String PARAM_NCBI_FIELDS_DESCRIPTION = "Fields";
  public static final String PARAM_NCBI_FIELDS_HELP =
    shortEnumString("The fields to include in the substitution.", NcbiTaxonomyFields.class);
  public static final String PARAM_NCBI_FIELDS_HELP_GUI = toHtml(PARAM_NCBI_FIELDS_HELP);
}
