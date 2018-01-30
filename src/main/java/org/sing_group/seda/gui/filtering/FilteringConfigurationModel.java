package org.sing_group.seda.gui.filtering;

import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MIN_NUM_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MAX_NUM_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MIN_SEQUENCE_LENGTH_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.MAX_SEQUENCE_LENGTH_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REFERENCE_INDEX_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_BY_SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_NON_MULTIPLE_OF_THREE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.REMOVE_STOP_CODONS_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.STARTING_CODON_ADDED;
import static org.sing_group.seda.gui.filtering.FilteringConfigurationEventType.STARTING_CODON_REMOVED;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequenceCountFilterSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.RemoveStopCodonsSequenceTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterBySequenceLengthTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterByStartCodonTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveBySizeSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveInFrameStopCodonsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveNonTripletsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class FilteringConfigurationModel extends AbstractTransformationProvider {
  private final SortedSet<String> startingCodons;
  private boolean removeStopCodons;
  private boolean removeNonMultipleOfThree;
  private boolean removeIfInFrameStopCodon;
  private boolean removeBySizeDifference;
  private int sizeDifference;
  private int referenceIndex;
  private int minNumOfSequences;
  private int maxNumOfSequences;
  private int minSequenceLength;
  private int maxSequenceLength;

  public FilteringConfigurationModel() {
    this.startingCodons = new TreeSet<>();
    this.removeStopCodons = false;
    this.removeNonMultipleOfThree = false;
    this.removeIfInFrameStopCodon = false;
    this.removeBySizeDifference = false;
    this.sizeDifference = 10;
    this.referenceIndex = 1;
    this.minNumOfSequences = 1;
    this.maxNumOfSequences = 0;
    this.minSequenceLength = 0;
    this.maxSequenceLength = 0;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    final List<SequenceTransformation> seqTransformations = new LinkedList<>();
    final List<SequencesGroupTransformation> sequencesGroupTransformations = new LinkedList<>();
    final List<SequencesGroupDatasetTransformation> datasetTransformations = new LinkedList<>();

    if (!this.startingCodons.isEmpty() && this.startingCodons.size() != 64) {
      sequencesGroupTransformations.add(new FilterByStartCodonTransformation(this.startingCodons, factory));
    }

    if (this.removeStopCodons) {
      seqTransformations.add(new RemoveStopCodonsSequenceTransformation(factory));
    }

    if (this.removeNonMultipleOfThree) {
      sequencesGroupTransformations.add(new RemoveNonTripletsSequencesGroupTransformation(factory));
    }

    if (this.removeIfInFrameStopCodon) {
      sequencesGroupTransformations.add(new RemoveInFrameStopCodonsSequencesGroupTransformation(factory));
    }

    if (this.minSequenceLength > 0 || this.maxSequenceLength > 0) {
      sequencesGroupTransformations.add(new FilterBySequenceLengthTransformation(this.minSequenceLength, this.maxSequenceLength, factory));
    }

    if (this.minNumOfSequences > 1 || this.maxNumOfSequences > 1) {
      datasetTransformations.add(new SequenceCountFilterSequencesGroupDatasetTransformation(this.minNumOfSequences, this.maxNumOfSequences, factory));
    }

    if (this.removeBySizeDifference) {
      sequencesGroupTransformations.add(new RemoveBySizeSequencesGroupTransformation(this.referenceIndex - 1, ((double) this.sizeDifference) / 100d, factory));
    }

    if (!seqTransformations.isEmpty()) {
      sequencesGroupTransformations.add(new ComposedSequencesGroupTransformation(factory, seqTransformations));
    }

    if (!sequencesGroupTransformations.isEmpty()) {
      datasetTransformations.add(new ComposedSequencesGroupDatasetTransformation(factory, sequencesGroupTransformations));
    }

    if (datasetTransformations.size() == 1) {
      return datasetTransformations.get(0);
    } else {
      return SequencesGroupDatasetTransformation.concat(datasetTransformations.stream().toArray(SequencesGroupDatasetTransformation[]::new));
    }
  }

  public boolean isRemoveStopCodons() {
    return removeStopCodons;
  }

  public void setRemoveStopCodons(boolean removeStopCodons) {
    if (this.removeStopCodons != removeStopCodons) {
      final boolean oldValue = this.removeStopCodons;
      this.removeStopCodons = removeStopCodons;
      this.fireTransformationsConfigurationModelEvent(
        REMOVE_STOP_CODONS_CHANGED, oldValue, this.removeStopCodons
      );
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

  @Override
  public boolean isValidTransformation() {
    return this.isValidSequenceLengthConfiguration() && this.isValidNumberOfSequencesConfiguration();
  }

  public boolean isValidSequenceLengthConfiguration() {
    return this.maxSequenceLength == 0 || this.minSequenceLength <= this.maxSequenceLength;
  }

  public boolean isValidNumberOfSequencesConfiguration() {
    return this.maxNumOfSequences == 0 || this.minNumOfSequences <= this.maxNumOfSequences;
  }
}
