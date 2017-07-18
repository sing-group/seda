package org.sing_group.seda.gui;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static org.sing_group.seda.transformation.dataset.MSADatasetTransformation.toMSADatasetTransformation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.LazyDatatypeFactory;
import org.sing_group.seda.plugin.SedaPluginManager;
import org.sing_group.seda.plugin.spi.SedaGuiPlugin;
import org.sing_group.seda.plugin.spi.SedaPluginFactory;
import org.sing_group.seda.transformation.dataset.MSADatasetTransformation;

public class SedaPanel extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	private static final ImageIcon ICON_WORKING = new ImageIcon(SedaPanel.class.getResource("image/working.gif"));

	private final PathSelectionPanel panelPathSelection;
	private final OutputConfigurationPanel panelOutputConfig;
//	private final TransformationsConfigurationPanel panelTransformations;
	
	private final JButton btnGenerate;
	
	private final DatatypeFactory datatypeFactory;
	private final DatasetProcessor processor;
	
	private final SedaGuiPlugin[] guiPlugins;
	
	public SedaPanel(SedaPluginManager pluginManager) {
	  this.guiPlugins = pluginManager.getFactories()
	    .flatMap(SedaPluginFactory::getGuiPlugins)
    .toArray(SedaGuiPlugin[]::new);
	  
		this.datatypeFactory = new LazyDatatypeFactory();
		this.processor = new DatasetProcessor(this.datatypeFactory);
		
		this.btnGenerate = new JButton("Generate");
		
		this.panelPathSelection = new PathSelectionPanel();
		this.panelOutputConfig = new OutputConfigurationPanel();

		final JPanel panelOutputConfigContainer = new JPanel();
		panelOutputConfigContainer.add(this.panelOutputConfig);
		
		final JPanel panelOutput = new JPanel(new BorderLayout());
		panelOutput.add(panelOutputConfigContainer, BorderLayout.CENTER);
		panelOutput.add(this.btnGenerate, BorderLayout.SOUTH);
		
		this.addTab("File Selection", this.panelPathSelection);
		for (SedaGuiPlugin plugin : this.guiPlugins) {
		  final JPanel container = new JPanel();
		  container.add(plugin.getEditor());
		  
		  this.addTab(plugin.getName(), container);
		}
		this.addTab("Output", panelOutput);
		
		this.updateGenerateButton();
		
		this.btnGenerate.addActionListener(event -> this.generate());
		this.getPathSelectionModel().addPathSelectionModelListener(event -> {
			if (event.getType().isSelectedEvent())
				updateGenerateButton();
		});
	}
	
	private void updateGenerateButton() {
		this.btnGenerate.setEnabled(getPathSelectionModel().countSelectedPaths() > 0);
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
		return this.panelPathSelection.getModel();
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
			frame.setSize(new Dimension(800, 600));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			
			frame.setVisible(true);
		});
	}
}
