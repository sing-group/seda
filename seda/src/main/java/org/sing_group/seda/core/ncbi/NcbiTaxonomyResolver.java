/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.ncbi;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NcbiTaxonomyResolver {
  private int timeoutMillis;

  public NcbiTaxonomyResolver() {
    this(10000);
  }

  public NcbiTaxonomyResolver(int timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  public Optional<NcbiTaxonomyInfo> resolve(URL url) {
    try {
      Document doc = Jsoup.parse(url, this.timeoutMillis);
      Optional<Element> lineageDl = findLineageDl(doc);
      if (lineageDl.isPresent()) {
        return extractNcbiTaxonomyInfo(lineageDl.get());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  private Optional<Element> findLineageDl(Document doc) {
    Elements dlElements = doc.getElementsByTag("dl");
    for (Element dlElement : dlElements) {
      if (dlElement.childNodeSize() > 0) {
        Element dtChild = dlElement.child(0);
        if (dtChild.childNodeSize() > 0) {
          if (dtChild.child(0).tagName().equals("a")) {
            Element link = dtChild.child(0);
            if (link.childNodeSize() == 1 && link.child(0).tagName().equals("em")) {
              if (link.child(0).text().equals("Lineage")) {
                return Optional.of(dlElement);
              }
            }
          }
        }
      }
    }
    return Optional.empty();
  }

  private Optional<NcbiTaxonomyInfo> extractNcbiTaxonomyInfo(Element lineageDl) {
    Map<NcbiTaxonomyFields, String> values = new HashMap<>();
    if (lineageDl.getElementsByTag("dd").size() > 0) {
      Element lineageDd = lineageDl.getElementsByTag("dd").get(0);
      for (NcbiTaxonomyFields field : NcbiTaxonomyFields.values()) {
        Elements fieldElements = lineageDd.getElementsByAttributeValue("title", field.nodeTitle());
        for (Element element : fieldElements) {
          if (element.attr("title").equals(field.nodeTitle())) {
            values.put(field, element.text());
            break;
          }
        }
      }

    }
    return Optional.of(new NcbiTaxonomyInfo(values));
  }
}
