/*
 * #%L
 * SEquence DAtaset builder CGA plugin
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
package org.sing_group.seda.cga.cli;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_CGA_RESULTS_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_CGA_RESULTS_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_CGA_RESULTS_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_COMPI_TASKS_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_COMPI_TASKS_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_COMPI_TASKS_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_DOCKER_MODE_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_INTRON_BP_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_INTRON_BP_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_INTRON_BP_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MAX_DIST_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MAX_DIST_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MAX_DIST_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MIN_FULL_NUCLEOTIDE_SIZE_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MIN_FULL_NUCLEOTIDE_SIZE_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MIN_FULL_NUCLEOTIDE_SIZE_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_MIN_SELECTION_CRITERION_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_REFERENCE_FASTA_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_REFERENCE_FASTA_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_REFERENCE_FASTA_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SELECTION_CORRECTION_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SELECTION_CORRECTION_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SELECTION_CORRECTION_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SELECTION_CRITERION_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SELECTION_CRITERION_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SKIP_PULL_DOCKER_IMAGES_HELP;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SKIP_PULL_DOCKER_IMAGES_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.PARAM_SKIP_PULL_DOCKER_IMAGES_SHORT_NAME;
import static org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo.SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.cga.execution.CgaBinariesExecutor;
import org.sing_group.seda.cga.execution.CgaCompiPipelineConfiguration;
import org.sing_group.seda.cga.execution.DefaultDockerCgaBinariesExecutor;
import org.sing_group.seda.cga.plugin.core.CgaPipelineSedaPluginInfo;
import org.sing_group.seda.cga.transformation.dataset.CgaResults;
import org.sing_group.seda.cga.transformation.provider.CgaPipelineTransformationProvider;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class CgaPipelineCommand extends ExternalSoftwareExecutionCommand {
  
  private static final CgaCompiPipelineConfiguration DEFAULT_CGA_PARAMETERS = 
    new CgaCompiPipelineConfiguration();

  public static final FileOption OPTION_REFERENCE_FASTA =
    new FileOption(
      PARAM_REFERENCE_FASTA_NAME, PARAM_REFERENCE_FASTA_SHORT_NAME, 
      PARAM_REFERENCE_FASTA_HELP, false, true
    );
  
  public static final DefaultValuedStringOption OPTION_CGA_RESULTS =
    new DefaultValuedStringOption(
      PARAM_CGA_RESULTS_NAME, PARAM_CGA_RESULTS_SHORT_NAME,
      PARAM_CGA_RESULTS_HELP, CgaResults.PREDICTED_CDS.name().toLowerCase()
    );
  
  public static final IntegerDefaultValuedStringConstructedOption OPTION_NUM_TASKS =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_COMPI_TASKS_NAME, PARAM_COMPI_TASKS_SHORT_NAME,
      PARAM_COMPI_TASKS_HELP, 4
    );
  
  public static final IntegerDefaultValuedStringConstructedOption OPTION_MAX_DIST =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_MAX_DIST_NAME, PARAM_MAX_DIST_SHORT_NAME, 
      PARAM_MAX_DIST_HELP, DEFAULT_CGA_PARAMETERS.getMaxDist()
    );
  
  public static final IntegerDefaultValuedStringConstructedOption OPTION_MIN_FULL_NUCLEOTIDE_SIZE =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_MIN_FULL_NUCLEOTIDE_SIZE_NAME, PARAM_MIN_FULL_NUCLEOTIDE_SIZE_SHORT_NAME, 
      PARAM_MIN_FULL_NUCLEOTIDE_SIZE_HELP, DEFAULT_CGA_PARAMETERS.getMinFullNucleotideSize() 
    );
  
  public static final IntegerDefaultValuedStringConstructedOption OPTION_INTRON_BP =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_INTRON_BP_NAME, PARAM_INTRON_BP_SHORT_NAME, 
      PARAM_INTRON_BP_HELP, DEFAULT_CGA_PARAMETERS.getIntronBp()
    );
  
  public static final DefaultValuedStringOption OPTION_SELECTION_CRITERION =
    new DefaultValuedStringOption(
      PARAM_SELECTION_CRITERION_NAME, PARAM_SELECTION_CRITERION_SHORT_NAME, 
      PARAM_MIN_SELECTION_CRITERION_HELP, DEFAULT_CGA_PARAMETERS.getSelectionCriterion().name().toLowerCase()
    );
  
  
  public static final IntegerDefaultValuedStringConstructedOption OPTION_SELECTION_CORRECTION =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_SELECTION_CORRECTION_NAME, PARAM_SELECTION_CORRECTION_SHORT_NAME, 
      PARAM_SELECTION_CORRECTION_HELP, DEFAULT_CGA_PARAMETERS.getSelectionCorrection()
    );
  
  public static final BooleanOption OPTION_SKIP_PULL_DOCKER_IMAGES_NAME =
    new BooleanOption(
      PARAM_SKIP_PULL_DOCKER_IMAGES_NAME, PARAM_SKIP_PULL_DOCKER_IMAGES_SHORT_NAME, 
      PARAM_SKIP_PULL_DOCKER_IMAGES_HELP, true, false
    );
  
  public static final StringOption OPTION_DOCKER_MODE =
    new StringOption(
      PARAM_DOCKER_MODE_NAME, PARAM_DOCKER_MODE_SHORT_NAME, PARAM_DOCKER_MODE_HELP, true, true
    );

  @Override
  public String getName() {
    return SHORT_NAME;
  }

  @Override
  public String getDescriptiveName() {
    return NAME;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }
  
  @Override
  protected String getSedaGroup() {
    return CgaPipelineSedaPluginInfo.GROUP;
  }

  @Override
  protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
    return Collections.emptyMap();
  }

  @Override
  protected List<Option<?>> getLocalOptionsList() {
    return emptyList();
  }

  @Override
  protected List<Option<?>> createExternalSedaOptions() {
    return asList(
      OPTION_REFERENCE_FASTA,
      OPTION_CGA_RESULTS,
      OPTION_NUM_TASKS,
      OPTION_MAX_DIST,
      OPTION_INTRON_BP,
      OPTION_MIN_FULL_NUCLEOTIDE_SIZE,
      OPTION_SELECTION_CRITERION,
      OPTION_SELECTION_CORRECTION,
      OPTION_SKIP_PULL_DOCKER_IMAGES_NAME,
      OPTION_DOCKER_MODE
     );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    CgaPipelineTransformationProvider provider = new CgaPipelineTransformationProvider();
    
    if(!parameters.hasOption(OPTION_REFERENCE_FASTA)) {
      formattedValidationError("The reference fasta file is mandatory.");
    }
    
    Path referenceFasta = parameters.getSingleValue(OPTION_REFERENCE_FASTA).toPath();

    if (!Files.exists(referenceFasta)) {
      formattedValidationError("Invalid path. The path to the reference fasta file must be valid and exist.");
    }
    provider.setReferenceFasta(referenceFasta.toFile());
    
    CgaResults cgaResults = null;

    try {
      cgaResults = CgaResults.valueOf(parameters.getSingleValueString(OPTION_CGA_RESULTS).toUpperCase());
      provider.setCgaResults(cgaResults);
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_CGA_RESULTS);
    }

    if (parameters.hasOption(OPTION_NUM_TASKS)) {
      provider.setCompiTasks(parameters.getSingleValue(OPTION_NUM_TASKS));
    }


    int maxDist = parameters.getSingleValue(OPTION_MAX_DIST);

    int intronBp = parameters.getSingleValue(OPTION_INTRON_BP);
    
    int minFullNucleotideSize = parameters.getSingleValue(OPTION_MIN_FULL_NUCLEOTIDE_SIZE);
    
    CgaCompiPipelineConfiguration.SelectionCriterion selectionCriterion = null;
    
    try {
      selectionCriterion = CgaCompiPipelineConfiguration.SelectionCriterion.valueOf(parameters.getSingleValueString(OPTION_SELECTION_CRITERION).toUpperCase());
    } catch (IllegalArgumentException e) {
      invalidEnumValue(OPTION_SELECTION_CRITERION);
    }
    
    int selectionCorrection = parameters.getSingleValue(OPTION_SELECTION_CORRECTION);
    
    boolean skipPullDockerImages = parameters.hasOption(OPTION_SKIP_PULL_DOCKER_IMAGES_NAME);
    
    provider.setCgaCompiPipelineConfiguration(
      new CgaCompiPipelineConfiguration(
        maxDist, intronBp, minFullNucleotideSize, selectionCriterion, 
        selectionCorrection, skipPullDockerImages
      )
    );
    
    CgaBinariesExecutor executor = null;
    if (parameters.hasOption(OPTION_DOCKER_MODE)) {
      executor = new DefaultDockerCgaBinariesExecutor(parameters.getSingleValue(OPTION_DOCKER_MODE));
    } else {
      executor = new DefaultDockerCgaBinariesExecutor();
    }

    provider.setCgaBinariesExecutor(Optional.of(executor));

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<CgaPipelineTransformationProvider>()
      .read(parametersFile, CgaPipelineTransformationProvider.class);
  }
}
