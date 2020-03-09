package org.sing_group.seda.gui.translation;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class TranslateSequencesPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private SequenceTranslationConfigurationPanel sequenceTranslationConfigurationPanel;
  private TranslateSequencesTransformationProvider transformationProvider;

  public TranslateSequencesPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.sequenceTranslationConfigurationPanel = new SequenceTranslationConfigurationPanel(false);
    this.sequenceTranslationConfigurationPanel.addPropertyChangeListener(
      new SequenceTranslationPanelPropertyChangeAdapter() {
        @Override
        protected void translationPropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void joinFramesPropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void framesPropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void codonTablePropertyChanged() {
          translationConfigurationChanged();
        }

        @Override
        protected void reverseSequencesPropertyChanged() {
          translationConfigurationChanged();
        }
      }
    );
    this.add(new CenteredJPanel(sequenceTranslationConfigurationPanel), BorderLayout.CENTER);
  }

  private void translationConfigurationChanged() {
    if (this.sequenceTranslationConfigurationPanel.isValidUserSelection()) {
      this.transformationProvider.setTranslationConfiguration(
        this.sequenceTranslationConfigurationPanel.getSequenceTranslationConfiguration()
      );
    } else {
      this.transformationProvider.clearTranslationConfiguration();
    }
  }

  private void initTransformationProvider() {
    this.transformationProvider = new TranslateSequencesTransformationProvider();
    this.translationConfigurationChanged();
  }

  public TranslateSequencesTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(TranslateSequencesTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;
    if (transformationProvider.getTranslationConfiguration() != null) {
      this.sequenceTranslationConfigurationPanel
        .setSequenceTranslationConfiguration(transformationProvider.getTranslationConfiguration());
    }
  }
}
