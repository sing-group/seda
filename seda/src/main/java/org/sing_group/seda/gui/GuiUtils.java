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

import static java.util.Arrays.stream;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultFormatter;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.input.text.JIntegerTextField;

public final class GuiUtils {
  public static final String PROPERTY_OUTPUT_DIRECTORY = "seda.output.directory";
  public static final String PROPERTY_INPUT_DIRECTORY = "seda.input.directory";
  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION = "seda.local.execution.enabled";

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
    List<FileFilter> fileFilters, Consumer<Path> pathProcessor
  ) {
    fileChooser.setFileSelectionMode(selectionMode);
    fileChooser.setMultiSelectionEnabled(multipleSelection);
    fileChooser.setSelectedFile(new File(""));

    if(!fileFilters.isEmpty()) {
      fileFilters.forEach(fileChooser::addChoosableFileFilter);
    }

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

    if(!fileFilters.isEmpty()) {
      fileFilters.forEach(fileChooser::removeChoosableFileFilter);
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

  public static boolean openWebpage(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(uri);
        return true;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return false;
  }
}
