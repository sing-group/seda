package org.sing_group.seda.util;

import java.util.Arrays;
import java.util.List;

public class OsUtils {
  public static boolean isWindows() {
    return getOsName().startsWith("Windows");
  }

  private static String getOsName() {
    return System.getProperty("os.name");
  }

  public static List<String> getInvalidWindowsFileCharacters() {
    return Arrays.asList("<", ">", ":", "\"", "/", "|", "?", "*");
  }

  public static List<String> getInvalidLinuxFileCharacters() {
    return Arrays.asList("/");
  }

  public static List<String> getInvalidOsFileCharacters() {
    if (isWindows()) {
      return getInvalidOsFileCharacters();
    }
    return getInvalidLinuxFileCharacters();
  }
}