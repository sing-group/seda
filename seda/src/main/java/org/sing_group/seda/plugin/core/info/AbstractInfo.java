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
package org.sing_group.seda.plugin.core.info;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    plainHelp = plainHelp.replace("\n", "<br/><br/>");
    plainHelp = plainHelp.replace("\t", "");

    if (addLineBreakOnStops) {
      plainHelp = plainHelp.replace(". ", ".<br/><br/>");
    }
    
    return wrapHtmlTag("html", plainHelp);
  }

  private static String wrapHtmlTag(String tag, String text) {
    StringBuilder sb = new StringBuilder();

    sb.append("<").append(tag).append(">").append(text).append("</").append(tag).append(">");

    return sb.toString();
  }

  public static <E extends Enum<E>> String shortEnumString(String description, Class<E> clazz) {
    StringBuilder sb = new StringBuilder(description);

    sb.append(
      EnumSet.allOf(clazz).stream().map(Enum::name).map(String::toLowerCase).collect(joining(", ", " One of: ", "."))
    );

    return sb.toString();
  }

  public static String longEnumStringForCli(String description, Map<String, String> enumDescriptions) {
    StringBuilder sb = new StringBuilder(description);

    sb.append(" It can be one of: \n");

    enumDescriptions.forEach((k, v) -> {
      sb.append("\t- ").append(k).append(": ").append(v).append("\n");
    });

    return sb.toString();
  }

  public static String longEnumStringForGui(String description, Map<String, String> enumDescriptions) {
    StringBuilder sb = new StringBuilder();

    sb.append(description).append(" It can be one of: <br/><ul>");

    enumDescriptions.forEach((k, v) -> {
      sb.append("<li>").append(k).append(": ").append(v).append("</ul>");
    });

    return sb.toString();
  }

  public static <E extends Enum<E>> Map<String, String> cliMap(E[] constants, String... strings) {
    Map<String, String> toret = new HashMap<String, String>();

    for (int i = 0; i < constants.length; i++) {
      toret.put(constants[i].name().toLowerCase(), strings[i]);
    }

    return toret;
  }

  public static <E extends Enum<E>> Map<String, String> guiMap(E[] constants, String... strings) {
    Map<String, String> toret = new HashMap<String, String>();

    for (int i = 0; i < constants.length; i++) {
      toret.put(constants[i].toString(), strings[i]);
    }

    return toret;
  }
}
