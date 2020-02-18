/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.execution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.io.FastaWriter;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.ChangePropertiesSequenceTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupSortTransformation;

public class SappAnnotationPipeline {
  private final DatatypeFactory factory;
  private final SappBinariesExecutor sappBinariesExecutor;
  private final BedToolsBinariesExecutor bedToolsBinariesExecutor;
  private final SappSpecies sappSpecies;
  private final SappCodon sappCodon;

  public SappAnnotationPipeline(
    SappBinariesExecutor sappBinariesExecutor,
    BedToolsBinariesExecutor bedToolsBinariesExecutor,
    SappCodon sappCodon, SappSpecies sappSpecies
  ) {
    this(
      DatatypeFactory.getDefaultDatatypeFactory(),
      sappBinariesExecutor, bedToolsBinariesExecutor,
      sappCodon, sappSpecies
    );
  }

  public SappAnnotationPipeline(
    DatatypeFactory factory,
    SappBinariesExecutor sappBinariesExecutor,
    BedToolsBinariesExecutor bedToolsBinariesExecutor,
    SappCodon sappCodon, SappSpecies sappSpecies
  ) {
    this.factory = factory;
    this.sappBinariesExecutor = sappBinariesExecutor;
    this.bedToolsBinariesExecutor = bedToolsBinariesExecutor;
    this.sappSpecies = sappSpecies;
    this.sappCodon = sappCodon;
  }

  public void annotate(File inputFasta, File outputFasta) throws IOException, InterruptedException {
    final Path sappAnnotationDirectory = Files.createTempDirectory("seda_sapp_annotation");

    final Path convertedFastaFile = sappAnnotationDirectory.resolve(inputFasta.getName() + ".hdt");

    this.sappBinariesExecutor.fasta2hdt(
      inputFasta,
      convertedFastaFile.toFile(),
      "sample_identifier",
      this.sappCodon,
      this.sappSpecies,
      "-genome -chromosome"
    );

    final Path augustusResultFile = sappAnnotationDirectory.resolve(inputFasta.getName() + "_augustus.hdt");

    this.sappBinariesExecutor.augustus(
      convertedFastaFile.toFile(),
      augustusResultFile.toFile(),
      this.sappCodon,
      this.sappSpecies
    );

    final Path bedToolsInputBedFile = sappAnnotationDirectory.resolve(convertedFastaFile.toFile().getName() + ".gff3");
    final Path bedToolsFilteredInputBedFile =
      sappAnnotationDirectory.resolve(convertedFastaFile.toFile().getName() + ".gff3.filtered");

    new SappGff3Parser(bedToolsInputBedFile.toFile()).extractCdsRegionsByGeneToFile(bedToolsFilteredInputBedFile.toFile());

    final Path bedToolsInputFastaFile =
      sappAnnotationDirectory.resolve(convertedFastaFile.toFile().getName() + ".fasta");
    final Path bedToolsOutputFastaFile =
      sappAnnotationDirectory.resolve(bedToolsInputBedFile.toFile().getName() + ".fasta");

    this.bedToolsBinariesExecutor
      .getFasta(
        bedToolsInputFastaFile.toFile(), bedToolsFilteredInputBedFile.toFile(), bedToolsOutputFastaFile.toFile(),
        "-name"
      );

    final Path bedToolsConcatenatedFastaFile =
      sappAnnotationDirectory.resolve(bedToolsOutputFastaFile.toFile().getName() + ".concatenated");

    FastaWriter
      .writeFasta(
        bedToolsConcatenatedFastaFile, concatenateSequences(
          bedToolsOutputFastaFile, factory
        ).getSequences()
      );

    Files.move(bedToolsConcatenatedFastaFile, outputFasta.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  private static SequencesGroup concatenateSequences(Path fasta, DatatypeFactory factory) {
    Map<String, Object> sequencePropertiesMap = new HashMap<>();
    sequencePropertiesMap.put(Sequence.PROPERTY_CHAIN_COLUMNS, 80);

    SequenceTransformation transformation =
      new ChangePropertiesSequenceTransformation(
        factory, sequencePropertiesMap
      );

    SequencesGroupDataset dataset =
      SequencesGroupDatasetTransformation.concat(
        new ConcatenateSequencesGroupDatasetTransformation(
          "concatenaded", new SequenceNameHeaderMatcher()
        ),
        new ComposedSequencesGroupDatasetTransformation(
          new ComposedSequencesGroupTransformation(
            transformation
          ),
          new SequencesGroupSortTransformation(SequenceComparator.ALPHABPETICAL.getComparator(SequenceTarget.HEADER), false, factory)
        )
      )
        .transform(
          factory.newSequencesGroupDataset(
            factory.newSequencesGroup(
              fasta
            )
          )
        );

    return dataset.getSequencesGroups().findFirst().get();
  }
}
