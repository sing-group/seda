/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.getorf.cli;

 import static java.util.Arrays.asList;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.DEFAULT_MAX_SIZE;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.DEFAULT_MIN_SIZE;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.GROUP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_ADDITIONAL_PARAMS_SHORT_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_DOCKER_MODE_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_FIND_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_FIND_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_FIND_SHORT_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_LOCAL_MODE_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MAX_SIZE_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MAX_SIZE_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MAX_SIZE_SHORT_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MIN_SIZE_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MIN_SIZE_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_MIN_SIZE_SHORT_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_TABLE_HELP;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_TABLE_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PARAM_TABLE_SHORT_NAME;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.PROPERTY_ENABLE_LOCAL_EXECUTION_EMBOSS;
import static org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo.SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_DOCKER_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.AbstractInfo.PARAM_LOCAL_MODE_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.emboss.core.EmbossGetOrfSedaPluginInfo;
import org.sing_group.seda.emboss.execution.DefaultEmbossBinariesExecutor;
import org.sing_group.seda.emboss.execution.DockerEmbossBinariesExecutor;
import org.sing_group.seda.emboss.execution.EmbossBinariesExecutor;
import org.sing_group.seda.emboss.getorf.datatype.FindParam;
import org.sing_group.seda.emboss.getorf.datatype.TableParam;
import org.sing_group.seda.emboss.transformation.provider.GetOrfTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

 public class GetOrfCommand extends ExternalSoftwareExecutionCommand {

   public static final StringOption OPTION_DOCKER_MODE =
     new StringOption(
       SOFTWARE_EXECUTION_CATEGORY,
       PARAM_DOCKER_MODE_NAME, PARAM_DOCKER_MODE_SHORT_NAME,
       PARAM_DOCKER_MODE_HELP, true, true
     );

   public static final StringOption OPTION_LOCAL_MODE =
     new StringOption(
       SOFTWARE_EXECUTION_CATEGORY,
       PARAM_LOCAL_MODE_NAME, PARAM_LOCAL_MODE_SHORT_NAME,
       PARAM_LOCAL_MODE_HELP, true, true
     );
   
   public static final DefaultValuedStringOption OPTION_TABLE =
     new DefaultValuedStringOption(
       PARAM_TABLE_NAME, PARAM_TABLE_SHORT_NAME, PARAM_TABLE_HELP,
       EmbossGetOrfSedaPluginInfo.DEFAULT_TABLE
     );

   public static final StringOption OPTION_FIND =
     new StringOption(PARAM_FIND_NAME, PARAM_FIND_SHORT_NAME, PARAM_FIND_HELP, true, true);
   
   public static final DefaultValuedStringOption OPTION_MIN_SIZE =
     new DefaultValuedStringOption(
       PARAM_MIN_SIZE_NAME, PARAM_MIN_SIZE_SHORT_NAME, PARAM_MIN_SIZE_HELP,  
       Integer.toString(DEFAULT_MIN_SIZE)
     );

   public static final DefaultValuedStringOption OPTION_MAX_SIZE =
     new DefaultValuedStringOption(
       PARAM_MAX_SIZE_NAME, PARAM_MAX_SIZE_SHORT_NAME, PARAM_MAX_SIZE_HELP,  
       Integer.toString(DEFAULT_MAX_SIZE)
     );

   public static final StringOption OPTION_ADDITIONAL_PARAMS =
     new StringOption(
       PARAM_ADDITIONAL_PARAMS_NAME, PARAM_ADDITIONAL_PARAMS_SHORT_NAME, PARAM_ADDITIONAL_PARAMS_HELP,
       true, true
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
   protected Map<Option<?>, String> getLocalOptionsToEnablePropertyMap() {
     return fromKeyValue(OPTION_LOCAL_MODE, PROPERTY_ENABLE_LOCAL_EXECUTION_EMBOSS);
   }

   @Override
   protected List<Option<?>> getLocalOptionsList() {
     return asList(OPTION_LOCAL_MODE);
   }

   @Override
   protected List<Option<?>> createExternalSedaOptions() {
     return asList(
       OPTION_LOCAL_MODE, 
       OPTION_DOCKER_MODE, 
       OPTION_TABLE,
       OPTION_FIND,
       OPTION_MIN_SIZE,
       OPTION_MAX_SIZE,
       OPTION_ADDITIONAL_PARAMS
     );
   }
   
   @Override
   protected List<Option<?>> getMandatoryOptions() {
     return Arrays.asList(OPTION_FIND);
   }

   @Override
   protected TransformationProvider getTransformation(Parameters parameters) {
     GetOrfTransformationProvider provider = new GetOrfTransformationProvider();
     
     provider.setFind(getEnumValue(parameters, FindParam.class, OPTION_FIND));
     provider.setTable(getEnumValue(parameters, TableParam.class, OPTION_TABLE));
     provider.setMinSize(getIntegerFromStringOption(parameters, OPTION_MIN_SIZE));
     provider.setMaxSize(getIntegerFromStringOption(parameters, OPTION_MAX_SIZE));
     
     Optional<String> additionalParameters = getAdditionalParameters(parameters);
     if (additionalParameters.isPresent()) {
       provider.setAdditionalParameters(additionalParameters.get());
     }

     provider.setEmbossBinariesExecutor(Optional.of(getEmbossBinariesExecutor(parameters)));

     return provider;
   }

   private EmbossBinariesExecutor getEmbossBinariesExecutor(Parameters parameters) {
     validateSingleExecutionMode(parameters, OPTION_LOCAL_MODE, OPTION_DOCKER_MODE);
     
     EmbossBinariesExecutor executor =
       new DockerEmbossBinariesExecutor(DockerEmbossBinariesExecutor.getDefaultDockerImage());

     if (parameters.hasOption(OPTION_LOCAL_MODE)) {
       File embossBinariesDirectory = new File(parameters.getSingleValueString(OPTION_LOCAL_MODE));

       if (embossBinariesDirectory.isDirectory()) {
         executor = new DefaultEmbossBinariesExecutor(embossBinariesDirectory);
       } else {
         formattedValidationError("The specified EMBOSS directory does not exist or it is not a directory.");
       }
     }

     if (parameters.hasOption(OPTION_DOCKER_MODE)) {
       executor = new DockerEmbossBinariesExecutor(parameters.getSingleValue(OPTION_DOCKER_MODE));
     }
     
     return executor;
  }

   private Optional<String> getAdditionalParameters(Parameters parameters) {
     if (!parameters.hasOption(OPTION_ADDITIONAL_PARAMS)) {
       return Optional.empty();
     } else {
       return Optional.of(parameters.getSingleValue(OPTION_ADDITIONAL_PARAMS));
     }
   }

   @Override
   protected TransformationProvider getTransformation(File parametersFile) throws IOException {
     return new JsonObjectReader<GetOrfTransformationProvider>()
         .read(parametersFile, GetOrfTransformationProvider.class);
   }
 }
