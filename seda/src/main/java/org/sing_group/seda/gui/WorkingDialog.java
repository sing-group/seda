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
