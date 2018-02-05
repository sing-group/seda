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
