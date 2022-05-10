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
package org.sing_group.seda.gui.filtering;

import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.HEADER_FILTERING_CONFIGURATION_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MAX_NUM_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MAX_SEQUENCE_LENGTH_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MIN_NUM_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MIN_SEQUENCE_LENGTH_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REFERENCE_FILE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REFERENCE_INDEX_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_BY_SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_NON_MULTIPLE_OF_THREE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.STARTING_CODON_ADDED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.STARTING_CODON_REMOVED;
import static org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation.concat;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.FilterType;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.Level;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.Mode;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultTransformationValidation;
import org.sing_group.seda.plugin.spi.TransformationValidation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.HeaderCountFilteringSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequenceCountFilterSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterBySequenceLengthTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterByStartCodonTransformation;
import org.sing_group.seda.transformation.sequencesgroup.HeaderCountFilteringSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveBySizeSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveInFrameStopCodonsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveNonTripletsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class FilteringConfigurationTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private final SortedSet<String> startingCodons;
  private boolean removeNonMultipleOfThree;
  private boolean removeIfInFrameStopCodon;
  private boolean removeBySizeDifference;
  private int sizeDifference;
  private int referenceIndex;
  @XmlElement
  private File referenceFile;
  private int minNumOfSequences;
  private int maxNumOfSequences;
  private int minSequenceLength;
  private int maxSequenceLength;
  private HeaderFilteringConfiguration headerFilteringConfiguration;

  public FilteringConfigurationTransformationProvider() {
    this.startingCodons = new TreeSet<>();
    this.removeNonMultipleOfThree = false;
    this.removeIfInFrameStopCodon = false;
    this.removeBySizeDifference = false;
    this.sizeDifference = 10;
    this.referenceIndex = 1;
    this.minNumOfSequences = 1;
    this.maxNumOfSequences = 0;
    this.minSequenceLength = 0;
    this.maxSequenceLength = 0;
    this.headerFilteringConfiguration = new HeaderFilteringConfiguration();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    final List<SequenceTransformation> seqTransformations = new LinkedList<>();
    final List<SequencesGroupTransformation> sequencesGroupTransformations = new LinkedList<>();
    final List<SequencesGroupDatasetTransformation> datasetTransformations = new LinkedList<>();

    if (!this.startingCodons.isEmpty() && this.startingCodons.size() != 64) {
      sequencesGroupTransformations.add(new FilterByStartCodonTransformation(this.startingCodons, factory));
    }

    if (this.removeNonMultipleOfThree) {
      sequencesGroupTransformations.add(new RemoveNonTripletsSequencesGroupTransformation(factory));
    }

    if (this.removeIfInFrameStopCodon) {
      sequencesGroupTransformations.add(new RemoveInFrameStopCodonsSequencesGroupTransformation(factory));
    }

    if (this.minSequenceLength > 0 || this.maxSequenceLength > 0) {
      sequencesGroupTransformations
        .add(new FilterBySequenceLengthTransformation(this.minSequenceLength, this.maxSequenceLength, factory));
    }

    if (this.minNumOfSequences > 1 || this.maxNumOfSequences > 1) {
      datasetTransformations.add(
        new SequenceCountFilterSequencesGroupDatasetTransformation(
          this.minNumOfSequences, this.maxNumOfSequences, factory
        )
      );
    }

    if (this.removeBySizeDifference) {
      if (this.referenceFile == null) {
        sequencesGroupTransformations.add(
          new RemoveBySizeSequencesGroupTransformation(
            this.referenceIndex - 1, ((double) this.sizeDifference) / 100d, factory
          )
        );
      } else {
        sequencesGroupTransformations.add(
          new RemoveBySizeSequencesGroupTransformation(
            getReferenceSequence(factory).get(), ((double) this.sizeDifference) / 100d, factory
          )
        );
      }
    }

    if (!seqTransformations.isEmpty()) {
      sequencesGroupTransformations.add(new ComposedSequencesGroupTransformation(factory, seqTransformations));
    }

    if (headerFilteringConfiguration.isUseFilter()) {
      HeaderMatcher matcher;

      if (headerFilteringConfiguration.getFilterType().equals(FilterType.SEQUENCE_NAME)) {
        matcher = new SequenceNameHeaderMatcher();
      } else {
        RegexConfiguration regexConfiguration =
          new RegexConfiguration(
            headerFilteringConfiguration.isCaseSensitive(),
            headerFilteringConfiguration.getRegexGroup(),
            headerFilteringConfiguration.isQuotePattern()
          );

        matcher =
          new RegexHeaderMatcher(
            headerFilteringConfiguration.getFilterString(),
            headerFilteringConfiguration.getHeaderTarget(),
            regexConfiguration
          );
      }

      if (headerFilteringConfiguration.getLevel().equals(Level.SEQUENCE)) {
        sequencesGroupTransformations.add(
          new HeaderCountFilteringSequencesGroupTransformation(
            factory, matcher, headerFilteringConfiguration.getMin(),
            headerFilteringConfiguration.getMax(),
            headerFilteringConfiguration.getMode().equals(Mode.KEEP)
          )
        );
      } else {
        datasetTransformations.add(
          new HeaderCountFilteringSequencesGroupDatasetTransformation(
            factory, matcher, headerFilteringConfiguration.getMin(),
            headerFilteringConfiguration.getMax(),
            headerFilteringConfiguration.getMode().equals(Mode.KEEP)
          )
        );
      }
    }

    if (!sequencesGroupTransformations.isEmpty()) {
      datasetTransformations
        .add(new ComposedSequencesGroupDatasetTransformation(factory, sequencesGroupTransformations));
    }

    if (datasetTransformations.size() == 1) {
      return datasetTransformations.get(0);
    } else {
      return concat(datasetTransformations.stream().toArray(SequencesGroupDatasetTransformation[]::new));
    }
  }

  public boolean isRemoveNonMultipleOfThree() {
    return removeNonMultipleOfThree;
  }

  public void setRemoveNonMultipleOfThree(boolean removeNonMultipleOfThree) {
    if (this.removeNonMultipleOfThree != removeNonMultipleOfThree) {
      final boolean oldValue = this.removeNonMultipleOfThree;
      this.removeNonMultipleOfThree = removeNonMultipleOfThree;
      this.fireTransformationsConfigurationModelEvent(
        REMOVE_NON_MULTIPLE_OF_THREE_CHANGED, oldValue, this.removeNonMultipleOfThree
      );
    }
  }

  public boolean isRemoveIfInFrameStopCodon() {
    return removeIfInFrameStopCodon;
  }

  public void setRemoveIfInFrameStopCodon(boolean removeIfInFrameStopCodon) {
    if (this.removeIfInFrameStopCodon != removeIfInFrameStopCodon) {
      final boolean oldValue = this.removeIfInFrameStopCodon;
      this.removeIfInFrameStopCodon = removeIfInFrameStopCodon;
      this.fireTransformationsConfigurationModelEvent(
        REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED, oldValue, this.removeIfInFrameStopCodon
      );
    }
  }

  public int getReferenceIndex() {
    return referenceIndex;
  }

  public void setReferenceIndex(int referenceIndex) {
    if (this.referenceIndex != referenceIndex) {
      final int oldValue = this.referenceIndex;
      this.referenceIndex = referenceIndex;

      this.fireTransformationsConfigurationModelEvent(
        REFERENCE_INDEX_CHANGED, oldValue, this.referenceIndex
      );
    }
  }

  public void clearReferenceFile() {
    final File oldValue = this.referenceFile;
    this.referenceFile = null;

    this.fireTransformationsConfigurationModelEvent(
      REFERENCE_FILE_CHANGED, oldValue, this.referenceFile
    );
  }

  public void setReferenceFile(File referenceFile) {
    if (this.referenceFile != referenceFile && isValidReferenceFile(referenceFile)) {
      final File oldValue = this.referenceFile;
      this.referenceFile = referenceFile;

      this.fireTransformationsConfigurationModelEvent(
        REFERENCE_FILE_CHANGED, oldValue, this.referenceFile
      );
    }
  }

  public Optional<File> getReferenceFile() {
    return Optional.ofNullable(referenceFile);
  }

  public int getMinNumOfSequences() {
    return minNumOfSequences;
  }

  public void setMinNumOfSequences(int minNumOfSequences) {
    if (this.minNumOfSequences != minNumOfSequences) {
      final int oldValue = this.minNumOfSequences;
      this.minNumOfSequences = minNumOfSequences;

      this.fireTransformationsConfigurationModelEvent(
        MIN_NUM_OF_SEQUENCES_CHANGED, oldValue, this.minNumOfSequences
      );
    }
  }

  public int getMaxNumOfSequences() {
    return maxNumOfSequences;
  }

  public void setMaxNumOfSequences(int maxNumOfSequences) {
    if (this.maxNumOfSequences != maxNumOfSequences) {
      final int oldValue = this.maxNumOfSequences;
      this.maxNumOfSequences = maxNumOfSequences;

      this.fireTransformationsConfigurationModelEvent(
        MAX_NUM_OF_SEQUENCES_CHANGED, oldValue, this.maxNumOfSequences
      );
    }
  }

  public int getMinSequenceLength() {
    return minSequenceLength;
  }

  public void setMinSequenceLength(int minSequenceLength) {
    if (this.minSequenceLength != minSequenceLength) {
      final int oldValue = this.minSequenceLength;
      this.minSequenceLength = minSequenceLength;

      this.fireTransformationsConfigurationModelEvent(
        MIN_SEQUENCE_LENGTH_CHANGED, oldValue, this.minSequenceLength
      );
    }
  }

  public int getMaxSequenceLength() {
    return maxSequenceLength;
  }

  public void setMaxSequenceLength(int maxSequenceLength) {
    if (this.maxSequenceLength != maxSequenceLength) {
      final int oldValue = this.maxSequenceLength;
      this.maxSequenceLength = maxSequenceLength;

      this.fireTransformationsConfigurationModelEvent(
        MAX_SEQUENCE_LENGTH_CHANGED, oldValue, this.maxSequenceLength
      );
    }
  }

  public boolean hasStartingCodon(String codon) {
    return this.startingCodons.contains(codon);
  }

  public void addStartingCodon(String codon) {
    if (this.startingCodons.add(codon)) {
      this.fireTransformationsConfigurationModelEvent(
        STARTING_CODON_ADDED, codon
      );
    }
  }

  public void removeStartingCodon(String codon) {
    if (this.startingCodons.remove(codon)) {
      this.fireTransformationsConfigurationModelEvent(
        STARTING_CODON_REMOVED, codon
      );
    }
  }

  public Stream<String> getStartingCodons() {
    return startingCodons.stream();
  }

  public boolean isRemoveBySizeDifference() {
    return removeBySizeDifference;
  }

  public void setRemoveBySizeDifference(boolean removeBySizeDifference) {
    if (this.removeBySizeDifference != removeBySizeDifference) {
      final boolean oldValue = this.removeBySizeDifference;
      this.removeBySizeDifference = removeBySizeDifference;

      this.fireTransformationsConfigurationModelEvent(
        REMOVE_BY_SIZE_DIFFERENCE_CHANGED, oldValue, this.removeBySizeDifference
      );
    }
  }

  public int getSizeDifference() {
    return sizeDifference;
  }

  public void setSizeDifference(int sizeDifference) {
    if (this.sizeDifference != sizeDifference) {
      final int oldValue = this.sizeDifference;
      this.sizeDifference = sizeDifference;

      this.fireTransformationsConfigurationModelEvent(
        SIZE_DIFFERENCE_CHANGED, oldValue, this.sizeDifference
      );
    }
  }

  public HeaderFilteringConfiguration getHeaderFilteringConfiguration() {
    return headerFilteringConfiguration;
  }

  public void setHeaderFilteringConfiguration(HeaderFilteringConfiguration headerFilteringConfiguration) {
    if (!this.headerFilteringConfiguration.equals(headerFilteringConfiguration)) {
      final HeaderFilteringConfiguration oldValue = this.headerFilteringConfiguration;
      this.headerFilteringConfiguration = headerFilteringConfiguration;

      this.fireTransformationsConfigurationModelEvent(
        HEADER_FILTERING_CONFIGURATION_CHANGED, oldValue, this.headerFilteringConfiguration
      );
    }
  }

  @Override
  public boolean isValidTransformation() {
    return this.isValidSequenceLengthConfiguration() && this.isValidNumberOfSequencesConfiguration()
      && this.isValidReferenceSequenceConfiguration() && this.isValidHeaderFilteringConfiguration();
  }

  @Override
  public TransformationValidation validate() {
    List<String> errorList = new ArrayList<>();

    if (!this.isValidSequenceLengthConfiguration()) {
      errorList.add("Sequence length configuration is not valid");
    }

    if (!this.isValidNumberOfSequencesConfiguration()) {
      errorList.add("Number of sequences configuration is not valid");
    }

    if (!this.isValidReferenceSequenceConfiguration()) {
      errorList.add("Reference sequence configuration is not valid");
    }

    if (!this.isValidHeaderFilteringConfiguration()) {
      errorList.add("Header filtering configuration is not valid");
    }

    if (errorList.isEmpty()) {
      return new DefaultTransformationValidation();
    } else {
      return new DefaultTransformationValidation(errorList);
    }
  }

  public boolean isValidSequenceLengthConfiguration() {
    return this.maxSequenceLength == 0 || this.minSequenceLength <= this.maxSequenceLength;
  }

  public boolean isValidNumberOfSequencesConfiguration() {
    return this.maxNumOfSequences == 0 || this.minNumOfSequences <= this.maxNumOfSequences;
  }

  public boolean isValidHeaderFilteringConfiguration() {
    return this.headerFilteringConfiguration.isValidConfiguration();
  }

  public boolean isValidReferenceSequenceConfiguration() {
    return this.referenceFile == null || this.isValidReferenceFile();
  }

  public boolean isValidReferenceFile() {
    return isValidReferenceFile(this.referenceFile);
  }

  public static boolean isValidReferenceFile(File referenceFile) {
    return referenceFile != null && getReferenceSequence(referenceFile).isPresent();
  }

  private static Optional<Sequence> getReferenceSequence(File referenceFile) {
    return getReferenceSequence(DatatypeFactory.getDefaultDatatypeFactory(), referenceFile);
  }

  private Optional<Sequence> getReferenceSequence(DatatypeFactory factory) {
    return getReferenceSequence(factory, this.referenceFile);
  }

  private static Optional<Sequence> getReferenceSequence(DatatypeFactory factory, File referenceFile) {
    try {
      return factory.newSequencesGroup(referenceFile.toPath()).getSequences().findFirst();
    } catch (RuntimeException e) {
      return Optional.empty();
    }
  }
}
