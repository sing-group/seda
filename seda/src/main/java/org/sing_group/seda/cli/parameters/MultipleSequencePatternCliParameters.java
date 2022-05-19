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
package org.sing_group.seda.cli.parameters;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_GROUP_MODE_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_GROUP_MODE_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_GROUP_MODE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_WITHOUT_PATTERN_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_WITHOUT_PATTERN_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_WITHOUT_PATTERN_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_WITH_PATTERN_HELP;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_WITH_PATTERN_NAME;
import static org.sing_group.seda.plugin.core.info.plugin.ReallocateReferenceSequencesSedaPluginInfo.PARAM_WITH_PATTERN_SHORT_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.datatype.pattern.SequencePattern;
import org.sing_group.seda.datatype.pattern.SequencePatternGroup;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.StringOption;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class MultipleSequencePatternCliParameters {

  private class Pattern {
    private String pattern;
    private Boolean contains;
    private Boolean caseSensitive;
    private Integer minOccurrences;
    private Integer numGroup;

    public Pattern(String patternParam, Boolean contains) {
      this.contains = contains;
      if (patternParam.matches(CONFIG_PATTERN_REGEX)) {
        this.pattern = patternParam.split(":")[1];

        String config =
          patternParam
            .split(":")[0]
              .replace("config", "")
              .replace("(", "")
              .replace(")", "");
        this.numGroup = Integer.parseInt(config.split("/")[0]);
        this.caseSensitive = Boolean.parseBoolean(config.split("/")[1]);
        this.minOccurrences = Integer.parseInt(config.split("/")[2]);

      } else {
        this.pattern = patternParam;
        this.caseSensitive = DEFAULT_CASE_SENSITIVE;
        this.minOccurrences = DEFAULT_MIN_OCURRENCES;
      }
    }

    public SequencePattern getSequencePattern() {
      return new SequencePattern(this.pattern, this.minOccurrences, this.caseSensitive, this.contains);
    }

    public Integer getNumGroup() {
      return numGroup;
    }
  }

  private final String CONFIG_PATTERN_REGEX = "config\\([1-9+]/(true|false)/[1-9+]\\):.+";
  private final String CONFIG_GROUP_MODE_REGEX =
    "[1-9+]:" + Stream.of(EvaluableSequencePattern.GroupMode.values()).map(EvaluableSequencePattern.GroupMode::name)
      .map(String::toUpperCase).collect(joining("|", "(", ")"));

  private final Boolean DEFAULT_CASE_SENSITIVE = false;
  private final Integer DEFAULT_MIN_OCURRENCES = 1;
  private final EvaluableSequencePattern.GroupMode DEFAULT_GROUP_MODE = EvaluableSequencePattern.GroupMode.ANY;
  private Parameters parameters;

  public static final StringOption OPTION_WITH_PATTERN =
    new StringOption(PARAM_WITH_PATTERN_NAME, PARAM_WITH_PATTERN_SHORT_NAME, PARAM_WITH_PATTERN_HELP, true, true, true);
  public static final StringOption OPTION_WITHOUT_PATTERN =
    new StringOption(
      PARAM_WITHOUT_PATTERN_NAME, PARAM_WITHOUT_PATTERN_SHORT_NAME, PARAM_WITHOUT_PATTERN_HELP, true, true, true
    );

  public static final StringOption OPTION_GROUP_MODE =
    new StringOption(PARAM_GROUP_MODE_NAME, PARAM_GROUP_MODE_SHORT_NAME, PARAM_GROUP_MODE_HELP, true, true, true);

  public MultipleSequencePatternCliParameters(Parameters parameters) {
    this.parameters = parameters;
  }

  public static List<Option<?>> getOptionList() {
    return asList(
      OPTION_WITH_PATTERN,
      OPTION_WITHOUT_PATTERN,
      OPTION_GROUP_MODE
    );
  }

  public SequencePatternGroup getSequencePatternGroup() throws IllegalArgumentException {

    if (!this.parameters.hasOption(OPTION_WITH_PATTERN) && !this.parameters.hasOption(OPTION_WITHOUT_PATTERN)) {
      throw new IllegalArgumentException(
        "At least one pattern option is mandatory. Check help command to see the available options."
      );
    }

    List<Pattern> patternList = new ArrayList<>();

    if (this.parameters.hasOption(OPTION_WITH_PATTERN)) {
      patternList.addAll(
        this.parameters.getAllValues(OPTION_WITH_PATTERN).stream()
          .map(pattern -> new Pattern(pattern, true))
          .collect(Collectors.toList())
      );
    }

    if (this.parameters.hasOption(OPTION_WITHOUT_PATTERN)) {
      patternList.addAll(
        this.parameters.getAllValues(OPTION_WITHOUT_PATTERN).stream()
          .map(pattern -> new Pattern(pattern, false))
          .collect(Collectors.toList())
      );
    }

    Map<Integer, EvaluableSequencePattern.GroupMode> groupsMode = getGroupsMode();

    List<SequencePatternGroup> sequencePatternGroups =
      groupsMode.keySet().stream()
        .map(numGroup -> new SequencePatternGroup(groupsMode.get(numGroup), getPatternsByGroup(patternList, numGroup)))
        .collect(Collectors.toList());

    SequencePattern[] sequencePatternsWithoutGroup = getPatternsByGroup(patternList, null);

    if (sequencePatternsWithoutGroup.length > 0) {
      sequencePatternGroups.add(new SequencePatternGroup(DEFAULT_GROUP_MODE, sequencePatternsWithoutGroup));
    }

    return new SequencePatternGroup(getSelectedMode(), sequencePatternGroups.toArray(new SequencePatternGroup[0]));
  }

  private SequencePattern[] getPatternsByGroup(List<Pattern> patternList, Integer numGroup) {
    return patternList.stream()
      .filter(pattern -> Objects.equals(pattern.getNumGroup(), numGroup))
      .map(Pattern::getSequencePattern)
      .toArray(SequencePattern[]::new);
  }

  private Map<Integer, EvaluableSequencePattern.GroupMode> getGroupsMode() {
    Map<Integer, EvaluableSequencePattern.GroupMode> numGroup = new HashMap<>();

    if (this.parameters.hasOption(OPTION_GROUP_MODE)) {
      List<String> groupsModeList = this.parameters.getAllValues(OPTION_GROUP_MODE);

      numGroup =
        groupsModeList.stream()
          .filter(groupMode -> groupMode.matches(CONFIG_GROUP_MODE_REGEX))
          .map(this::processGroupConfig)
          .flatMap(map -> map.entrySet().stream())
          .collect(
            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
          );
    }

    return numGroup;
  }

  private Map<Integer, EvaluableSequencePattern.GroupMode> processGroupConfig(String groupConfig) {
    Map<Integer, EvaluableSequencePattern.GroupMode> groupMap = new HashMap<>();

    if (groupConfig.matches(CONFIG_GROUP_MODE_REGEX)) {
      EvaluableSequencePattern.GroupMode groupMode = null;

      try {
        groupMode = EvaluableSequencePattern.GroupMode.valueOf(groupConfig.split(":")[1]);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
          "Invalid value for " + PARAM_GROUP_MODE_NAME + " (" + PARAM_GROUP_MODE_HELP + ")"
        );
      }
      groupMap.put(Integer.parseInt(groupConfig.split(":")[0]), groupMode);
    }

    return groupMap;
  }

  private EvaluableSequencePattern.GroupMode getSelectedMode() {
    EvaluableSequencePattern.GroupMode groupMode = DEFAULT_GROUP_MODE;

    if (this.parameters.hasOption(OPTION_GROUP_MODE)) {
      groupMode =
        this.parameters.getAllValues(OPTION_GROUP_MODE).stream()
          .filter(s -> !s.matches(CONFIG_GROUP_MODE_REGEX))
          .findFirst()
          .map(value -> {
            try {
              return EvaluableSequencePattern.GroupMode.valueOf(value);
            } catch (IllegalArgumentException e) {
              throw new IllegalArgumentException(
                "Invalid value for " + PARAM_GROUP_MODE_NAME + " (" + PARAM_GROUP_MODE_HELP + ")"
              );
            }
          })
          .orElse(EvaluableSequencePattern.GroupMode.ANY);
    }

    return groupMode;
  }
}
