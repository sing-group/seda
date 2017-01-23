package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;

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
import org.sing_group.seda.transformation.dataset.MSADatasetTransformation;

public class SedaPanel extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	
	private static final ImageIcon ICON_WORKING = new ImageIcon(SedaPanel.class.getResource("image/working.gif"));

	private final PathSelectionPanel panelPathSelection;
	private final OutputConfigurationPanel panelOutputConfig;
	private final TransformationsConfigurationPanel panelTransformations;
	
	private final JButton btnGenerate;
	
	private final DatatypeFactory factory;
	private final DatasetProcessor processor;
	
	public SedaPanel() {
		super();
		
		this.factory = new LazyDatatypeFactory();
		this.processor = new DatasetProcessor(this.factory);
		
		this.btnGenerate = new JButton("Generate");
		
		this.panelPathSelection = new PathSelectionPanel();
		this.panelOutputConfig = new OutputConfigurationPanel();
		this.panelTransformations = new TransformationsConfigurationPanel();

		final JPanel panelOutputConfigContainer = new JPanel();
		panelOutputConfigContainer.add(this.panelOutputConfig);
		
		final JPanel panelTransformationsContainer = new JPanel();
		panelTransformationsContainer.add(this.panelTransformations);
		
		final JPanel panelOutput = new JPanel(new BorderLayout());
		panelOutput.add(panelOutputConfigContainer, BorderLayout.CENTER);
		panelOutput.add(this.btnGenerate, BorderLayout.SOUTH);
		
		this.addTab("File Selection", this.panelPathSelection);
		this.addTab("Transformations", panelTransformationsContainer);
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
	
	private void generate() {
		final JDialog dialog = new JDialog((JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this), "Executing task", true);
		
		dialog.setContentPane(new JLabel("Running task...", ICON_WORKING, SwingConstants.LEADING));
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		
		new CustomSwingWorker(() -> {
			final PathSelectionModel pathsModel = getPathSelectionModel();
			final OutputConfigurationModel outputModel = getOutputConfigModel();
			final TransformationsConfigurationModel transformationsModel = getTransformationsModel();
			
			final Stream<Path> paths = pathsModel.getSelectedPaths()
				.map(Paths::get);
			
			final Path output = outputModel.getOutputDirectory();
			final int groupSize = outputModel.isSplitInSubdirectories() ?
				outputModel.getSubdirectorySize() : 0;
				
			final MSADatasetTransformation transformation = transformationsModel.toTransformation(this.factory);
			
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

	private TransformationsConfigurationModel getTransformationsModel() {
		return this.panelTransformations.getModel();
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
			
			frame.setContentPane(new SedaPanel());
			frame.setSize(new Dimension(800, 600));
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			
			frame.setVisible(true);
		});
	}
}
