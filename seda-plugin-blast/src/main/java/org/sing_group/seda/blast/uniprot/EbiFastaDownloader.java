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
package org.sing_group.seda.blast.uniprot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class EbiFastaDownloader {

  private static final String BASE_URL = "https://www.uniprot.org/uniprot/";
  private static final int DELAY = 1000;

  public static void downloadFasta(File output, List<String> entries) throws IOException {
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    for (String entry : entries) {
      HttpGet httpGet = new HttpGet(BASE_URL + entry + ".fasta");
      HttpResponse response = httpClient.execute(httpGet);

      if (response.getStatusLine().getStatusCode() == 200) {
        try (FileOutputStream outstream = new FileOutputStream(output, true)) {
          response.getEntity().writeTo(outstream);
        }
        try {
          Thread.sleep(DELAY);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      } else {
        throw new IOException("Error downloading entry " + entry);
      }
    }
  }

  public static void main(String[] args) throws IOException {
    downloadFasta(
      new File("/tmp/test.fasta"),
      Arrays.asList("F7CN20", "B0X778", "A0A0P0WRT9")
    );
  }
}
