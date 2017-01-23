package org.sing_group.seda.gui;

import static org.sing_group.seda.gui.TransformationsConfigurationEvent.of;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.MIN_NUM_OF_SEQUENCES_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.REFERENCE_INDEX_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.REMOVE_BY_SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.REMOVE_NON_MULTIPLE_OF_THREE_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.REMOVE_STOP_CODONS_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.SIZE_DIFFERENCE_CHANGED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.STARTING_CODON_ADDED;
import static org.sing_group.seda.gui.TransformationsConfigurationEvent.TransformationConfigurationEventType.STARTING_CODON_REMOVED;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.DefaultDatatypeFactory;
import org.sing_group.seda.transformation.dataset.ComposedMSADatasetTransformation;
import org.sing_group.seda.transformation.dataset.MSADatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequenceCountFilterMSADatasetTransformation;
import org.sing_group.seda.transformation.msa.ComposedMSATransformation;
import org.sing_group.seda.transformation.msa.FilterByStartCodonTransformation;
import org.sing_group.seda.transformation.msa.MultipleSequenceAlignmentTransformation;
import org.sing_group.seda.transformation.msa.RemoveBySizeMSATransformation;
import org.sing_group.seda.transformation.msa.RemoveInFrameStopCodonsMSATransformation;
import org.sing_group.seda.transformation.msa.RemoveNonTripletsMSATransformation;
import org.sing_group.seda.transformation.sequence.RemoveStopCodonsSequenceTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;

public class TransformationsConfigurationModel {
	private final SortedSet<String> startingCodons;
	private boolean removeStopCodons;
	private boolean removeNonMultipleOfThree;
	private boolean removeIfInFrameStopCodon;
	private boolean removeBySizeDifference;
	private int sizeDifference;
	private int referenceIndex;
	private int minNumOfSequences;
	
	private final List<TransformationsConfigurationModelListener> listeners;
	
	public TransformationsConfigurationModel() {
		this.startingCodons = new TreeSet<>();
		this.removeStopCodons = true;
		this.removeNonMultipleOfThree = true;
		this.removeIfInFrameStopCodon = true;
		this.removeBySizeDifference = true;
		this.sizeDifference = 10;
		this.referenceIndex = 0;
		this.minNumOfSequences = 4;
		
		this.listeners = new CopyOnWriteArrayList<>();
	}

	public MSADatasetTransformation toTransformation(DatatypeFactory factory) {
		final List<SequenceTransformation> seqTransformations = new LinkedList<>();
		final List<MultipleSequenceAlignmentTransformation> msaTransformations = new LinkedList<>();
		final List<MSADatasetTransformation> datasetTransformations = new LinkedList<>();
		
		if (!this.startingCodons.isEmpty() && this.startingCodons.size() != 64) {
			msaTransformations.add(new FilterByStartCodonTransformation(this.startingCodons, factory));
		}
		
		if (this.removeStopCodons) {
			seqTransformations.add(new RemoveStopCodonsSequenceTransformation(factory));
		}
		
		if (this.removeNonMultipleOfThree) {
			msaTransformations.add(new RemoveNonTripletsMSATransformation(factory));
		}
		
		if (this.removeIfInFrameStopCodon) {
			msaTransformations.add(new RemoveInFrameStopCodonsMSATransformation(factory));
		}
		
		if (this.minNumOfSequences > 1) {
			datasetTransformations.add(new SequenceCountFilterMSADatasetTransformation(this.minNumOfSequences, factory));
		}
		
		if (this.removeBySizeDifference) {
			msaTransformations.add(new RemoveBySizeMSATransformation(this.referenceIndex, ((double) this.sizeDifference) / 100d, factory));
		}
		
		if (!seqTransformations.isEmpty()) {
			msaTransformations.add(new ComposedMSATransformation(factory, seqTransformations));
		}
		
		if (!msaTransformations.isEmpty()) {
			datasetTransformations.add(new ComposedMSADatasetTransformation(factory, msaTransformations));
		}
		
		if (datasetTransformations.size() == 1) {
			return datasetTransformations.get(0);
		} else {
			return MSADatasetTransformation.concat(datasetTransformations.stream().toArray(MSADatasetTransformation[]::new));
		}
	}
	
	
	public MSADatasetTransformation toTransformation() {
		return this.toTransformation(new DefaultDatatypeFactory());
	}

	public boolean isRemoveStopCodons() {
		return removeStopCodons;
	}

	public void setRemoveStopCodons(boolean removeStopCodons) {
		if (this.removeStopCodons != removeStopCodons) {
			this.removeStopCodons = removeStopCodons;
			this.fireTransformationsConfigurationModelEvent(of(REMOVE_STOP_CODONS_CHANGED, this.removeStopCodons));
		}
	}

	public boolean isRemoveNonMultipleOfThree() {
		return removeNonMultipleOfThree;
	}

	public void setRemoveNonMultipleOfThree(boolean removeNonMultipleOfThree) {
		if (this.removeNonMultipleOfThree != removeNonMultipleOfThree) {
			this.removeNonMultipleOfThree = removeNonMultipleOfThree;
			this.fireTransformationsConfigurationModelEvent(of(REMOVE_NON_MULTIPLE_OF_THREE_CHANGED, this.removeNonMultipleOfThree));
		}
	}

	public boolean isRemoveIfInFrameStopCodon() {
		return removeIfInFrameStopCodon;
	}

	public void setRemoveIfInFrameStopCodon(boolean removeIfInFrameStopCodon) {
		if (this.removeIfInFrameStopCodon != removeIfInFrameStopCodon) {
			this.removeIfInFrameStopCodon = removeIfInFrameStopCodon;
			this.fireTransformationsConfigurationModelEvent(of(REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED, this.removeIfInFrameStopCodon));
		}
	}

	public int getReferenceIndex() {
		return referenceIndex;
	}

	public void setReferenceIndex(int referenceIndex) {
		if (this.referenceIndex != referenceIndex) {
			this.referenceIndex = referenceIndex;
			this.fireTransformationsConfigurationModelEvent(of(REFERENCE_INDEX_CHANGED, this.referenceIndex));
		}
	}

	public int getMinNumOfSequences() {
		return minNumOfSequences;
	}

	public void setMinNumOfSequences(int minNumOfSequences) {
		if (this.minNumOfSequences != minNumOfSequences) {
			this.minNumOfSequences = minNumOfSequences;
			this.fireTransformationsConfigurationModelEvent(of(MIN_NUM_OF_SEQUENCES_CHANGED, this.minNumOfSequences));
		}
	}

	public boolean hasStartingCodon(String codon) {
		return this.startingCodons.contains(codon);
	}
	
	public void addStartingCodon(String codon) {
		if (this.startingCodons.add(codon)) {
			this.fireTransformationsConfigurationModelEvent(of(STARTING_CODON_ADDED, codon));
		}
	}
	
	public void removeStartingCodon(String codon) {
		if (this.startingCodons.remove(codon)) {
			this.fireTransformationsConfigurationModelEvent(of(STARTING_CODON_REMOVED, codon));
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
			this.removeBySizeDifference = removeBySizeDifference;
			this.fireTransformationsConfigurationModelEvent(of(REMOVE_BY_SIZE_DIFFERENCE_CHANGED, this.removeBySizeDifference));
		}
	}

	public int getSizeDifference() {
		return sizeDifference;
	}

	public void setSizeDifference(int sizeDifference) {
		if (this.sizeDifference != sizeDifference) {
			this.sizeDifference = sizeDifference;
			this.fireTransformationsConfigurationModelEvent(of(SIZE_DIFFERENCE_CHANGED, this.sizeDifference));
		}
	}
	
	public void addTransformationsConfigurationModelListener(TransformationsConfigurationModelListener listener) {
		if (!this.listeners.contains(listener))
			this.listeners.add(listener);
	}
	
	public boolean removeTransformationsConfigurationModelListener(TransformationsConfigurationModelListener listener) {
		return this.listeners.remove(listener);
	}
	
	public boolean containsTransformationsConfigurationModelListener(TransformationsConfigurationModelListener listener) {
		return this.listeners.contains(listener);
	}
	
	private void fireTransformationsConfigurationModelEvent(TransformationsConfigurationEvent event) {
		this.listeners.forEach(listener -> listener.configurationChanged(event));
	}
}
