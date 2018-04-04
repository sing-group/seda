/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.consensus;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationModel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.GenerateConsensusSequencesGroupTransformation;

public class GenerateConsensusSequenceTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaConfigurationModel reformatModel;
  private GenerateConsensusSequenceConfigurationPanel generateConsensusSequenceConfigurationPanel;

  public GenerateConsensusSequenceTransformationProvider(
    GenerateConsensusSequenceConfigurationPanel generateConsensusSequenceConfigurationPanel,
    ReformatFastaConfigurationModel reformatModel
  ) {
    this.generateConsensusSequenceConfigurationPanel = generateConsensusSequenceConfigurationPanel;
    this.reformatModel = reformatModel;
    this.reformatModel.addTransformationChangeListener(
      new TransformationChangeListener() {

        @Override
        public void onTransformationChange(TransformationChangeEvent event) {
          fireTransformationsConfigurationModelEvent(event.getType(), event.getNewValue());
        }
      }
    );
  }

  @Override
  public boolean isValidTransformation() {
    return reformatModel.isValidTransformation()
      && this.generateConsensusSequenceConfigurationPanel.isValidConfiguration();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        new GenerateConsensusSequencesGroupTransformation(
          factory,
          generateConsensusSequenceConfigurationPanel.getSequenceType(),
          generateConsensusSequenceConfigurationPanel.getMinimumPresence(),
          generateConsensusSequenceConfigurationPanel.isVerbose()
        )
      ),
      this.reformatModel.getTransformation(factory)
    );
  }

  public void sequenceTypeChanged() {
    fireTransformationsConfigurationModelEvent(
      GenerateConsensusSequenceTransformationChangeType.SEQUENCE_TYPE_CHANGED,
      generateConsensusSequenceConfigurationPanel.getSequenceType()
    );
  }

  public void minimumPresenceChanged() {
    fireTransformationsConfigurationModelEvent(
      GenerateConsensusSequenceTransformationChangeType.MINIMUM_PRESENCE_CHANGED,
      generateConsensusSequenceConfigurationPanel.getMinimumPresence()
    );
  }

  public void verboseChanged() {
    fireTransformationsConfigurationModelEvent(
      GenerateConsensusSequenceTransformationChangeType.VERBOSE_CHANGED,
      generateConsensusSequenceConfigurationPanel.isVerbose()
    );
  }
}
