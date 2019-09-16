/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLoweredSoftBevelBorder;
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.sing_group.gc4s.dialog.JOptionPaneMessage;
import org.sing_group.gc4s.input.tree.JTreeSelectionPanel;
import org.sing_group.gc4s.input.tree.PathCheckTreeSelectionModel;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.utilities.JTreeUtils;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.DefaultDatatypeFactory;
import org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.LazyDatatypeFactory;
import org.sing_group.seda.plugin.SedaPluginManager;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.util.FileUtils;

public class SedaPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private static final ImageIcon ICON_LOGO = new ImageIcon(SedaPanel.class.getResource("image/logo.png"));
  
  private final SedaGuiPlugin[] guiPlugins;
  private final Map<String, SedaGuiPlugin> guiPluginsMap = new HashMap<>();

  private JPanel cards;
  private JPanel pluginsPanel;
  private JButton btnProcessDataset;
  private JButton btnProcessClipboard;
  private List<String> cardsLabels;
  private Map<String, List<String>> operationGroups;
  private OutputConfigurationPanel panelOutputConfig;

  private SedaContext sedaContext = new SedaContext();
  private DatasetProcessor processor;
  private DatatypeFactory datatypeFactory;

  private SelectionPanel selectionPanel;
  private JTree operationsTree;

  public static final String WARNING_OUTPUT_DIR = OutputConfigurationPanel.TOOLTIP_WARNING + " Do you want to continue?";
  private JOptionPaneMessage outputDirWarningMessage = new JOptionPaneMessage(WARNING_OUTPUT_DIR);

  private boolean warnReprocessFiles = false;
  public static final String WARNING_REPROCESS_FILES = "The file selection has not changed since the last operation "
    + "executed. You may have forgotten to change it. Do you want to continue?";
  private JOptionPaneMessage reprocessFilesWarningMessage = new JOptionPaneMessage(WARNING_REPROCESS_FILES);

  public SedaPanel(SedaPluginManager pluginManager) {
    GuiUtils.configureUI();

    this.guiPlugins = pluginManager.getFactories()
      .flatMap(SedaPluginFactory::getGuiPlugins)
      .toArray(SedaGuiPlugin[]::new);

    this.init();

    this.updateDatatypeFactory();
  }

  private void updateDatatypeFactory() {
    this.datatypeFactory = getDatatypeFactory();
    this.processor = new DatasetProcessor(this.datatypeFactory);
    this.selectionPanel.setDatasetFactory(this.datatypeFactory);
  }

  private DatatypeFactory getDatatypeFactory() {
    return this.getOutputConfigModel().isInMemoryProcessingEnabled() ?
      new DefaultDatatypeFactory() : new LazyDatatypeFactory();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getSelectionPanel(), NORTH);
    this.add(getPluginsPanel(), CENTER);
    this.add(getPanelOutput(), SOUTH);

    this.updateProcessButtons();
    this.addListeners();
  }

  private Component getSelectionPanel() {
    this.selectionPanel = new SelectionPanel(this.datatypeFactory);
    this.selectionPanel.setBorder(createSectionBorder("Input"));

    return this.selectionPanel;
  }

  private static final Border createSectionBorder(String title) {
    return createTitledBorder(createLoweredSoftBevelBorder(), title);
  }

  private Component getPluginsPanel() {
    this.cards = new JPanel(new CardLayout());
    this.cardsLabels = new LinkedList<>();
    this.operationGroups = new HashMap<>();
    for (SedaGuiPlugin plugin : this.guiPlugins) {
      final JPanel editorContainer = new JPanel(new BorderLayout());
      editorContainer.add(plugin.getEditor(), BorderLayout.NORTH);
      
      final JScrollPane scrollPane = new JScrollPane();
      scrollPane.setViewportView(editorContainer);
      scrollPane.getVerticalScrollBar().setUnitIncrement(15);
      scrollPane.setBorder(createEmptyBorder(5, 5, 5, 5));

      this.operationGroups.putIfAbsent(plugin.getGroupName(), new LinkedList<>());
      this.operationGroups.get(plugin.getGroupName()).add(plugin.getName());
      
      this.cards.add(scrollPane, plugin.getName());
      this.cardsLabels.add(plugin.getName());
      this.guiPluginsMap.put(plugin.getName(), plugin);
      plugin.getTransformation().addTransformationChangeListener(this::onTransformationChange);
      plugin.setSedaContext(sedaContext);
    }

    this.operationsTree = getOperationsTree();
    this.operationsTree.getSelectionModel().addTreeSelectionListener(this::selectedOperationChanged);
    
    JTreeSelectionPanel treeSelectionPanel =
      new JTreeSelectionPanel(operationsTree, "Choose operation", true, false, true, false) {
      private static final long serialVersionUID = 1L;

      @Override
      protected Dimension getSelectionLabelMinimumSize() {
        return new Dimension(350, 30);
      }
      
      @Override
      protected Font getSelectionLabelFont() {
        return new JLabel().getFont().deriveFont(Font.BOLD);
      }
    };

    JPanel cardsNorthPanel = new JPanel();
    cardsNorthPanel.setLayout(new FlowLayout());
    cardsNorthPanel.add(Box.createHorizontalGlue());
    cardsNorthPanel.add(treeSelectionPanel);
    cardsNorthPanel.add(Box.createHorizontalGlue());

    this.pluginsPanel = new JPanel(new BorderLayout());
    this.pluginsPanel.add(cardsNorthPanel, BorderLayout.NORTH);
    this.cards.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    this.pluginsPanel.add(this.cards, BorderLayout.CENTER);
    this.pluginsPanel.setBorder(createSectionBorder("Process"));

    return this.pluginsPanel;
  }

  private JTree getOperationsTree() {
    Comparator<String> comparator = new Comparator<String>() {

      @Override
      public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
      }};
      
    List<String> groups = new LinkedList<>(this.operationGroups.keySet());
    Collections.sort(groups, comparator);

    TreePath initialPath = null;

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    for (String groupName : groups) {
      DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(groupName);
      root.add(currentNode);

      List<String> operations = this.operationGroups.get(groupName);
      Collections.sort(operations, comparator);
      for (String o : operations) {
        DefaultMutableTreeNode operationNode = new DefaultMutableTreeNode(o);
        currentNode.add(operationNode);
        if (o.equals(this.guiPlugins[0].getName())) {
          initialPath = new TreePath(operationNode.getPath());
        }
      }
    }

    JTree tree = new JTree(root);
    tree.setShowsRootHandles(true);
    tree.setRootVisible(false);
    tree.setSelectionModel(new PathCheckTreeSelectionModel());
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    JTreeUtils.collapseAll(tree);
    tree.getSelectionModel().setSelectionPath(initialPath);
    tree.updateUI();

    return tree;
  }

  private Component getPanelOutput() {
    this.panelOutputConfig = new OutputConfigurationPanel();
    getOutputConfigModel().addOutputConfigurationModelListener(this::outputConfigurationModelChanged);
    final JPanel panelOutputConfigContainer = new JPanel();
    panelOutputConfigContainer.add(this.panelOutputConfig);

    final JPanel panelOutput = new JPanel(new BorderLayout());
    panelOutput.add(panelOutputConfigContainer, BorderLayout.CENTER);
    this.btnProcessDataset = new JButton("Process selected files");
    this.btnProcessDataset.addActionListener(event -> this.processSelectedFiles());

    this.btnProcessClipboard = new JButton("Process clipboard content");
    this.btnProcessClipboard.addActionListener(event -> this.processClipboard());

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
    buttonsPanel.add(this.btnProcessDataset);
    buttonsPanel.add(Box.createHorizontalStrut(10));
    buttonsPanel.add(this.btnProcessClipboard);

    panelOutput.add(new CenteredJPanel(buttonsPanel), BorderLayout.SOUTH);
    panelOutput.setBorder(createSectionBorder("Output"));

    return panelOutput;
  }

  private void outputConfigurationModelChanged(OutputConfigurationModelEvent event) {
    if (event.getType().equals(OutputConfigurationModelEventType.IN_MEMORY_PROCESSING_ENABLED)) {
      this.updateDatatypeFactory();
    }
    if (event.getType().equals(OutputConfigurationModelEventType.OUTPUT_DIRECTORY_CHANGED)) {
      this.checkOutputDirectory();
    }
  }

  private void selectedOperationChanged(TreeSelectionEvent e) {
    SwingUtilities.invokeLater(() -> {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      CardLayout cl = (CardLayout) (cards.getLayout());
      cl.show(cards, getSelectedOperationName());
      this.updateProcessButtons();
      this.setCursor(Cursor.getDefaultCursor());
    });
  }

  private String getSelectedOperationName() {
    return ((DefaultMutableTreeNode) this.operationsTree.getSelectionModel().getSelectionPath().getLastPathComponent())
      .toString();
  }

  private void onTransformationChange(TransformationChangeEvent event) {
    this.updateProcessButtons();
  }

  private void updateSedaContext() {
    sedaContext.setSelectedPaths(getPathSelectionModel().getSelectedPaths().collect(toList()));
  }

  private void updateReprocessStatus(boolean warnReprocessFiles) {
    this.warnReprocessFiles = warnReprocessFiles;
  }

  private void updateProcessButtons() {
    boolean activePluginConfigurationValid = activePluginConfigurationIsValid();
    this.btnProcessClipboard.setEnabled(activePluginConfigurationValid);
    this.btnProcessDataset.setEnabled(getPathSelectionModel().countSelectedPaths() > 0 && activePluginConfigurationValid);

    this.btnProcessClipboard.setToolTipText(getActivePluginConfigurationTooltip());
    this.btnProcessDataset.setToolTipText(getActivePluginConfigurationTooltip());
  }

  private void checkOutputDirectory() {
    this.panelOutputConfig.showOutputDirectoryWarning(this.outputDirectoryOverwriteInput());
  }

  private boolean outputDirectoryOverwriteInput() {
    Set<String> inputDirectories = getPathSelectionModel()
      .getSelectedPaths().map(p -> new File(p).getParent())
      .collect(Collectors.toSet());

    return inputDirectories.contains(getOutputConfigModel().getOutputDirectoryPath());
  }

  private String getActivePluginConfigurationTooltip() {
    SedaGuiPlugin activePlugin = getActivePlugin();

    return activePlugin.getProcessDatasetButtonTooltipMessage().orElse(null);
  }

  private boolean activePluginConfigurationIsValid() {
    SedaGuiPlugin activePlugin = getActivePlugin();

    return activePlugin.getTransformation().isValidTransformation();
  }

  private SedaGuiPlugin getActivePlugin() {
    return guiPluginsMap.get(getSelectedOperationName());
  }

  private void addListeners() {
    this.getPathSelectionModel().addPathSelectionModelListener(
      event -> {
        if (event.getType().isSelectedEvent()) {
          updateProcessButtons();
          updateSedaContext();
          checkOutputDirectory();
          updateReprocessStatus(false);
        }
      }
    );
  }

  private SequencesGroupDatasetTransformation getTransformation() {
    SedaGuiPlugin activePlugin = getActivePlugin();

    return activePlugin.getTransformation().getTransformation(this.datatypeFactory);
  }

  private void processClipboard() {
    Optional<Path> temporaryClipboardFile = FileUtils.getTemporaryClipboardFile();
    if (temporaryClipboardFile.isPresent()) {
      try {
        this.datatypeFactory.newSequencesGroup(temporaryClipboardFile.get());
        int option =
          JOptionPane.showConfirmDialog(
            this, getDatasetPreview(temporaryClipboardFile.get()), "Process clipboard", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
          );
        if (option == JOptionPane.YES_OPTION) {
          processPaths(Arrays.asList(temporaryClipboardFile.get()).stream());
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
          this, "The clipboard content cannot be processed as a FASTA file.", "Error", JOptionPane.ERROR_MESSAGE
        );
      }
    } else {
      JOptionPane.showMessageDialog(
        this, "The clipboard is empty.", "Warning", JOptionPane.WARNING_MESSAGE
      );
    }
  }

  private JComponent getDatasetPreview(Path path) {
    String lines = "";
    try {
      lines = Files.readAllLines(path).stream().collect(joining("\n"));

    } catch (IOException e) {}
    JTextArea textArea = new JTextArea(lines);
    textArea.setColumns(100);
    textArea.setRows(20);

    return new JScrollPane(textArea);
  }

  private void processPaths(Stream<Path> paths) {
    final JDialog dialog =
      new WorkingDialog(
        (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),
        "Executing task", "Running " + getActivePlugin().getName()
      );

    new CustomSwingWorker(() -> {
      final OutputConfigurationModel outputModel = getOutputConfigModel();

      final Path output = outputModel.getOutputDirectory();
      final int groupSize = outputModel.isSplitInSubdirectories() ?
        outputModel.getSubdirectorySize() : 0;

      final SequencesGroupDatasetTransformation transformation = this.getTransformation();

      try {
        this.processor.process(paths, output, transformation, groupSize);

        dialog.dispose();
        JOptionPane.showMessageDialog(
          this,
          "Transformation completed without any error. You can find the resulting dataset in: " + output.toString(),
          "Transformation Completed",
          JOptionPane.INFORMATION_MESSAGE
        );
      } catch (Throwable e) {
        e.printStackTrace();
        dialog.dispose();
        JOptionPane.showMessageDialog(
          this,
          "Error transforming dataset: " + e.getMessage(),
          "Transformation Error",
          JOptionPane.ERROR_MESSAGE
        );
      }
    }).execute();

    dialog.setVisible(true);
  }

  private void processSelectedFiles() {

    if (this.warnReprocessFiles) {
      if (this.reprocessFilesWarningMessage.shouldBeShown()) {
        int option =
          JOptionPane.showConfirmDialog(
            this, this.reprocessFilesWarningMessage.getMessage(), "Warning", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
          );
        if (option == JOptionPane.NO_OPTION) {
          return;
        }
      }
    }

    if (this.outputDirectoryOverwriteInput()) {
      if (this.outputDirWarningMessage.shouldBeShown()) {
        int option =
          JOptionPane.showConfirmDialog(
            this, this.outputDirWarningMessage.getMessage(), "Warning", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
          );
        if (option == JOptionPane.NO_OPTION) {
          return;
        }
      }
    }

    final PathSelectionModel pathsModel = getPathSelectionModel();
    final Stream<Path> paths = pathsModel.getSelectedPaths().map(Paths::get);

    this.processPaths(paths);
    this.updateReprocessStatus(true);
  }

  private OutputConfigurationModel getOutputConfigModel() {
    return this.panelOutputConfig.getModel();
  }

  private PathSelectionModel getPathSelectionModel() {
    return this.selectionPanel.getModel();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      final JFrame frame = new JFrame("Sequence Dataset Builder");

      final SedaPluginManager pluginManager = new SedaPluginManager();

      frame.setContentPane(new SedaPanel(pluginManager));
      frame.setIconImage(ICON_LOGO.getImage());
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);

      frame.setVisible(true);
    });
  }
}
