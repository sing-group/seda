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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.sing_group.seda.pfam.PfamScanAnnotation;
import org.sing_group.seda.pfam.PfamScanAnnotations;
import org.sing_group.seda.pfam.PfamScanResultsParser;

public class PfamScanResultsParserTest {

  @Test
  public void parsePfamScanResults() throws IOException {
    Path output = Paths.get("src/test/resources/pfam-scan-result.out");

    PfamScanAnnotations annotations = PfamScanResultsParser.parse(output);

    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(
      new PfamScanAnnotation("29", "209", "PF06652.12", "Methuselah_N", "Domain"),
      annotations.get(0)
    );
    Assert.assertEquals(
      new PfamScanAnnotation("218", "470", "PF00002.24", "7tm_2", "Family"),
      annotations.get(1)
    );
  }
}
