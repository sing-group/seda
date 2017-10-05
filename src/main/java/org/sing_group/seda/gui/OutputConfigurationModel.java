package org.sing_group.seda.gui;

import static java.util.Objects.requireNonNull;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.of;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.IN_MEMORY_PROCESSING_ENABLED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.OUTPUT_DIRECTORY_CHANGED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.SPLIT_INTO_SUBDIRECTORIES_CHANGED;
import static org.sing_group.seda.gui.OutputConfigurationModelEvent.OutputConfigurationModelEventType.SUBDIRECTORIES_SIZE_CHANGED;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OutputConfigurationModel {
  public static final boolean DEFAULT_IN_MEMORY_PROCESSING = true;

  private Path outputDirectory;
  private boolean splitInSubdirectories;
  private boolean inMemoryProcessingEnabled = DEFAULT_IN_MEMORY_PROCESSING;
  private int subdirectorySize;

  private final List<OutputConfigurationModelListener> listeners;
  
  public OutputConfigurationModel() {
    this.outputDirectory = Paths.get(System.getProperty("user.home")).toAbsolutePath();
    this.splitInSubdirectories = false;
    this.subdirectorySize = 40;
    
    this.listeners = new CopyOnWriteArrayList<>();
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

      this.fireOutputConfigurationModelEvent(of(IN_MEMORY_PROCESSING_ENABLED, this.subdirectorySize));
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
