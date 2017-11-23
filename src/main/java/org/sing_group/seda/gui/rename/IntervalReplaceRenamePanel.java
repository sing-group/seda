package org.sing_group.seda.gui.rename;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.IntervalReplaceRenamer;
import org.sing_group.seda.datatype.DatatypeFactory;

public class IntervalReplaceRenamePanel extends AbstractRenamePanel {
  private static final long serialVersionUID = 1L;

  private JXTextField fromStringTextField;
  private JXTextField toStringTextField;
  private JXTextField replacementTextField;

  public IntervalReplaceRenamePanel() {
    this.init();
  }

  private void init() {
    this.add(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] toret = new InputParameter[3];
    toret[0] = getFromStringParameter();
    toret[1] = getToStringParameter();
    toret[2] = getReplacementStringParameter();

    return toret;
  }

  private InputParameter getFromStringParameter() {
    this.fromStringTextField = new JXTextField("From");
    this.fromStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("From", this.fromStringTextField, "The starting string of the interval.");
  }

  private InputParameter getToStringParameter() {
    this.toStringTextField = new JXTextField("Delimiter");
    this.toStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("To", this.toStringTextField, "The ending string of the interval.");
  }

  private InputParameter getReplacementStringParameter() {
    this.replacementTextField = new JXTextField("Replacement");
    this.replacementTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("Replacement", this.replacementTextField, "The interval replacement.");
  }

  private String getFromString() {
    return this.fromStringTextField.getText();
  }

  private String getToString() {
    return this.toStringTextField.getText();
  }

  private String getReplacement() {
    return this.replacementTextField.getText();
  }

  @Override
  public boolean isValidConfiguration() {
    return !getFromString().isEmpty() && !getToString().isEmpty();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory, HeaderTarget target) {
    return new IntervalReplaceRenamer(factory, target, getFromString(), getToString(), getReplacement());
  }
}
