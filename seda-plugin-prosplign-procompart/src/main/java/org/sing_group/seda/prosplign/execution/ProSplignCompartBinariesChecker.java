/*
 * #%L
 * SEquence DAtaset builder ProSplign/ProCompart plugin
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.prosplign.execution;

import static org.sing_group.seda.core.execution.BinaryCheckingUtils.checkCommand;

import java.util.function.Function;

import org.sing_group.seda.core.execution.BinaryCheckException;

public class ProSplignCompartBinariesChecker {
  
  public static void checkProSplignCompartPath(Function<String, String> composeCommand) throws BinaryCheckException {
    checkProSplign(composeCommand);
    checkProCompart(composeCommand);
  }

  private static void checkProSplign(Function<String, String> composeCommand) throws BinaryCheckException {
    checkCommand(composeCommand.apply(ProSplignCompartEnvironment.getInstance().getProSplignCommand() + " -version"), 1);
  }

  private static void checkProCompart(Function<String, String> composeCommand) throws BinaryCheckException {
    checkCommand(composeCommand.apply(ProSplignCompartEnvironment.getInstance().getProCompartCommand()), 2, 1);
  }
}
