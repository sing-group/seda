/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.rename;

import org.sing_group.seda.core.rename.HeaderRenameTransformationChangeType;
import org.sing_group.seda.core.rename.HeaderRenamerTransformation;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class RenameHeaderConfigurationModel extends AbstractTransformationProvider implements RenamePanelEventListener {

  private RenameTransformationConfigurationPanel panel;

  public RenameHeaderConfigurationModel(RenameTransformationConfigurationPanel panel) {
    this.panel = panel;
    this.panel.addRenamePanelEventListener(this);
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation
      .concat(
        new ComposedSequencesGroupDatasetTransformation(factory, getHeaderRenameTransformation(factory))
      );
  }

  private SequencesGroupTransformation getHeaderRenameTransformation(DatatypeFactory factory) {
    return new HeaderRenamerTransformation(this.panel.getHeaderRenamer(factory));
  }

  @Override
  public boolean isValidTransformation() {
    return panel.isValidConfiguration();
  }

  @Override
  public void onRenameConfigurationChanged(Object source) {
    renameConfigurationChanged();
  }

  private void renameConfigurationChanged() {
    this.fireTransformationsConfigurationModelEvent(
      HeaderRenameTransformationChangeType.CONFIGURATION_CHANGED, null
    );
  }
}
