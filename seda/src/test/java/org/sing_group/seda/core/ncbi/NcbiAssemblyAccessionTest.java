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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.sing_group.seda.core.ncbi.NcbiAssemblyAccesionResolver.NCBI_URL;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class NcbiAssemblyAccessionTest {

  @Test
  public void testEquals() throws MalformedURLException, URISyntaxException {
    NcbiAssemblyAccession actual = new NcbiAssemblyAccession(
      "GCF_000151265.2",
      "Micromonas pusilla CCMP1545 (green algae)",
      new URL(NCBI_URL + "Taxonomy/Browser/wwwtax.cgi?id=564608")
    );
    
    assertEquals(new NcbiAssemblyAccession(
      "GCF_000151265.2",
      "Micromonas pusilla CCMP1545 (green algae)",
      new URL(NCBI_URL + "Taxonomy/Browser/wwwtax.cgi?id=564608")
    ), actual);
  }

  @Test
  public void testNotEquals() throws MalformedURLException, URISyntaxException {
    NcbiAssemblyAccession actual = new NcbiAssemblyAccession(
      "GCF_000151265",
      "Micromonas pusilla CCMP1545 (green algae)",
      new URL(NCBI_URL + "Taxonomy/Browser/wwwtax.cgi?id=564608")
    );
    
    assertNotEquals(new NcbiAssemblyAccession(
      "GCF_000151265.2",
      "Micromonas pusilla CCMP1545 (green algae)",
      new URL(NCBI_URL + "Taxonomy/Browser/wwwtax.cgi?id=564608")
    ), actual);
  }
}
