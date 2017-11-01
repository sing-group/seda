package org.sing_group.seda.gui.ncbi;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.InvalidClassException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListDataEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.event.ListDataAdapter;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.list.ExtendedDefaultListModel;
import org.sing_group.gc4s.list.JParallelListsPanel;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyFields;

public class NcbiTaxonomyConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_DELIMITER = "seda.taxonomyconfiguration.delimiter";
  public static final String PROPERTY_FIELDS = "seda.taxonomyconfiguration.fields";

  private static final String HELP_DELIMITER = "The fields delimiter.";
  private static final String HELP_FIELDS = "The fields to include in the substitution.";

  private JXTextField delimiterTextField;
  private JList<NcbiTaxonomyFields> selectedElementsList;

  public NcbiTaxonomyConfigurationPanel() {
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
    parameters.add(getDelimiterParameter());
    parameters.add(getFieldsParameter());

    return parameters.toArray(new InputParameter[parameters.size()]);
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
        SwingUtilities.invokeLater(NcbiTaxonomyConfigurationPanel.this::delimiterChanged);
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

  private InputParameter getFieldsParameter() {
    JParallelListsPanel<NcbiTaxonomyFields> parallelLists;
    try {
      parallelLists = new JParallelListsPanel<>(
        createLeftList(), createRightList(), "Unselected", "Selected", true, false
      );

      return new InputParameter("Fields:", parallelLists, HELP_FIELDS);
    } catch (InvalidClassException e) {
      throw new RuntimeException();
    }
  }

  private JList<NcbiTaxonomyFields> createLeftList() {
    return createList(Arrays.asList(NcbiTaxonomyFields.values()));
  }

  private JList<NcbiTaxonomyFields> createRightList() {
    selectedElementsList = createList(Collections.emptyList());
    selectedElementsList.getModel().addListDataListener(
      new ListDataAdapter() {

        @Override
        public void intervalRemoved(ListDataEvent e) {
          listChanged();
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
          listChanged();
        }

        private void listChanged() {
          SwingUtilities.invokeLater(NcbiTaxonomyConfigurationPanel.this::selectedElementsListChanged);
        }
      }
    );

    return selectedElementsList;
  }

  private static JList<NcbiTaxonomyFields> createList(List<NcbiTaxonomyFields> data) {
    ExtendedDefaultListModel<NcbiTaxonomyFields> listModel =
      new ExtendedDefaultListModel<NcbiTaxonomyFields>();
    listModel.addElements(data);

    return new JList<NcbiTaxonomyFields>(listModel);
  }

  private void selectedElementsListChanged() {
    this.firePropertyChange(PROPERTY_FIELDS, null, getFields());
  }

  public List<NcbiTaxonomyFields> getFields() {
    List<NcbiTaxonomyFields> selectedFields = new LinkedList<>();
    ListModel<NcbiTaxonomyFields> model = this.selectedElementsList.getModel();

    for (int i = 0; i < model.getSize(); i++) {
      selectedFields.add(model.getElementAt(i));
    }

    return selectedFields;
  }

  public boolean isValidInput() {
    return true;
  }
}
