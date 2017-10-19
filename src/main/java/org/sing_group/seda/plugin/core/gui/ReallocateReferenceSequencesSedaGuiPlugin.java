package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.pattern.reallocate.ReallocateReferenceSequencesPluginPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ReallocateReferenceSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final ReallocateReferenceSequencesPluginPanel panel;

  public ReallocateReferenceSequencesSedaGuiPlugin() {
    this.panel = new ReallocateReferenceSequencesPluginPanel();
  }

  @Override
  public String getName() {
    return "Reallocate reference sequences";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getReallocateSequencesTransformationProvider();
  }
}
