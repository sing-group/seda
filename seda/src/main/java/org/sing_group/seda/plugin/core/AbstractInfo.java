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

import java.util.Collections;
import java.util.List;

public abstract class AbstractInfo {

  public static String toHtml(String plainHelp, boolean addLineBreakOnStops) {
    return toHtml(plainHelp, Collections.emptyList(), Collections.emptyList(), addLineBreakOnStops);
  }

  public static String toHtml(
    String plainHelp, List<String> boldWords, List<String> italicWords, boolean addLineBreakOnStops
  ) {

    for (String word : boldWords) {
      plainHelp = plainHelp.replace(word, wrapHtmlTag("b", word, false));
    }

    for (String word : italicWords) {
      plainHelp = plainHelp.replace(word, wrapHtmlTag("i", word, false));
    }

    if (addLineBreakOnStops) {
      plainHelp = plainHelp.replace(".", "</br>");
    }

    return wrapHtmlTag("html", plainHelp, true);
  }

  private static String wrapHtmlTag(String tag, String text, boolean closeTag) {
    String wrapped = "<" + tag + ">".concat(text);
    wrapped = wrapped.concat("<" + tag + ">");

    if (closeTag) {
      wrapped = wrapped.replace("<", "</");
    }

    return wrapped;
  }
}
