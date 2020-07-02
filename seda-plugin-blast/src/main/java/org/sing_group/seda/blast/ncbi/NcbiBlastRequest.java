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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.sing_group.seda.blast.ncbi.parameters.NcbiBlastParameter;
import org.xml.sax.SAXException;

public class NcbiBlastRequest {

  private static final String BASE_URL = "https://blast.ncbi.nlm.nih.gov/blast/Blast.cgi";

  private NcbiBlastType blastType;
  private NcbiBlastDatabase blastDatabase;
  private String query;
  private List<NcbiBlastParameter> parameters;

  private String requestId;
  private NcbiBlastRequestStatus status;
  private NcbiBlastHits blastHits;

  public NcbiBlastRequest(String requestId) {
    this.requestId = requestId;
  }

  public NcbiBlastRequest(
    NcbiBlastType blastType, NcbiBlastDatabase blastDatabase, String query, List<NcbiBlastParameter> parameters
  ) {
    this.blastType = blastType;
    this.blastDatabase = blastDatabase;
    this.query = query;
    this.parameters = parameters;
    this.checkBlastAndDatabaseCompatibility();
  }

  private void checkBlastAndDatabaseCompatibility() {
    if (!this.blastType.getDatabaseType().equals(this.blastDatabase.getSequenceType())) {
      throw new IllegalArgumentException(
        "The selected BLAST operation requires a " + this.blastType.getDatabaseType().toString()
          + " database but the selected database type is " + this.blastDatabase.getSequenceType()
      );
    }
  }

  public void query() throws ClientProtocolException, IOException, URISyntaxException {
    if (this.requestId != null) {
      throw new RuntimeException("A query has already been done with request ID = " + this.requestId);
    }

    URIBuilder ub = new URIBuilder(BASE_URL);
    ub.addParameter("CMD", "Put");
    ub.addParameter("PROGRAM", this.blastType.getProgram());
    ub.addParameter("DATABASE", this.blastDatabase.getName());
    ub.addParameter("QUERY", this.query);
    parameters.forEach(p -> {
      ub.addParameter(p.paramName(), p.value());
    });

    HttpPost httpPost = new HttpPost(ub.toString());
    httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpResponse response = httpClient.execute(httpPost);
    String responseString = EntityUtils.toString(response.getEntity());
    httpClient.close();

    requestId = NcbiBlastRequestPageParser.getRequestId(responseString);
  }

  public Optional<String> getRequestId() {
    return Optional.ofNullable(this.requestId);
  }

  public NcbiBlastRequestStatus getStatus() throws URISyntaxException, ClientProtocolException, IOException {
    if (this.requestId == null) {
      return NcbiBlastRequestStatus.UNSUBMITED;
    }

    if (this.status == null || this.status == NcbiBlastRequestStatus.WAITING) {
      URIBuilder ub = new URIBuilder(BASE_URL);
      ub.addParameter("CMD", "Get");
      ub.addParameter("FORMAT_OBJECT", "SearchInfo");
      ub.addParameter("RID", this.requestId);

      HttpGet httpGet = new HttpGet(ub.toString());

      CloseableHttpClient httpClient = HttpClientBuilder.create().build();
      HttpResponse response = httpClient.execute(httpGet);
      String responseString = EntityUtils.toString(response.getEntity());
      httpClient.close();

      status = NcbiBlastRequestPageParser.getRequestStatus(responseString);
    }

    return status;
  }

  public NcbiBlastHits getBlastHits() throws URISyntaxException, ClientProtocolException, IOException {
    if (this.requestId == null) {
      throw new RuntimeException("The query has not been submited yet");
    }

    if (this.blastHits == null) {
      if (this.getStatus().equals(NcbiBlastRequestStatus.READY_WITH_HITS)) {
        URIBuilder ub = new URIBuilder(BASE_URL);
        ub.addParameter("CMD", "Get");
        ub.addParameter("FORMAT_TYPE", "XML");
        ub.addParameter("RID", this.requestId);

        HttpGet httpGet = new HttpGet(ub.toString());

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpGet);
        String responseString = EntityUtils.toString(response.getEntity());
        httpClient.close();

        try {
          this.blastHits = NcbiBlastHits.fromXml(responseString);
        } catch (SAXException e) {
          throw new IOException(e);
        }
      } else {
        this.blastHits = new NcbiBlastHits();
      }
    }

    return this.blastHits;
  }
}
