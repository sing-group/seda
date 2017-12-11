package org.sing_group.seda.gui.grow;

import static org.sing_group.seda.gui.GuiUtils.bindIntegerTextField;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class GrowSequencesConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private GrowSequencesConfigurationModel model;

  private JIntegerTextField minimumOverlappingTf;

  public GrowSequencesConfigurationPanel() {
    this.model = new GrowSequencesConfigurationModel();
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getMainPanel(), BorderLayout.CENTER);
    this.model.addTransformationChangeListener(this::modelChanged);
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new InputParametersPanel(getParameters());

    return new CenteredJPanel(mainPanel);
  }

  private InputParameter[] getParameters() {
    InputParameter[] parameters = new InputParameter[1];
    parameters[0] = getNumberOfSequencesParameter();

    return parameters;
  }

  private InputParameter getNumberOfSequencesParameter() {
    minimumOverlappingTf = new JIntegerTextField(this.model.getMinimumOverlapping());
    minimumOverlappingTf.setColumns(10);
    bindIntegerTextField(minimumOverlappingTf, this.model::setMinimumOverlapping);

    return new InputParameter(
      "Minimum overlapping: ", minimumOverlappingTf, "The minimum overlapping to merge two sequences."
    );
  }

  private void modelChanged(TransformationChangeEvent event) {
    SwingUtilities.invokeLater(
      () -> {
        switch ((GrowSequencesConfigurationChangeType) event.getType()) {
          case MINIMUM_OVERLAPPING_CHANGED:
            updateMinimumOverlapping();
            break;
        }
      }
    );
  }

  private void updateMinimumOverlapping() {
    this.minimumOverlappingTf.setValue(this.model.getMinimumOverlapping());
  }

  public TransformationProvider getModel() {
    return this.model;
  }
}