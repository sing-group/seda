package org.sing_group.seda.gui;

import java.awt.Component;

import javax.swing.JFrame;

public class TestGuiUtils {

  public static final void showComponent(Component component) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(component);
    frame.pack();
    frame.setVisible(true);
  }
}
