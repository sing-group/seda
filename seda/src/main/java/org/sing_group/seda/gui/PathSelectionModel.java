/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static java.util.Arrays.stream;
import static java.util.Collections.sort;
import static org.sing_group.seda.gui.PathSelectionModelEvent.of;
import static org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType.ADD_AVAILABLE;
import static org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType.ADD_SELECTED;
import static org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType.CLEAR_AVAILABLE;
import static org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType.CLEAR_SELECTED;
import static org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType.REMOVE_AVAILABLE;
import static org.sing_group.seda.gui.PathSelectionModelEvent.FileSelectionEventType.REMOVE_SELECTED;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathSelectionModel {
  private final List<String> availablePaths;
  private final List<String> selectedPaths;

  private final List<PathSelectionModelListener> listeners;

  public PathSelectionModel() {
    this.availablePaths = new ArrayList<>();
    this.selectedPaths = new ArrayList<>();

    this.listeners = new CopyOnWriteArrayList<>();
  }

  public Stream<String> getAvailablePaths() {
    return this.availablePaths.stream();
  }

  public Stream<String> getSelectedPaths() {
    return this.selectedPaths.stream();
  }

  public int countAvailablePaths() {
    return this.availablePaths.size();
  }

  public int countSelectedPaths() {
    return this.selectedPaths.size();
  }

  public String getAvailablePath(int index) {
    return this.availablePaths.get(index);
  }

  public String getSelectedPath(int index) {
    return this.selectedPaths.get(index);
  }

  public void clearAvailablePaths() {
    final int lastIndex = this.availablePaths.size() - 1;
    this.availablePaths.clear();
    firePathSelectionPanelModelEvent(of(CLEAR_AVAILABLE, lastIndex));
  }

  public void clearSelectedPaths() {
    final int lastIndex = this.selectedPaths.size() - 1;
    this.selectedPaths.clear();
    firePathSelectionPanelModelEvent(of(CLEAR_SELECTED, lastIndex));
  }

  public void addAvailablePath(Path path) {
    this.addAvailablePath(path.toAbsolutePath().toString());
  }

  public void addAvailablePath(String path) {
    if (!this.selectedPaths.contains(path) && !this.availablePaths.contains(path)) {
      this.availablePaths.add(path);
      sort(this.availablePaths);

      firePathSelectionPanelModelEvent(of(
        ADD_AVAILABLE, path, this.availablePaths.indexOf(path)
      ));
    }
  }

  public void addAvailablePaths(Path ... paths) {
    stream(paths).forEach(this::addAvailablePath);
  }

  public void addAvailablePaths(String ... paths) {
    stream(paths).forEach(this::addAvailablePath);
  }

  public void selectPath(Path path) {
    this.selectPath(path.toAbsolutePath().toString());
  }

  public void selectPath(String path) {
    if (this.availablePaths.contains(path)) {
      final int index = this.availablePaths.indexOf(path);
      this.availablePaths.remove(path);
      firePathSelectionPanelModelEvent(of(
        REMOVE_AVAILABLE, path, index
      ));

      this.selectedPaths.add(path);
      sort(this.selectedPaths);

      firePathSelectionPanelModelEvent(of(
        ADD_SELECTED, path, this.selectedPaths.indexOf(path)
      ));
    }
  }

  public void selectPaths(Path ... paths) {
    stream(paths).forEach(this::selectPath);
  }

  public void selectPaths(String ... paths) {
    stream(paths).forEach(this::selectPath);
  }

  public void removeSelectedPath(Path path) {
    this.removeSelectedPath(path.toAbsolutePath().toString());
  }

  public void removeSelectedPath(String path) {
    if (this.selectedPaths.contains(path)) {
      final int index = this.selectedPaths.indexOf(path);

      this.selectedPaths.remove(path);
      firePathSelectionPanelModelEvent(of(REMOVE_SELECTED, path, index));

      this.addAvailablePath(path);
    }
  }

  public void removeSelectedPaths(Path ... paths) {
    stream(paths).forEach(this::removeSelectedPath);
  }

  public void removeSelectedPaths(String ... paths) {
    stream(paths).forEach(this::removeSelectedPath);
  }

  public void addPathSelectionModelListener(PathSelectionModelListener listener) {
    if (!this.listeners.contains(listener))
      this.listeners.add(listener);
  }

  public boolean removePathSelectionModelListener(PathSelectionModelListener listener) {
    return this.listeners.remove(listener);
  }

  public boolean containsPathSelectionModelListener(PathSelectionModelListener listener) {
    return this.listeners.contains(listener);
  }

  private void firePathSelectionPanelModelEvent(PathSelectionModelEvent event) {
    this.listeners.forEach(listener -> listener.modelChanged(event));
  }

  public void saveAvailablePaths(Path path) throws IOException {
    writeToFile(path, this.getAvailablePaths().collect(Collectors.joining("\n")));
  }

  public void saveSelectedPaths(Path path) throws IOException {
    writeToFile(path, this.getSelectedPaths().collect(Collectors.joining("\n")));
  }

  private void writeToFile(Path path, String str) throws IOException {
    Files.write(path, str.getBytes());
  }

}
