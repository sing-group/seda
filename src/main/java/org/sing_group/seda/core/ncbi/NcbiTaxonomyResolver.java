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
