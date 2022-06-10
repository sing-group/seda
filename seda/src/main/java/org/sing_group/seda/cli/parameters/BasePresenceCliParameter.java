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

import org.sing_group.seda.core.operations.BasePresence;

public class BasePresenceCliParameter extends BasePresence {
  private static final double MIN_PRESENCE = 0.0;
  private static final double MAX_PRESENCE = 1.0;
  private static final String CONFIG_BASE_FILTER_REGEX = "config\\((1|0(.[0-9]+)?)/(1|0(.[0-9]+)?)\\):[a-zA-Z]+";

  public BasePresenceCliParameter(String basePresence) {
    double minimumPresence = MIN_PRESENCE;
    double maximumPresence = MAX_PRESENCE;
    String base = basePresence;

    if (basePresence.contains("config")) {
      if (!basePresence.matches(CONFIG_BASE_FILTER_REGEX)) {
        throw new IllegalArgumentException(
          "Wrong configuration. Type 'help <command>' to see the available options."
        );
      }
      String config =
        basePresence
          .split(":")[0]
            .replace("config", "")
            .replace("(", "")
            .replace(")", "");
      minimumPresence = Double.parseDouble(config.split("/")[0]);
      maximumPresence = Double.parseDouble(config.split("/")[1]);
      base = basePresence.split(":")[1];
    }

    this.minimumPresence = minimumPresence;
    this.maximumPresence = maximumPresence;
    this.bases = requireNonEmptyList(getBasesList(base.toCharArray()));
  }
}
