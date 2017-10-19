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
