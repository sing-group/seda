package org.sing_group.seda.gui.concatenate;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ConcatenateSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String HELP_HEADER_TARGET = "<html>The part of the header that must be considered in order to "
    + "detect the sequences to be concatenated.<br/>"
    + "With <b>Name</b> only the sequence identifier is taken.<br/>"
    + "With <b>All</b> the entire header (identifier and description) is considered.</html>";

  private ConcatenateSequencesTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;

  private JXTextField nameTextField;
  private RadioButtonsPanel<HeaderTarget> headerTarget;

  public ConcatenateSequencesConfigurationPanel() {
    this.init();
    this.transformationProvider = new ConcatenateSequencesTransformationProvider(this, this.reformatPanel.getModel());
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
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
    parameters.add(getHeaderTargetParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getMergeNameParameter() {
    this.nameTextField = new JXTextField("Name");
    this.nameTextField.setColumns(20);
    this.nameTextField.getDocument().addDocumentListener(
      new DocumentAdapter() {

        @Override
        public void removeUpdate(DocumentEvent e) {
          nameChanged();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
          nameChanged();
        }
      }
    );

    return new InputParameter("Name:", this.nameTextField, "The name of the merged file.");
  }

  private void nameChanged() {
    this.transformationProvider.nameChanged();
  }

  private InputParameter getHeaderTargetParameter() {
    this.headerTarget = new RadioButtonsPanel<>(Arrays.asList(HeaderTarget.NAME, HeaderTarget.ALL), 1, 0);
    this.headerTarget.addItemListener(this::headerTargetChanged);

    return new InputParameter("Header target: ", this.headerTarget, HELP_HEADER_TARGET);
  }

  private void headerTargetChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.transformationProvider.headerTargetChanged();
    }
  }

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(BorderFactory.createTitledBorder("Reformat output file"));

    return this.reformatPanel;
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }

  public boolean isValidConfiguration() {
    return this.isValidMergeName();
  }

  private boolean isValidMergeName() {
    return getMergeName() != null && !getMergeName().isEmpty();
  }

  public String getMergeName() {
    return this.nameTextField.getText();
  }

  public HeaderTarget getHeaderTarget() {
    return this.headerTarget.getSelectedItem().get();
  }
}
