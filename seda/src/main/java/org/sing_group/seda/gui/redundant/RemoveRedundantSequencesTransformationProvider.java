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
package org.sing_group.seda.gui.redundant;

import java.util.Map;

import javax.swing.event.ChangeEvent;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.gui.translation.SequenceTranslationPanel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeType;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class RemoveRedundantSequencesTransformationProvider extends AbstractTransformationProvider
  implements RemoveRedundantSequencesConfigurationPanelListener {
  public enum RemoveRedundantSequencesConfiguratioEventType implements TransformationChangeType {
    CONFIGURATION_CHANGED
  }

  private RemoveRedundantSequencesConfigurationPanel configurationPanel;

  public RemoveRedundantSequencesTransformationProvider(RemoveRedundantSequencesConfigurationPanel configurationPanel
  ) {
    this.configurationPanel = configurationPanel;
    this.configurationPanel.addRemoveRedundantSequencesConfigurationPanelListener(this);
  }

  @Override
  public boolean isValidTransformation() {
    return configurationPanel.isValidUserSelection();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation
      .concat(
        new ComposedSequencesGroupDatasetTransformation(
          factory, getRemoveRedundantSequencesTransformation(factory)
        )
      );
  }

  private SequencesGroupTransformation getRemoveRedundantSequencesTransformation(DatatypeFactory factory) {
    if (isTranslationSelected()) {
      return new RemoveRedundantSequencesTransformation(
        this.configurationPanel.getConfiguration(), getSequenceTranslationConfiguration(), factory
      );
    } else {
      return new RemoveRedundantSequencesTransformation(this.configurationPanel.getConfiguration(), factory);
    }
  }

  @Override
  public void configurationChanged(ChangeEvent event) {
    this.fireTransformationsConfigurationModelEvent(RemoveRedundantSequencesConfiguratioEventType.CONFIGURATION_CHANGED, null);
  }

  private SequenceTranslationPanel getTranslationPanel() {
    return this.configurationPanel.getTranslationPanel();
  }

  protected boolean isTranslationSelected() {
    return getTranslationPanel().isTranslationSelected();
  }

  protected SequenceTranslationConfiguration getSequenceTranslationConfiguration() {
    return new SequenceTranslationConfiguration(getCodonTable(), isReverseSequences(), getTranslationFrames());
  }

  protected int[] getTranslationFrames() {
    return getTranslationPanel().getTranslationFrames();
  }

  protected boolean isReverseSequences() {
    return getTranslationPanel().isReverseSequences();
  }

  protected Map<String, String> getCodonTable() {
    return getTranslationPanel().getCodonTable();
  }
}
