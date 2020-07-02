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

import static java.nio.file.Files.readAllLines;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.sing_group.seda.blast.ncbi.NcbiBlastRequestPageParser;
import org.sing_group.seda.blast.ncbi.NcbiBlastRequestStatus;

public class NcbiBlastRequestPageParserTest {

  @Test
  public void testSubmissionSuccess() throws IOException {
    String response = getString(Paths.get("src/test/resources/ncbi_web/ncbi_web_output_success.html"));

    Assert.assertEquals("F48T4WPR016", NcbiBlastRequestPageParser.getRequestId(response));
  }

  @Test(expected = IOException.class)
  public void testSubmissionFailure() throws IOException {
    String response = getString(Paths.get("src/test/resources/ncbi_web/ncbi_web_output_error.html"));

    NcbiBlastRequestPageParser.getRequestId(response);
  }

  @Test
  public void testStatusUnknown() throws IOException {
    String response = getString(Paths.get("src/test/resources/ncbi_web/ncbi_web_output_status_unknown.html"));

    Assert.assertEquals(NcbiBlastRequestStatus.UNKNOWN, NcbiBlastRequestPageParser.getRequestStatus(response));
  }

  @Test
  public void testStatusReady() throws IOException {
    String response = getString(Paths.get("src/test/resources/ncbi_web/ncbi_web_output_status_ready.html"));

    Assert.assertEquals(NcbiBlastRequestStatus.READY, NcbiBlastRequestPageParser.getRequestStatus(response));
  }

  @Test
  public void testStatusReadyWithHits() throws IOException {
    String response = getString(Paths.get("src/test/resources/ncbi_web/ncbi_web_output_status_ready_with_hits.html"));

    Assert.assertEquals(NcbiBlastRequestStatus.READY_WITH_HITS, NcbiBlastRequestPageParser.getRequestStatus(response));
  }

  private static String getString(Path path) throws IOException {
    return readAllLines(path).stream().collect(joining("\n"));
  }
}
