package org.sing_group.seda.gui;

import java.awt.Component;
import java.awt.Window;

import org.sing_group.gc4s.dialog.AbstractInputJDialog;

public abstract class AbstractVisualizationDialog extends AbstractInputJDialog {
  private static final long serialVersionUID = 1L;

  protected AbstractVisualizationDialog(Window parent) {
    super(parent);
    this.configureDialog();
  }

  private void configureDialog() {
    this.setResizable(true);
    this.getDescriptionPane().setVisible(false);
    this.cancelButton.setVisible(false);
    this.okButton.setEnabled(true);
  }

  @Override
  protected String getDescription() {
    return "";
  }

  @Override
  public void setVisible(boolean b) {
    this.pack();
    super.setVisible(b);
  }
  
  public static void visualize(Window parent, Component component, String title) {
    new AbstractVisualizationDialog(parent) {
      private static final long serialVersionUID = 1L;

      @Override
      protected String getDialogTitle() {
        return title;
      }

      @Override
      protected Component getInputComponentsPane() {
        return component;
      }
    }.setVisible(true);
  }
}
