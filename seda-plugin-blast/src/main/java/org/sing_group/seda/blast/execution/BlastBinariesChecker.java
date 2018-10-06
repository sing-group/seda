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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.sing_group.seda.blast.datatype.blast.BlastEnvironment;
import org.sing_group.seda.blast.datatype.blast.BlastType;

public class BlastBinariesChecker {

  public static void checkBlastPath(File path) throws BinaryCheckException {
    checkMakeBlastDb(path);
    checkBlastDbCmd(path);
    checkBlastDbAliasTool(path);
    checkBlastCommands(path);
  }

  private static void checkMakeBlastDb(File path) throws BinaryCheckException {
    checkCommand(composeBlastCommand(path,
      BlastEnvironment.getInstance().getMakeBlastDbCommand()) + " -version", 2);
  }

  private static void checkBlastDbCmd(File path) throws BinaryCheckException {
    checkCommand(composeBlastCommand(path,
      BlastEnvironment.getInstance().getBlastDbCmdCommand()) + " -version", 2);
  }

  private static void checkBlastDbAliasTool(File path) throws BinaryCheckException {
    checkCommand(composeBlastCommand(path,
      BlastEnvironment.getInstance().getBlastDbAliasToolCommand()) + " -version",2);
  }

  public static String composeBlastCommand(String command) {
    return composeBlastCommand(null, command);
  }

  private static void checkBlastCommands(File path) throws BinaryCheckException {
    for (BlastType blastType : BlastType.values()) {
      String command = composeBlastCommand(path, blastType.getCommand());
      checkCommand(command + " -version", 2);
    }
  }

  public static String composeBlastCommand(File blastPath, String command) {
    if (blastPath == null) {
      return Paths.get(command).toString();
    } else {
      return blastPath.toPath().resolve(command).toString();
    }
  }

  protected static void checkCommand(String command, int expectedOutputLinesCount) throws BinaryCheckException {
    final Runtime runtime = Runtime.getRuntime();

    try {
      final Process process = runtime.exec(command);

      final BufferedReader br = new BufferedReader(
        new InputStreamReader(process.getInputStream())
      );

      StringBuilder sb = new StringBuilder();

      String line;
      int countLines = 0;
      while ((line = br.readLine()) != null) {
        sb.append(line).append('\n');
        countLines++;
      }

      if (countLines != expectedOutputLinesCount) {
        throw new BinaryCheckException("Unrecognized version output", command);
      }

      final int exitStatus = process.waitFor();
      if (exitStatus != 0) {
        throw new BinaryCheckException(
          "Invalid exit status: " + exitStatus,
          command
        );
      }
    } catch (IOException e) {
      throw new BinaryCheckException("Error while checking version", e, command);
    } catch (InterruptedException e) {
      throw new BinaryCheckException("Error while checking version", e, command);
    }
  }
}
