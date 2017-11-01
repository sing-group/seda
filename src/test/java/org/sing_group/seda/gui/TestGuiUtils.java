package org.sing_group.seda.gui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;

public class TestGuiUtils {

  public static final TransformationChangeListener TRANSFORMATION_CHANGE_LISTENER =
    new TransformationChangeListener() {

      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        System.err.println(
          "Transformation changed: " + event.getType() + ". Is valid? " + event.getProvider().isValidTransformation()
        );
      }
    };

  public static final PropertyChangeListener PROPERTY_CHANGE_LISTENER = new PropertyChangeListener() {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      System.err.println("Property changed:");
      System.err.println("\t- " + evt.getPropertyName());
      System.err.println("\t- " + evt.getNewValue());
    }
  };

  public static final void showComponent(Component component) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(component);
    frame.pack();
    frame.setVisible(true);
  }
}
