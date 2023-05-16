/*
 * #%L
 * SEquence DAtaset builder PfamScan plugin
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
package org.sing_group.seda.pfam.cli;

import static java.util.Arrays.asList;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.DEFAULT_BATCH_DELAY_FACTOR;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.DEFAULT_ERROR_POLICY;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.DEFAULT_EVALUE;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_ACTIVE_SITE_PREDICTION_HELP;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_ACTIVE_SITE_PREDICTION_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_ACTIVE_SITE_PREDICTION_SHORT_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_BATCH_DELAY_FACTOR_HELP;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_BATCH_DELAY_FACTOR_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_BATCH_DELAY_FACTOR_SHORT_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_EMAIL_HELP;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_EMAIL_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_EMAIL_SHORT_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_ERROR_POLICY_HELP;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_ERROR_POLICY_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_ERROR_POLICY_SHORT_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_EVALUE_HELP;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_EVALUE_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.PARAM_EVALUE_SHORT_NAME;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.pfam.PfamScanSequenceErrorPolicy;
import org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo;
import org.sing_group.seda.pfam.transformations.provider.PfamScanTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class PfamScanCommand extends SedaCommand {
  
  public static final StringOption OPTION_EMAIL =
    new StringOption(
      PARAM_EMAIL_NAME, PARAM_EMAIL_SHORT_NAME, PARAM_EMAIL_HELP,
      true, true
    );
  
  public static final FlagOption OPTION_ACTIVE_SITE_PREDICTION =
    new FlagOption(
      PARAM_ACTIVE_SITE_PREDICTION_NAME, PARAM_ACTIVE_SITE_PREDICTION_SHORT_NAME, PARAM_ACTIVE_SITE_PREDICTION_HELP
    );
  
  public static final DefaultValuedStringOption OPTION_EVALUE =
    new DefaultValuedStringOption(
      PARAM_EVALUE_NAME, PARAM_EVALUE_SHORT_NAME, PARAM_EVALUE_HELP, Double.toString(DEFAULT_EVALUE)
    );
  
  public static final DefaultValuedStringOption OPTION_ERROR_POLICY =
    new DefaultValuedStringOption(
      PARAM_ERROR_POLICY_NAME, PARAM_ERROR_POLICY_SHORT_NAME, PARAM_ERROR_POLICY_HELP, DEFAULT_ERROR_POLICY.name().toLowerCase()
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_BATCH_DELAY_FACTOR =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_BATCH_DELAY_FACTOR_NAME, PARAM_BATCH_DELAY_FACTOR_SHORT_NAME, PARAM_BATCH_DELAY_FACTOR_HELP, DEFAULT_BATCH_DELAY_FACTOR
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
    return PfamScanSedaPluginInfo.GROUP;
  }
  
  @Override
  protected List<Option<?>> getMandatoryOptions() {
    return asList(
      OPTION_EMAIL
    );
  }

  @Override
  protected List<Option<?>> createSedaOptions() {
    return asList(
      OPTION_EMAIL,
      OPTION_ACTIVE_SITE_PREDICTION,
      OPTION_EVALUE,
      OPTION_ERROR_POLICY,
      OPTION_BATCH_DELAY_FACTOR
    );
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    PfamScanTransformationProvider provider = new PfamScanTransformationProvider();
    provider.setEmail(parameters.getSingleValueString(OPTION_EMAIL));

    if (parameters.hasFlag(OPTION_ACTIVE_SITE_PREDICTION)) {
      provider.setActiveSitePrediction(true);
    }
    if (parameters.hasOption(OPTION_EVALUE)) {
      provider.setEvalue(getDoubleValue(parameters, OPTION_EVALUE));
    }
    if (parameters.hasOption(OPTION_ERROR_POLICY)) {
      provider.setErrorPolicy(getEnumValue(parameters, PfamScanSequenceErrorPolicy.class, OPTION_ERROR_POLICY));
    }
    if (parameters.hasOption(OPTION_BATCH_DELAY_FACTOR)) {
      provider.setBatchDelayFactor(parameters.getSingleValue(OPTION_BATCH_DELAY_FACTOR));
    }

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<PfamScanTransformationProvider>()
      .read(parametersFile, PfamScanTransformationProvider.class);
  }
}
