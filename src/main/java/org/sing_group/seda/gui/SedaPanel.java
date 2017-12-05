package org.sing_group.seda.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.util.stream.Collectors.toList;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLoweredSoftBevelBorder;
import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.sing_group.gc4s.dialog.JOptionPaneMessage;
import org.sing_group.gc4s.ui.CenteredJPanel;
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

  private final SedaGuiPlugin[] guiPlugins;
  private final Map<String, SedaGuiPlugin> guiPluginsMap = new HashMap<>();

  private JPanel cards;
  private JPanel pluginsPanel;
  private JButton btnProcessDataset;
  private JButton btnProcessClipboard;
  private LinkedList<String> cardsLabels;
  private JComboBox<String> cardSelectionCombo;
  private OutputConfigurationPanel panelOutputConfig;

  private SedaContext sedaContext = new SedaContext();
  private DatasetProcessor processor;
  private DatatypeFactory datatypeFactory;

  private SelectionPanel selectionPanel;

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
    for (SedaGuiPlugin plugin : this.guiPlugins) {
      final JScrollPane container = new JScrollPane();
      container.setViewportView(plugin.getEditor());
      container.setBorder(createEmptyBorder(5, 5, 5, 5));

      this.cards.add(container, plugin.getName());
      this.cardsLabels.add(plugin.getName());
      this.guiPluginsMap.put(plugin.getName(), plugin);
      plugin.getTransformation().addTransformationChangeListener(this::onTransformationChange);
      plugin.setSedaContext(sedaContext);
    }

    this.cardSelectionCombo = new JComboBox<String>(
      this.cardsLabels.toArray(new String[this.cardsLabels.size()]));
    this.cardSelectionCombo.setEditable(false);
    this.cardSelectionCombo.addItemListener(this::cardItemChanged);

    JPanel cardsNorthPanel = new JPanel();
    cardsNorthPanel.setLayout(new FlowLayout());
    cardsNorthPanel.add(Box.createHorizontalGlue());
    cardsNorthPanel.add(new JLabel("Operation:"));
    cardsNorthPanel.add(this.cardSelectionCombo);

    this.pluginsPanel = new JPanel(new BorderLayout());
    this.pluginsPanel.add(cardsNorthPanel, BorderLayout.NORTH);
    this.cards.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
    this.pluginsPanel.add(this.cards, BorderLayout.CENTER);
    this.pluginsPanel.setBorder(createSectionBorder("Process"));

    return this.pluginsPanel;
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

  private void cardItemChanged(ItemEvent evt) {
    CardLayout cl = (CardLayout) (cards.getLayout());
    cl.show(cards, (String) evt.getItem());
    this.updateProcessButtons();
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
    return guiPluginsMap.get(this.cardSelectionCombo.getSelectedItem());
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
      lines = Files.readAllLines(path).stream().collect(Collectors.joining("\n"));

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
      frame.setSize(new Dimension(900, 600));
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null);

      frame.setVisible(true);
    });
  }
}
