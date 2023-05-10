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
package org.sing_group.seda.blast.cli.uniprot;

import static java.util.Arrays.asList;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_ALIGNMENT_CUTOFF;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_DATABASE_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_EXPECTATION_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_FILTER_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_MATRIX_OPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DEFAULT_OUTPUT_TYPE;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.GROUP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_DATABASE_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_DATABASE_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_DATABASE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_EXPECTATION_VALUE_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_EXPECTATION_VALUE_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_EXPECTATION_VALUE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_FILTER_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_FILTER_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_FILTER_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_GAPPED_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_GAPPED_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_GAPPED_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_HITS_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_HITS_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_HITS_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_MATRIX_OPTION_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_MATRIX_OPTION_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_MATRIX_OPTION_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_HELP;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.PARAM_OUTPUT_TYPE_SHORT_NAME;
import static org.sing_group.seda.blast.plugin.core.UniProtBlastSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.blast.ncbi.parameters.OutputTypeParameter;
import org.sing_group.seda.blast.transformation.provider.uniprot.UniProtBlastTransformationProvider;
import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.AlignmentCutoffOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.FilterOption;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.MatrixOption;

public class UniProtBlastCommand extends SedaCommand {

  public static final DefaultValuedStringOption OPTION_DATABASE =
    new DefaultValuedStringOption(
      PARAM_DATABASE_NAME, PARAM_DATABASE_SHORT_NAME, PARAM_DATABASE_HELP,
      DEFAULT_DATABASE_OPTION.name().toLowerCase()
    );
  
  
  public static final DefaultValuedStringOption OPTION_OUTPUT_TYPE =
    new DefaultValuedStringOption(
      PARAM_OUTPUT_TYPE_NAME, PARAM_OUTPUT_TYPE_SHORT_NAME, PARAM_OUTPUT_TYPE_HELP,
      DEFAULT_OUTPUT_TYPE.name().toLowerCase()
    );
  
  public static final DefaultValuedStringOption OPTION_EXPECTATION_VALUE =
    new DefaultValuedStringOption(
      PARAM_EXPECTATION_VALUE_NAME, PARAM_EXPECTATION_VALUE_SHORT_NAME, PARAM_EXPECTATION_VALUE_HELP,
      DEFAULT_EXPECTATION_OPTION.name().toLowerCase()
    );
  
  public static final DefaultValuedStringOption OPTION_MATRIX_OPTION =
    new DefaultValuedStringOption(
      PARAM_MATRIX_OPTION_NAME, PARAM_MATRIX_OPTION_SHORT_NAME, PARAM_MATRIX_OPTION_HELP,
      DEFAULT_MATRIX_OPTION.name().toLowerCase()
    );
  
  public static final DefaultValuedStringOption OPTION_FILTER =
    new DefaultValuedStringOption(
      PARAM_FILTER_NAME, PARAM_FILTER_SHORT_NAME, PARAM_FILTER_HELP,
      DEFAULT_FILTER_OPTION.name().toLowerCase()
    );
  
  public static final FlagOption GAPPED_OPTION =
    new FlagOption(PARAM_GAPPED_NAME, PARAM_GAPPED_SHORT_NAME, PARAM_GAPPED_HELP); 

  public static final DefaultValuedStringOption OPTION_HITS =
    new DefaultValuedStringOption(
      PARAM_HITS_NAME, PARAM_HITS_SHORT_NAME, PARAM_HITS_HELP,
      DEFAULT_ALIGNMENT_CUTOFF.name().toLowerCase()
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
  protected List<Option<?>> createSedaOptions() {
    return asList(
      OPTION_DATABASE,
      OPTION_OUTPUT_TYPE,
      OPTION_EXPECTATION_VALUE,
      OPTION_MATRIX_OPTION,
      OPTION_FILTER,
      GAPPED_OPTION,
      OPTION_HITS
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    UniProtBlastTransformationProvider provider = new UniProtBlastTransformationProvider();
    
    provider.setDatabase(getEnumValue(parameters, DatabaseOption.class, OPTION_DATABASE));
    provider.setOutputType(getEnumValue(parameters, OutputTypeParameter.class, OPTION_OUTPUT_TYPE));
    provider.setExpectation(getEnumValue(parameters, ExpectationOption.class, OPTION_EXPECTATION_VALUE));
    provider.setMatrix(getEnumValue(parameters, MatrixOption.class, OPTION_MATRIX_OPTION));
    provider.setFilter(getEnumValue(parameters, FilterOption.class, OPTION_FILTER));
    provider.setGapped(parameters.hasFlag(GAPPED_OPTION));
    provider.setHits(getEnumValue(parameters, AlignmentCutoffOption.class, OPTION_HITS));
    
    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<UniProtBlastTransformationProvider>()
      .read(parametersFile, UniProtBlastTransformationProvider.class);
  }
}
