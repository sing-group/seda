package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;

import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.gui.translation.SequenceTranslationConfigurationPanel;
import org.sing_group.seda.gui.translation.TranslateSequencesTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class TranslateSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final SequenceTranslationConfigurationPanel panel;
  private final TranslateSequencesTransformationProvider model;

  public TranslateSequencesSedaGuiPlugin() {
    this.panel = new SequenceTranslationConfigurationPanel(false);
    this.model = new TranslateSequencesTransformationProvider(this.panel);
  }

  @Override
  public String getName() {
    return "Translate";
  }

  @Override
  public Component getEditor() {
    return new CenteredJPanel(this.panel);
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.model;
  }
}
