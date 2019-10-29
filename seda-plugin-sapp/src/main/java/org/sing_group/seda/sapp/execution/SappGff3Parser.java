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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * This class helps in filtering and converting the GFF3 file produced by Augustus. This file include one or more
 * predicted genes (each one delimited by {@code start gene gN} and {@code end gene gN}) for one or more input sequences
 * (delimited by {@code prediction on sequence number X}).
 * 
 * The {@code extractCdsRegionsByGeneToFile} method does the following processing and saves the transformed GFF3 into a
 * new file:
 * <ol>
 * <li>Filter predictions to retain only those with name {@code CDS}.</li>
 * <li><Changes the name of each GFF3 line by the {@code <sequence-name>_<gene-number>}.</li>
 * </ol>
 * 
 * This way, all CDS predictions in the same gene and sequence will have the exact name.
 * 
 * @author hlfernandez
 *
 */
public class SappGff3Parser {
  private File source;

  public SappGff3Parser(File source) {
    this.source = source;
  }

  /**
   * This method applies the following processing and saves the transformed GFF3 into a new file:
   * <ol>
   * <li>Filter predictions to retain only those with name {@code CDS}.</li>
   * <li><Changes the name of each GFF3 line by the {@code <sequence-name>_<gene-number>}.</li>
   * </ol>
   * 
   * @param dest the file to write the transformed GFF3
   * @throws IOException if an error occurs writing the output file
   */
  public void extractCdsRegionsByGeneToFile(File dest) throws IOException {
    Map<String, Integer> geneBySequenceCount = new HashMap<>();
    String originalSequence = null;
    List<String> cdsLines = new LinkedList<>();
    Integer currentGene = null;
    for (String line : Files.readAllLines(this.source.toPath())) {
      if (line.startsWith("# start gene g")) {
        currentGene = geneBySequenceCount.getOrDefault(originalSequence, 0) + 1;
        geneBySequenceCount.put(originalSequence, currentGene);
      } else if (line.startsWith("# ----- prediction on sequence number")) {
        int start = line.indexOf("name = ") + 7;
        originalSequence = line.substring(start, line.indexOf(")", start));
      } else if (!line.startsWith("#") && line.contains("\tCDS\t")) {
        if (originalSequence == null) {
          throw new IllegalStateException("# ----- prediction on sequence number before first GFF3 line.");
        }
        if (currentGene == null) {
          throw new IllegalStateException("# start gene line not found before first GFF3 line.");
        }
        String gffLine = line.replace("\tCDS\t", "\t" + originalSequence + "_" + currentGene + "\t");
        cdsLines.add(gffLine);
      }
    }

    write(dest.toPath(), cdsLines.stream().collect(joining("\n", "", "\n")).getBytes());
  }
}
