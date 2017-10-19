package org.sing_group.seda.core.rename;

import java.util.Collections;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;

public class RenameTestUtils {
  public static final DatatypeFactory FACTORY = DatatypeFactory.getDefaultDatatypeFactory();

  public static final Sequence TEST_1 = newSequence("SequenceA", "[gen = A] [Other = 1]");
  public static final Sequence TEST_2 = newSequence("SequenceB", "[gen = B] [Other = 2]");
  public static final Sequence TEST_3 = newSequence("SequenceC", "[gen = C] [Other = 3]");

  public static final SequencesGroup GROUP = FACTORY.newSequencesGroup("Group", TEST_1, TEST_2, TEST_3);

  public static Sequence newSequence(String name, String description) {
    return FACTORY.newSequence(name, description, "", Collections.emptyMap());
  }
}
