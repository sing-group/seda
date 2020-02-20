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
package org.sing_group.seda.gui.concatenate;

import static java.awt.BorderLayout.CENTER;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.SwingUtilities.invokeLater;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.filtering.HeaderFilteringConfiguration.FilterType;
import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.filtering.SequenceNameHeaderMatcher;
import org.sing_group.seda.gui.filtering.header.RegexHeaderMatcherConfigurationPanel;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;

public class ConcatenateSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String HELP_FILTER_TYPE = "<html><i><b>Sequence name</b></i> means that the sequences are "
    + "concatenated if they have the same sequence names (identifiers).<br/><i><b>Regular expression</b></i> means "
    + "sequences are concatenade by matching headers using the configuration specified below.</html>";

  private static final String HELP_REGEX_MATCHER = "The regular expression configuration to match the sequence headers "
      + "that must be concatenated. Check the manual for examples of regular expressions.";

  private static final Optional<HeaderMatcher> SEQUENCE_NAME_MATCHER = of(new SequenceNameHeaderMatcher());

  private ConcatenateSequencesTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;

  private JXTextField nameTextField;
  private RadioButtonsPanel<FilterType> filterTypeRbtn;
  private RegexHeaderMatcherConfigurationPanel regexHeaderMatcherConfigurationPanel;

  public ConcatenateSequencesConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, Y_AXIS));
    mainPanel.add(getParametersPanel());
    mainPanel.add(getReformatFastaConfigurationPanel());

    return new CenteredJPanel(mainPanel);
  }

  private Component getParametersPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getMergeNameParameter());
    parameters.add(getFilterTypeParameter());
    parameters.add(getStringParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getMergeNameParameter() {
    this.nameTextField = new JXTextField("Name");
    this.nameTextField.setColumns(20);
    this.nameTextField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        nameChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        nameChanged();
      }
    });

    return new InputParameter("Name:", this.nameTextField, "The name of the merged file.");
  }

  private void nameChanged() {
    this.transformationProvider.setMergeName(this.nameTextField.getText());
  }

  private InputParameter getFilterTypeParameter() {
    this.filterTypeRbtn = new RadioButtonsPanel<>(FilterType.values(), 1, 0);
    this.filterTypeRbtn.addItemListener(this::filterTypeChanged);

    return new InputParameter("Sequence matching mode: ", this.filterTypeRbtn, HELP_FILTER_TYPE);
  }

  private void filterTypeChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.checkStringComponent();
      this.regularExpressionChanged();
    }
  }

  private InputParameter getStringParameter() {
    this.regexHeaderMatcherConfigurationPanel = new RegexHeaderMatcherConfigurationPanel();
    this.regexHeaderMatcherConfigurationPanel.setBorder(createTitledBorder("Header matcher configuration"));
    this.checkStringComponent();
    this.regexHeaderMatcherConfigurationPanel.addPropertyChangeListener(new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (RegexHeaderMatcherConfigurationPanel.PROPERTIES.contains(evt.getPropertyName())) {
          regularExpressionChanged();
        }
      }
    });

    return new InputParameter("", this.regexHeaderMatcherConfigurationPanel, HELP_REGEX_MATCHER);
  }

  protected void regularExpressionChanged() {
    Optional<HeaderMatcher> headerMatcher = this.getHeaderMatcher();
    if (headerMatcher.isPresent()) {
      this.transformationProvider.setHeaderMatcher(headerMatcher.get());
    } else {
      this.transformationProvider.removeHeaderMatcher();
    }
  }

  private void checkStringComponent() {
    invokeLater(() -> {
      boolean enabled = getFilterType().equals(FilterType.REGEX);
      this.regexHeaderMatcherConfigurationPanel.setInputControlsEnabled(enabled);
    });
  }

  private Optional<HeaderMatcher> getHeaderMatcher() {
    if (this.filterTypeRbtn.getSelectedItem().get().equals(FilterType.SEQUENCE_NAME)) {
      return SEQUENCE_NAME_MATCHER;
    } else {
      return ofNullable(this.regexHeaderMatcherConfigurationPanel.getRegexHeaderMatcher().orElse(null));
    }
  }

  private FilterType getFilterType() {
    return this.filterTypeRbtn.getSelectedItem().get();
  }

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(createTitledBorder("Reformat output file"));

    return this.reformatPanel;
  }

  private void initTransformationProvider() {
    this.transformationProvider = new ConcatenateSequencesTransformationProvider();
    this.transformationProvider.setReformatFastaTransformationProvider(this.reformatPanel.getTransformationProvider());
    this.transformationProvider.setHeaderMatcher(SEQUENCE_NAME_MATCHER.get());
  }

  public ConcatenateSequencesTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setTransformationProvider(ConcatenateSequencesTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;

    this.nameTextField.setText(this.transformationProvider.getMergeName());
    if (this.transformationProvider.getHeaderMatcher() instanceof SequenceNameHeaderMatcher) {
      this.filterTypeRbtn.setSelectedItem(FilterType.SEQUENCE_NAME);
    } else {
      this.regexHeaderMatcherConfigurationPanel
        .setRegexHeaderMatcher((RegexHeaderMatcher) this.transformationProvider.getHeaderMatcher());
      this.filterTypeRbtn.setSelectedItem(FilterType.REGEX);
    }
    this.reformatPanel.setTransformationProvider(this.transformationProvider.getReformatFastaTransformationProvider());
  }
}
