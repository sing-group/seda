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

import static javax.swing.Box.createHorizontalGlue;
import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;

import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.text.JIntegerTextField;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.visualization.table.MapTableViewer;
import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.gui.CommonFileChooser;

public class SequenceTranslationConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String CUTOM_TABLE_INFO_LABEL = "<html>This option allows using a custom codon conversion "
    + "table. If not selected, the standard codon table is used.</html>";
  private static final String SHOW_CUTOM_TABLE_TOOLTIP = "<html>Make double-click here to see the current codon table.</html>";
  private static final String JOIN_FRAMES_INFO_LABEL = "<html>When frames 1, 2 and 3 are considered, this option "
    + "allows indicating whether translated frames must be considered together or separately.</html>";
  private static final String REVERSE_SEQUENCES_INFO_LABEL = "<html>Whether reverse complement of sequences must be "
    + "calculated before translation or not. If not selected, sequences are used as they are introduced.</html>";

  public static final String PROPERTY_JOIN_FRAMES = "seda.sequencetranslationpanel.joinframes";
  public static final String PROPERTY_FRAMES = "seda.sequencetranslationpanel.frames";
  public static final String PROPERTY_CODON_TABLE = "seda.sequencetranslationpanel.codontable";
  public static final String PROPERTY_REVERSE_SEQUENCES = "seda.sequencetranslationpanel.reversesequences";

  private boolean ignoreGuiEvents = false; 
  private boolean showJoinFramesCheckbox;

  private JIntegerTextField fixedFrameTf;
  private JRadioButton fixedFrameRb;
  private JCheckBox joinFramesCb;
  private JRadioButton allFramesRb;
  private JRadioButton customCodonTableRb;
  private JRadioButton standardCodonTableRb;
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
    fixedFramePanel.add(createHorizontalStrut(10));

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
    fixedFramePanel.add(createHorizontalStrut(10));

    allFramesRb = new JRadioButton("Considering frames 1, 2 and 3");
    fixedFramePanel.add(allFramesRb);
    fixedFramePanel.add(createHorizontalGlue());

    ButtonGroup framesButtonGroup = new ButtonGroup();
    framesButtonGroup.add(fixedFrameRb);
    framesButtonGroup.add(allFramesRb);

    allFramesRb.addItemListener(this::conversionConfigurationChanged);
    fixedFrameRb.addItemListener(this::conversionConfigurationChanged);

    JPanel joinFramesPanel = new JPanel();
    joinFramesPanel.setLayout(new BoxLayout(joinFramesPanel, BoxLayout.X_AXIS));
    joinFramesPanel.add(createHorizontalGlue());
    joinFramesCb = new JCheckBox("Join frames", false);
    joinFramesPanel.add(joinFramesCb);
    joinFramesCb.setEnabled(false);
    joinFramesCb.addItemListener(this::joinFramesChanged);
    JLabel joinFramesPanelInfo = new JLabel(Icons.ICON_INFO_2_16);
    joinFramesPanelInfo.setToolTipText(JOIN_FRAMES_INFO_LABEL);
    joinFramesPanel.add(createHorizontalStrut(2));
    joinFramesPanel.add(joinFramesPanelInfo);

    JPanel customCodonTablePanel = new JPanel();
    customCodonTablePanel.setLayout(new BoxLayout(customCodonTablePanel, BoxLayout.X_AXIS));

    standardCodonTableRb = new JRadioButton("Standard", true);
    customCodonTableRb = new JRadioButton("Custom");

    ButtonGroup codonTableButtonGroup = new ButtonGroup();
    codonTableButtonGroup.add(customCodonTableRb);
    codonTableButtonGroup.add(standardCodonTableRb);

    customCodonTableRb.addItemListener(this::customCodonTableSelectionChanged);
    standardCodonTableRb.addItemListener(this::standardCodonTableSelectionChanged);
    JLabel customCodonTableInfo = new JLabel(Icons.ICON_INFO_2_16);
    customCodonTableInfo.setToolTipText(CUTOM_TABLE_INFO_LABEL);
    JLabel showCustomCodonTable = new JLabel(Icons.ICON_LOOKUP_16);
    showCustomCodonTable.setToolTipText(SHOW_CUTOM_TABLE_TOOLTIP);

    showCustomCodonTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && !e.isConsumed()) {
          e.consume();
          showMessageDialog(
            SequenceTranslationConfigurationPanel.this,
            new JScrollPane(new MapTableViewer<>(getCodonTable())), "Codon table", PLAIN_MESSAGE
          );
        }
      }
    });

    customCodonTablePanel.add(new JLabel("Codon table: "));
    customCodonTablePanel.add(createHorizontalStrut(10));
    customCodonTablePanel.add(standardCodonTableRb);
    customCodonTablePanel.add(createHorizontalStrut(10));
    customCodonTablePanel.add(customCodonTableRb);
    customCodonTablePanel.add(createHorizontalStrut(10));
    customCodonTablePanel.add(showCustomCodonTable);
    customCodonTablePanel.add(createHorizontalStrut(2));
    customCodonTablePanel.add(customCodonTableInfo);
    customCodonTablePanel.add(createHorizontalGlue());

    fixedFramePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

    JPanel reverseSequencesPanel = new JPanel();
    reverseSequencesPanel.setLayout(new BoxLayout(reverseSequencesPanel, BoxLayout.X_AXIS));

    reverseSequencesCb = new JCheckBox("Use reverse complement sequences", false);
    reverseSequencesCb.addItemListener(this::reverseSequencesChanged);
    JLabel reverseSequencesInfo = new JLabel(Icons.ICON_INFO_2_16);
    reverseSequencesInfo.setToolTipText(REVERSE_SEQUENCES_INFO_LABEL);
    reverseSequencesPanel.add(reverseSequencesCb);
    reverseSequencesPanel.add(reverseSequencesInfo);
    reverseSequencesPanel.add(createHorizontalGlue());

    this.add(fixedFramePanel);
    if (this.showJoinFramesCheckbox) {
      this.add(joinFramesPanel);
    }
    this.add(customCodonTablePanel);
    this.add(reverseSequencesPanel);
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

  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
    if (!ignoreGuiEvents) {
      super.firePropertyChange(propertyName, oldValue, newValue);
    }
  }

  private void standardCodonTableSelectionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.firePropertyChange(PROPERTY_CODON_TABLE, null, this.getCodonTable());
    }
  }

  private void customCodonTableSelectionChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED && !ignoreGuiEvents) {
      JFileChooser fileChooser = CommonFileChooser.getInstance().getFilechooser();
      int selection = fileChooser.showSaveDialog(this);
      if (selection == JFileChooser.APPROVE_OPTION) {
        if (fileChooser.getSelectedFile() != null) {
          loadCustomMap(fileChooser.getSelectedFile());
        } else {
          this.customCodonTable.clear();
        }
        this.firePropertyChange(PROPERTY_CODON_TABLE, null, this.getCodonTable());
      } else {
        this.standardCodonTableRb.setSelected(true);
      }
    }
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

  private boolean isJoinFrames() {
    return this.joinFramesCb.isSelected() && !this.fixedFrameRb.isSelected();
  }

  private boolean isReverseSequences() {
    return this.reverseSequencesCb.isSelected();
  }

  private int[] getTranslationFrames() {
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

  private Map<String, String> getCodonTable() {
    return this.customCodonTableRb.isSelected() ? this.customCodonTable : SequenceUtils.STANDARD_CODON_TABLE;
  }

  public boolean isValidUserSelection() {
    return isTranslationConfigurationValid();
  }

  private boolean isTranslationConfigurationValid() {
    return (this.allFramesRb.isSelected() || isValidFixedFrame())
            && isCustomCodonTableConfigurationValid();
  }

  private boolean isCustomCodonTableConfigurationValid() {
    return !this.customCodonTableRb.isSelected() || !this.customCodonTable.isEmpty();
  }

  private boolean isValidFixedFrame() {
    int fixedFrame = this.fixedFrameTf.getValue();

    return fixedFrame > 0 && fixedFrame < 4;
  }

  public void enableControls(boolean enabled) {
    this.allFramesRb.setEnabled(enabled);
    this.fixedFrameRb.setEnabled(enabled);
    this.fixedFrameTf.setEnabled(enabled);
    this.customCodonTableRb.setEnabled(enabled);
    this.joinFramesCb.setEnabled(enabled && this.allFramesRb.isSelected());
    this.reverseSequencesCb.setEnabled(enabled);
  }

  public SequenceTranslationConfiguration getSequenceTranslationConfiguration() {
    if (!showJoinFramesCheckbox) {
      return new SequenceTranslationConfiguration(getCodonTable(), isReverseSequences(), getTranslationFrames());
    } else {
      return new SequenceTranslationConfiguration(
        getCodonTable(), isReverseSequences(), isJoinFrames(), getTranslationFrames()
      );
    }
  }

  public void setSequenceTranslationConfiguration(SequenceTranslationConfiguration configuration) {
    this.ignoreGuiEvents = true;

    this.joinFramesCb.setSelected(configuration.isJoinFrames());
    if (configuration.getFrames().size() == 1) {
      this.fixedFrameRb.setSelected(true);
      this.fixedFrameTf.setValue(configuration.getFrames().get(0));
    }
    this.reverseSequencesCb.setSelected(configuration.isReverseComplement());
    if (configuration.getCodonTable().equals(SequenceUtils.STANDARD_CODON_TABLE)) {
      this.standardCodonTableRb.setSelected(true);
    } else {
      this.customCodonTableRb.setSelected(true);
      this.customCodonTable = configuration.getCodonTable();
    }

    this.ignoreGuiEvents = false;
  }
}
