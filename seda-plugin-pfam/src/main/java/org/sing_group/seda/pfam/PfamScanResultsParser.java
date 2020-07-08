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
import java.nio.file.Files;
import java.nio.file.Path;

public class PfamScanResultsParser {

  public static PfamScanAnnotations parse(Path outputFile) throws IOException {
    PfamScanAnnotations annotations = new PfamScanAnnotations();

    Files.readAllLines(outputFile).stream()
      .filter(l -> !l.isEmpty() && !l.startsWith("#"))
      .forEach(l -> {
        String[] line = l.split("\\s+");

        annotations.add(
          new PfamScanAnnotation(
            line[1], line[2], line[5], line[6], line[7]
          )
        );
      });

    return annotations;
  }
}
