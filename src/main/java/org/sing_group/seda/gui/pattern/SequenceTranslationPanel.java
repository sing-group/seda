package org.sing_group.seda.gui.pattern;

import java.awt.BorderLayout;
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

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.filechooser.JFileChooserPanel;
import org.sing_group.gc4s.filechooser.JFileChooserPanelBuilder;
import org.sing_group.gc4s.text.JIntegerTextField;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.gui.CommonFileChooser;

public class SequenceTranslationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final String INFO_LABEL = "<html>If this option is selected, then input nucleic acid sequences are "
    + "translated into amino acid sequences before applying the pattern matching. <br/>If a translated sequence "
    + "matches the defined pattern, then the input nucleic acid sequence is reported.</html>";
  private static final String CUTOM_TABLE_INFO_LABEL = "<html>This option allows using a custom codon conversion "
    + "table. If not selected, the standard codon table is used.</html> ";
  private static final String JOIN_FRAMES_INFO_LABEL = "<html>When frames 1, 2 and 3 are considered, this option "
    + "allows indicating whether translated frames must be considered together or separately.</html> ";

  private JIntegerTextField fixedFrameTf;
  private JRadioButton fixedFrameRb;
  private JCheckBox convertCb;
  private JCheckBox joinFramesCb;
  private JRadioButton allFramesRb;
  private JCheckBox customCodonTableCb;
  private JFileChooserPanel customCodonTableFileChooser;
  private Map<String, String> customCodonTable = Collections.emptyMap();
  private JXTaskPane translationConfigurationTaskPane;

  public SequenceTranslationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getConversionCheckPanel(), BorderLayout.NORTH);
    this.add(getConversionConfigurationPanelContainer(), BorderLayout.CENTER);

    this.convertCb.doClick();
  }

  private JPanel getConversionCheckPanel() {
    JPanel checkPanel = new JPanel();
    checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.X_AXIS));
    convertCb = new JCheckBox("Convert to amino acid sequence before pattern matching", true);
    checkPanel.add(convertCb);
    convertCb.addItemListener(this::conversionStatusChanged);
    JLabel convertInfoLabel = new JLabel(Icons.ICON_INFO_2_16);
    convertInfoLabel.setToolTipText(INFO_LABEL);
    checkPanel.add(convertInfoLabel);

    return checkPanel;
  }

  private JPanel getConversionConfigurationPanelContainer() {
    final JXTaskPaneContainer customOptionsTaskPaneContainer =
      new JXTaskPaneContainer();
    customOptionsTaskPaneContainer.setOpaque(false);
    customOptionsTaskPaneContainer.setBorder(
      BorderFactory.createEmptyBorder(10, 10, 10, 10));

    translationConfigurationTaskPane = new JXTaskPane();
    translationConfigurationTaskPane.setTitle("Translation configuration");
    translationConfigurationTaskPane.add(getConversionConfigurationPanel());
    translationConfigurationTaskPane.setCollapsed(true);
    customOptionsTaskPaneContainer.add(translationConfigurationTaskPane);

    return customOptionsTaskPaneContainer;
  }

  private JPanel getConversionConfigurationPanel() {
    JPanel configPanel = new JPanel();
    configPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Configuration"));
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));

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

    configPanel.add(fixedFramePanel);
    configPanel.add(joinFramesPanel);
    configPanel.add(customCodonTablePanel);

    return configPanel;
  }

  private void customCodonTableFileSelected(ChangeEvent event) {
    if (this.customCodonTableFileChooser.getSelectedFile() != null) {
      loadCustomMap(this.customCodonTableFileChooser.getSelectedFile());
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
    this.fireConfigurationChangedEvent();
  }

  private void conversionStatusChanged(ItemEvent event) {
    this.updateControlsStatus();
    this.fireConfigurationChangedEvent();
  }

  private void updateControlsStatus() {
    boolean enabled = this.convertCb.isSelected() && this.convertCb.isEnabled();

    this.allFramesRb.setEnabled(enabled);
    this.fixedFrameRb.setEnabled(enabled);
    this.fixedFrameTf.setEnabled(enabled);
    this.customCodonTableCb.setEnabled(enabled);
    this.joinFramesCb.setEnabled(enabled && this.allFramesRb.isSelected());
    this.translationConfigurationTaskPane.setCollapsed(!enabled);
  }

  private void joinFramesChanged(ItemEvent event) {
    this.fireConfigurationChangedEvent();
  }

  private void conversionConfigurationChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      this.fixedFrameTf.setEnabled(this.fixedFrameRb.isSelected());
      this.joinFramesCb.setEnabled(!this.fixedFrameRb.isSelected());
      this.fireConfigurationChangedEvent();
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
      this.fireConfigurationChangedEvent();
    } catch (ParseException e) {}
  }

  public boolean isTranslationSelected() {
    return this.convertCb.isSelected();
  }

  public boolean isJoinFrames() {
    return this.joinFramesCb.isSelected() && !this.fixedFrameRb.isSelected();
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

  private void fireConfigurationChangedEvent() {
    ChangeEvent event = new ChangeEvent(this);
    for (SequenceTranslationPanelListener l : this.getSequenceTranslationPanelListeners()) {
      l.configurationChanged(event);
    }
  }

  public synchronized void addSequenceTranslationPanelListener(SequenceTranslationPanelListener l) {
    this.listenerList.add(SequenceTranslationPanelListener.class, l);
  }

  public synchronized SequenceTranslationPanelListener[] getSequenceTranslationPanelListeners() {
    return this.listenerList.getListeners(SequenceTranslationPanelListener.class);
  }

  public boolean isValidUserSelection() {
    return !isTranslationSelected() || isTranslationConfigurationValid();
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

  public void setConversionEnabled(boolean enabled) {
    this.convertCb.setEnabled(enabled);
    this.updateControlsStatus();
  }
}
