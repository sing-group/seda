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
package org.sing_group.seda.gui.consensus;

import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.MINIMUM_PRESENCE_CHANGED;
import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.SEQUENCE_TYPE_CHANGED;
import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.VERBOSE_CHANGED;

import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.GenerateConsensusSequencesGroupTransformation;

public class GenerateConsensusSequenceTransformationProvider extends AbstractTransformationProvider {
  private boolean verbose;
  private double minimumPresence;
  private SequenceType sequenceType;
  private ReformatFastaTransformationProvider reformatModel;

  public GenerateConsensusSequenceTransformationProvider(ReformatFastaTransformationProvider reformatModel) {
    this.reformatModel = reformatModel;
    this.reformatModel.addTransformationChangeListener(
      new TransformationChangeListener() {

        @Override
        public void onTransformationChange(TransformationChangeEvent event) {
          fireTransformationsConfigurationModelEvent(event);
        }
      }
    );
  }

  @Override
  public boolean isValidTransformation() {
    return reformatModel.isValidTransformation()
      && this.isValidMinimumPresenceValue();
  }

  private boolean isValidMinimumPresenceValue() {
    return minimumPresence >= 0 && minimumPresence <= 1.0;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        new GenerateConsensusSequencesGroupTransformation(factory, this.sequenceType, this.minimumPresence, this.verbose)
      ),
      this.reformatModel.getTransformation(factory)
    );
  }

  public void setSequenceType(SequenceType newSequenceType) {
    if (newSequenceType != null && (this.sequenceType == null || !this.sequenceType.equals(newSequenceType))) {
      this.sequenceType = newSequenceType;
      fireTransformationsConfigurationModelEvent(SEQUENCE_TYPE_CHANGED, this.sequenceType);
    }
  }

  public void setMinimumPresence(double newMinimumPresence) {
    if (this.minimumPresence != newMinimumPresence) {
      this.minimumPresence = newMinimumPresence;
      fireTransformationsConfigurationModelEvent(MINIMUM_PRESENCE_CHANGED, this.minimumPresence);
    }
  }

  public void setVerbose(boolean newIsVerbose) {
    if (this.verbose != newIsVerbose) {
      this.verbose = newIsVerbose;
      fireTransformationsConfigurationModelEvent(VERBOSE_CHANGED, this.verbose);
    }
  }
}
