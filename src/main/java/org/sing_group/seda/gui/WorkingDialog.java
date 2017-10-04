package org.sing_group.seda.gui;

import java.awt.Container;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.sing_group.gc4s.ui.CenteredJPanel;

public class WorkingDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  public static final ImageIcon ICON_WORKING = new ImageIcon(WorkingDialog.class.getResource("image/working.gif"));

  private String label;

  public WorkingDialog(Frame owner, String title, String label) {
    this(owner, title, label, true);
  }

  public WorkingDialog(Frame owner, String title, String label, boolean modal) {
    super(owner, title, modal);

    this.label = label;

    this.init();
  }

  private void init() {
    this.setContentPane(getPanel());
    this.pack();
    this.setLocationRelativeTo(null);
  }

  private Container getPanel() {
    JPanel panel = new CenteredJPanel(new JLabel(this.label, ICON_WORKING, SwingConstants.LEADING));
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    return panel;
  }
}
