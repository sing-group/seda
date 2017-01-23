package org.sing_group.seda.gui;

import static org.sing_group.seda.gui.GuiUtils.bindCheckBox;
import static org.sing_group.seda.gui.GuiUtils.bindSpinner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class TransformationsConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final TransformationsConfigurationModel model;
	
	private final JCheckBox chkRemoveStopCodons;
	private final JCheckBox chkRemoveNonMultipleOfThree;
	private final JCheckBox chkRemoveIfInFrameStopCodon;
	private final JCheckBox chkRemoveBySizeDifference;
	private final JSpinner spnSizeDifference;
	private final JSpinner spnReferenceIndex;
	private final JSpinner spnMinNumberOfSequences;

	private final JLabel lblSizeDifference;
	private final JLabel lblReferenceIndex;
	
	private final Map<String, JCheckBox> codonToChk;
	private final JButton btnUnselectCodons;
	private final JButton btnSelectCodons;
	
	public TransformationsConfigurationPanel() {
		super(new GridBagLayout());
		
		this.model = new TransformationsConfigurationModel();

		this.codonToChk = new HashMap<>();
		
		this.chkRemoveStopCodons = new JCheckBox(
			"Remove stop codons", this.model.isRemoveStopCodons()
		);
		this.chkRemoveNonMultipleOfThree = new JCheckBox(
			"Remove sequences with a non multiple of three sequence", this.model.isRemoveNonMultipleOfThree()
		);
		this.chkRemoveIfInFrameStopCodon = new JCheckBox(
			"Remove sequences with in-frame stop codons", this.model.isRemoveStopCodons()
		);
		this.chkRemoveBySizeDifference = new JCheckBox(
			"Remove by size difference", this.model.isRemoveBySizeDifference()
		);
		
		this.spnSizeDifference = new JSpinner(
			new SpinnerNumberModel(this.model.getSizeDifference(), 0, Integer.MAX_VALUE, 1)
		);
		this.spnReferenceIndex = new JSpinner(
			new SpinnerNumberModel(this.model.getReferenceIndex(), 0, Integer.MAX_VALUE, 1)
		);
		this.spnMinNumberOfSequences = new JSpinner(
			new SpinnerNumberModel(this.model.getMinNumOfSequences(), 0, Integer.MAX_VALUE, 1)
		);
		
		this.lblSizeDifference = new JLabel("Maximum size difference (%)");
		this.lblSizeDifference.setLabelFor(this.spnSizeDifference);
		
		this.lblReferenceIndex = new JLabel("Reference sequence index");
		this.lblReferenceIndex.setLabelFor(this.spnReferenceIndex);
		
		this.btnSelectCodons = new JButton("Select all codons");
		this.btnUnselectCodons = new JButton("Unselect all codons");
		
		final JLabel lblMinNumberOfSequences = new JLabel("Minimum number of sequences");
		lblMinNumberOfSequences.setLabelFor(this.spnMinNumberOfSequences);
		
		final JLabel lblValidStartingCodons = new JLabel("Valid starting codons:");
		
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1d;
		gbc.weighty = 1d;
		gbc.ipadx = 8;
		gbc.ipady = 8;
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.gridwidth = 8;
		this.add(this.chkRemoveStopCodons, gbc);
		
		gbc.gridy = 1;
		this.add(this.chkRemoveNonMultipleOfThree, gbc);
		
		gbc.gridy = 2;
		this.add(this.chkRemoveIfInFrameStopCodon, gbc);
		
		gbc.gridy = 3;
		this.add(this.chkRemoveBySizeDifference, gbc);
		
		gbc.gridy = 4;
		gbc.gridwidth = 4;
		this.add(lblSizeDifference, gbc);
		gbc.gridx = 4;
		this.add(spnSizeDifference, gbc);
		
		gbc.gridy = 5;
		gbc.gridx = 0;
		this.add(lblReferenceIndex, gbc);
		gbc.gridx = 4;
		this.add(spnReferenceIndex, gbc);
		
		gbc.gridy = 6;
		gbc.gridx = 0;
		this.add(lblMinNumberOfSequences, gbc);
		gbc.gridx = 4;
		this.add(spnMinNumberOfSequences, gbc);
		
		gbc.gridy = 7;
		gbc.gridx = 0;
		this.add(lblValidStartingCodons, gbc);
		
		gbc.gridy = 8;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		final char[] nucleotides = new char[] { 'A', 'C', 'T', 'G'};
		int counter = 0;
		for (char first : nucleotides) {
			for (char second : nucleotides) {
				for (char third : nucleotides) {
					final String codon = new String(new char[] { first, second, third });
					
					final JCheckBox chkCodon = new JCheckBox(codon, this.model.hasStartingCodon(codon));
					this.codonToChk.put(codon, chkCodon);
					
					this.add(chkCodon, gbc);
					
					if (++counter % 8 == 0) {
						gbc.gridy = gbc.gridy + 1;
						gbc.gridx = 0;
					} else {
						gbc.gridx = gbc.gridx + 1;
					}
				}
			}
		}
		
		gbc.gridwidth = 4;
		this.add(this.btnSelectCodons, gbc);
		gbc.gridx = 4;
		this.add(this.btnUnselectCodons, gbc);
		
		this.toggleSizeDifferenceControls();
		
		bindCheckBox(this.chkRemoveStopCodons, model::setRemoveStopCodons);
		bindCheckBox(this.chkRemoveNonMultipleOfThree, model::setRemoveNonMultipleOfThree);
		bindCheckBox(this.chkRemoveIfInFrameStopCodon, model::setRemoveIfInFrameStopCodon);
		bindCheckBox(this.chkRemoveBySizeDifference, model::setRemoveBySizeDifference);
		
		bindSpinner(this.spnSizeDifference, model::setSizeDifference);
		bindSpinner(this.spnReferenceIndex, model::setReferenceIndex);
		bindSpinner(this.spnMinNumberOfSequences, model::setMinNumOfSequences);
		
		this.codonToChk.entrySet().forEach(entry -> {
			final String codon = entry.getKey();
			final JCheckBox chkCodon = entry.getValue();

			chkCodon.addItemListener(event -> {
				if (chkCodon.isSelected())
					this.model.addStartingCodon(codon);
				else
					this.model.removeStartingCodon(codon);
			});
		});
		
		this.btnSelectCodons.addActionListener(event -> this.codonToChk.keySet().forEach(model::addStartingCodon));
		this.btnUnselectCodons.addActionListener(event -> this.codonToChk.keySet().forEach(model::removeStartingCodon));
		
		this.model.addTransformationsConfigurationModelListener(event -> {
			switch(event.getType()) {
			case STARTING_CODON_ADDED:
			case STARTING_CODON_REMOVED:
				updateStartingCodons((String) event.getNewValue());
			break;
			case REMOVE_STOP_CODONS_CHANGED:
				updateRemoveStopCodons();
			break;
			case REMOVE_NON_MULTIPLE_OF_THREE_CHANGED:
				updateRemoveNonMultipleOfThree();
			break;
			case REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED:
				updateRemoveIfInFrameStopCodon();
			break;
			case REMOVE_BY_SIZE_DIFFERENCE_CHANGED:
				updateRemoveBySizeDifference();
			break;
			case SIZE_DIFFERENCE_CHANGED:
				updateMinNumberOfSequences();
			break;
			case REFERENCE_INDEX_CHANGED:
				updateReferenceIndex();
			break;
			case MIN_NUM_OF_SEQUENCES_CHANGED:
				updateMinNumberOfSequences();
			break;
			}
		});
	}
	
	public TransformationsConfigurationModel getModel() {
		return model;
	}
	
	public void updateStartingCodons(String codon) {
		this.codonToChk.get(codon).setSelected(this.model.hasStartingCodon(codon));
	}

	public void updateRemoveStopCodons() {
		this.chkRemoveStopCodons.setSelected(this.model.isRemoveStopCodons());
	}

	public void updateRemoveNonMultipleOfThree() {
		this.chkRemoveNonMultipleOfThree.setSelected(this.model.isRemoveNonMultipleOfThree());
	}

	public void updateRemoveIfInFrameStopCodon() {
		this.chkRemoveIfInFrameStopCodon.setSelected(this.model.isRemoveIfInFrameStopCodon());
	}

	public void updateRemoveBySizeDifference() {
		this.chkRemoveBySizeDifference.setSelected(this.model.isRemoveBySizeDifference());
		this.toggleSizeDifferenceControls();
	}

	public void updateSizeDifference() {
		this.spnSizeDifference.setValue(this.model.getSizeDifference());
	}

	public void updateReferenceIndex() {
		this.spnReferenceIndex.setValue(this.model.getReferenceIndex());
	}

	public void updateMinNumberOfSequences() {
		this.spnMinNumberOfSequences.setValue(this.model.getMinNumOfSequences());
	}

	private void toggleSizeDifferenceControls() {
		final boolean enabled = this.model.isRemoveBySizeDifference();
		
		this.spnSizeDifference.setEnabled(enabled);
		this.spnReferenceIndex.setEnabled(enabled);
		this.lblSizeDifference.setEnabled(enabled);
		this.lblReferenceIndex.setEnabled(enabled);
	}
	
}
