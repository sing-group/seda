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

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class NcbiEutils {

  private static final String BASE_URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi";
  private static final int DELAY = 2000;
  private static final int BATCH_SIZE = 100;

  /*
   * https://www.ncbi.nlm.nih.gov/books/NBK25500/#_chapter1_Downloading_Full_Records_
   */
  public static void downloadFasta(File output, List<String> uids, String entrezDatabase)
    throws URISyntaxException, ClientProtocolException, IOException {
    for (int i = 0; i < uids.size(); i = i + BATCH_SIZE) {
      int start = i;
      int end = Math.min(i + BATCH_SIZE, uids.size());
      _downloadFasta(output, uids.subList(start, end), entrezDatabase);

      try {
        Thread.sleep(DELAY);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static void _downloadFasta(File output, List<String> uids, String entrezDatabase)
    throws URISyntaxException, ClientProtocolException, IOException {
    URIBuilder ub = new URIBuilder(BASE_URL);
    ub.addParameter("db", entrezDatabase);
    ub.addParameter("id", uids.stream().collect(joining(",")));
    ub.addParameter("rettype", "fasta");
    ub.addParameter("retmode", "text");

    HttpGet httpGet = new HttpGet(ub.toString());

    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpResponse response = httpClient.execute(httpGet);

    if (response.getStatusLine().getStatusCode() == 200) {
      if (response.getEntity() != null) {}
      try (FileOutputStream outstream = new FileOutputStream(output, true)) {
        response.getEntity().writeTo(outstream);
      } finally {
        httpClient.close();
      }
    } else {
      throw new IOException("Query failed");
    }
  }
}
