package org.sing_group.seda.gui;

import javax.swing.JFileChooser;

public class CommonFileChooser {
  private static final JFileChooser DEFAULT_FILECHOOSER = new JFileChooser(".");
  private static CommonFileChooser INSTANCE = null;
  private JFileChooser filechooser;

  private CommonFileChooser() {
    filechooser = DEFAULT_FILECHOOSER;
  }

  private synchronized static void createInstance() {
    if (INSTANCE == null) {
      INSTANCE = new CommonFileChooser();
    }
  }

  public static CommonFileChooser getInstance() {
    if (INSTANCE == null) {
      createInstance();
    }
    return INSTANCE;
  }

  public JFileChooser getFilechooser() {
    return filechooser;
  }

  public void setFilechooser(JFileChooser filechooser) {
    this.filechooser = filechooser;
  }
}
