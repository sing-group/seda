/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.cli.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.sing_group.seda.datatype.pattern.SequencePattern;
import org.sing_group.seda.datatype.pattern.SequencePatternGroup;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern.GroupMode;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.DefaultParameters;
import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.ParameterValue;

public class MultipleSequencePatternCliParametersTest {

  @Test
  public void testWithoutPatterns() {
    Map<Option<?>, ParameterValue<?>> parametersMap = new HashMap<Option<?>, ParameterValue<?>>();
    parametersMap.put(
      MultipleSequencePatternCliParameters.OPTION_WITHOUT_PATTERN,
      new MultipleParameterValue(Arrays.asList("Sequence1", "Sequence2"))
    );
    parametersMap.put(
      MultipleSequencePatternCliParameters.OPTION_GROUP_MODE,
      new MultipleParameterValue(Arrays.asList("all"))
    );
    
    SequencePatternGroup expected = new SequencePatternGroup(
      GroupMode.ALL,
      new SequencePatternGroup(
        GroupMode.ALL,
        new SequencePattern("Sequence1", 1, true, false),
        new SequencePattern("Sequence2", 1, true, false)
      )
    );

    SequencePatternGroup result = new MultipleSequencePatternCliParameters(
      new DefaultParameters(parametersMap)).getSequencePatternGroup();

    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testWithoutPatternsAndConfiguration() {
    Map<Option<?>, ParameterValue<?>> parametersMap = new HashMap<Option<?>, ParameterValue<?>>();
    parametersMap.put(
      MultipleSequencePatternCliParameters.OPTION_WITHOUT_PATTERN,
      new MultipleParameterValue(Arrays.asList("config(1/true/1):Sequence1", "config(1/true/1):Sequence2"))
      );
    parametersMap.put(
      MultipleSequencePatternCliParameters.OPTION_GROUP_MODE,
      new MultipleParameterValue(Arrays.asList("1:all"))
    );
    
    SequencePatternGroup expected = new SequencePatternGroup(
      GroupMode.ANY,
      new SequencePatternGroup(
        GroupMode.ALL,
        new SequencePattern("Sequence1", 1, true, false),
        new SequencePattern("Sequence2", 1, true, false)
      )
    );

    SequencePatternGroup result = new MultipleSequencePatternCliParameters(
      new DefaultParameters(parametersMap)).getSequencePatternGroup();

    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testMultipleGroups() {
    Map<Option<?>, ParameterValue<?>> parametersMap = new HashMap<Option<?>, ParameterValue<?>>();
    parametersMap.put(
      MultipleSequencePatternCliParameters.OPTION_WITH_PATTERN,
      new MultipleParameterValue(Arrays.asList("config(1/false/2):AAAA", "config(1/false/2):CCCC", "config(2/false/3):TTTTA"))
      );
    parametersMap.put(
      MultipleSequencePatternCliParameters.OPTION_GROUP_MODE,
      new MultipleParameterValue(Arrays.asList("1:all", "any"))
    );
    
    SequencePatternGroup expected = new SequencePatternGroup(
      GroupMode.ANY,
      new SequencePatternGroup(
        GroupMode.ALL,
        new SequencePattern("AAAA", 2, false, true),
        new SequencePattern("CCCC", 2, false, true)
      ),
      new SequencePatternGroup(
        GroupMode.ANY,
        new SequencePattern("TTTTA", 3, false, true)
      )
    );

    SequencePatternGroup result = new MultipleSequencePatternCliParameters(
      new DefaultParameters(parametersMap)).getSequencePatternGroup();

    Assert.assertEquals(expected, result);
  }
}
