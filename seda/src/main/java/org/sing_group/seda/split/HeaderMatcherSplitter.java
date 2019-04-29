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
package org.sing_group.seda.split;

import static java.nio.file.Files.write;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;
import static org.sing_group.seda.util.OsUtils.isWindows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class HeaderMatcherSplitter implements SequencesGroupSplitter {
  private static final String UNMATCHED_SEQUENCES_GROUP = "Unmatched";

  private HeaderMatcher matcher;
  private DatatypeFactory factory;
  private File groupNamesDirectory;

  public HeaderMatcherSplitter(HeaderMatcher headerMatcher) {
    this(headerMatcher, null, getDefaultDatatypeFactory());
  }

  public HeaderMatcherSplitter(HeaderMatcher headerMatcher, File groupNamesDirectory) {
    this(headerMatcher, groupNamesDirectory, getDefaultDatatypeFactory());
  }

  public HeaderMatcherSplitter(HeaderMatcher headerMatcher, File groupNamesDirectory, DatatypeFactory factory) {
    this.matcher = headerMatcher;
    this.factory = factory;
    this.groupNamesDirectory = groupNamesDirectory;
  }

  @Override
  public List<SequencesGroup> split(SequencesGroup sequencesGroup) {
    String groupName = sequencesGroup.getName();
    List<SequencesGroup> groups = new LinkedList<>();
    Map<String, List<Sequence>> groupsMap = new HashMap<>();

    sequencesGroup.getSequences().forEach(s -> {
      Optional<String> match = matcher.match(s);
      String key = match.orElse(UNMATCHED_SEQUENCES_GROUP);

      groupsMap.putIfAbsent(key, new LinkedList<>());
      groupsMap.get(key).add(s);
    });

    /*
     * Group names are mapped because in Windows systems file names are case
     * insensitive. Thus, if there are two groups with names "GROUPA" and "groupA",
     * they will be written to the same file. By doing this, in Windows systems
     * these groups will be written in files "GROUPA_1" and "groupA_2".
     */
    Map<String, String> getGroupNamesMap = getGroupNamesMap(groupsMap.keySet());

    groupsMap.keySet().forEach(key -> {
      String groupNameForFileName = getGroupNamesMap.get(key);
      SequencesGroup newGroup = factory.newSequencesGroup(
          groupName + "_" + groupNameForFileName, sequencesGroup.getProperties(), groupsMap.get(key));
      groups.add(newGroup);
    });

    if (this.groupNamesDirectory != null) {
      try {
        this.saveGroupNamesDirectory(sequencesGroup.getName(), groupsMap.keySet());
      } catch (IOException e) {
        throw new TransformationException(e);
      }
    }

    return groups;
  }

  private Map<String, String> getGroupNamesMap(Set<String> keySet) {
    List<String> keys = new LinkedList<>(keySet);
    if (isWindows()) {
      return getUniqueNames(keys);
    } else {
      return keys.stream().collect(toMap(k -> k, k -> k));
    }
  }

  private void saveGroupNamesDirectory(String name, Set<String> keySet) throws IOException {
    write(
      new File(this.groupNamesDirectory, name + ".txt").toPath(),
      keySet.stream().collect(joining("\n")).getBytes(StandardCharsets.UTF_8)
    );
  }

  private static final Map<String, String> getUniqueNames(List<String> strings) {
    Map<String, Integer> counts = new HashMap<>();
    Map<String, String> toret = new HashMap<>();
    Set<Integer> toSkip = new HashSet<>();

    for (int i = 0; i < strings.size(); i++) {
      if (toSkip.contains(i)) {
        continue;
      }

      String a = strings.get(i);
      counts.putIfAbsent(a.toLowerCase(), 1);
      for (int j = i + 1; j < strings.size(); j++) {
        if (toSkip.contains(j)) {
          continue;
        }

        String b = strings.get(j);
        if (a.equalsIgnoreCase(b)) {
          toret.putIfAbsent(a, a + "_" + counts.get(a.toLowerCase()));
          toret.putIfAbsent(b, b + "_" + (counts.get(a.toLowerCase()) + 1));
          counts.put(a.toLowerCase(), counts.get(a.toLowerCase()) + 1);
          toSkip.add(j);
        }
      }
      toret.putIfAbsent(a, a);
    }

    return toret;
  }
}
