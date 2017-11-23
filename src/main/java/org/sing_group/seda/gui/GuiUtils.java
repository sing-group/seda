package org.sing_group.seda.gui;

import static java.util.Arrays.stream;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.DefaultFormatter;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.text.JIntegerTextField;

public final class GuiUtils {
  public static final Color COLOR_ERROR = Color.decode("#FF9494");

  private GuiUtils() {}

  public static <T> void bindRadioButtonsPanel(RadioButtonsPanel<T> chk, Consumer<T> setter) {
    chk.addItemListener(
      event -> {
        if (chk.getSelectedItem().isPresent()) {
          setter.accept(chk.getSelectedItem().get());
        }
      }
    );
  }

  public static void bindCheckBox(JCheckBox chk, Consumer<Boolean> setter) {
    chk.addItemListener(event -> setter.accept(chk.isSelected()));
  }

  public static void bindToggleButton(JToggleButton toggleButton, Consumer<Boolean> setter) {
    toggleButton.addItemListener(event -> setter.accept(toggleButton.isSelected()));
  }

  public static void bindIntegerTextField(JIntegerTextField textField, IntConsumer setter) {
    textField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateValue();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        updateValue();
      }

      private void updateValue() {
        setter.accept(textField.getValue());
      }
    });
  }

  public static void bindSpinner(JSpinner spn, IntConsumer setter) {
    final NumberEditor editor = (NumberEditor) spn.getEditor();
    final JFormattedTextField txtField = editor.getTextField();
    final DefaultFormatter formatter = (DefaultFormatter) txtField.getFormatter();
    formatter.setCommitsOnValidEdit(true);

    spn.addChangeListener(event -> setter.accept((Integer) spn.getValue()));
  }

  public static void showFileChooserAndProcess(
    JFileChooser fileChooser, Component parent, int selectionMode, int dialogMode, boolean multipleSelection,
    Consumer<Path> pathProcessor
  ) {
    fileChooser.setFileSelectionMode(selectionMode);
    fileChooser.setMultiSelectionEnabled(multipleSelection);

    int option = showFileChooser(fileChooser, dialogMode, parent);
    if (option == JFileChooser.APPROVE_OPTION) {
      if (multipleSelection) {
        stream(fileChooser.getSelectedFiles())
          .map(File::toPath)
        .forEach(pathProcessor);
      } else {
        pathProcessor.accept(fileChooser.getSelectedFile().toPath());
      }
    }
  }

  private static int showFileChooser(JFileChooser fileChooser, int dialogMode, Component parent) {
    if (dialogMode == JFileChooser.SAVE_DIALOG) {
      return fileChooser.showSaveDialog(parent);
    } else {
      return fileChooser.showOpenDialog(parent);
    }
  }

  public static void configureUI() {
    UIManager.put("ToolTip.font", ((Font) UIManager.get("ToolTip.font")).deriveFont(14f));
  }
}
