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
package org.sing_group.seda.gui.split;

import static org.sing_group.seda.gui.split.SplitConfigurationEventType.INDEPENDENT_EXTRACTIONS_CHANGED;
import static org.sing_group.seda.gui.split.SplitConfigurationEventType.NUMBER_OF_FILES_CHANGED;
import static org.sing_group.seda.gui.split.SplitConfigurationEventType.NUMBER_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.split.SplitConfigurationEventType.RANDOMIZE_SELECTION_CHANGED;
import static org.sing_group.seda.gui.split.SplitConfigurationEventType.RANDOM_SEED_CHANGED;
import static org.sing_group.seda.gui.split.SplitConfigurationEventType.SPLIT_MODE_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.split.DefaultSequencesSort;
import org.sing_group.seda.core.split.NumberOfFilesSplitter;
import org.sing_group.seda.core.split.NumberOfSequencesAndNumberOfFilesSplitter;
import org.sing_group.seda.core.split.NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter;
import org.sing_group.seda.core.split.NumberOfSequencesSplitter;
import org.sing_group.seda.core.split.RandomSequencesSort;
import org.sing_group.seda.core.split.SequencesGroupSplitMode;
import org.sing_group.seda.core.split.SequencesGroupSplitter;
import org.sing_group.seda.core.split.SequencesSort;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SplitSequencesGroupDatasetTransformation;

@XmlRootElement
public class SplitConfigurationTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private SequencesGroupSplitMode splitMode;
  @XmlElement
  private boolean randomize;
  @XmlElement
  private int randomSeed;
  @XmlElement
  private boolean independentExtractions;
  @XmlElement
  private int numFiles;
  @XmlElement
  private int numSequences;

  public SplitConfigurationTransformationProvider() {}

  public SplitConfigurationTransformationProvider(
    SequencesGroupSplitMode splitMode, boolean randomize, int randomSeed, boolean independentExtractions,
    int numFiles, int numSequences
  ) {
    this.splitMode = splitMode;
    this.randomize = randomize;
    this.numSequences = numSequences;
    this.randomSeed = randomSeed;
    this.numFiles = numFiles;
    this.numSequences = numSequences;
  }

  @Override
  public boolean isValidTransformation() {
    return this.splitMode != null;
  }

  @Override
  public Validation validate() {
    return this.splitMode == null ? new DefaultValidation("Split mode can't be null.") : new DefaultValidation();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    final SequencesGroupSplitter splitter;

    switch (getSplitMode()) {
      case FIXED_FILES:
        splitter = new NumberOfFilesSplitter(getNumFiles(), getSequencesSort(), factory);
        break;
      case FIXED_SEQUENCES_PER_FILE:
        splitter = new NumberOfSequencesSplitter(getNumSequences(), getSequencesSort(), factory);
        break;
      case SEQUENCES_PER_FILE_AND_FILES:
        if (isIndependentExtractions()) {
          splitter =
            new NumberOfSequencesAndNumberOfFilesWithIndependentExtractionsSplitter(
              getNumFiles(), getNumSequences(), getSequencesSort(), factory
            );
        } else {
          splitter =
            new NumberOfSequencesAndNumberOfFilesSplitter(
              getNumFiles(), getNumSequences(), getSequencesSort(), factory
            );
        }
        break;
      default:
        throw new IllegalStateException("Illegal split mode");
    }

    return new SplitSequencesGroupDatasetTransformation(splitter, factory);
  }

  public SequencesGroupSplitMode getSplitMode() {
    return this.splitMode;
  }

  public int getNumSequences() {
    return this.numSequences;
  }

  public int getNumFiles() {
    return this.numFiles;
  }

  private SequencesSort getSequencesSort() {
    if (this.isRandomize()) {
      return new RandomSequencesSort(this.getRandomSeed());
    } else {
      return new DefaultSequencesSort();
    }
  }

  public int getRandomSeed() {
    return this.randomSeed;
  }

  public boolean isIndependentExtractions() {
    return this.independentExtractions;
  }

  public boolean isRandomize() {
    return randomize;
  }

  public void setRandomize(boolean randomize) {
    if (this.randomize != randomize) {
      this.randomize = randomize;
      this.fireTransformationsConfigurationModelEvent(RANDOMIZE_SELECTION_CHANGED, isRandomize());
    }
  }

  public void setRandomSeed(int randomSeed) {
    if (this.randomSeed != randomSeed) {
      this.randomSeed = randomSeed;
      this.fireTransformationsConfigurationModelEvent(RANDOM_SEED_CHANGED, getRandomSeed());
    }
  }

  public void setSplitMode(SequencesGroupSplitMode splitMode) {
    if (this.splitMode == null || !this.splitMode.equals(splitMode)) {
      this.splitMode = splitMode;
      this.fireTransformationsConfigurationModelEvent(SPLIT_MODE_CHANGED, getSplitMode());
    }
  }

  public void numFiles(int numFiles) {
    if (this.numFiles != numFiles) {
      this.numFiles = numFiles;
      this.fireTransformationsConfigurationModelEvent(NUMBER_OF_FILES_CHANGED, getNumFiles());
    }
  }

  public void setNumSequences(int numSequences) {
    if (this.numSequences != numSequences) {
      this.numSequences = numSequences;
      this.fireTransformationsConfigurationModelEvent(NUMBER_OF_SEQUENCES_CHANGED, getSplitMode());
    }
  }

  public void setIndependentExtractions(boolean independentExtractions) {
    if (this.independentExtractions != independentExtractions) {
      this.independentExtractions = independentExtractions;
      this.fireTransformationsConfigurationModelEvent(INDEPENDENT_EXTRACTIONS_CHANGED, isIndependentExtractions());
    }
  }
}
