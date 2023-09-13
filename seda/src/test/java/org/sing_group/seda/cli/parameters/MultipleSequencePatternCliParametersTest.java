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
