package org.sing_group.seda.gui;

import static javax.swing.BorderFactory.createEmptyBorder;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class SelectionPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON_TAB_FILES = new ImageIcon(SedaPanel.class.getResource("image/tab-files.png"));
  private static final ImageIcon ICON_TAB_PREVIEW = new ImageIcon(SedaPanel.class.getResource("image/tab-preview.png"));

  private JLabel filesLabel;
  private PathSelectionPanel panelPathSelection;
  private JTabbedPane tabs;

  private FilesPreviewPanel panelFilePreview;
  
  
  public SelectionPanel() {
    this.init();
  }

  private void init() {
    this.panelPathSelection = new PathSelectionPanel();
    this.panelPathSelection.setBorder(createEmptyBorder(5, 5, 5, 5));
    this.panelFilePreview = new FilesPreviewPanel(
      this.panelPathSelection.getModel());
    this.panelFilePreview.setBorder(createEmptyBorder(5, 5, 5, 5));

    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(Box.createHorizontalGlue());
    this.add(getFilesLabel());
    this.add(Box.createHorizontalStrut(10));
    this.add(getEditSelectionButton());
    this.add(Box.createHorizontalGlue());
  }

  private JLabel getFilesLabel() {
    this.filesLabel = new JLabel();
    this.updateFilesLabelText();
    return this.filesLabel;
  }

  private void updateFilesLabelText() {
    StringBuilder sb = new StringBuilder();
    sb
      .append(panelPathSelection.getModel().countSelectedPaths())
      .append(" files selected (")
      .append(panelPathSelection.getModel().countAvailablePaths())
      .append(" available)");
    this.filesLabel.setText(sb.toString());
  }

  private JButton getEditSelectionButton() {
    JButton editSelectionBtn = new JButton("Edit selection");
    editSelectionBtn.addActionListener(event -> this.editFilesSelection());
    return editSelectionBtn;
  }

  private void editFilesSelection() {
    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
        getFilesSelectionPanel(),
        "Edit file selection",
        JOptionPane.PLAIN_MESSAGE);
    this.updateFilesLabelText();
  }

  private JComponent getFilesSelectionPanel() {
    if(this.tabs == null) {
      this.tabs = new JTabbedPane();
      tabs.setUI(new MetalTabbedPaneUI() {
        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex,
          int fontHeight) {
          return 32;
        }
      });
      this.tabs.addTab("File selection", ICON_TAB_FILES, this.panelPathSelection);
      this.tabs.addTab("File preview", ICON_TAB_PREVIEW, this.panelFilePreview);
    }
    return this.tabs;
  }

  public PathSelectionModel getModel() {
    return this.panelPathSelection.getModel();
  }
}
