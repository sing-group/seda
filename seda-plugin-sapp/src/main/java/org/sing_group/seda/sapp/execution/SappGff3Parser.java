/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.execution;

import static java.nio.file.Files.write;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class SappGff3Parser {
  private File source;

  public SappGff3Parser(File source) {
    this.source = source;
  }

  public void extractCdsRegionsByGeneToFile(File dest) throws IOException {
    String currentGene = null;
    List<String> cdsLines = new LinkedList<>();
    for (String line : Files.readAllLines(this.source.toPath())) {
      if (line.startsWith("# start gene ")) {
        currentGene = line.replaceAll("# start gene ", "");
      } else if (!line.startsWith("#") && line.contains("\tCDS\t")) {
        if (currentGene == null) {
          throw new IllegalStateException("# start gene line not found before first GFF3 line.");
        }
        String gffLine = line.replace("\tCDS\t", "\t" + currentGene + "\t");
        cdsLines.add(gffLine);
      }
    }

    write(dest.toPath(), cdsLines.stream().collect(joining("\n", "", "\n")).getBytes());
  }
}
