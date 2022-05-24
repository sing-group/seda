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
package org.sing_group.seda.cli.parameters;

import static org.sing_group.seda.plugin.core.info.common.HeaderMatcherInfo.PARAM_SEQUENCE_MATCHING_HELP;
import static org.sing_group.seda.plugin.core.info.common.HeaderMatcherInfo.PARAM_SEQUENCE_MATCHING_NAME;
import static org.sing_group.seda.plugin.core.info.common.HeaderMatcherInfo.PARAM_SEQUENCE_MATCHING_SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class HeaderMatcherParameters {

  private final DefaultValuedStringOption sequenceMatchingOption;

  public HeaderMatcherParameters() {
    this(PARAM_SEQUENCE_MATCHING_NAME, PARAM_SEQUENCE_MATCHING_SHORT_NAME, PARAM_SEQUENCE_MATCHING_HELP);
  }

  public HeaderMatcherParameters(String name, String shortName) {
    this(name, shortName, PARAM_SEQUENCE_MATCHING_HELP);
  }

  public HeaderMatcherParameters(String name, String shortName, String help) {
    this.sequenceMatchingOption = new DefaultValuedStringOption(
      name, shortName, help, HeaderFilteringConfiguration.FilterType.SEQUENCE_NAME.name().toLowerCase()
    );
  }

  public HeaderMatcher getHeaderMatcher(Parameters parameters) throws IllegalArgumentException {
    HeaderMatcher headerMatcher = null;
    if (
      parameters.getSingleValue(this.sequenceMatchingOption)
        .equalsIgnoreCase(HeaderFilteringConfiguration.FilterType.SEQUENCE_NAME.name())
    ) {
      headerMatcher = new SequenceNameHeaderMatcher();
    } else {
      headerMatcher = RegexHeaderMatcherParameters.getRegexHeaderMatcher(parameters);
    }

    return headerMatcher;
  }

  public List<Option<?>> getOptionList() {
    final List<Option<?>> options = new ArrayList<>();
    options.add(this.sequenceMatchingOption);
    options.addAll(RegexHeaderMatcherParameters.getOptionList());

    return options;
  }
}
