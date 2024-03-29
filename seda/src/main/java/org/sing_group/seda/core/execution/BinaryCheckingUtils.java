/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class BinaryCheckingUtils {
  public static void checkCommand(String command, int expectedOutputLinesCount) throws BinaryCheckException {
    checkCommand(command, expectedOutputLinesCount, 0);
  }

  public static void checkCommand(String command, int expectedOutputLinesCount, int expectedExitStatus)
    throws BinaryCheckException {
    final Runtime runtime = Runtime.getRuntime();

    try {
      final Process process = runtime.exec(command);

      final BufferedReader br =
        new BufferedReader(
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
      if (exitStatus != expectedExitStatus) {
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

  public static void checkCommand(String command, List<String> firstOutputLines) throws BinaryCheckException {
    checkCommand(command, firstOutputLines, Process::getInputStream);
  }

  public static void checkCommandErrorOutput(String command, List<String> firstOutputLines)
    throws BinaryCheckException {
    checkCommand(command, firstOutputLines, Process::getErrorStream);
  }

  public static void checkCommand(
    String command, List<String> firstOutputLines, Function<Process, InputStream> processStream
  )
    throws BinaryCheckException {
    final Runtime runtime = Runtime.getRuntime();

    try {
      final Process process = runtime.exec(command);

      final BufferedReader br =
        new BufferedReader(
          new InputStreamReader(processStream.apply(process))
        );

      List<String> output = new LinkedList<String>();

      String line;
      while ((line = br.readLine()) != null) {
        output.add(line);
      }

      boolean equals = true;
      for(int i = 0; i < firstOutputLines.size(); i++) {
        String a = firstOutputLines.get(i);

        try {
          String b = output.get(i);
          equals = a.equals(b);
        } catch(IndexOutOfBoundsException e) {
          equals = false;
        }

        if(!equals) {
          throw new BinaryCheckException("Unrecognized version output", command);
        }
      }

      process.waitFor();
    } catch (IOException e) {
      throw new BinaryCheckException("Error while checking version", e, command);
    } catch (InterruptedException e) {
      throw new BinaryCheckException("Error while checking version", e, command);
    }
  }
}
