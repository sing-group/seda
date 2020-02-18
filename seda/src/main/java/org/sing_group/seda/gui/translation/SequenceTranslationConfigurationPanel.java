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
package org.sing_group.seda.gui.translation;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.input.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.gui.CommonFileChooser;

public class SequenceTranslationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String CUTOM_TABLE_INFO_LABEL = "<html>This option allows using a custom codon conversion "
    + "table. If not selected, the standard codon table is used.</html>";
  private static final String JOIN_FRAMES_INFO_LABEL = "<html>When frames 1, 2 and 3 are considered, this option "
    + "allows indicating whether translated frames must be considered together or separately.</html>";
  private static final String REVERSE_SEQUENCES_INFO_LABEL = "<html>Whether reverse complement of sequences must be "
    + "calculated before translation or not. If not selected, sequences are used as they are introduced.</html>";

  public static final String PROPERTY_JOIN_FRAMES = "seda.sequencetranslationpanel.joinframes";
  public static final String PROPERTY_FRAMES = "seda.sequencetranslationpanel.frames";
  public static final String PROPERTY_CODON_TABLE = "seda.sequencetranslationpanel.codontable";
  public static final String PROPERTY_REVERSE_SEQUENCES = "seda.sequencetranslationpanel.reversesequences";

  private boolean showJoinFramesCheckbox;

  private JIntegerTextField fixedFrameTf;
  private JRadioButton fixedFrameRb;
  private JCheckBox joinFramesCb;
  private JRadioButton allFramesRb;
  private JCheckBox customCodonTableCb;
  private JFileChooserPanel customCodonTableFileChooser;
  private Map<String, String> customCodonTable = Collections.emptyMap();
  private JCheckBox reverseSequencesCb;

  public SequenceTranslationConfigurationPanel(boolean showJoinFramesCheckbox) {
    this.showJoinFramesCheckbox = showJoinFramesCheckbox;
    this.init();
  }

  private void init() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JPanel fixedFramePanel = new JPanel();
    fixedFramePanel.setLayout(new BoxLayout(fixedFramePanel, BoxLayout.X_AXIS));

    fixedFrameRb = new JRadioButton("Starting at fixed frame", true);
    fixedFramePanel.add(fixedFrameRb);
    fixedFramePanel.add(Box.createHorizontalStrut(10));

    fixedFrameTf = new JIntegerTextField(1);
    fixedFramePanel.add(fixedFrameTf);
    fixedFrameTf.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        fixedFrameChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        fixedFrameChanged();
      }
    });
    fixedFramePanel.add(Box.createHorizontalStrut(10));

    allFramesRb = new JRadioButton("Considering frames 1, 2 and 3");
    fixedFramePanel.add(allFramesRb);
    fixedFramePanel.add(Box.createHorizontalGlue());

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(fixedFrameRb);
    buttonGroup.add(allFramesRb);

    allFramesRb.addItemListener(this::conversionConfigurationChanged);
    fixedFrameRb.addItemListener(this::conversionConfigurationChanged);

    JPanel joinFramesPanel = new JPanel();
    joinFramesPanel.setLayout(new BoxLayout(joinFramesPanel, BoxLayout.X_AXIS));
    joinFramesPanel.add(Box.createHorizontalGlue());
    joinFramesCb = new JCheckBox("Join frames", false);
    joinFramesPanel.add(joinFramesCb);
    joinFramesCb.setEnabled(false);
    joinFramesCb.addItemListener(this::joinFramesChanged);
    JLabel joinFramesPanelInfo = new JLabel(Icons.ICON_INFO_2_16);
    joinFramesPanelInfo.setToolTipText(JOIN_FRAMES_INFO_LABEL);
    joinFramesPanel.add(Box.createHorizontalStrut(2));
    joinFramesPanel.add(joinFramesPanelInfo);

    JPanel customCodonTablePanel = new JPanel();
    customCodonTablePanel.setLayout(new BoxLayout(customCodonTablePanel, BoxLayout.X_AXIS));

    customCodonTableCb = new JCheckBox("Use a custom codon code", false);
    customCodonTableCb.addItemListener(this::customCodonTableSelectionChanged);
    JLabel customCodonTableInfo = new JLabel(Icons.ICON_INFO_2_16);
    customCodonTableInfo.setToolTipText(CUTOM_TABLE_INFO_LABEL);

    customCodonTablePanel.add(customCodonTableCb);
    customCodonTablePanel.add(customCodonTableInfo);
    customCodonTablePanel.add(Box.createHorizontalStrut(10));
    customCodonTableFileChooser =
      JFileChooserPanelBuilder.createOpenJFileChooserPanel()
        .withFileChooser(CommonFileChooser.getInstance().getFilechooser()).build();
    customCodonTableFileChooser.getBrowseAction().setEnabled(false);
    customCodonTableFileChooser.addFileChooserListener(this::customCodonTableFileSelected);
    customCodonTablePanel.add(customCodonTableFileChooser);
    customCodonTablePanel.add(Box.createHorizontalGlue());

    fixedFramePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

    JPanel reverseSequencesPanel = new JPanel();
    reverseSequencesPanel.setLayout(new BoxLayout(reverseSequencesPanel, BoxLayout.X_AXIS));

    reverseSequencesCb = new JCheckBox("Use reverse complement sequences", false);
    reverseSequencesCb.addItemListener(this::reverseSequencesChanged);
    JLabel reverseSequencesInfo = new JLabel(Icons.ICON_INFO_2_16);
    reverseSequencesInfo.setToolTipText(REVERSE_SEQUENCES_INFO_LABEL);
    reverseSequencesPanel.add(reverseSequencesCb);
    reverseSequencesPanel.add(reverseSequencesInfo);
    reverseSequencesPanel.add(Box.createHorizontalGlue());

    this.add(fixedFramePanel);
    if (this.showJoinFramesCheckbox) {
      this.add(joinFramesPanel);
    }
    this.add(customCodonTablePanel);
    this.add(reverseSequencesPanel);
  }

  private void customCodonTableFileSelected(ChangeEvent event) {
    if (this.customCodonTableFileChooser.getSelectedFile() != null) {
      loadCustomMap(this.customCodonTableFileChooser.getSelectedFile());
    } else {
      this.customCodonTable.clear();
    }
  }

  private void loadCustomMap(File file) {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(file));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.customCodonTable = new HashMap<>();
    for (final String name : properties.stringPropertyNames()) {
      this.customCodonTable.put(name, properties.getProperty(name));
    }
  }

  private void customCodonTableSelectionChanged(ItemEvent event) {
    this.customCodonTableFileChooser.getBrowseAction().setEnabled(this.customCodonTableCb.isSelected());
    this.firePropertyChange(PROPERTY_CODON_TABLE, null, this.getCodonTable());
  }

  private void joinFramesChanged(ItemEvent event) {
    this.firePropertyChange(PROPERTY_JOIN_FRAMES, null, this.joinFramesCb.isSelected());
  }

  private void reverseSequencesChanged(ItemEvent event) {
    this.firePropertyChange(PROPERTY_REVERSE_SEQUENCES, null, this.reverseSequencesCb.isSelected());
  }

  private void conversionConfigurationChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.fixedFrameTf.setEnabled(this.fixedFrameRb.isSelected());
      this.joinFramesCb.setEnabled(!this.fixedFrameRb.isSelected());
      this.firePropertyChange(PROPERTY_FRAMES, null, this.getTranslationFrames());
    }
  }

  private void fixedFrameChanged() {
    try {
      this.fixedFrameTf.commitEdit();
      if (isValidFixedFrame()) {
        this.fixedFrameTf.setBackground(null);
      } else {
        this.fixedFrameTf.setBackground(Color.RED);
      }
      this.firePropertyChange(PROPERTY_FRAMES, null, this.getTranslationFrames());
    } catch (ParseException e) {}
  }

  public boolean isJoinFrames() {
    return this.joinFramesCb.isSelected() && !this.fixedFrameRb.isSelected();
  }

  public boolean isReverseSequences() {
    return this.reverseSequencesCb.isSelected();
  }

  public int[] getTranslationFrames() {
    if (this.fixedFrameRb.isSelected()) {
      return new int[] {
        this.fixedFrameTf.getValue()
      };
    } else {
      return new int[] {
        1, 2, 3
      };
    }
  }

  public Map<String, String> getCodonTable() {
    return this.customCodonTableCb.isSelected() ? this.customCodonTable : SequenceUtils.STANDARD_CODON_TABLE;
  }

  public boolean isValidUserSelection() {
    return isTranslationConfigurationValid();
  }

  private boolean isTranslationConfigurationValid() {
    return (this.allFramesRb.isSelected() || isValidFixedFrame())
            && isCustomCodonTableConfigurationValid();
  }

  private boolean isCustomCodonTableConfigurationValid() {
    return !this.customCodonTableCb.isSelected() || !this.customCodonTable.isEmpty();
  }

  private boolean isValidFixedFrame() {
    int fixedFrame = this.fixedFrameTf.getValue();

    return fixedFrame > 0 && fixedFrame < 4;
  }

  public void enableControls(boolean enabled) {
    this.allFramesRb.setEnabled(enabled);
    this.fixedFrameRb.setEnabled(enabled);
    this.fixedFrameTf.setEnabled(enabled);
    this.customCodonTableCb.setEnabled(enabled);
    this.joinFramesCb.setEnabled(enabled && this.allFramesRb.isSelected());
  }
}
