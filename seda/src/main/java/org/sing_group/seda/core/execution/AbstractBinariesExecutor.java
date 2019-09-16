/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.core.execution;

import static java.nio.file.Files.readAllLines;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class AbstractBinariesExecutor {
  protected void executeCommand(List<String> parameters, File errorFile, File outputFile) throws IOException, InterruptedException {
    ProcessBuilder pBuilder = new ProcessBuilder(parameters.toArray(new String[parameters.size()]));
    pBuilder.redirectError(errorFile);
    pBuilder.redirectOutput(outputFile);

    int result = pBuilder.start().waitFor();

    if (result > 0) {
      String error = readAllLines(errorFile.toPath()).stream().collect(joining("\n"));
      String output = readAllLines(outputFile.toPath()).stream().collect(joining("\n"));

      throw new IOException(
        "Error running command. Exit status: " + result + ". Error: " + output + "\n" + error
      );
    }
  }

  protected void executeCommand(List<String> parameters) throws IOException, InterruptedException {
    final Runtime runtime = Runtime.getRuntime();
    final Process process = runtime.exec(parameters.toArray(new String[parameters.size()]));
    process.waitFor();
  }

  protected void executeCommand(List<String> parameters, File workingDirectory)
    throws IOException, InterruptedException {
    ProcessBuilder pBuilder =
      new ProcessBuilder(parameters.toArray(new String[parameters.size()]))
        .directory(workingDirectory);
    pBuilder.start().waitFor();
  }

  protected void executeCommand(List<String> parameters, File workingDirectory, InputLineCallback callBack)
    throws InterruptedException, IOException {
    String commandString = parameters.stream().collect(joining(" "));
    List<InputLineCallback> callBacks = asList(callBack);

    ProcessBuilder pBuilder =
      new ProcessBuilder(parameters.toArray(new String[parameters.size()]))
        .directory(workingDirectory);

    Process process;
    try {
      process = pBuilder.start();

    final LoggerCallbackInputThread inThread = new LoggerCallbackInputThread(commandString, process.getInputStream(), callBacks);
    final LoggerCallbackErrorThread errThread = new LoggerCallbackErrorThread(commandString, process.getErrorStream(), callBacks);

    inThread.start();
    errThread.start();

    process.waitFor();

    inThread.join();
    errThread.join();
    } catch (IOException e) {
      notifyError(callBacks, "Error executing command: " + commandString, e);

      throw new IOException("Error executing command: " + commandString, e);
    } finally {
      inputFinished(callBacks);
    }
  }

  protected static class LoggerCallbackInputThread extends Thread {
    private final String command;
    private final BufferedReader reader;
    private final List<InputLineCallback> callbacks;

    public LoggerCallbackInputThread(String command, InputStream is, List<InputLineCallback> callbacks) {
      this.setDaemon(true);

      this.command = command;
      this.reader = new BufferedReader(new InputStreamReader(is));
      this.callbacks =  callbacks;
    }

    @Override
    public void run() {
      try {
        String line;
        while ((line = this.reader.readLine()) != null) {
          notifyLine(callbacks, line);
        }
      } catch (IOException e) {
        notifyError(callbacks, "Error executing command: " + this.command, e);
      }
    }
  }

  protected static class LoggerCallbackErrorThread extends Thread {
    private final String command;
    private final BufferedReader reader;
    private final List<InputLineCallback> callbacks;

    public LoggerCallbackErrorThread(String command, InputStream is, List<InputLineCallback> callbacks) {
      this.setDaemon(true);

      this.command = command;
      this.reader = new BufferedReader(new InputStreamReader(is));
      this.callbacks =  callbacks;
    }

    @Override
    public void run() {
      try {
        String line;
        while ((line = this.reader.readLine()) != null) {
          notifyError(callbacks, line, null);
        }
      } catch (IOException e) {
        notifyError(callbacks, "Error executing command: " + this.command, e);
      }
    }
  }

  protected static void inputStarted(List<InputLineCallback> callbacks) {
    for (InputLineCallback callback : callbacks) {
      callback.inputStarted();
    }
  }

  protected static void inputFinished(List<InputLineCallback> callbacks) {
    for (InputLineCallback callback : callbacks) {
      callback.inputFinished();
    }
  }

  protected static void notifyInfo(List<InputLineCallback> callbacks, String message) {
    for (InputLineCallback callback : callbacks) {
      callback.info(message);
    }
  }

  protected static void notifyLine(List<InputLineCallback> callbacks, String line) {
    for (InputLineCallback callback : callbacks) {
      callback.line(line);
    }
  }

  protected static void notifyError(List<InputLineCallback> callbacks, String message, Exception e) {
    for (InputLineCallback callback : callbacks) {
      callback.error(message, e);
    }
  }
}
