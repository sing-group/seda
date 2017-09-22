package org.sing_group.seda.gui.sort;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class SortConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private final SortConfigurationModel sortModel;

  private RadioButtonsPanel<SequenceTarget> sequenceTargetRbtnPanel;
  private RadioButtonsPanel<SequenceComparator> sortCriteriaRbtnPanel;
  private JCheckBox descendingSort;

  public SortConfigurationPanel() {
    this.init();
    this.sortModel = new SortConfigurationModel(sequenceTargetRbtnPanel, descendingSort, sortCriteriaRbtnPanel);
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new InputParametersPanel(getParameters());

    return new CenteredJPanel(mainPanel);
  }

  private InputParameter[] getParameters() {
    InputParameter[] parameters = new InputParameter[3];
    parameters[0] = getSequenceTargetParameter();
    parameters[1] = getSortModeParameter();
    parameters[2] = getSortCriteriaParameter();

    return parameters;
  }

  private InputParameter getSequenceTargetParameter() {
    sequenceTargetRbtnPanel = new RadioButtonsPanel<>(SequenceTarget.values(), 1, SequenceTarget.values().length);

    return new InputParameter(
      "Sort on:", sequenceTargetRbtnPanel, "Whether sort must be applied on sequence headers or sequences themselves."
    );
  }

  private InputParameter getSortModeParameter() {
    descendingSort = new JCheckBox("Descending", false);

    return new InputParameter("", descendingSort, "Use this option to sort in descending order.");
  }

  private InputParameter getSortCriteriaParameter() {
    sortCriteriaRbtnPanel = new RadioButtonsPanel<>(SequenceComparator.values(), 1, SequenceComparator.values().length);

    return new InputParameter("Criteria:", sortCriteriaRbtnPanel, "The sort criteria to be applied.");
  }

  public TransformationProvider getModel() {
    return this.sortModel;
  }
}
