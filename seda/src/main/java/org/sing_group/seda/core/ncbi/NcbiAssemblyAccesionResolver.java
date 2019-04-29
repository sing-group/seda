/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NcbiAssemblyAccesionResolver {
  public static final String NCBI_URL = "https://www.ncbi.nlm.nih.gov";

  private static final Pattern ACCESSION_PATTERN = Pattern.compile("GC[AF]_[0-9]*" + Pattern.quote(".") + "[0-9]*");

  private int timeoutMillis;

  public NcbiAssemblyAccesionResolver() {
    this(10000);
  }

  public NcbiAssemblyAccesionResolver(int timeoutMillis) {
    this.timeoutMillis = timeoutMillis;
  }

  public List<NcbiAssemblyAccession> resolve(String... names) {
    List<NcbiAssemblyAccession> toret = new LinkedList<NcbiAssemblyAccession>();
    for (String name : names) {
      Optional<NcbiAssemblyAccession> accession = resolve(name);
      if (accession.isPresent()) {
        toret.add(accession.get());
      }
    }
    return toret;
  }

  public Optional<NcbiAssemblyAccession> resolve(String name) {
    Matcher matcher = ACCESSION_PATTERN.matcher(name);
    if (!matcher.find()) {
      return Optional.empty();
    }
    String accession = matcher.group(0);

    return resolveAccession(name, accession);
  }

  private Optional<NcbiAssemblyAccession> resolveAccession(String name, String accession) {
    try {
      Document doc = Jsoup.parse(assemblyUrl(accession), this.timeoutMillis);
      Elements infoTableSearch = doc.getElementsByAttributeValue("class", "assembly_summary_new margin_t0");
      if (infoTableSearch.size() > 0) {
        Element infoTable = infoTableSearch.get(0);
        if (infoTable.children().size() > 1) {
          Element firstChild = infoTable.child(0);
          Element organismNameDd;
          if (firstChild.text().contains("Description")) {
            organismNameDd = infoTable.child(3);
          } else {
            organismNameDd = infoTable.child(1);
          }
          if (organismNameDd.children().size() > 0) {
            Element organismLink = organismNameDd.child(0);
            return Optional
              .of(new NcbiAssemblyAccession(accession, organismLink.text(), taxonomyUrl(organismLink.attr("href"))));
          }
        }
      } else {
        Elements rprtElements = doc.getElementsByAttributeValue("class", "rprt");
        if (rprtElements.size() > 0) {
          Element rprtDiv = rprtElements.get(0);
          if (rprtDiv.children().size() > 0) {
            Element pOrganism = rprtDiv.child(0);
            if(pOrganism.children().size() > 0) {
              Optional<NcbiAssemblyAccession> res =
                resolveAccession(name, pOrganism.child(0).attr("href").replace("/assembly/", ""));
              if (res.isPresent()) {
                return Optional
                  .of(new NcbiAssemblyAccession(accession, res.get().getOrganismName(), res.get().getTaxonomyUrl()));
              }
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  private URL assemblyUrl(String accession) throws MalformedURLException {
    return new URL(NCBI_URL + "/assembly/" + accession);
  }

  private URL taxonomyUrl(String path) throws MalformedURLException {
    return new URL(NCBI_URL + path);
  }
}
