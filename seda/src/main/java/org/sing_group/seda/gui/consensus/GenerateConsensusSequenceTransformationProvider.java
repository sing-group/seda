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

import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.CONSENSUS_BASE_STRATEGY_CHANGED;
import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.MINIMUM_PRESENCE_CHANGED;
import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.SEQUENCE_TYPE_CHANGED;
import static org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationChangeType.VERBOSE_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.bio.SequenceType;
import org.sing_group.seda.bio.consensus.ConsensusBaseStrategy;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultTransformationValidation;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.plugin.spi.TransformationValidation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.GenerateConsensusSequencesGroupTransformation;

@XmlRootElement
public class GenerateConsensusSequenceTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private boolean verbose;
  @XmlElement
  private double minimumPresence;
  @XmlElement
  private SequenceType sequenceType;
  @XmlElement
  private ConsensusBaseStrategy consensusBaseStrategy;

  private ReformatFastaTransformationProvider reformatFastaTransformationProvider;

  private TransformationChangeListener reformatFastaTransformationChangeListener = new TransformationChangeListener() {
    @Override
    public void onTransformationChange(TransformationChangeEvent event) {
      fireTransformationsConfigurationModelEvent(event);
    }
  };

  public GenerateConsensusSequenceTransformationProvider() {}

  @Override
  public boolean isValidTransformation() {
    return reformatFastaTransformationProvider.isValidTransformation()
      && this.isValidMinimumPresenceValue();
  }

  @Override
  public TransformationValidation validate() {
    if (!reformatFastaTransformationProvider.isValidTransformation()) {
      return new DefaultTransformationValidation("Reformat fasta provider is not valid");
    }
    if (!this.isValidMinimumPresenceValue()) {
      return new DefaultTransformationValidation("Minimum Presence Value is not valid");
    }
    return new DefaultTransformationValidation();
  }

  private boolean isValidMinimumPresenceValue() {
    return minimumPresence >= 0 && minimumPresence <= 1.0;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        new GenerateConsensusSequencesGroupTransformation(
          factory,
          this.sequenceType,
          this.consensusBaseStrategy,
          this.minimumPresence,
          this.verbose
        )
      ),
      this.reformatFastaTransformationProvider.getTransformation(factory)
    );
  }

  public void setSequenceType(SequenceType newSequenceType) {
    if (newSequenceType != null && (this.sequenceType == null || !this.sequenceType.equals(newSequenceType))) {
      this.sequenceType = newSequenceType;
      fireTransformationsConfigurationModelEvent(SEQUENCE_TYPE_CHANGED, this.sequenceType);
    }
  }

  public SequenceType getSequenceType() {
    return sequenceType;
  }

  public void setConsensusBaseStrategy(ConsensusBaseStrategy newConsensusBaseStrategy) {
    if (
      newConsensusBaseStrategy != null
        && (this.consensusBaseStrategy == null || !this.consensusBaseStrategy.equals(newConsensusBaseStrategy))
    ) {
      this.consensusBaseStrategy = newConsensusBaseStrategy;
      fireTransformationsConfigurationModelEvent(CONSENSUS_BASE_STRATEGY_CHANGED, this.consensusBaseStrategy);
    }
  }

  public ConsensusBaseStrategy getConsensusBaseStrategy() {
    return consensusBaseStrategy;
  }

  public void setMinimumPresence(double newMinimumPresence) {
    if (this.minimumPresence != newMinimumPresence) {
      this.minimumPresence = newMinimumPresence;
      fireTransformationsConfigurationModelEvent(MINIMUM_PRESENCE_CHANGED, this.minimumPresence);
    }
  }

  public double getMinimumPresence() {
    return minimumPresence;
  }

  public void setVerbose(boolean newIsVerbose) {
    if (this.verbose != newIsVerbose) {
      this.verbose = newIsVerbose;
      fireTransformationsConfigurationModelEvent(VERBOSE_CHANGED, this.verbose);
    }
  }

  public boolean isVerbose() {
    return verbose;
  }

  public ReformatFastaTransformationProvider getReformatFastaTransformationProvider() {
    return reformatFastaTransformationProvider;
  }

  public void setReformatFastaTransformationProvider(
    ReformatFastaTransformationProvider reformatFastaTransformationProvider
  ) {
    this.reformatFastaTransformationProvider = reformatFastaTransformationProvider;
    this.reformatFastaTransformationProvider
      .addTransformationChangeListener(this.reformatFastaTransformationChangeListener);
  }
}
