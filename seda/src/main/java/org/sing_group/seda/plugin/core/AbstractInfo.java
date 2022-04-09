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

import static java.util.Collections.emptyList;

import java.util.List;

public abstract class AbstractInfo {

  public static final String PARAM_DOCKER_MODE_NAME = "docker-mode";
  public static final String PARAM_DOCKER_MODE_SHORT_NAME = "dk";
  public static final String PARAM_DOCKER_MODE_DESCRIPTION = "Uses a docker image to execute the transformation";

  public static final String PARAM_LOCAL_MODE_NAME = "local-mode";
  public static final String PARAM_LOCAL_MODE_SHORT_NAME = "lc";
  public static final String PARAM_LOCAL_MODE_DESCRIPTION = "Uses a local binary to execute the transformation";

  public static String toHtml(String plainHelp) {
    return toHtml(plainHelp, true);
  }

  public static String toHtml(String plainHelp, boolean addLineBreakOnStops) {
    return toHtml(plainHelp, emptyList(), emptyList(), addLineBreakOnStops);
  }

  public static String toHtml(
    String plainHelp, List<String> boldWords, List<String> italicWords, boolean addLineBreakOnStops
  ) {

    for (String word : boldWords) {
      plainHelp = plainHelp.replace(word, wrapHtmlTag("b", word));
    }

    for (String word : italicWords) {
      plainHelp = plainHelp.replace(word, wrapHtmlTag("i", word));
    }

    plainHelp = plainHelp.replace("\n", "<br/>");
    plainHelp = plainHelp.replace("\t", "");

    if (addLineBreakOnStops) {
      plainHelp = plainHelp.replace(". ", ".<br/>");
    }

    return wrapHtmlTag("html", plainHelp);
  }

  private static String wrapHtmlTag(String tag, String text) {
    StringBuilder sb = new StringBuilder();

    sb.append("<").append(tag).append(">").append(text).append("</").append(tag).append(">");

    return sb.toString();
  }
}
