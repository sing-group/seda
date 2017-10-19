package org.sing_group.seda.gui.rename;

import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;

public interface RenameConfigurationPanel {
  public boolean isValidConfiguration();

  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory, HeaderTarget target);
}
