package org.sing_group.seda.transformation.sequence;

import java.util.HashMap;
import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;

public class UndoAlignmentSequenceTransformation extends ReplaceCharactersSequenceTransformation {
  private static final Map<String, String> REPLACEMENTS = new HashMap<>();
  {
    REPLACEMENTS.put("-", "");
  }

  public UndoAlignmentSequenceTransformation() {
    this(DatatypeFactory.getDefaultDatatypeFactory());
  }

  public UndoAlignmentSequenceTransformation(DatatypeFactory factory) {
    super(factory, REPLACEMENTS);
  }
}
