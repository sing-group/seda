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
package org.sing_group.seda.gui.filtering.base;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation.BasePresence;

public class FilterByBasePresenceConfigurationModel extends AbstractTransformationProvider {
  private FilterByBasePresenceConfigurationPanel filterByBasePresenceConfigurationPanel;

  public FilterByBasePresenceConfigurationModel(
    FilterByBasePresenceConfigurationPanel filterByBasePresenceConfigurationPanel
  ) {
    this.filterByBasePresenceConfigurationPanel = filterByBasePresenceConfigurationPanel;
    filterByBasePresenceConfigurationPanel.getBasesConfigurationPanel().addPropertyChangeListener(
      MultipleBasePresenceConfigurationPanel.PROPERTY_BASE_PRESENCES, new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          basesConfigurationChanged();
        }
      }
    );
  }

  private void basesConfigurationChanged() {
    fireTransformationsConfigurationModelEvent(FilterByBaseConfigurationEventType.BASES_CHANGED, getBasePresences());
  }

  @Override
  public boolean isValidTransformation() {
    return filterByBasePresenceConfigurationPanel.getBasesConfigurationPanel().isValidValue();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new ComposedSequencesGroupDatasetTransformation(
      new FilterByBasePresenceTransformation(
        factory, getBasePresences()
      )
    );
  }

  private List<BasePresence> getBasePresences() {
    return filterByBasePresenceConfigurationPanel.getBasesConfigurationPanel().getBasePresences();
  }

}
