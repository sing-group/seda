/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.cli.command;

import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_FROM_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_FROM_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_FROM_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_INTERVAL_REPLACEMENT_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_INTERVAL_REPLACEMENT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_INTERVAL_REPLACEMENT_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_TO_DELIMITER_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_TO_DELIMITER_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.PARAM_TO_DELIMITER_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderReplaceIntervalSedaPluginInfo.SHORT_NAME;

import java.util.ArrayList;
import java.util.List;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.IntervalReplaceRenamer;
import org.sing_group.seda.gui.rename.RenameHeaderTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedStringOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class RenameHeaderReplaceIntervalCommand extends RenameHeaderCommand {
  public static final StringOption OPTION_FROM = new StringOption(
    PARAM_FROM_NAME, PARAM_FROM_SHORT_NAME, PARAM_FROM_HELP, true, true
  );

  public static final StringOption OPTION_TO_DELIMITER = new StringOption(
    PARAM_TO_DELIMITER_NAME, PARAM_TO_DELIMITER_SHORT_NAME, PARAM_TO_DELIMITER_HELP, true, true
  );

  public static final DefaultValuedStringOption OPTION_REPLACEMENT = new DefaultValuedStringOption(
    PARAM_INTERVAL_REPLACEMENT_NAME, PARAM_INTERVAL_REPLACEMENT_SHORT_NAME, PARAM_INTERVAL_REPLACEMENT_HELP, ""
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
  protected List<Option<?>> createSedaOptions() {
    final List<Option<?>> options = new ArrayList<>();

    options.addAll(super.createSedaOptions());
    options.add(OPTION_FROM);
    options.add(OPTION_TO_DELIMITER);
    options.add(OPTION_REPLACEMENT);

    return options;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    RenameHeaderTransformationProvider provider = new RenameHeaderTransformationProvider();

    checkMandatoryOption(parameters, OPTION_FROM);
    checkMandatoryOption(parameters, OPTION_TO_DELIMITER);

    String from = parameters.getSingleValue(OPTION_FROM);
    String to = parameters.getSingleValue(OPTION_TO_DELIMITER);
    String replacement = parameters.getSingleValue(OPTION_REPLACEMENT);

    HeaderTarget headerTarget = getHeaderTarget(parameters);

    provider.setHeaderRenamer(new IntervalReplaceRenamer(headerTarget, from, to, replacement));

    return provider;
  }
}
