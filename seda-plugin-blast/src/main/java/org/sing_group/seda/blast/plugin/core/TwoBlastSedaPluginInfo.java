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

import java.util.Arrays;

import org.sing_group.seda.blast.datatype.TwoWayBlastMode;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class TwoBlastSedaPluginInfo extends AbstractInfo {

  public static final String NAME = "BLAST: two-way ortholog identification";
  public static final String SHORT_NAME = "two-way";
  public static final String DESCRIPTION =
    "Find sequence orthologs in a set of FASTA files using the Reciprocal Best Hits method.";
  public static final String GROUP = Group.GROUP_BLAST.getName();

  public static final String PARAM_DATABASE_SEQUENCE_TYPE_NAME = "sequence-type";
  public static final String PARAM_DATABASE_SEQUENCE_TYPE_SHORT_NAME = "st";
  public static final String PARAM_DATABASE_SEQUENCE_TYPE_DESCRIPTION = "Sequence type";
  public static final String PARAM_DATABASE_SEQUENCE_TYPE_HELP =
    "The type of the sequences in the database. This is automatically selected based on the blast command to execute.";
  public static final String PARAM_DATABASE_SEQUENCE_TYPE_HELP_GUI = toHtml(PARAM_DATABASE_SEQUENCE_TYPE_HELP);

  public static final String PARAM_STORE_DATABASE_NAME = "store-database";
  public static final String PARAM_STORE_DATABASE_SHORT_NAME = "sd";
  public static final String PARAM_STORE_DATABASE_DESCRIPTION = "Store databases";
  public static final String PARAM_STORE_DATABASE_HELP =
    "Whether blast databases must be stored or not. By choosing to store them, they can be reused for future analysis.";
  public static final String PARAM_STORE_DATABASE_HELP_GUI = toHtml(PARAM_STORE_DATABASE_HELP, true);

  public static final String PARAM_DATABASE_DIRECTORY_NAME = "database-directory";
  public static final String PARAM_DATABASE_DIRECTORY_SHORT_NAME = "sd";
  public static final String PARAM_DATABASE_DIRECTORY_DESCRIPTION = "Databases directory";
  public static final String PARAM_DATABASE_DIRECTORY_HELP = "The directory where databases must be stored.";
  public static final String PARAM_DATABASE_DIRECTORY_HELP_GUI = toHtml(PARAM_DATABASE_DIRECTORY_HELP);

  public static final String PARAM_QUERY_MODE_NAME = "query-mode";
  public static final String PARAM_QUERY_MODE_SHORT_NAME = "qm";
  public static final String PARAM_QUERY_MODE_DESCRIPTION = "Mode";

  public static final String PARAM_QUERY_MODE_HELP =
    longEnumStringForCli(
      "The mode in which the query should be performed.",
      cliMap(
        TwoWayBlastMode.values(),
        Arrays.stream(TwoWayBlastMode.values()).map(TwoWayBlastMode::toString).toArray(String[]::new)
      )
    );
  public static final String PARAM_QUERY_MODE_HELP_GUI = toHtml("The mode in which the query should be performed.");

  public static final String PARAM_NUM_THREADS_NAME = "num-threads";
  public static final String PARAM_NUM_THREADS_SHORT_NAME = "th";
  public static final String PARAM_NUM_THREADS_DESCRIPTION = "Num. threads";
  public static final String PARAM_NUM_THREADS_HELP = "Number of threads to use.";
  public static final String PARAM_NUM_THREADS_HELP_GUI = toHtml(PARAM_NUM_THREADS_HELP);

  public static final TwoWayBlastMode DEFAULT_QUERY_MODE = TwoWayBlastMode.EXACT;
}
