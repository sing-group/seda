/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.blast.cli.ncbi;

import static java.util.Arrays.asList;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.DEFAULT_OUTPUT_TYPE;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.GROUP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_EVALUE_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_EVALUE_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_EVALUE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_FILTER_APPLY_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_FILTER_APPLY_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_FILTER_APPLY_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_FILTER_LOOKUP_DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_FILTER_LOOKUP_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_FILTER_LOOKUP_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_HITS_LIST_SIZE_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_HITS_LIST_SIZE_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_HITS_LIST_SIZE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_MATRIX_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_MATRIX_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_MATRIX_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_NCBI_BLAST_DATABASE_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_NCBI_BLAST_DATABASE_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_NCBI_BLAST_TYPE_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_NCBI_BLAST_TYPE_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_NCBI_BLAST_TYPE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_THRESHOLD_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_THRESHOLD_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_THRESHOLD_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_WORD_SIZE_HELP;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_WORD_SIZE_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.PARAM_WORD_SIZE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.NcbiBlastSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.blast.ncbi.NcbiBlastDatabase;
import org.sing_group.seda.blast.ncbi.NcbiBlastType;
import org.sing_group.seda.blast.ncbi.parameters.FilterParameter;
import org.sing_group.seda.blast.ncbi.parameters.MatrixParameter;
import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.transformation.provider.ncbi.NcbiBlastTransformationProvider;
import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class NcbiBlastCommand extends SedaCommand {

  public static final StringOption OPTION_BLAST_TYPE =
    new StringOption(
      PARAM_NCBI_BLAST_TYPE_NAME, PARAM_NCBI_BLAST_TYPE_SHORT_NAME, PARAM_NCBI_BLAST_TYPE_HELP,
      true, true
    );

  public static final StringOption OPTION_BLAST_DATABASE =
    new StringOption(
      PARAM_NCBI_BLAST_DATABASE_NAME, PARAM_NCBI_BLAST_DATABASE_NAME, PARAM_NCBI_BLAST_DATABASE_HELP,
      true, true
    );

  public static final DefaultValuedStringOption OPTION_OUTPUT_TYPE =
    new DefaultValuedStringOption(
      PARAM_OUTPUT_TYPE_NAME, PARAM_OUTPUT_TYPE_SHORT_NAME, PARAM_OUTPUT_TYPE_HELP,
      DEFAULT_OUTPUT_TYPE.name().toLowerCase()
    );

  public static final StringOption OPTION_MATRIX =
    new StringOption(
      PARAM_MATRIX_NAME, PARAM_MATRIX_SHORT_NAME, PARAM_MATRIX_HELP, true, true
    );

  public static final FlagOption OPTION_FILTER =
    new FlagOption(PARAM_FILTER_APPLY_NAME, PARAM_FILTER_APPLY_SHORT_NAME, PARAM_FILTER_APPLY_HELP);

  public static final FlagOption OPTION_FILTER_LOOKUP =
    new FlagOption(PARAM_FILTER_LOOKUP_NAME, PARAM_FILTER_LOOKUP_SHORT_NAME, PARAM_FILTER_LOOKUP_DESCRIPTION);

  public static final StringOption OPTION_EVALUE =
    new StringOption(
      PARAM_EVALUE_NAME, PARAM_EVALUE_SHORT_NAME, PARAM_EVALUE_HELP, true, true
    );

  public static final IntegerOption OPTION_WORD_SIZE =
    new IntegerOption(
      PARAM_WORD_SIZE_NAME, PARAM_WORD_SIZE_SHORT_NAME, PARAM_WORD_SIZE_HELP, true
    );

  public static final IntegerOption OPTION_HITS_LIST_SIZE =
    new IntegerOption(
      PARAM_HITS_LIST_SIZE_NAME, PARAM_HITS_LIST_SIZE_SHORT_NAME, PARAM_HITS_LIST_SIZE_HELP, true
    );

  public static final IntegerOption OPTION_THRESHOLD =
    new IntegerOption(
      PARAM_THRESHOLD_NAME, PARAM_THRESHOLD_SHORT_NAME, PARAM_THRESHOLD_HELP, true
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
    return GROUP;
  }

  @Override
  protected List<Option<?>> getMandatoryOptions() {
    return asList(
      OPTION_BLAST_TYPE,
      OPTION_BLAST_DATABASE
    );
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(
      OPTION_BLAST_TYPE,
      OPTION_BLAST_DATABASE,
      OPTION_OUTPUT_TYPE,
      OPTION_MATRIX,
      OPTION_FILTER,
      OPTION_FILTER_LOOKUP,
      OPTION_EVALUE,
      OPTION_WORD_SIZE,
      OPTION_HITS_LIST_SIZE,
      OPTION_THRESHOLD
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    NcbiBlastTransformationProvider provider = new NcbiBlastTransformationProvider();
    
    provider.setBlastDatabase(getEnumValue(parameters, NcbiBlastDatabase.class, OPTION_BLAST_DATABASE));
    provider.setBlastType(getEnumValue(parameters, NcbiBlastType.class, OPTION_BLAST_TYPE));
    provider.setOutputType(getEnumValue(parameters, OutputTypeParameter.class, OPTION_OUTPUT_TYPE));

    if (parameters.hasOption(OPTION_MATRIX)) {
      provider.setMatrix(getEnumValue(parameters, MatrixParameter.class, OPTION_MATRIX));
    }
    provider.setFilter(getFilter(parameters));
    if (parameters.hasOption(OPTION_EVALUE)) {
      provider.setExpectValue(getDoubleValue(parameters, OPTION_EVALUE));
    }
    if (parameters.hasOption(OPTION_HITS_LIST_SIZE)) {
      provider.setHitListSizeValue(parameters.getSingleValue(OPTION_HITS_LIST_SIZE));
    }
    if (parameters.hasOption(OPTION_WORD_SIZE)) {
      provider.setWordSizeValue(parameters.getSingleValue(OPTION_WORD_SIZE));
    }
    if (parameters.hasOption(OPTION_THRESHOLD)) {
      provider.setThresholdValue(parameters.getSingleValue(OPTION_THRESHOLD));
    }
    
    return provider;
  }
  
  private FilterParameter getFilter(Parameters parameters) {
    if (parameters.hasFlag(OPTION_FILTER)) {
      return new FilterParameter(true, parameters.hasFlag(OPTION_FILTER_LOOKUP));
    } else {
      return new FilterParameter(false);
    }
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<NcbiBlastTransformationProvider>()
      .read(parametersFile, NcbiBlastTransformationProvider.class);
  }
}
