/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.blast.execution;

import static org.sing_group.seda.core.execution.BinaryCheckingUtils.checkCommand;

import java.util.function.Function;

import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.core.execution.BinaryCheckException;

public class BlastBinariesChecker {

  public static void checkBlastPath(Function<String, String> composeBlastCommand) throws BinaryCheckException {
    checkMakeBlastDb(composeBlastCommand);
    checkBlastDbCmd(composeBlastCommand);
    checkBlastDbAliasTool(composeBlastCommand);
    checkBlastCommands(composeBlastCommand);
  }

  private static void checkMakeBlastDb(Function<String, String> composeBlastCommand) throws BinaryCheckException {
    checkCommand(composeBlastCommand.apply(BlastEnvironment.getInstance().getMakeBlastDbCommand() + " -version"), 2);
  }

  private static void checkBlastDbCmd(Function<String, String> composeBlastCommand) throws BinaryCheckException {
    checkCommand(composeBlastCommand.apply(BlastEnvironment.getInstance().getBlastDbCmdCommand() + " -version"), 2);
  }

  private static void checkBlastDbAliasTool(Function<String, String> composeBlastCommand) throws BinaryCheckException {
    checkCommand(
      composeBlastCommand.apply(BlastEnvironment.getInstance().getBlastDbAliasToolCommand() + " -version"), 2
    );
  }

  private static void checkBlastCommands(Function<String, String> composeBlastCommand) throws BinaryCheckException {
    for (BlastType blastType : BlastType.values()) {
      checkCommand(composeBlastCommand.apply(blastType.getCommand() + " -version"), 2);
    }
  }
}
