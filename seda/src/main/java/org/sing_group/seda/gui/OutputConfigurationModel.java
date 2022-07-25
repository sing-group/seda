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

import static java.lang.System.getProperty;
import static java.util.Objects.requireNonNull;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.of;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.IN_MEMORY_PROCESSING_ENABLED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.OUTPUT_DIRECTORY_CHANGED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.SPLIT_INTO_SUBDIRECTORIES_CHANGED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.SUBDIRECTORIES_SIZE_CHANGED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.WRITE_GZIP;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.sing_group.seda.util.SedaProperties;

public class OutputConfigurationModel {
  public static final boolean DEFAULT_IN_MEMORY_PROCESSING = true;
  public static final boolean DEFAULT_WRITE_GZIP = false;

  private Path outputDirectory;
  private boolean splitInSubdirectories;
  private boolean inMemoryProcessingEnabled = DEFAULT_IN_MEMORY_PROCESSING;
  private int subdirectorySize;
  private boolean writeGzip = DEFAULT_WRITE_GZIP;

  private final List<OutputConfigurationModelListener> listeners;

  public OutputConfigurationModel() {
    this.outputDirectory = Paths.get(getInitialOutputDirectory()).toAbsolutePath();
    this.splitInSubdirectories = false;
    this.subdirectorySize = 40;

    this.listeners = new CopyOnWriteArrayList<>();
  }

  private String getInitialOutputDirectory() {
    return getProperty(SedaProperties.PROPERTY_OUTPUT_DIRECTORY, getProperty("user.home"));
  }

  public String getOutputDirectoryPath() {
    return outputDirectory.toAbsolutePath().toString();
  }

  public Path getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(Path outputDirectory) {
    requireNonNull(outputDirectory);

    if (!outputDirectory.isAbsolute())
      outputDirectory = outputDirectory.toAbsolutePath();

    if (!this.outputDirectory.equals(outputDirectory)) {
      this.outputDirectory = outputDirectory;

      this.fireOutputConfigurationModelEvent(of(OUTPUT_DIRECTORY_CHANGED, this.outputDirectory));
    }
  }

  public boolean isSplitInSubdirectories() {
    return splitInSubdirectories;
  }

  public void setSplitInSubdirectories(boolean splitInSubdirectories) {
    if (this.splitInSubdirectories != splitInSubdirectories) {
      this.splitInSubdirectories = splitInSubdirectories;

      this.fireOutputConfigurationModelEvent(of(SPLIT_INTO_SUBDIRECTORIES_CHANGED, this.splitInSubdirectories));
    }
  }

  public int getSubdirectorySize() {
    return subdirectorySize;
  }

  public void setSubdirectorySize(int subdirectorySize) {
    if (this.subdirectorySize != subdirectorySize) {
      this.subdirectorySize = subdirectorySize;

      this.fireOutputConfigurationModelEvent(of(SUBDIRECTORIES_SIZE_CHANGED, this.subdirectorySize));
    }
  }

  public boolean isInMemoryProcessingEnabled() {
    return inMemoryProcessingEnabled;
  }

  public void setInMemoryProcessingEnabled(boolean inMemoryProcessingEnabled) {
    if (this.inMemoryProcessingEnabled != inMemoryProcessingEnabled) {
      this.inMemoryProcessingEnabled = inMemoryProcessingEnabled;

      this.fireOutputConfigurationModelEvent(of(IN_MEMORY_PROCESSING_ENABLED, this.inMemoryProcessingEnabled));
    }
  }

  public boolean isWriteGzip() {
    return writeGzip;
  }

  public void setWriteGzip(boolean writeGzip) {
    if (this.writeGzip != writeGzip) {
      this.writeGzip = writeGzip;
      this.fireOutputConfigurationModelEvent(of(WRITE_GZIP, this.inMemoryProcessingEnabled));
    }
  }

  public void addOutputConfigurationModelListener(OutputConfigurationModelListener listener) {
    if (!this.listeners.contains(listener))
      this.listeners.add(listener);
  }

  public boolean removeOutputConfigurationModelListener(OutputConfigurationModelListener listener) {
    return this.listeners.remove(listener);
  }

  public boolean containsOutputConfigurationModelListener(OutputConfigurationModelListener listener) {
    return this.listeners.contains(listener);
  }

  private void fireOutputConfigurationModelEvent(OutputConfigurationModelEvent event) {
    this.listeners.forEach(listener -> listener.configurationChanged(event));
  }
}
