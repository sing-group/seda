/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.stream.Collectors.joining;
import static org.sing_group.seda.core.execution.BinaryCheckingUtils.checkCommand;

import java.util.Set;

import org.sing_group.seda.util.OsUtils;

public class DockerExecutionUtils {

  public static void checkDockerAvailability() throws BinaryCheckException {
    try {
      checkCommand("docker --version", 1);
    } catch (BinaryCheckException bce) {
      throw new BinaryCheckException("Error checking docker availability", bce, "docker --version");
    }
  }

  public static String dockerPath(String path) {
    if (OsUtils.isWindows()) {
      return path.replaceAll("^(?i)c:", "/c").replaceAll("\\\\", "/");
    }
    if (OsUtils.isMacOs()) {
      return "/private" + path;
    }
    return path;
  }

  public static String getMountDockerDirectoriesString(Set<String> directoriesToMount) {
    return directoriesToMount.stream().map(d -> "-v" + dockerPath(d) + ":" + dockerPath(d)).collect(joining(" "));
  }
}
