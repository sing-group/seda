/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.transformation.dataset;

import static java.nio.file.Files.createTempDirectory;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.cga.execution.CgaBinariesExecutor;
import org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration;
import org.sing_group.seda.cga.execution.CgaPipeline;
import org.sing_group.seda.cga.execution.CgaPipelineParameters;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class CgaPipelineSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {

  private DatatypeFactory factory;
  private CgaCompiPipelineConfiguration configuration;
  private File referenceFasta;
  private CgaResults cgaResults;
  private String additionalCompiParameters;
  private CgaBinariesExecutor dockerCgaBinariesExecutor;

  public CgaPipelineSequencesGroupDatasetTransformation(
    DatatypeFactory factory, CgaCompiPipelineConfiguration configuration, File referenceFasta, CgaResults cgaResults,
    String additionalCompiParameters, CgaBinariesExecutor dockerCgaBinariesExecutor
  ) {
    this.factory = factory;
    this.configuration = configuration;
    this.referenceFasta = referenceFasta;
    this.cgaResults = cgaResults;
    this.additionalCompiParameters = additionalCompiParameters;
    this.dockerCgaBinariesExecutor = dockerCgaBinariesExecutor;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) throws TransformationException {
    try {
      return runPipeline(dataset);
    } catch (IOException | InterruptedException e) {
      throw new TransformationException("Error while running CGA. " + e.getMessage());
    }
  }

  private SequencesGroupDataset runPipeline(SequencesGroupDataset dataset) throws IOException, InterruptedException {
    SequencesGroup reference = this.factory.newSequencesGroup(this.referenceFasta.toPath());

    List<SequencesGroup> results = new LinkedList<>();
    for (SequencesGroup sequencesGroup : dataset.getSequencesGroups().collect(toList())) {
      File temporaryWorkingDir = createTempDirectory(sequencesGroup.getName()).toFile();

      new CgaPipeline(
        dockerCgaBinariesExecutor,
        new CgaPipelineParameters(temporaryWorkingDir, this.configuration, this.additionalCompiParameters),
        sequencesGroup, reference
      ).run();

      results.add(
        this.factory.newSequencesGroup(
          this.newSequencesGroupName(sequencesGroup.getName()), sequencesGroup.getProperties(),
          this.getResults(temporaryWorkingDir)
        )
      );
    }

    return this.factory.newSequencesGroupDataset(results.toArray(new SequencesGroup[results.size()]));
  }

  private String newSequencesGroupName(String name) {
    return name + "_" + this.cgaResults.getResultsFile().replace("/", "_");
  }

  private List<Sequence> getResults(File workingDir) {
    return this.factory.newSequencesGroup(new File(workingDir, this.cgaResults.getResultsFile()).toPath())
      .getSequences().collect(toList());
  }
}
