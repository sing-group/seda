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

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLoweredSoftBevelBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.JFileChooser.FILES_ONLY;
import static javax.swing.JFileChooser.OPEN_DIALOG;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static org.sing_group.seda.datatype.DatatypeFactory.newFactory;
import static org.sing_group.seda.gui.GuiUtils.showFileChooserAndProcess;
import static org.sing_group.seda.gui.SelectionPanel.CHARSET_SUPPORT;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
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
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.gc4s.ui.menu.HamburgerMenu;
import org.sing_group.gc4s.ui.menu.HamburgerMenu.Size;
import org.sing_group.gc4s.utilities.ExtendedAbstractAction;
import org.sing_group.gc4s.utilities.JTreeUtils;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.DatasetProcessorConfiguration;
import org.sing_group.seda.plugin.SedaPluginManager;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.util.FileUtils;
import org.sing_group.seda.util.SedaApplicationInfo;

public class SedaPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON_LOGO = new ImageIcon(SedaPanel.class.getResource("image/logo.png"));
  private static final String INITIAL_PLUGIN_NAME = "Filtering";

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
  private JTreeSelectionPanel  operationsTreeSelectionPanel;

  private ExtendedAbstractAction saveCurrentOperationConfiguration;
  private ExtendedAbstractAction loadCurrentOperationConfiguration;

  public static final String WARNING_OUTPUT_DIR =
    OutputConfigurationPanel.TOOLTIP_WARNING
      + " Do you want to continue?";
  private JOptionPaneMessage outputDirWarningMessage = new JOptionPaneMessage(WARNING_OUTPUT_DIR);

  public static final String WARNING_OVERWRITE_CONFIG =
    "The selected configuration file already exists."
      + " Do you want to overwrite it?";
  private JOptionPaneMessage overwriteConfigWarningMessage = new JOptionPaneMessage(WARNING_OVERWRITE_CONFIG);

  private boolean warnReprocessFiles = false;
  public static final String WARNING_REPROCESS_FILES =
    "The file selection has not changed since the last operation "
      + "executed. You may have forgotten to change it. Do you want to continue?";
  private JOptionPaneMessage reprocessFilesWarningMessage = new JOptionPaneMessage(WARNING_REPROCESS_FILES);

  public SedaPanel(SedaPluginManager pluginManager) {
    GuiUtils.configureUI();

    this.guiPlugins =
      pluginManager.getFactories().flatMap(SedaPluginFactory::getGuiPlugins)
        .sorted((p1, p2) -> {
          if (p1.getName().equals(INITIAL_PLUGIN_NAME)) {
            return -1;
          } else {
            return p2.getName().equals(INITIAL_PLUGIN_NAME) ? 1 : p1.getName().compareTo(p2.getName());
          }
        })
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
    return newFactory(
      this.getOutputConfigModel().isInMemoryProcessingEnabled(), this.selectionPanel.isCharsetSupportEnabled()
    );
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

    this.operationsTreeSelectionPanel =
      new JTreeSelectionPanel(
        operationsTree, "Choose operation", true, false,
        true, false
      ) {
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
    operationsTreeSelectionPanel.setMaximumSize(new Dimension(100, 200));

    JPanel cardsNorthPanel = new JPanel();
    cardsNorthPanel.setLayout(new BoxLayout(cardsNorthPanel, BoxLayout.X_AXIS));
    cardsNorthPanel.add(Box.createHorizontalGlue());
    cardsNorthPanel.add(operationsTreeSelectionPanel);
    cardsNorthPanel.add(Box.createHorizontalGlue());
    cardsNorthPanel.add(getOperationsMenu());

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
      }
    };

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

  private Component getOperationsMenu() {
    HamburgerMenu menu = new HamburgerMenu(Size.SIZE16);

    this.saveCurrentOperationConfiguration =
      new ExtendedAbstractAction(
        "Save current configuration",
        Icons.ICON_DOWNLOAD_2_16,
        this::saveCurrentOperationConfiguration
      );
    menu.add(this.saveCurrentOperationConfiguration);

    this.loadCurrentOperationConfiguration =
      new ExtendedAbstractAction(
        "Load and update current configuration",
        Icons.ICON_UPLOAD_2_16,
        this::loadCurrentOperationConfiguration
      );
    menu.add(this.loadCurrentOperationConfiguration);

    return menu;
  }

  private void saveCurrentOperationConfiguration() {
    SedaGuiPlugin activePlugin = getActivePlugin();

    if (activePlugin.canSaveTransformation()) {
      JFileChooser fileChooser = CommonFileChooser.getInstance().getFilechooser();
      showFileChooserAndProcess(fileChooser, this, FILES_ONLY, SAVE_DIALOG, false, emptyList(), file -> {
        saveTransformation(activePlugin, file.toFile());
      });
    }
  }

  private void saveTransformation(SedaGuiPlugin plugin, File file) {
    if (file.exists()) {
      if (this.overwriteConfigWarningMessage.shouldBeShown()) {
        if (showWarning(this, this.overwriteConfigWarningMessage) == JOptionPane.NO_OPTION) {
          return;
        }
      }
    }

    try {
      plugin.saveTransformation(file);
    } catch (IOException e) {
      this.handleException(e, "An error ocurred while saving the configuration.");
    }
  }

  private static int showWarning(Component parent, JOptionPaneMessage warningMessage) {
    return showConfirmDialog(parent, warningMessage.getMessage(), "Warning", YES_NO_OPTION, WARNING_MESSAGE);
  }

  private void loadCurrentOperationConfiguration() {
    SedaGuiPlugin activePlugin = getActivePlugin();
    JFileChooser fileChooser = CommonFileChooser.getInstance().getFilechooser();

    if (activePlugin.canSaveTransformation()) {
      showFileChooserAndProcess(fileChooser, this, FILES_ONLY, OPEN_DIALOG, false, emptyList(), file -> {
        loadTransformation(activePlugin, file.toFile());
      });
    }
  }
  
  private void loadTransformation(SedaGuiPlugin plugin, File file) {
    try {
      plugin.loadTransformation(file);
      plugin.getTransformation().addTransformationChangeListener(this::onTransformationChange);
      this.onTransformationChange();
    } catch (IOException e) {
      this.handleException(
        e,
        "An error ocurred while loading the configuration.\n\n"
          + "Please, make sure that the corresponding configuration file is valid and correspond to the active operation."
      );
    }
  }

  private void handleException(IOException e, String message) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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
      this.updateSedaContextProcessingMode();
    }
    if (event.getType().equals(OutputConfigurationModelEventType.OUTPUT_DIRECTORY_CHANGED)) {
      this.checkOutputDirectory();
    }
  }

  private void updateSedaContextProcessingMode() {
    this.sedaContext.setInMemoryProcessingEnabled(this.getOutputConfigModel().isInMemoryProcessingEnabled());
  }

  private void selectedOperationChanged(TreeSelectionEvent e) {
    CardLayout cl = (CardLayout) (cards.getLayout());
    cl.show(cards, getSelectedOperationName());
    this.onTransformationChange();
  }

  private String getSelectedOperationName() {
    return ((DefaultMutableTreeNode) this.operationsTree.getSelectionModel().getSelectionPath().getLastPathComponent())
      .toString();
  }

  private void updateTransformationActionsState() {
    SedaGuiPlugin activePlugin = getActivePlugin();

    this.saveCurrentOperationConfiguration
      .setEnabled(activePlugin.canSaveTransformation() && activePlugin.getTransformation().isValidTransformation());
    this.loadCurrentOperationConfiguration.setEnabled(activePlugin.canSaveTransformation());
  }
  
  private void updateTransformationDescriptionTooltip() {
    SedaGuiPlugin activePlugin = getActivePlugin();
    
    this.operationsTreeSelectionPanel.getSelectionLabel().setToolTipText(activePlugin.getDescription());
  }

  private void onTransformationChange(TransformationChangeEvent event) {
    this.onTransformationChange();
  }

  private void onTransformationChange() {
    SwingUtilities.invokeLater(() -> {
      getActivePlugin().getEditor().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      this.updateProcessButtons();
      this.updateTransformationActionsState();
      this.updateTransformationDescriptionTooltip();
      getActivePlugin().getEditor().setCursor(Cursor.getDefaultCursor());
    });
  }

  private void updateSedaContextSelectedPaths() {
    sedaContext.setSelectedPaths(getPathSelectionModel().getSelectedPaths().collect(toList()));
  }

  private void updateReprocessStatus(boolean warnReprocessFiles) {
    this.warnReprocessFiles = warnReprocessFiles;
  }

  private void updateProcessButtons() {
    boolean activePluginConfigurationValid = activePluginConfigurationIsValid();
    this.btnProcessClipboard.setEnabled(activePluginConfigurationValid);
    this.btnProcessDataset
      .setEnabled(getPathSelectionModel().countSelectedPaths() > 0 && activePluginConfigurationValid);

    this.btnProcessClipboard.setToolTipText(getActivePluginConfigurationTooltip());
    this.btnProcessDataset.setToolTipText(getActivePluginConfigurationTooltip());
  }

  private void checkOutputDirectory() {
    this.panelOutputConfig.showOutputDirectoryWarning(this.outputDirectoryOverwriteInput());
  }

  private boolean outputDirectoryOverwriteInput() {
    Set<String> inputDirectories =
      getPathSelectionModel().getSelectedPaths().map(p -> new File(p).getParent())
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
    this.getPathSelectionModel().addPathSelectionModelListener(event -> {
      if (event.getType().isSelectedEvent()) {
        updateProcessButtons();
        updateSedaContextSelectedPaths();
        checkOutputDirectory();
        updateReprocessStatus(false);
      }
    });

    this.selectionPanel.addPropertyChangeListener(CHARSET_SUPPORT, new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        charsetSupportChanged();
      }
    });
  }

  private void charsetSupportChanged() {
    this.sedaContext.setCharsetSupport(this.selectionPanel.isCharsetSupportEnabled());

    this.updateDatatypeFactory();
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
          JOptionPane.showConfirmDialog(this, getDatasetPreview(temporaryClipboardFile.get()),
            "Process clipboard", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
          );
        if (option == JOptionPane.YES_OPTION) {
          processPaths(Arrays.asList(temporaryClipboardFile.get()).stream());
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
          this, "The clipboard content cannot be processed as a FASTA file.", "Error",
          JOptionPane.ERROR_MESSAGE
        );
      }
    } else {
      JOptionPane.showMessageDialog(this, "The clipboard is empty.", "Warning", JOptionPane.WARNING_MESSAGE);
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
      new WorkingDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),
        "Executing task", "Running " + getActivePlugin().getName()
      );

    new CustomSwingWorker(() -> {
      final OutputConfigurationModel outputModel = getOutputConfigModel();

      final Path output = outputModel.getOutputDirectory();
      final int groupSize = outputModel.isSplitInSubdirectories() ? outputModel.getSubdirectorySize() : 0;
      final DatasetProcessorConfiguration configuration =
        new DatasetProcessorConfiguration(groupSize, outputModel.isWriteGzip());

      final SequencesGroupDatasetTransformation transformation = this.getTransformation();

      long start = System.nanoTime();
      try {
        this.processor.process(paths, output, transformation, configuration);
        long end = System.nanoTime();

        dialog.dispose();

        long interval = end - start;

        StringBuilder message = new StringBuilder("Transformation completed without any error.");
        message
          .append("\n\nYou can find the resulting dataset in: ")
          .append(output.toString())
          .append(".\n\nThe operation took ")
          .append(nanosToHms(interval))
          .append(" to complete.");

        JOptionPane.showMessageDialog(this, message.toString(), "Transformation Completed", INFORMATION_MESSAGE);
      } catch (Throwable e) {
        long end = System.nanoTime();
        e.printStackTrace();
        dialog.dispose();
        long interval = end - start;

        StringBuilder message = new StringBuilder("Error transforming dataset: ");
        message
          .append(e.getMessage())
          .append(".<br/><br/>The operation ran ")
          .append(nanosToHms(interval))
          .append(" before failing.");

        if (e instanceof OutOfMemoryError) {
          message
            .append("<br/><br/>Please, visit the SEDA manual to know how to fix this issue: ")
            .append(
              "<a href=\"https://www.sing-group.org/seda/manual/installation-and-configuration.html#increasing-ram-memory\">"
            )
            .append("Installation and configuration / Increasing RAM memory</a>");
        }

        JOptionPane.showMessageDialog(this, new HtmlMessage(message.toString()), "Transformation Error", ERROR_MESSAGE);
      }
    }).execute();

    dialog.setVisible(true);
  }

  private String nanosToHms(long nanos) {
    return String.format(
      "%02d:%02d:%02d",
      TimeUnit.NANOSECONDS.toHours(nanos),
      TimeUnit.NANOSECONDS.toMinutes(nanos) % TimeUnit.HOURS.toMinutes(1),
      TimeUnit.NANOSECONDS.toSeconds(nanos) % TimeUnit.MINUTES.toSeconds(1)
    );
  }

  private void processSelectedFiles() {

    if (this.warnReprocessFiles) {
      if (this.reprocessFilesWarningMessage.shouldBeShown()) {
        if (showWarning(this, this.reprocessFilesWarningMessage) == JOptionPane.NO_OPTION) {
          return;
        }
      }
    }

    if (this.outputDirectoryOverwriteInput()) {
      if (this.outputDirWarningMessage.shouldBeShown()) {
        if (showWarning(this, this.outputDirWarningMessage) == JOptionPane.NO_OPTION) {
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
      Locale.setDefault(Locale.ENGLISH);

      final JFrame frame = new JFrame(SedaApplicationInfo.getName());

      final SedaPluginManager pluginManager = new SedaPluginManager();

      frame.setContentPane(new SedaPanel(pluginManager));
      frame.setIconImage(ICON_LOGO.getImage());
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setMinimumSize(new Dimension(200, 200));

      frame.setVisible(true);
    });
  }
}
