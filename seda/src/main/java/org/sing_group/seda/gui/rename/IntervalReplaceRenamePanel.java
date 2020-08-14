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
package org.sing_group.seda.gui.rename;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.IntervalReplaceRenamer;

public class IntervalReplaceRenamePanel extends AbstractRenameHeaderPanel {
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
    this.fromStringTextField.setColumns(10);
    this.fromStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("From", this.fromStringTextField, "The starting string of the interval.");
  }

  private InputParameter getToStringParameter() {
    this.toStringTextField = new JXTextField("Delimiter");
    this.toStringTextField.setColumns(10);
    this.toStringTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("To", this.toStringTextField, "The ending string of the interval.");
  }

  private InputParameter getReplacementStringParameter() {
    this.replacementTextField = new JXTextField("Replacement");
    this.replacementTextField.setColumns(10);
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
  public HeaderRenamer getHeaderRenamer(HeaderTarget target) {
    return new IntervalReplaceRenamer(target, getFromString(), getToString(), getReplacement());
  }

  public void setHeaderRenamer(IntervalReplaceRenamer renamer) {
    this.fromStringTextField.setText(renamer.getFrom());
    this.toStringTextField.setText(renamer.getTo());
    this.replacementTextField.setText(renamer.getReplacement());
  }
}
