package org.sing_group.seda.gui;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLoweredSoftBevelBorder;
import static javax.swing.BorderFactory.createTitledBorder;
import static org.sing_group.seda.transformation.dataset.MSADatasetTransformation.toMSADatasetTransformation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.LazyDatatypeFactory;
import org.sing_group.seda.plugin.SedaPluginManager;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;
import org.sing_group.seda.transformation.dataset.MSADatasetTransformation;

public class SedaPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON_WORKING = new ImageIcon(SedaPanel.class.getResource("image/working.gif"));

  private final SedaGuiPlugin[] guiPlugins;
  private final Map<String, SedaGuiPlugin> guiPluginsMap = new HashMap<>();

  private JPanel cards;
  private JPanel pluginsPanel;
  private JButton btnGenerate;
  private LinkedList<String> cardsLabels;
  private JComboBox<String> cardSelectionCombo;
  private OutputConfigurationPanel panelOutputConfig;

  private final DatasetProcessor processor;
  private final DatatypeFactory datatypeFactory;

  private SelectionPanel selectionPanel;

  public SedaPanel(SedaPluginManager pluginManager) {
    this.guiPlugins = pluginManager.getFactories()
      .flatMap(SedaPluginFactory::getGuiPlugins)
      .toArray(SedaGuiPlugin[]::new);

    this.datatypeFactory = new LazyDatatypeFactory();
    this.processor = new DatasetProcessor(this.datatypeFactory);

    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(getSelectionPanel(), NORTH);
    this.add(getPluginsPanel(), CENTER);
    this.add(getPanelOutput(), SOUTH);

    this.updateGenerateButton();
    this.addListeners();
  }

  private Component getSelectionPanel() {
    this.selectionPanel = new SelectionPanel();
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
    final JPanel panelOutputConfigContainer = new JPanel();
    panelOutputConfigContainer.add(this.panelOutputConfig);

    final JPanel panelOutput = new JPanel(new BorderLayout());
    panelOutput.add(panelOutputConfigContainer, BorderLayout.CENTER);
    this.btnGenerate = new JButton("Generate");
    this.btnGenerate.addActionListener(event -> this.generate());

    panelOutput.add(this.btnGenerate, BorderLayout.SOUTH);
    panelOutput.setBorder(createSectionBorder("Output"));

    return panelOutput;
  }

  private void cardItemChanged(ItemEvent evt) {
    CardLayout cl = (CardLayout) (cards.getLayout());
    cl.show(cards, (String) evt.getItem());
  }

  private void updateGenerateButton() {
    this.btnGenerate.setEnabled(getPathSelectionModel().countSelectedPaths() > 0);
  }

  private void addListeners() {
    this.getPathSelectionModel().addPathSelectionModelListener(event -> {
      if (event.getType().isSelectedEvent())
        updateGenerateButton();
    });
  }

  private MSADatasetTransformation getTransformation() {
    return stream(this.guiPlugins)
      .map(SedaGuiPlugin::getTransformation)
      .map(transformation -> transformation.getTransformation(this.datatypeFactory))
    .collect(toMSADatasetTransformation());
  }

  private void generate() {
    final JDialog dialog = new JDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this), "Executing task", true);

    dialog.setContentPane(new JLabel("Running task...", ICON_WORKING, SwingConstants.LEADING));
    dialog.pack();
    dialog.setLocationRelativeTo(null);

    new CustomSwingWorker(() -> {
      final PathSelectionModel pathsModel = getPathSelectionModel();
      final OutputConfigurationModel outputModel = getOutputConfigModel();

      final Stream<Path> paths = pathsModel.getSelectedPaths()
        .map(Paths::get);

      final Path output = outputModel.getOutputDirectory();
      final int groupSize = outputModel.isSplitInSubdirectories() ?
        outputModel.getSubdirectorySize() : 0;

      final MSADatasetTransformation transformation = this.getTransformation();

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

  private OutputConfigurationModel getOutputConfigModel() {
    return this.panelOutputConfig.getModel();
  }

  private PathSelectionModel getPathSelectionModel() {
    return this.selectionPanel.getModel();
  }

  private static class CustomSwingWorker extends SwingWorker<Void, Void> {
    private final Runnable task;

    public CustomSwingWorker(Runnable task) {
      this.task = requireNonNull(task);
    }

    @Override
    protected Void doInBackground() throws Exception {
      this.task.run();

      return null;
    }
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
