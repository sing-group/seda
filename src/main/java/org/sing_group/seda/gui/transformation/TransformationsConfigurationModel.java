package org.sing_group.seda.gui.transformation;

import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.MIN_NUM_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.REFERENCE_INDEX_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.REMOVE_BY_SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.REMOVE_NON_MULTIPLE_OF_THREE_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.REMOVE_STOP_CODONS_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.STARTING_CODON_ADDED;
import static org.sing_group.seda.gui.transformation.TransformationConfigurationEventType.STARTING_CODON_REMOVED;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequenceCountFilterSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.RemoveStopCodonsSequenceTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterByStartCodonTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveBySizeSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveInFrameStopCodonsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveNonTripletsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class TransformationsConfigurationModel extends AbstractTransformationProvider {
  private final SortedSet<String> startingCodons;
  private boolean removeStopCodons;
  private boolean removeNonMultipleOfThree;
  private boolean removeIfInFrameStopCodon;
  private boolean removeBySizeDifference;
  private int sizeDifference;
  private int referenceIndex;
  private int minNumOfSequences;
  
  public TransformationsConfigurationModel() {
    this.startingCodons = new TreeSet<>();
    this.removeStopCodons = true;
    this.removeNonMultipleOfThree = true;
    this.removeIfInFrameStopCodon = true;
    this.removeBySizeDifference = true;
    this.sizeDifference = 10;
    this.referenceIndex = 0;
    this.minNumOfSequences = 4;
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
    
    if (this.minNumOfSequences > 1) {
      datasetTransformations.add(new SequenceCountFilterSequencesGroupDatasetTransformation(this.minNumOfSequences, factory));
    }
    
    if (this.removeBySizeDifference) {
      sequencesGroupTransformations.add(new RemoveBySizeSequencesGroupTransformation(this.referenceIndex, ((double) this.sizeDifference) / 100d, factory));
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
    return true;
  }
}
