/*
 * #%L
 * SEquence DAtaset builder PfamScan plugin
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
package org.sing_group.seda.pfam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.sing_group.seda.datatype.Sequence;

public class PfamScanRequest {

  private static final String URL_RUN = "https://www.ebi.ac.uk/Tools/services/rest/pfamscan/run";
  private static final String URL_STATUS = "https://www.ebi.ac.uk/Tools/services/rest/pfamscan/status/";
  private static final String URL_RESULT = "https://www.ebi.ac.uk/Tools/services/rest/pfamscan/result/%s/out";

  private Sequence sequence;
  private PfamScanRequestConfiguration configuration;

  private String requestId = null;
  private PfamScanStatus status = null;
  private PfamScanAnnotations annotations = null;

  public PfamScanRequest(String requestId) {
    this.requestId = requestId;
  }

  public PfamScanRequest(Sequence sequence, PfamScanRequestConfiguration configuration) {
    this.sequence = sequence;
    this.configuration = configuration;
  }

  public Optional<String> getRequestId() {
    return Optional.ofNullable(this.requestId);
  }

  public void query() throws ClientProtocolException, IOException {
    if (this.requestId != null) {
      throw new RuntimeException("A query has already been done with request ID = " + this.requestId);
    }

    HttpPost httpPost = new HttpPost(URL_RUN);

    httpPost.setEntity(buildMultipartEntity());

    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpResponse response = httpClient.execute(httpPost);

    if (response.getStatusLine().getStatusCode() == 200) {
      this.requestId = EntityUtils.toString(response.getEntity());
      httpClient.close();
    } else {
      httpClient.close();
      throw new IOException("Query failed: " + response.getStatusLine().getReasonPhrase());
    }
  }

  private HttpEntity buildMultipartEntity() {
    MultipartEntityBuilder entityBuilder =
      MultipartEntityBuilder.create()
        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        .addTextBody("database", "pfam-a")
        .addTextBody("asp", this.configuration.isActiveSitePrediction() ? "true" : "false")
        .addTextBody("format", "txt")
        .addTextBody("email", this.configuration.getEmail())
        .addTextBody("title", this.sequence.getName())
        .addTextBody("sequence", this.sequence.getChain());

    return entityBuilder.build();
  }

  public PfamScanStatus getStatus() throws ClientProtocolException, IOException {
    if (this.requestId == null) {
      return PfamScanStatus.UNSUBMITED;
    }

    if (this.status == null || this.status == PfamScanStatus.RUNNING) {
      HttpGet httpGet = new HttpGet(new StringBuilder(URL_STATUS).append(this.requestId).toString());

      CloseableHttpClient httpClient = HttpClientBuilder.create().build();
      HttpResponse response = httpClient.execute(httpGet);

      int status = response.getStatusLine().getStatusCode();
      String reasonPhrase = response.getStatusLine().getReasonPhrase();
      String responseString = EntityUtils.toString(response.getEntity());
      httpClient.close();

      if (status == 200) {
        this.status = PfamScanStatus.valueOf(responseString);
      } else {
        throw new IOException(status + ": " + reasonPhrase);
      }
    }

    return this.status;
  }

  public PfamScanAnnotations getResult() throws ClientProtocolException, IOException {
    if (this.annotations != null) {
      return this.annotations;
    }

    if (this.requestId == null) {
      throw new RuntimeException("The query has not been submited yet");
    }

    if (this.status.equals(PfamScanStatus.FINISHED)) {
      HttpGet httpGet = new HttpGet(String.format(URL_RESULT, this.requestId));

      CloseableHttpClient httpClient = HttpClientBuilder.create().build();
      HttpResponse response = httpClient.execute(httpGet);

      int status = response.getStatusLine().getStatusCode();

      if (status == 200) {
        Path outputFile = Files.createTempFile("seda-pfam-scan-results", ".out");
        response.getEntity().writeTo(new FileOutputStream(outputFile.toFile()));
        httpClient.close();

        this.annotations = PfamScanResultsParser.parse(outputFile);
        return this.annotations;
      } else {
        String reasonPhrase = response.getStatusLine().getReasonPhrase();
        httpClient.close();
        throw new IOException(status + ": " + reasonPhrase);
      }
    } else {
      throw new IllegalStateException("The query status must be FINISHED. Current status: " + this.status);
    }
  }
}
