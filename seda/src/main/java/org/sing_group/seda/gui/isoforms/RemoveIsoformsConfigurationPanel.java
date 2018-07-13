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
package org.sing_group.seda.gui.isoforms;

import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.gui.filtering.header.RegexHeaderMatcherConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class RemoveIsoformsConfigurationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final String DESCRIPTION_MINIMUM_WORD_LENGTH = "The minimum length of word to consider that two "
			+ "sequences are isoforms.";
	private static final String DESCRIPTION_ADD_REMOVED_ISOFORM_NAMES = "<html>Whether the removed isoform names should "
			+ "be added to the headers of the selected ones. <br/>This allows an easy identification of those sequences that "
			+ "had isoforms in the output files.</html>";
	private static final String DESCRIPTION_ISOFORM_SELECTION_CRITERIA = "The configuration of the criteria to select "
			+ "which isoform should go to the output file.";
	private static final String DESCRIPTION_HEADER_MATCHER = "<html>This option allows to specify whether sequences must "
			+ "be grouped before the identification of the isoforms. <br/>Leave it empty if isoforms must be removed at a "
			+ "file level.<br/>In contrast, if you want to make groups of sequences before the identification of the "
			+ "isoforms, here it is possible to configure <br/>how sequence headers must be matched in order to group sequences. "
			+ "Check the manual for examples.</html>";

	private JIntegerTextField minimumWordLenthTf;
	private JCheckBox addRemovedIsoformNamesCheckBox;
	private DefaultSequenceIsoformConfigurationPanel isoformSelectorPanel;
	private RegexHeaderMatcherConfigurationPanel headerMatcherPanel;
	private RemoveIsoformsTransformationProvider transformationProvider;

  public RemoveIsoformsConfigurationPanel() {
  	this.transformationProvider = new RemoveIsoformsTransformationProvider();
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(getParametersPanel(), BorderLayout.CENTER);

    return new CenteredJPanel(mainPanel);
  }

  private JPanel getParametersPanel() {
    return new InputParametersPanel(getInputParameters());
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getMinimumWordLengthParameter());
    parameters.add(getAddRemovedIsoformNamesParameter());
    parameters.add(getIsoformSelectorParameter());
    parameters.add(getHeaderMatcherParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

	private InputParameter getMinimumWordLengthParameter() {
		this.minimumWordLenthTf = new JIntegerTextField(250);
		this.minimumWordLenthTf.getDocument().addDocumentListener(new DocumentAdapter() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				minimumWordChanged();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				minimumWordChanged();
			}
		});

		return new InputParameter("Minimum word length: ", this.minimumWordLenthTf, DESCRIPTION_MINIMUM_WORD_LENGTH);
	}

	private void minimumWordChanged() {
		this.transformationProvider.setMinimumWordLength(this.minimumWordLenthTf.getValue());
	}

	private InputParameter getAddRemovedIsoformNamesParameter() {
		this.addRemovedIsoformNamesCheckBox = new JCheckBox("Add removed isoform names to headers?");
		this.addRemovedIsoformNamesCheckBox.addItemListener(this::addRemovedIsoformNamesChanged);

		return new InputParameter("", this.addRemovedIsoformNamesCheckBox, DESCRIPTION_ADD_REMOVED_ISOFORM_NAMES);
	}

	private void addRemovedIsoformNamesChanged(ItemEvent event) {
		this.transformationProvider.setAddRemovedIsoformNames(this.addRemovedIsoformNamesCheckBox.isSelected());
	}

	private InputParameter getIsoformSelectorParameter() {
		this.isoformSelectorPanel = new DefaultSequenceIsoformConfigurationPanel();
		this.isoformSelectorPanel.setBorder(createTitledBorder("Isoform selection criteria"));
		this.transformationProvider.setIsoformSelector(this.isoformSelectorPanel.getSelector());
		this.isoformSelectorPanel.addPropertyChangeListener(this::isoformSelectorChanged);

		return new InputParameter("", isoformSelectorPanel, DESCRIPTION_ISOFORM_SELECTION_CRITERIA);
	}

	private void isoformSelectorChanged(PropertyChangeEvent event) {
		if (DefaultSequenceIsoformConfigurationPanel.PROPERTIES.contains(event.getPropertyName())) {
			this.transformationProvider.setIsoformSelector(this.isoformSelectorPanel.getSelector());
		}
	}

	private InputParameter getHeaderMatcherParameter() {
		this.headerMatcherPanel = new RegexHeaderMatcherConfigurationPanel();
		this.headerMatcherPanel.setBorder(createTitledBorder("Header matcher configuration"));
		this.headerMatcherPanel.addPropertyChangeListener(this::headerMatcherChanged);

		return new InputParameter("", headerMatcherPanel, DESCRIPTION_HEADER_MATCHER);
	}

	private void headerMatcherChanged(PropertyChangeEvent event) {
		if (RegexHeaderMatcherConfigurationPanel.PROPERTIES.contains(event.getPropertyName())) {
			Optional<RegexHeaderMatcher> headerMatcher = this.headerMatcherPanel.getRegexHeaderMatcher();
			if (headerMatcher.isPresent()) {
				this.transformationProvider.setHeaderMatcher(headerMatcher.get());
			}
		}
	}

	public TransformationProvider getModel() {
		return this.transformationProvider;
	}
}

