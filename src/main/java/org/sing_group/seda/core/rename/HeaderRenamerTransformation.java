package org.sing_group.seda.core.rename;

import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class HeaderRenamerTransformation implements SequencesGroupTransformation {

  private HeaderRenamer headerRenamer;

  public HeaderRenamerTransformation(HeaderRenamer headerRenamer) {
    this.headerRenamer = headerRenamer;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    return headerRenamer.rename(sequencesGroup);
  }
}
