package org.sing_group.seda.gui.merge;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationPanel;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class MergeConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private JXTextField nameTextField;
  private MergeTransformationProvider transformationProvider;
  private ReformatFastaConfigurationPanel reformatPanel;

  public MergeConfigurationPanel() {
    this.init();
    this.transformationProvider = new MergeTransformationProvider(this.reformatPanel.getModel());
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

  private JPanel getParametersPanel() {
    return new InputParametersPanel(getInputParameters());
  }

  private InputParameter getInputParameters() {
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

  private Component getReformatFastaConfigurationPanel() {
    this.reformatPanel = new ReformatFastaConfigurationPanel();
    this.reformatPanel.setBorder(BorderFactory.createTitledBorder("Reformat output file"));

    return this.reformatPanel;
  }

  private void nameChanged() {
    this.transformationProvider.setName(this.nameTextField.getText());
  }

  public TransformationProvider getModel() {
    return this.transformationProvider;
  }
}
