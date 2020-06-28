/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui;

import java.awt.Color;
import java.awt.Font;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HtmlMessage extends JEditorPane {
  private static final long serialVersionUID = 1L;

  public HtmlMessage(String htmlBody) {
    super("text/html", "<html><body style=\"" + getStyle() + "\">" + htmlBody + "</body></html>");

    this.addHyperlinkListener(new HyperlinkListener() {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
          try {
            GuiUtils.openWebpage(e.getURL().toURI());
          } catch (URISyntaxException e1) {}
        }
      }
    });

    this.setEditable(false);
    this.setBorder(null);
  }

  static StringBuffer getStyle() {
    JLabel label = new JLabel();
    Font font = label.getFont();
    Color color = label.getBackground();

    StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
    style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
    style.append("font-size:" + font.getSize() + "pt;");
    style.append("background-color: rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");");

    return style;
  }
}