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

import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class PatternFilteringSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "Pattern filtering";
  public static final String SHORT_NAME = "pattern-filtering";
  public static final String DESCRIPTION = "Filter sequences based on a text pattern that can be interpreted as either regular expression or plain text.";
  public static final String GROUP = Group.GROUP_FILTERING.getName();

  public static final String PARAM_SEQUENCE_TARGET_NAME = "sequence-target";
  public static final String PARAM_SEQUENCE_TARGET_SHORT_NAME = "st";
  public static final String PARAM_SEQUENCE_TARGET_DESCRIPTION = "Sequence Target";
  public static final String PARAM_SEQUENCE_TARGET_HELP = shortEnumString(
    "The part of the sequences to look for the patterns.", SequenceTarget.class
  );

  public static final String PARAM_CONVERT_AMINO_ACID_DESCRIPTION = "Convert to amino acid sequence before pattern matching";
  public static final String PARAM_CONVERT_AMINO_ACID_HELP = "If this option is selected, then input nucleic acid sequences are translated into amino acid "
    + "sequences before applying the pattern matching. If a translated sequence matches the defined pattern, then the input nucleic acid sequence is reported.";
  public static final String PARAM_CONVERT_AMINO_ACID_HELP_GUI = toHtml(PARAM_CONVERT_AMINO_ACID_HELP);
}
