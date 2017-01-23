package org.sing_group.seda.gui;

import static org.sing_group.seda.gui.GuiUtils.bindCheckBox;
import static org.sing_group.seda.gui.GuiUtils.bindSpinner;
import static org.sing_group.seda.gui.GuiUtils.showFileChooserAndProcess;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class OutputConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JButton btnOutputDirectory;
	private final JLabel lblOutputDirectory;
	private final JCheckBox chkSplitInSubdirectories;
	private final JLabel lblSubdirectoriesSize;
	private final JSpinner spnSubdirectoriesSize;
	
	private final JFileChooser fileChooser;
	
	private final OutputConfigurationModel model;
	
	public OutputConfigurationPanel() {
		super(new GridLayout(3, 1, 0, 4));
		
		this.model = new OutputConfigurationModel();
		
		this.fileChooser = new JFileChooser(this.model.getOutputDirectory().toFile());
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.fileChooser.setMultiSelectionEnabled(false);
		
		this.lblOutputDirectory = new JLabel(this.model.getOutputDirectoryPath());
		this.btnOutputDirectory = new JButton("Change output directory");
		this.lblOutputDirectory.setLabelFor(this.btnOutputDirectory);
		
		this.lblSubdirectoriesSize = new JLabel("Files by subdirectory");
		this.chkSplitInSubdirectories = new JCheckBox("Split in subdirectories", this.model.isSplitInSubdirectories());
		this.lblSubdirectoriesSize.setLabelFor(this.chkSplitInSubdirectories);
		
		this.spnSubdirectoriesSize = new JSpinner(
			new SpinnerNumberModel(this.model.getSubdirectorySize(), 1, Integer.MAX_VALUE, 1)
		);
		
		final JPanel panelOutputDirectory = new JPanel(new BorderLayout(10, 0));
		panelOutputDirectory.add(this.btnOutputDirectory, BorderLayout.WEST);
		panelOutputDirectory.add(this.lblOutputDirectory, BorderLayout.CENTER);
		
		final JPanel panelSubdirectoriesSize = new JPanel(new BorderLayout(10, 0));
		panelSubdirectoriesSize.add(this.lblSubdirectoriesSize, BorderLayout.WEST);
		panelSubdirectoriesSize.add(this.spnSubdirectoriesSize, BorderLayout.CENTER);
		
		this.add(panelOutputDirectory);
		this.add(this.chkSplitInSubdirectories);
		this.add(panelSubdirectoriesSize);
		
		this.toggleSubdirectoriesOptions();
		
		this.btnOutputDirectory.addActionListener(event -> changeOutputDirectory());
		
		bindCheckBox(this.chkSplitInSubdirectories, this.model::setSplitInSubdirectories);
		bindSpinner(this.spnSubdirectoriesSize, this.model::setSubdirectorySize);
		
		this.model.addOutputConfigurationModelListener(event -> {
			switch(event.getType()) {
			case OUTPUT_DIRECTORY_CHANGED:
				updateOutputDirectory();
			break;
			case SPLIT_INTO_SUBDIRECTORIES_CHANGED:
				updateSplitIntoSubdirectories();
			break;
			case SUBDIRECTORIES_SIZE_CHANGED:
				updateSubdirectoriesSize();
			break;
			}
		});
	}

	public OutputConfigurationModel getModel() {
		return this.model;
	}
	
	private void changeOutputDirectory() {
		showFileChooserAndProcess(this.fileChooser, this, JFileChooser.DIRECTORIES_ONLY, false,	this.model::setOutputDirectory);
	}

	private void updateOutputDirectory() {
		this.lblOutputDirectory.setText(this.model.getOutputDirectoryPath());
	}

	private void updateSplitIntoSubdirectories() {
		this.chkSplitInSubdirectories.setSelected(this.model.isSplitInSubdirectories());
		this.toggleSubdirectoriesOptions();
	}
	
	private void updateSubdirectoriesSize() {
		this.spnSubdirectoriesSize.setValue(this.model.getSubdirectorySize());
	}

	private void toggleSubdirectoriesOptions() {
		final boolean splitInSubdirectories = this.model.isSplitInSubdirectories();
		
		this.lblSubdirectoriesSize.setEnabled(splitInSubdirectories);
		this.spnSubdirectoriesSize.setEnabled(splitInSubdirectories);
	}
	
}
