/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.ncbi;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class NcbiBlastRequestPageParser {

  public static String getRequestId(String response) throws IOException {
    Document document = Jsoup.parse(response);
    String requestId = document.getElementById("rid").attr("value");
    if (!requestId.trim().isEmpty()) {
      return requestId;
    } else {
      Elements errorElements = document.getElementsByClass("error");
      StringBuilder exceptionMessage = new StringBuilder("Request ID not found");
      errorElements.forEach(e -> {
        if (e.tagName().equals("p")) {
          exceptionMessage.append(": ").append(e.html());
        }
      });
      throw new IOException(exceptionMessage.toString());
    }
  }

  public static NcbiBlastRequestStatus getRequestStatus(String response) throws IOException {

    NcbiBlastRequestStatus status = NcbiBlastRequestStatus.UNKNOWN;

    for (String qBlastInfo : getQBlastInfoComments(response)) {
      if (qBlastInfo.contains("Status=WAITING")) {
        return NcbiBlastRequestStatus.WAITING;
      } else if (qBlastInfo.contains("Status=FAILED")) {
        return NcbiBlastRequestStatus.FAILED;
      } else if (qBlastInfo.contains("Status=UNKNOWN")) {
        return NcbiBlastRequestStatus.UNKNOWN;
      } else if (qBlastInfo.contains("Status=READY")) {
        status = NcbiBlastRequestStatus.READY;
      } else if (qBlastInfo.contains("ThereAreHits=yes")) {
        return NcbiBlastRequestStatus.READY_WITH_HITS;
      }
    }

    return status;
  }

  private static List<String> getQBlastInfoComments(String response) {
    Document document = Jsoup.parse(response);
    List<String> comments = new LinkedList<String>();

    for (Element e : document.getAllElements()) {
      for (Node n : e.childNodes()) {
        if (n instanceof Comment && ((Comment) n).getData().contains("QBlastInfoBegin")) {
          comments.add(((Comment) n).getData());
        }
      }
    }

    return comments;
  }
}
