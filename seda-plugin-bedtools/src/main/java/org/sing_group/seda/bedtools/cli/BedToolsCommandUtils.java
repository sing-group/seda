/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.cli;

import java.io.File;

import org.sing_group.seda.bedtools.execution.BedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.DefaultBedToolsBinariesExecutor;
import org.sing_group.seda.bedtools.execution.DockerBedToolsBinariesExecutor;
import org.sing_group.seda.cli.ExternalSoftwareExecutionCommand;

import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class BedToolsCommandUtils {
  public static BedToolsBinariesExecutor getBedToolsBinariesExecutor(
    Parameters parameters, StringOption localMode, StringOption dockerMode
  ) {
    ExternalSoftwareExecutionCommand.validateSingleExecutionMode(
      parameters, localMode, dockerMode, " for bedtools"
    );
    
    BedToolsBinariesExecutor executor =
      new DockerBedToolsBinariesExecutor(DockerBedToolsBinariesExecutor.getDefaultDockerImage());

    if (parameters.hasOption(localMode)) {
      File bedToolsBinary = new File(parameters.getSingleValueString(localMode));

      if (bedToolsBinary.isFile()) {
        executor = new DefaultBedToolsBinariesExecutor(bedToolsBinary);
      } else {
        ExternalSoftwareExecutionCommand
          .formattedValidationError("The specified bedtools binary file does not exist or it is not a file.");
      }
    }

    if (parameters.hasOption(dockerMode)) {
      executor = new DockerBedToolsBinariesExecutor(parameters.getSingleValue(dockerMode));
    }

    return executor;
  }
}
