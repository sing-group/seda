package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.swing.AbstractListModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType;

public class PathSelectionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final ImageIcon ICON_ARROW_LEFT = new ImageIcon(TransformationsConfigurationPanel.class.getResource("image/arrow-left.png"));
	private static final ImageIcon ICON_ARROW_RIGHT = new ImageIcon(TransformationsConfigurationPanel.class.getResource("image/arrow-right.png"));

	private final JList<String> listAvailableFiles;
	private final JList<String> listSelectedFiles;

	private final JButton btnLoadDirectory;
	private final JButton btnLoadFiles;
	private final JButton btnLoadFileList;

	private final JCheckBox chkRecursiveSearch;
	private final JCheckBox chkHideCommonPath;

	private final JButton btnAdd;
	private final JButton btnRemove;

	private final JButton btnClearAvailable;
	private final JButton btnClearSelected;

	private final JFileChooser fileChooser;

	private final PathSelectionModel model;

	public PathSelectionPanel() {
		super(new GridBagLayout());

		this.model = new PathSelectionModel();

		this.listAvailableFiles = new JList<String>(
			new CustomListModel(
				this.model,
				PathSelectionModel::countAvailablePaths,
				PathSelectionModel::getAvailablePath,
				PathSelectionModel::getAvailablePaths,
				FileSelectionEventType.ADD_AVAILABLE,
				FileSelectionEventType.REMOVE_AVAILABLE,
				FileSelectionEventType.CLEAR_AVAILABLE
			)
		);
		this.listSelectedFiles = new JList<String>(
			new CustomListModel(
				this.model,
				PathSelectionModel::countSelectedPaths,
				PathSelectionModel::getSelectedPath,
				PathSelectionModel::getSelectedPaths,
				FileSelectionEventType.ADD_SELECTED,
				FileSelectionEventType.REMOVE_SELECTED,
				FileSelectionEventType.CLEAR_SELECTED
			)
		);
		this.listAvailableFiles.setCellRenderer(new CustomListRenderer());
		this.listSelectedFiles.setCellRenderer(new CustomListRenderer());

		this.btnLoadDirectory = new JButton("Load directory");
		this.btnLoadFiles = new JButton("Load files");
		this.btnLoadFileList = new JButton("Load file list");

		this.chkRecursiveSearch = new JCheckBox("Recursive file search", true);
		this.chkHideCommonPath = new JCheckBox("Hide common path", true);

		this.btnAdd = new JButton(ICON_ARROW_RIGHT);
		this.btnRemove = new JButton(ICON_ARROW_LEFT);

		this.btnClearAvailable = new JButton("Clear available list");
		this.btnClearSelected = new JButton("Clear selected list");

		this.fileChooser = new JFileChooser(".");
		this.fileChooser.setMultiSelectionEnabled(true);

		final JPanel panelAvailable = new JPanel(new BorderLayout());
		final JPanel panelAvailableButtons = new JPanel(new GridBagLayout());
		final GridBagConstraints gbcAvaliableButtons = new GridBagConstraints();
		gbcAvaliableButtons.fill = GridBagConstraints.BOTH;

		panelAvailableButtons.add(this.btnLoadDirectory, gbcAvaliableButtons);
		gbcAvaliableButtons.gridx = 1;
		panelAvailableButtons.add(this.btnLoadFiles, gbcAvaliableButtons);
		gbcAvaliableButtons.gridx = 2;
		panelAvailableButtons.add(this.btnLoadFileList, gbcAvaliableButtons);

		gbcAvaliableButtons.gridy = 1;
		gbcAvaliableButtons.gridx = 0;
		gbcAvaliableButtons.gridwidth = 3;
		gbcAvaliableButtons.anchor = GridBagConstraints.BASELINE_LEADING;
		panelAvailableButtons.add(this.chkRecursiveSearch, gbcAvaliableButtons);
		
		gbcAvaliableButtons.gridy = 2;
		panelAvailableButtons.add(this.chkHideCommonPath, gbcAvaliableButtons);

		panelAvailable.add(panelAvailableButtons, BorderLayout.NORTH);
		panelAvailable.add(new JScrollPane(this.listAvailableFiles), BorderLayout.CENTER);
		panelAvailable.add(this.btnClearAvailable, BorderLayout.SOUTH);
		
		
		final JPanel panelCentralButtons = new JPanel();
		final BoxLayout layout = new BoxLayout(panelCentralButtons, BoxLayout.Y_AXIS);
		panelCentralButtons.setLayout(layout);

		panelCentralButtons.add(this.btnAdd);
		panelCentralButtons.add(this.btnRemove);

		final JPanel panelSelected = new JPanel(new BorderLayout());
		final JLabel label = new JLabel("Selected Files");

		panelSelected.add(label, BorderLayout.NORTH);
		panelSelected.add(new JScrollPane(this.listSelectedFiles), BorderLayout.CENTER);
		panelSelected.add(this.btnClearSelected, BorderLayout.SOUTH);

		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1d;
		gbc.weightx = 1d;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(panelAvailable, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0d;
		gbc.fill = GridBagConstraints.NONE;
		this.add(panelCentralButtons, gbc);

		gbc.gridx = 2;
		gbc.weightx = 1d;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(panelSelected, gbc);

		this.btnLoadDirectory.addActionListener(e -> this.loadDirectory());
		this.btnLoadFiles.addActionListener(e -> this.loadFile());
		this.btnLoadFileList.addActionListener(e -> this.loadFileList());

		this.btnAdd.addActionListener(e -> this.selectFiles());
		this.btnRemove.addActionListener(e -> this.unselectFiles());

		this.btnClearAvailable.addActionListener(e -> this.model.clearAvailablePaths());
		this.btnClearSelected.addActionListener(e -> this.model.clearSelectedPaths());
		
		this.chkHideCommonPath.addItemListener(e -> {
			listAvailableFiles.repaint();
			listSelectedFiles.repaint();
		});
	}

	public PathSelectionModel getModel() {
		return model;
	}
	
	private boolean isHideCommonPath() {
		return this.chkHideCommonPath.isSelected();
	}

	private void selectFiles() {
		final List<String> selectedFiles = this.listAvailableFiles.getSelectedValuesList();

		this.model.selectPaths(selectedFiles.stream().toArray(String[]::new));
	}

	private void unselectFiles() {
		final List<String> selectedFiles = this.listSelectedFiles.getSelectedValuesList();

		this.model.removeSelectedPaths(selectedFiles.stream().toArray(String[]::new));
	}

	private void loadFileList() {
		showFileChooserAndProcess(
			JFileChooser.FILES_ONLY, false, path -> {
				try {
					Files.lines(path)
						.filter(line -> !line.trim().isEmpty())
						.forEach(model::addAvailablePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		);
	}

	private void loadFile() {
		showFileChooserAndProcess(JFileChooser.FILES_ONLY, true, model::addAvailablePath);
	}

	private void loadDirectory() {
		showFileChooserAndProcess(
			JFileChooser.DIRECTORIES_ONLY, true, directory -> {
				try {
					if (this.chkRecursiveSearch.isSelected()) {
						Files.walkFileTree(
							directory, new SimpleFileVisitor<Path>() {
								@Override
								public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
									requireNonNull(file);
									requireNonNull(attrs);

									model.addAvailablePath(file);

									return FileVisitResult.CONTINUE;
								}
							}
						);
					} else {
						Files.list(directory)
							.filter(Files::isRegularFile)
							.forEach(model::addAvailablePath);
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		);
	}

	private void showFileChooserAndProcess(int selectionMode, boolean multipleSelection, Consumer<Path> pathProcesser) {
		GuiUtils.showFileChooserAndProcess(this.fileChooser, this, selectionMode, multipleSelection, pathProcesser);
	}

	private final static class CustomListModel extends AbstractListModel<String> {
		private static final long serialVersionUID = 1L;

		private final IntSupplier sizeFunction;
		private final IntFunction<String> getFunction;
		private final Supplier<Stream<String>> getPathsFunction;
		
		private String commonPrefix;

		public CustomListModel(
			final PathSelectionModel model,
			final ToIntFunction<PathSelectionModel> sizeFunction,
			final BiFunction<PathSelectionModel, Integer, String> getFunction,
			final Function<PathSelectionModel, Stream<String>> getPathsFunction,
			final FileSelectionEventType additionEvent,
			final FileSelectionEventType removalEvent,
			final FileSelectionEventType clearEvent
		) {
			this.sizeFunction = () -> sizeFunction.applyAsInt(model);
			this.getFunction = index -> getFunction.apply(model, index);
			this.getPathsFunction = () -> getPathsFunction.apply(model);

			model.addPathSelectionModelListener(
				event -> {
					final Integer index = event.getIndex();

					if (event.getType() == additionEvent) {
						this.updateCommonPrefix();
						this.fireIntervalAdded(event, index, index);
					} else if (event.getType() == removalEvent) {
						this.updateCommonPrefix();
						this.fireIntervalRemoved(event, index, index);
					} else if (event.getType() == clearEvent) {
						this.updateCommonPrefix();
						this.fireContentsChanged(event, 0, index);
					}
				}
			);
		}

		@Override
		public int getSize() {
			return this.sizeFunction.getAsInt();
		}

		@Override
		public String getElementAt(int index) {
			return this.getFunction.apply(index);
		}

		public String getCommonPath() {
			return this.commonPrefix;
		}
		
		private void updateCommonPrefix() {
			final int size = this.getSize();
			if (size == 0) {
				this.commonPrefix = "";
			} else if (size == 1) {
				final char separator = File.separatorChar;
				final String path = this.getElementAt(0);
				this.commonPrefix = path.substring(0, path.lastIndexOf(separator) + 1);
			} else {
				this.commonPrefix = this.getPathsFunction.get()
					.reduce("", (p1, p2) -> {
						if (p1.isEmpty()) return p2;
						if (p2.isEmpty()) return p1;
						
						String commonPrefix = "";
						for (int i = 0; i < Math.min(p1.length(), p2.length()); i++) {
							if (p1.charAt(i) == p2.charAt(i)) {
								commonPrefix += p1.charAt(i);
							} else {
								break;
							}
						}
						
						return commonPrefix;
					});
			}
		}
	}

	private final class CustomListRenderer implements ListCellRenderer<String> {
		private final ListCellRenderer<Object> renderer;

		public CustomListRenderer() {
			this.renderer = new DefaultListCellRenderer();
		}

		@Override
		public Component getListCellRendererComponent(
			JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus
		) {
			final CustomListModel model = (CustomListModel) list.getModel();
			
			if (PathSelectionPanel.this.isHideCommonPath()) {
				final String commonPath = model.getCommonPath();
				
				if (commonPath.length() > 1)
					value = "+" + value.replaceFirst(Pattern.quote(commonPath), "");
			}
			
			return this.renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(
			() -> {
				final JFrame frame = new JFrame("File Selection");

				frame.setContentPane(new PathSelectionPanel());
				frame.setSize(new Dimension(800, 600));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);

				frame.setVisible(true);
			}
		);
	}
}
