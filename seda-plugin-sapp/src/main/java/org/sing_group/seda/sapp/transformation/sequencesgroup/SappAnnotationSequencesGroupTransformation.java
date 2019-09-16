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
package org.sing_group.seda.sapp.transformation.sequencesgroup;

import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.io.FastaWriter.writeFasta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.sapp.execution.SappAnnotationPipeline;
import org.sing_group.seda.sapp.execution.SappBinariesExecutor;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class SappAnnotationSequencesGroupTransformation implements SequencesGroupTransformation {
  private final DatatypeFactory factory;
  private final SappBinariesExecutor sappBinariesExecutor;
  private final BedToolsBinariesExecutor bedToolsBinariesExecutor;
  private final SappSpecies sappSpecies;
  private final SappCodon sappCodon;

  public SappAnnotationSequencesGroupTransformation(
    SappBinariesExecutor sappBinariesExecutor, BedToolsBinariesExecutor bedToolsBinariesExecutor,
    SappCodon sappCodon, SappSpecies sappSpecies
  ) {
    this(getDefaultDatatypeFactory(), sappBinariesExecutor, bedToolsBinariesExecutor, sappCodon, sappSpecies);
  }

  public SappAnnotationSequencesGroupTransformation(
    DatatypeFactory factory, SappBinariesExecutor sappBinariesExecutor,
    BedToolsBinariesExecutor bedToolsBinariesExecutor, SappCodon sappCodon, SappSpecies sappSpecies
  ) {
    this.sappBinariesExecutor = sappBinariesExecutor;
    this.bedToolsBinariesExecutor = bedToolsBinariesExecutor;
    this.factory = factory;
    this.sappSpecies = sappSpecies;
    this.sappCodon = sappCodon;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    try {
      return sappAnnotation(sequencesGroup, factory);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("Error while running the SAPP annotation commands." + e.getMessage());
    }
  }

  private SequencesGroup sappAnnotation(SequencesGroup sequencesGroup, DatatypeFactory factory)
    throws IOException, InterruptedException {
    final Path inputFasta = Files.createTempFile(sequencesGroup.getName(), "fasta");
    writeFasta(inputFasta, sequencesGroup.getSequences());

    final Path outputFasta = Files.createTempFile(sequencesGroup.getName(), "_sapp_annotation.fasta");

    new SappAnnotationPipeline(factory, sappBinariesExecutor, bedToolsBinariesExecutor, sappCodon, sappSpecies)
      .annotate(inputFasta.toFile(), outputFasta.toFile());

    final List<Sequence> annotatedSequences = factory.newSequencesGroup(outputFasta).getSequences().collect(toList());

    return factory.newSequencesGroup(sequencesGroup.getName(), sequencesGroup.getProperties(), annotatedSequences);
  }
}
