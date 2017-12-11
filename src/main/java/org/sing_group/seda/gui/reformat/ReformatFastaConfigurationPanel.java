package org.sing_group.seda.gui.reformat;

import static org.sing_group.seda.gui.GuiUtils.bindCheckBox;
import static org.sing_group.seda.gui.GuiUtils.bindIntegerTextField;
import static org.sing_group.seda.gui.GuiUtils.bindRadioButtonsPanel;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;

public class ReformatFastaConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private final ReformatFastaConfigurationModel model;

  private JIntegerTextField fragmentLength;
  private JCheckBox removeLineBreaks;
  private RadioButtonsPanel<LineBreakType> lineBreakTypeRbtn;

  public ReformatFastaConfigurationPanel() {
    this.model = new ReformatFastaConfigurationModel();
    this.init();
    this.model.addTransformationChangeListener(this::modelChanged);
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
    parameters[0] = getFragmentLengthParameter();
    parameters[1] = getRemoveLineBreaksParameter();
    parameters[2] = getLineBreakTypeParameter();

    return parameters;
  }

  private InputParameter getFragmentLengthParameter() {
    this.fragmentLength = new JIntegerTextField(this.model.getFragmentLength());
    bindIntegerTextField(this.fragmentLength, this.model::setFragmentLength);

    return new InputParameter(
      "Fragment length:", this.fragmentLength, "The length of the sequence fragments. "
        + "This option is incompatible with the 'Remove line breaks' option."
    );
  }

  private InputParameter getRemoveLineBreaksParameter() {
    this.removeLineBreaks = new JCheckBox("Remove line breaks", this.model.isRemoveLineBreaks());
    bindCheckBox(this.removeLineBreaks, this.model::setRemoveLineBreaks);

    return new InputParameter(
      "", this.removeLineBreaks, "Whether line breaks in sequences must be removed or ot. "
        + "This option is incompatible with the 'Fragment length' option."
    );
  }

  private InputParameter getLineBreakTypeParameter() {
    this.lineBreakTypeRbtn = new RadioButtonsPanel<>(LineBreakType.values(), 1, 0);
    this.lineBreakTypeRbtn.setSelectedItem(this.model.getLineBreakType());
    bindRadioButtonsPanel(this.lineBreakTypeRbtn, this.model::setLineBreakType);

    return new InputParameter("Line breaks: ", this.lineBreakTypeRbtn, "The type of the line breaks");
  }

  private void modelChanged(TransformationChangeEvent event) {
    SwingUtilities.invokeLater(
      () -> {
        switch ((ReformatConfigurationChangeType) event.getType()) {
          case FRAGMENT_LENGTH_CHANGED:
            updateFragmentLength();
            break;
          case REMOVE_LINE_BREAKS_CHANGED:
            updateRemoveLineBreaks();
            break;
          case LINE_BREAK_TYPE_CHANGED:
            updateLineBreakType();
            break;
        }
      }
    );
  }

  private void updateFragmentLength() {
    this.fragmentLength.setValue(this.model.getFragmentLength());
  }

  private void updateRemoveLineBreaks() {
    this.removeLineBreaks.setSelected(this.model.isRemoveLineBreaks());
    this.fragmentLength.setEditable(!this.model.isRemoveLineBreaks());
  }

  private void updateLineBreakType() {
    this.lineBreakTypeRbtn.setSelectedItem(this.model.getLineBreakType());
  }

  public ReformatFastaConfigurationModel getModel() {
    return this.model;
  }
}
