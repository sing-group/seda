package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.seda.gui.undoalignment.UndoAlignmentConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class UndoAlignmentGuiPlugin extends AbstractSedaGuiPlugin {
  private final UndoAlignmentConfigurationPanel panel;

  public UndoAlignmentGuiPlugin() {
    this.panel = new UndoAlignmentConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Undo alignment";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getModel();
  }
}
