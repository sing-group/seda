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

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class FilePreviewPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String DEFAULT_TEXT = "Select one file to show a preview.";
  private JTextArea fileTextArea;

  public FilePreviewPanel() {
    this.init();
  }

  private void init() {
    this.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
    this.setLayout(new BorderLayout());
    this.add(getFileTextArea(), BorderLayout.CENTER);
  }

  private Component getFileTextArea() {
    this.fileTextArea = new JTextArea(DEFAULT_TEXT);
    this.fileTextArea.setEditable(false);
    this.fileTextArea.setRows(10);
    this.fileTextArea.setLineWrap(true);

    return new JScrollPane(this.fileTextArea);
  }

  public void previewPath(String previewPath) {
    setText("Loading file " + previewPath + " ...");
    SwingUtilities.invokeLater(() -> {
      this.setText(readFirstSequence(new File(previewPath)));
    });
  }

  private String readFirstSequence(File file) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      StringBuilder sb = new StringBuilder();
      boolean firstLine = true;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(">") && !firstLine) {
          break;
        } else {
          sb.append(line).append("\n");
        }
        firstLine = false;
      }
      reader.close();

      return sb.toString();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "Error reading file.";
  }

  private void setText(String text) {
    this.fileTextArea.setText(text);
    scrolltop();
  }

  private void scrolltop() {
    this.fileTextArea.setSelectionStart(0);
    this.fileTextArea.setSelectionEnd(0);
  }

  public void clearPreviewPath() {
    this.setText(DEFAULT_TEXT);
  }
}
