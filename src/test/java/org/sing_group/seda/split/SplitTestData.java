package org.sing_group.seda.split;

import java.util.Collections;
import java.util.Map;

import org.sing_group.seda.datatype.DefaultSequence;
import org.sing_group.seda.datatype.DefaultSequencesGroup;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class SplitTestData {
  private static final Map<String, Object> PROPERTIES = Collections.emptyMap();
  private static final Sequence[] SEQUENCES_ARRAY;

  static {
    SEQUENCES_ARRAY = new Sequence[20];
    for (int i = 0; i < 20; i++) {
      SEQUENCES_ARRAY[i] = new DefaultSequence(">" + i, "", "A", PROPERTIES);
    }
  }

  public static final SequencesGroup SEQUENCES =
    new DefaultSequencesGroup("Group", SEQUENCES_ARRAY);
}
