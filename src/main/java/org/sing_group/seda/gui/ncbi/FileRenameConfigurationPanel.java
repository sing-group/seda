package org.sing_group.seda.gui.ncbi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;

public class FileRenameConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_POSITION = "seda.filerenameconfiguration.position";
  public static final String PROPERTY_DELIMITER = "seda.filerenameconfiguration.delimiter";

  private static final String HELP_POSITION = "The position where the substitution must be placed.";
  private static final String HELP_DELIMITER = "The delimiter for the substitution.";

  private JComboBox<RenameMode> positionCombo;
  private JXTextField delimiterTextField;

  public FileRenameConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getParametersPanel(), BorderLayout.CENTER);
  }

  private Component getParametersPanel() {
    return new InputParametersPanel(getParameters());
  }

  private InputParameter[] getParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getPositionParameter());
    parameters.add(getDelimiterParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getPositionParameter() {
    this.positionCombo = new JComboBox<>(RenameMode.values());
    this.positionCombo.addItemListener(this::positionChanged);

    return new InputParameter("Position:", this.positionCombo, HELP_POSITION);
  };

  private void positionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.firePropertyChange(PROPERTY_POSITION, null, getPosition());
    }
  }

  public RenameMode getPosition() {
    return (RenameMode) this.positionCombo.getSelectedItem();
  }

  private InputParameter getDelimiterParameter() {
    this.delimiterTextField = new JXTextField("Delimiter");
    this.delimiterTextField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        documentChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        documentChanged();
      }

      private void documentChanged() {
        SwingUtilities.invokeLater(FileRenameConfigurationPanel.this::delimiterChanged);
      }
    });

    return new InputParameter("Delimiter:", this.delimiterTextField, HELP_DELIMITER);
  }

  private void delimiterChanged() {
    this.firePropertyChange(PROPERTY_DELIMITER, null, getDelimiter());
  }

  public String getDelimiter() {
    return this.delimiterTextField.getText();
  }

  public boolean isValidInput() {
    return true;
  }
}
