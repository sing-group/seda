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

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.sing_group.gc4s.input.filechooser.ExtensionFileFilter;
import org.sing_group.gc4s.utilities.FileDrop;
import org.sing_group.gc4s.utilities.FileDropListener;
import org.sing_group.seda.core.ncbi.NcbiDatasetProcessor;
import org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType;
import org.sing_group.seda.io.IOUtils;

public class PathSelectionPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final ImageIcon ICON_FILES = 
    new ImageIcon(PathSelectionPanel.class.getResource("image/files.png"));
  private static final ImageIcon ICON_FILE_TXT =
    new ImageIcon(PathSelectionPanel.class.getResource("image/file-txt.png"));
  private static final ImageIcon ICON_FILE_ZIP =
    new ImageIcon(PathSelectionPanel.class.getResource("image/file-zip.png"));
  private static final ImageIcon ICON_FOLDER = 
    new ImageIcon(PathSelectionPanel.class.getResource("image/folder.png"));
  private static final ImageIcon ICON_ARROW_LEFT =
    new ImageIcon(PathSelectionPanel.class.getResource("image/arrow-left.png"));
  private static final ImageIcon ICON_ARROWS_LEFT =
    new ImageIcon(PathSelectionPanel.class.getResource("image/arrows-left.png"));
  private static final ImageIcon ICON_ARROW_RIGHT =
    new ImageIcon(PathSelectionPanel.class.getResource("image/arrow-right.png"));
  private static final ImageIcon ICON_ARROWS_RIGHT =
    new ImageIcon(PathSelectionPanel.class.getResource("image/arrows-right.png"));

  private static final List<FileFilter> FILE_FILTERS =
    asList(
      new ExtensionFileFilter(".*\\.txt", "TXT files", false),
      new ExtensionFileFilter(".*\\.fasta|.*\\.fa", "FASTA files", false),
      new ExtensionFileFilter(
        ".*\\.fasta.gz|.*\\\\.fasta.gzip|.*\\\\.fa.gz|.*\\.fa.gzip", "Compressed FASTA files", false
      ),
      new ExtensionFileFilter(".*\\.gz|.*\\\\.gzip", "Gzip files", false)
    );

  private final JLabel lblAvailable;
  private final JLabel lblSelected;
  private final JFileChooser fileChooser;
  private final PathSelectionModel model;
  private final JCheckBox chkRecursiveSearch;
  private final JCheckBox chkHideCommonPath;
  private final JList<String> listAvailableFiles;
  private final JList<String> listSelectedFiles;

  public PathSelectionPanel() {
    super(new BorderLayout());

    this.model = new PathSelectionModel();

    this.listAvailableFiles =
      new JList<String>(
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
    this.listSelectedFiles =
      new JList<String>(
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

    new FileDrop(this.listAvailableFiles, new FileDropListener() {
      public void filesDropped(File[] files) {
        asList(files).stream().filter(File::isFile).forEach(f -> model.addAvailablePath(f.getAbsolutePath()));
      }
    });

    listAvailableFiles.setCellRenderer(new CustomListRenderer());
    listSelectedFiles.setCellRenderer(new CustomListRenderer());

    final JButton btnLoadDirectory = new JButton("Load from directory", ICON_FOLDER);
    final JButton btnLoadFiles = new JButton("Load files", ICON_FILES);
    final JButton btnLoadFileList = new JButton("Load file list", ICON_FILE_TXT);
    final JButton btnLoadNcbiDataset = new JButton("Load NCBI file", ICON_FILE_ZIP);
    btnLoadNcbiDataset.setToolTipText("Loads an NCBI ZIP file containing a dataset download");

    this.chkRecursiveSearch = new JCheckBox("Recursive load from directory", false);
    this.chkHideCommonPath = new JCheckBox("Hide common path", true);

    final JButton btnAddAll = new JButton(ICON_ARROWS_RIGHT);
    final JButton btnAdd = new JButton(ICON_ARROW_RIGHT);
    final JButton btnRemove = new JButton(ICON_ARROW_LEFT);
    final JButton btnRemoveAll = new JButton(ICON_ARROWS_LEFT);

    final JButton btnClearAvailable = new JButton("Clear available list");
    final JButton btnSaveAvailableList = new JButton("Save available list");
    final JButton btnClearSelected = new JButton("Clear selected list");
    final JButton btnSaveSelectedList = new JButton("Save selected list");

    this.fileChooser = CommonFileChooser.getInstance().getFilechooser();
    this.fileChooser.setMultiSelectionEnabled(true);

    final JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(false);

    toolBar.add(btnLoadDirectory);
    toolBar.add(btnLoadFiles);
    toolBar.add(btnLoadFileList);
    toolBar.add(btnLoadNcbiDataset);
    toolBar.addSeparator();
    toolBar.add(this.chkRecursiveSearch);
    toolBar.add(this.chkHideCommonPath);

    JPanel panelAvailableButtons = new JPanel(new GridLayout(1, 2));
    panelAvailableButtons.add(btnClearAvailable);
    panelAvailableButtons.add(btnSaveAvailableList);

    final JPanel panelAvailable = new JPanel(new BorderLayout(0, 4));
    this.lblAvailable = new JLabel("Available Files");
    lblAvailable.setBorder(BorderFactory.createEmptyBorder(7, 2, 2, 2));
    panelAvailable.add(lblAvailable, BorderLayout.NORTH);
    panelAvailable.add(new JScrollPane(listAvailableFiles), BorderLayout.CENTER);
    panelAvailable.add(panelAvailableButtons, BorderLayout.SOUTH);

    JPanel panelSelectedButtons = new JPanel(new GridLayout(1, 2));
    panelSelectedButtons.add(btnClearSelected);
    panelSelectedButtons.add(btnSaveSelectedList);

    final JPanel panelSelected = new JPanel(new BorderLayout(0, 4));
    this.lblSelected = new JLabel("Selected Files");
    lblSelected.setBorder(BorderFactory.createEmptyBorder(7, 2, 2, 2));
    panelSelected.add(lblSelected, BorderLayout.NORTH);
    panelSelected.add(new JScrollPane(this.listSelectedFiles), BorderLayout.CENTER);
    panelSelected.add(panelSelectedButtons, BorderLayout.SOUTH);

    final JPanel panelCentralButtons = new JPanel();
    final BoxLayout layout = new BoxLayout(panelCentralButtons, BoxLayout.Y_AXIS);
    panelCentralButtons.setLayout(layout);

    panelCentralButtons.add(btnAddAll);
    panelCentralButtons.add(Box.createVerticalStrut(5));
    panelCentralButtons.add(btnAdd);
    panelCentralButtons.add(Box.createVerticalStrut(1));
    panelCentralButtons.add(btnRemove);
    panelCentralButtons.add(Box.createVerticalStrut(5));
    panelCentralButtons.add(btnRemoveAll);

    final JPanel panelCentral = new JPanel(new GridBagLayout());
    final GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weighty = 1d;
    gbc.weightx = 1d;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.BOTH;
    panelCentral.add(panelAvailable, gbc);

    gbc.gridx = 1;
    gbc.weightx = 0d;
    gbc.fill = GridBagConstraints.NONE;
    panelCentral.add(panelCentralButtons, gbc);

    gbc.gridx = 2;
    gbc.weightx = 1d;
    gbc.fill = GridBagConstraints.BOTH;
    panelCentral.add(panelSelected, gbc);

    this.add(toolBar, BorderLayout.NORTH);
    this.add(panelCentral, BorderLayout.CENTER);

    btnLoadDirectory.addActionListener(e -> this.loadDirectory());
    btnLoadFiles.addActionListener(e -> this.loadFile());
    btnLoadFileList.addActionListener(e -> this.loadFileList());
    btnLoadNcbiDataset.addActionListener(e -> this.loadNcbiDataset());

    btnAddAll.addActionListener(e -> this.selectAllFiles());
    btnAdd.addActionListener(e -> this.selectFiles());
    btnRemove.addActionListener(e -> this.unselectFiles());
    btnRemoveAll.addActionListener(e -> this.unselectAllFiles());

    btnClearAvailable.addActionListener(e -> this.model.clearAvailablePaths());
    btnSaveAvailableList.addActionListener(e -> this.saveAvailablePaths());
    btnClearSelected.addActionListener(e -> this.model.clearSelectedPaths());
    btnSaveSelectedList.addActionListener(e -> this.saveSelectedPaths());

    this.chkHideCommonPath.addItemListener(e -> {
      listAvailableFiles.updateUI();
      listSelectedFiles.updateUI();
    });

    this.updateAvailableAndSelectedLabels();

    this.model.addPathSelectionModelListener(e -> {
      updateAvailableAndSelectedLabels();
    });
  }

  private void saveAvailablePaths() {
    showFileChooserAndProcess(
      JFileChooser.FILES_ONLY, JFileChooser.SAVE_DIALOG, false, path -> {
        try {
          this.model.saveAvailablePaths(path);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    );
  }

  private void saveSelectedPaths() {
    showFileChooserAndProcess(
      JFileChooser.FILES_ONLY, JFileChooser.SAVE_DIALOG, false, path -> {
        try {
          this.model.saveSelectedPaths(path);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    );
  }

  public PathSelectionModel getModel() {
    return model;
  }

  private boolean isRecursiveSearch() {
    return this.chkRecursiveSearch.isSelected();
  }

  private boolean isHideCommonPath() {
    return this.chkHideCommonPath.isSelected();
  }

  private void selectAllFiles() {
    this.model.selectPaths(this.model.getAvailablePaths().toArray(String[]::new));
  }

  private void selectFiles() {
    final List<String> selectedFiles = this.listAvailableFiles.getSelectedValuesList();

    this.model.selectPaths(selectedFiles.stream().toArray(String[]::new));
  }

  private void unselectFiles() {
    final List<String> selectedFiles = this.listSelectedFiles.getSelectedValuesList();

    this.model.removeSelectedPaths(selectedFiles.stream().toArray(String[]::new));
  }

  private void unselectAllFiles() {
    this.model.removeSelectedPaths(this.model.getSelectedPaths().toArray(String[]::new));
  }

  private void loadFileList() {
    showFileChooserAndProcess(
      JFileChooser.FILES_ONLY, JFileChooser.OPEN_DIALOG, false, path -> {
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
    showFileChooserAndProcess(
      JFileChooser.FILES_ONLY, JFileChooser.OPEN_DIALOG, true, model::addAvailablePath, FILE_FILTERS
    );
  }

  private void loadDirectory() {
    showFileChooserAndProcess(
      JFileChooser.DIRECTORIES_ONLY, JFileChooser.OPEN_DIALOG, true, directory -> {
        try {
          if (this.isRecursiveSearch()) {
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

  private void loadNcbiDataset() {
    final JDialog dialog = new WorkingDialog(
        (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this),
        "Processing NCBI ZIP file", "Processing NCBI ZIP file");

    new CustomSwingWorker(() -> {
      showFileChooserAndProcess(
          JFileChooser.FILES_ONLY, JFileChooser.OPEN_DIALOG, true, zipFile -> {
            System.out.println(zipFile);
            try {
              new NcbiDatasetProcessor(zipFile.toFile())
                  .process(IOUtils.createSedaUserHomeDirectory("SEDA_NCBI_datasets"))
                  .forEach(f -> model.addAvailablePath(f.toPath()));
            } catch (IOException e) {
              e.printStackTrace();
            }
            dialog.dispose();
          },
          () -> {
            dialog.dispose();
          });
    }).execute();

    dialog.setVisible(true);
  }

  private void updateAvailableAndSelectedLabels() {
    this.lblAvailable.setText(String.format("Available Files (%d)", this.getModel().countAvailablePaths()));
    this.lblSelected.setText(String.format("Selected Files (%d)", this.getModel().countSelectedPaths()));
  }

  private void showFileChooserAndProcess(
    int selectionMode, int dialogMode, boolean multipleSelection, Consumer<Path> pathProcesser
  ) {
    showFileChooserAndProcess(selectionMode, dialogMode, multipleSelection, pathProcesser, emptyList(), () -> {});
  }

  private void showFileChooserAndProcess(
    int selectionMode, int dialogMode, boolean multipleSelection, Consumer<Path> pathProcesser, Runnable onCancel
  ) {
    showFileChooserAndProcess(selectionMode, dialogMode, multipleSelection, pathProcesser, emptyList(), onCancel);
  }

  private void showFileChooserAndProcess(
    int selectionMode, int dialogMode, boolean multipleSelection, Consumer<Path> pathProcesser,
    List<FileFilter> fileFilters
  ) {
    GuiUtils.showFileChooserAndProcess(
      this.fileChooser, this, selectionMode, dialogMode, multipleSelection, fileFilters, pathProcesser, () -> {}
    );
  }

  private void showFileChooserAndProcess(
    int selectionMode, int dialogMode, boolean multipleSelection, Consumer<Path> pathProcesser,
    List<FileFilter> fileFilters, Runnable onCancel
  ) {
    GuiUtils.showFileChooserAndProcess(
      this.fileChooser, this, selectionMode, dialogMode, multipleSelection, fileFilters, pathProcesser, onCancel
    );
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
        final String path = this.getElementAt(0);
        this.commonPrefix = getDirectory(path);
      } else {
        this.commonPrefix =
          this.getPathsFunction.get()
            .reduce("", (p1, p2) -> {
              if (p1.isEmpty())
                return p2;
              if (p2.isEmpty())
                return p1;

              String p1ForPrefix = getDirectory(p1);
              String p2ForPrefix = getDirectory(p2);

              String commonPrefix = "";
              for (int i = 0; i < Math.min(p1ForPrefix.length(), p2ForPrefix.length()); i++) {
                if (p1ForPrefix.charAt(i) == p2ForPrefix.charAt(i)) {
                  commonPrefix += p1ForPrefix.charAt(i);
                } else {
                  break;
                }
              }

              return commonPrefix;
            }
            );
      }
    }
  }

  private static String getDirectory(String path) {
    return path.substring(0, path.lastIndexOf(File.separatorChar) + 1);
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

      Component component =
        this.renderer.getListCellRendererComponent(list, getCellValue(list, value), index, isSelected, cellHasFocus);

      if (component instanceof JComponent) {
        ((JComponent) component).setToolTipText(value);
      }

      return component;
    }

    private String getCellValue(JList<? extends String> list, String value) {
      final CustomListModel model = (CustomListModel) list.getModel();

      if (PathSelectionPanel.this.isHideCommonPath()) {
        final String commonPath = model.getCommonPath();

        if (commonPath != null && commonPath.length() > 1) {
          return "+" + value.replaceFirst(Pattern.quote(commonPath), "");
        }
      }

      return value;

    }
  }
}
