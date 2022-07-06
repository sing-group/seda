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
package org.sing_group.seda.gui.configuration_panel.filtering;

import static javax.swing.SwingUtilities.invokeLater;
import static org.sing_group.seda.gui.GuiUtils.bindCheckBox;
import static org.sing_group.seda.gui.GuiUtils.bindSpinner;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_IN_FRAME_STOP_CODONS_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_IN_FRAME_STOP_CODONS_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCES_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCES_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCE_LENGTH_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MAX_SIZE_DIFFERENCE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MAX_SIZE_DIFFERENCE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCES_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCES_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCE_LENGTH_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_MINIMUM_SEQUENCE_LENGTH_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_NON_MULTIPLE_3_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_NON_MULTIPLE_3_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_REMOVE_SIZE_DIFFERENCE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_REMOVE_SIZE_DIFFERENCE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_SEQUENCE_FILE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_SEQUENCE_FILE_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_SEQUENCE_INDEX_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_SEQUENCE_INDEX_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_STARTING_CODON_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.FilteringSedaPluginInfo.PARAM_STARTING_CODON_HELP_GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.InputParametersPanel.DescriptionAlignment;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.utilities.builder.JButtonBuilder;
import org.sing_group.seda.gui.CommonFileChooser;
import org.sing_group.seda.gui.GuiUtils;
import org.sing_group.seda.gui.configuration_panel.filtering.header.HeaderFilteringConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.provider.filtering.FilteringConfigurationEventType;
import org.sing_group.seda.transformation.provider.filtering.FilteringConfigurationTransformationProvider;

public class FilteringConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_STARTING_CODONS = PARAM_STARTING_CODON_HELP_GUI;
  private static final String HELP_REMOVE_NON_MULTIPLE_3 = PARAM_NON_MULTIPLE_3_HELP_GUI;
  private static final String HELP_REMOVE_SEQUENCES_IN_FRAME_STOP_CODONS = PARAM_IN_FRAME_STOP_CODONS_HELP_GUI;
  private static final String HELP_MINIMUM_SEQUENCE_LENGTH = PARAM_MINIMUM_SEQUENCE_LENGTH_HELP_GUI;
  private static final String HELP_MAXIMUM_SEQUENCE_LENGTH = PARAM_MAXIMUM_SEQUENCE_LENGTH_HELP_GUI;
  private static final String HELP_MINIMUM_NUMBER_OF_SEQUENCES = PARAM_MINIMUM_SEQUENCES_HELP_GUI;
  private static final String HELP_MAXIMUM_NUMBER_OF_SEQUENCES = PARAM_MAXIMUM_SEQUENCES_HELP_GUI;
  private static final String HELP_REMOVE_SIZE_DIFFERENCE = PARAM_REMOVE_SIZE_DIFFERENCE_HELP_GUI;
  private static final String HELP_MAX_SIZE_DIFFERENCE = PARAM_MAX_SIZE_DIFFERENCE_HELP_GUI;
  private static final String HELP_REFERENCE_SEQUENCE_INDEX = PARAM_SEQUENCE_INDEX_HELP_GUI;
  private static final String HELP_REFERENCE_SEQUENCE_FILE = PARAM_SEQUENCE_FILE_HELP_GUI;
  private static final String HELP_HEADER_COUNT_FILTERING =
    "<html>Filters sequences or files so that only those "
      + "meeting the specified criteria regarding counts on their headers are kept."
      + "<br/>Click the <i>Use this filter</i> button in order to show the corresponding configuration panel.<br/>"
      + "For instance, if you want to kep only those sequences with unique sequence names (or identifiers), you should "
      + "use the following configuration:<ul>"
      + "<li>Mode: keep.</li> <li>Level: sequence.</li> <li>Range: (1, 1). <li>Filter type: Sequence name.</li></li>"
      + "</ul></html>";

  private FilteringConfigurationTransformationProvider model;
  private TransformationChangeListener transformationChangeListener;

  private JXTaskPane translationConfigurationTaskPane;
  private JCheckBox chkRemoveNonMultipleOfThree;
  private JCheckBox chkRemoveIfInFrameStopCodon;
  private JCheckBox chkRemoveBySizeDifference;
  private JSpinner spnSizeDifference;
  private JSpinner spnReferenceIndex;
  private JSpinner spnMinNumberOfSequences;
  private JSpinner spnMaxNumberOfSequences;
  private JSpinner spnMinSequenceLength;
  private JSpinner spnMaxSequenceLength;
  private JFileChooserPanel referenceIndexFile;
  private JButton clearReferenceIndexFileButton;

  private Map<String, JCheckBox> codonToChk = new HashMap<>();
  private JButton btnUnselectCodons;
  private JButton btnSelectCodons;

  private HeaderFilteringConfigurationPanel headerFilteringParametersPanel;

  public FilteringConfigurationPanel() {
    this.add(new CenteredJPanel(getParametersPanel()));
  }

  private Component getParametersPanel() {
    this.model = new FilteringConfigurationTransformationProvider();

    InputParametersPanel parametersPanel = new InputParametersPanel(DescriptionAlignment.RIGHT, getParameters());

    this.toggleSizeDifferenceControls();

    this.bindGuiControls();

    this.referenceIndexFile.addFileChooserListener(f -> {
      referenceIndexFileChanged();
    });

    this.transformationChangeListener =
      event -> {
        switch ((FilteringConfigurationEventType) event.getType()) {
          case STARTING_CODON_ADDED:
          case STARTING_CODON_REMOVED:
            updateStartingCodons((String) event.getNewValue());
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
            updateSizeDifference();
            break;
          case REFERENCE_INDEX_CHANGED:
            updateReferenceIndex();
            break;
          case REFERENCE_FILE_CHANGED:
            updateReferenceFile();
            break;
          case MIN_NUM_OF_SEQUENCES_CHANGED:
            updateMinNumberOfSequences();
            break;
          case MAX_NUM_OF_SEQUENCES_CHANGED:
            updateMaxNumberOfSequences();
            break;
          case MIN_SEQUENCE_LENGTH_CHANGED:
            updateMinSequenceLength();
            break;
          case MAX_SEQUENCE_LENGTH_CHANGED:
            updateMaxSequenceLength();
            break;
          case HEADER_FILTERING_CONFIGURATION_CHANGED:
            updateHeaderFilteringConfiguration();
            break;
        }
      };

    this.model.addTransformationChangeListener(this.transformationChangeListener);

    return parametersPanel;
  }

  private void bindGuiControls() {
    bindCheckBox(this.chkRemoveNonMultipleOfThree, model::setRemoveNonMultipleOfThree);
    bindCheckBox(this.chkRemoveIfInFrameStopCodon, model::setRemoveIfInFrameStopCodon);
    bindCheckBox(this.chkRemoveBySizeDifference, model::setRemoveBySizeDifference);

    bindSpinner(this.spnSizeDifference, model::setSizeDifference);
    bindSpinner(this.spnReferenceIndex, model::setReferenceIndex);
    bindSpinner(this.spnMinNumberOfSequences, model::setMinNumOfSequences);
    bindSpinner(this.spnMaxNumberOfSequences, model::setMaxNumOfSequences);
    bindSpinner(this.spnMinSequenceLength, model::setMinSequenceLength);
    bindSpinner(this.spnMaxSequenceLength, model::setMaxSequenceLength);
  }

  private void referenceIndexFileChanged() {
    if (this.referenceIndexFile.getSelectedFile() == null) {
      model.clearReferenceFile();
    } else {
      model.setReferenceFile(this.referenceIndexFile.getSelectedFile());
      boolean validReferenceFile = model.isValidReferenceFile();
      if (!validReferenceFile && this.referenceIndexFile.getSelectedFile() != null) {
        JOptionPane.showMessageDialog(
          this,
          "Warning: the selected reference file is not valid. Please, select a different one.",
          "Invalid reference sequence file",
          JOptionPane.WARNING_MESSAGE
        );
        SwingUtilities.invokeLater(
          () -> {
            clearReferenceIndexFile();
          }
        );
      }
    }
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getValidStartingCodonsParameter());
    parameters.add(getRemoveNonMultipleOfThreeSequencesParameter());
    parameters.add(getRemoveSequencesWithInFrameStopCodonsParameter());
    parameters.add(getMinimumSequenceLengthParameter());
    parameters.add(getMaximumSequenceLengthParameter());
    parameters.add(getMinimumNumberOfSequencesParameter());
    parameters.add(getMaximumNumberOfSequencesParameter());
    parameters.add(getRemoveBySizeDifferenceParameter());
    parameters.add(getMaximumSizeDiferenceParameter());
    parameters.add(getReferenceSequenceIndexParameter());
    parameters.add(getReferenceSequenceFileParameter());
    parameters.add(getHeaderFilteringConfigurationPanel());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getValidStartingCodonsParameter() {
    final JXTaskPaneContainer customOptionsTaskPaneContainer = new JXTaskPaneContainer();
    customOptionsTaskPaneContainer.setOpaque(false);
    customOptionsTaskPaneContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    translationConfigurationTaskPane = new JXTaskPane();
    translationConfigurationTaskPane.setTitle(PARAM_STARTING_CODON_DESCRIPTION);
    translationConfigurationTaskPane.add(getValidStartingCodonsPanel());
    translationConfigurationTaskPane.setCollapsed(true);
    customOptionsTaskPaneContainer.add(translationConfigurationTaskPane);

    return new InputParameter("Valid starting codons:", customOptionsTaskPaneContainer, HELP_STARTING_CODONS);
  }

  private JPanel getValidStartingCodonsPanel() {
    JPanel validStartingCodons = new JPanel(new GridLayout(16, 4));
    final char[] nucleotides = new char[] {
      'A', 'C', 'T', 'G'
    };
    for (char first : nucleotides) {
      for (char second : nucleotides) {
        for (char third : nucleotides) {
          final String codon =
            new String(
              new char[] {
                first, second, third
              }
            );

          final JCheckBox chkCodon = new JCheckBox(codon, this.model.hasStartingCodon(codon));
          this.codonToChk.put(codon, chkCodon);

          validStartingCodons.add(chkCodon);
        }
      }
    }

    this.btnSelectCodons = new JButton("Select all codons");
    this.btnUnselectCodons = new JButton("Unselect all codons");
    JPanel buttons = new JPanel(new FlowLayout());
    buttons.add(this.btnSelectCodons);
    buttons.add(this.btnUnselectCodons);

    JPanel validStartingCodonsPanel = new JPanel(new BorderLayout());
    validStartingCodonsPanel.add(validStartingCodons, BorderLayout.CENTER);
    validStartingCodonsPanel.add(buttons, BorderLayout.NORTH);

    this.codonToChk.entrySet().forEach(
      entry -> {
        final String codon = entry.getKey();
        final JCheckBox chkCodon = entry.getValue();

        chkCodon.addItemListener(
          event -> {
            if (chkCodon.isSelected())
              this.model.addStartingCodon(codon);
            else
              this.model.removeStartingCodon(codon);
          }
        );
      }
    );

    this.btnSelectCodons.addActionListener(event -> this.codonToChk.keySet().forEach(this.model::addStartingCodon));
    this.btnUnselectCodons
      .addActionListener(event -> this.codonToChk.keySet().forEach(this.model::removeStartingCodon));

    return validStartingCodonsPanel;
  }

  private InputParameter getRemoveNonMultipleOfThreeSequencesParameter() {
    this.chkRemoveNonMultipleOfThree = new JCheckBox();
    this.chkRemoveNonMultipleOfThree.setSelected(this.model.isRemoveNonMultipleOfThree());

    return new InputParameter(
      PARAM_NON_MULTIPLE_3_DESCRIPTION + ":",
      this.chkRemoveNonMultipleOfThree, HELP_REMOVE_NON_MULTIPLE_3
    );
  }

  private InputParameter getRemoveSequencesWithInFrameStopCodonsParameter() {
    this.chkRemoveIfInFrameStopCodon = new JCheckBox();
    this.chkRemoveIfInFrameStopCodon.setSelected(this.model.isRemoveIfInFrameStopCodon());

    return new InputParameter(
      PARAM_IN_FRAME_STOP_CODONS_DESCRIPTION + ":",
      this.chkRemoveIfInFrameStopCodon, HELP_REMOVE_SEQUENCES_IN_FRAME_STOP_CODONS
    );
  }

  private InputParameter getMinimumSequenceLengthParameter() {
    this.spnMinSequenceLength =
      new JSpinner(
        new SpinnerNumberModel(this.model.getMinSequenceLength(), 0, Integer.MAX_VALUE, 1)
      );

    return new InputParameter(
      PARAM_MINIMUM_SEQUENCE_LENGTH_DESCRIPTION + ":", this.spnMinSequenceLength, HELP_MINIMUM_SEQUENCE_LENGTH
    );
  }

  private InputParameter getMaximumSequenceLengthParameter() {
    this.spnMaxSequenceLength =
      new JSpinner(
        new SpinnerNumberModel(this.model.getMaxSequenceLength(), 0, Integer.MAX_VALUE, 1)
      );

    return new InputParameter(
      PARAM_MAXIMUM_SEQUENCE_LENGTH_DESCRIPTION + ":", this.spnMaxSequenceLength, HELP_MAXIMUM_SEQUENCE_LENGTH
    );
  }

  private InputParameter getMinimumNumberOfSequencesParameter() {
    this.spnMinNumberOfSequences =
      new JSpinner(
        new SpinnerNumberModel(this.model.getMinNumOfSequences(), 0, Integer.MAX_VALUE, 1)
      );

    return new InputParameter(
      PARAM_MINIMUM_SEQUENCES_DESCRIPTION + ":", this.spnMinNumberOfSequences, HELP_MINIMUM_NUMBER_OF_SEQUENCES
    );
  }

  private InputParameter getMaximumNumberOfSequencesParameter() {
    this.spnMaxNumberOfSequences =
      new JSpinner(
        new SpinnerNumberModel(this.model.getMaxNumOfSequences(), 0, Integer.MAX_VALUE, 1)
      );

    return new InputParameter(
      PARAM_MAXIMUM_SEQUENCES_DESCRIPTION + ":", this.spnMaxNumberOfSequences, HELP_MAXIMUM_NUMBER_OF_SEQUENCES
    );
  }

  private InputParameter getRemoveBySizeDifferenceParameter() {
    this.chkRemoveBySizeDifference = new JCheckBox();
    this.chkRemoveBySizeDifference.setSelected(this.model.isRemoveBySizeDifference());

    return new InputParameter(
      PARAM_REMOVE_SIZE_DIFFERENCE_DESCRIPTION + ":", this.chkRemoveBySizeDifference, HELP_REMOVE_SIZE_DIFFERENCE
    );
  }

  private InputParameter getMaximumSizeDiferenceParameter() {
    this.spnSizeDifference =
      new JSpinner(
        new SpinnerNumberModel(this.model.getSizeDifference(), 0, Integer.MAX_VALUE, 1)
      );

    return new InputParameter(
      PARAM_MAX_SIZE_DIFFERENCE_DESCRIPTION + ":", this.spnSizeDifference, HELP_MAX_SIZE_DIFFERENCE
    );
  }

  private InputParameter getReferenceSequenceIndexParameter() {
    this.spnReferenceIndex =
      new JSpinner(
        new SpinnerNumberModel(this.model.getReferenceIndex(), 1, Integer.MAX_VALUE, 1)
      );

    return new InputParameter(
      PARAM_SEQUENCE_INDEX_DESCRIPTION + ":", this.spnReferenceIndex, HELP_REFERENCE_SEQUENCE_INDEX
    );
  }

  private InputParameter getReferenceSequenceFileParameter() {
    this.referenceIndexFile =
      JFileChooserPanelBuilder.createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser())
        .withLabel("")
        .build();
    this.referenceIndexFile.setClearSelectedFileActionEnabled(false);

    this.clearReferenceIndexFileButton =
      JButtonBuilder.newJButtonBuilder()
        .withIcon(Icons.ICON_TRASH_16)
        .withTooltip("Clears the selected reference sequence file.")
        .thatDoes(new AbstractAction() {
          private static final long serialVersionUID = 1L;

          @Override
          public void actionPerformed(ActionEvent e) {
            clearReferenceIndexFile();
          }
        }).build();

    JPanel referenceIndexFilePanel = new JPanel();
    referenceIndexFilePanel.setLayout(new BoxLayout(referenceIndexFilePanel, BoxLayout.X_AXIS));
    referenceIndexFilePanel.add(this.referenceIndexFile);
    referenceIndexFilePanel.add(Box.createHorizontalStrut(5));
    referenceIndexFilePanel.add(this.clearReferenceIndexFileButton);

    return new InputParameter(
      PARAM_SEQUENCE_FILE_DESCRIPTION + ":", referenceIndexFilePanel, HELP_REFERENCE_SEQUENCE_FILE
    );
  }

  private void clearReferenceIndexFile() {
    this.referenceIndexFile.clearSelectedFile();
  }

  private InputParameter getHeaderFilteringConfigurationPanel() {
    this.headerFilteringParametersPanel = new HeaderFilteringConfigurationPanel();
    this.headerFilteringParametersPanel.addPropertyChangeListener(
      HeaderFilteringConfigurationPanel.PROPERTY_FILTER_CONFIGURATION, new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          model.setHeaderFilteringConfiguration(headerFilteringParametersPanel.getHeaderFilteringConfiguration());
        }
      }
    );

    return new InputParameter(
      "Header count filtering:", this.headerFilteringParametersPanel, HELP_HEADER_COUNT_FILTERING
    );
  }

  public FilteringConfigurationTransformationProvider getTransformationProvider() {
    return model;
  }

  public void setTransformationProvider(FilteringConfigurationTransformationProvider model) {
    this.model.removeTranformationChangeListener(this.transformationChangeListener);
    this.model = model;

    this.codonToChk.keySet().forEach(this::updateStartingCodons);
    updateRemoveNonMultipleOfThree();
    updateRemoveIfInFrameStopCodon();
    updateRemoveBySizeDifference();
    updateSizeDifference();
    updateReferenceIndex();
    updateReferenceFile();
    updateMinNumberOfSequences();
    updateMaxNumberOfSequences();
    updateMinSequenceLength();
    updateMaxSequenceLength();
    updateHeaderFilteringConfiguration();

    invokeLater(() -> {
      this.translationConfigurationTaskPane.setCollapsed(!model.getStartingCodons().findFirst().isPresent());
    });

    this.bindGuiControls();

    this.model.addTransformationChangeListener(this.transformationChangeListener);
  }

  public void updateStartingCodons(String codon) {
    this.codonToChk.get(codon).setSelected(this.model.hasStartingCodon(codon));
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

  public void updateReferenceFile() {
    if (this.model.getReferenceFile().isPresent()) {
      this.referenceIndexFile.setSelectedFile(this.model.getReferenceFile().get());
    } else {
      clearReferenceIndexFile();
    }
  }

  public void updateHeaderFilteringConfiguration() {
    this.headerFilteringParametersPanel.setHeaderFilteringConfiguration(this.model.getHeaderFilteringConfiguration());
  }

  public void updateMinNumberOfSequences() {
    this.spnMinNumberOfSequences.setValue(this.model.getMinNumOfSequences());
  }

  public void updateMaxNumberOfSequences() {
    this.spnMaxNumberOfSequences.setValue(this.model.getMaxNumOfSequences());
  }

  public void updateMinSequenceLength() {
    this.spnMinSequenceLength.setValue(this.model.getMinSequenceLength());
    this.checkSequenceLengthConfiguration();
  }

  public void updateMaxSequenceLength() {
    this.spnMaxSequenceLength.setValue(this.model.getMaxSequenceLength());
    this.checkSequenceLengthConfiguration();
  }

  private void checkSequenceLengthConfiguration() {
    boolean valid = this.model.isValidSequenceLengthConfiguration();
    Color background = valid ? Color.WHITE : GuiUtils.COLOR_ERROR;
    this.spnMaxSequenceLength.getEditor().getComponent(0).setBackground(background);
    this.spnMinSequenceLength.getEditor().getComponent(0).setBackground(background);
  }

  private void toggleSizeDifferenceControls() {
    final boolean enabled = this.model.isRemoveBySizeDifference();

    this.spnSizeDifference.setEnabled(enabled);
    this.spnReferenceIndex.setEnabled(enabled);
    this.referenceIndexFile.getBrowseAction().setEnabled(enabled);
    this.clearReferenceIndexFileButton.setEnabled(enabled);
  }
}
