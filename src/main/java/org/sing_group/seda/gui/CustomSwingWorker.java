package org.sing_group.seda.gui;

import java.util.Objects;

import javax.swing.SwingWorker;

public class CustomSwingWorker extends SwingWorker<Void, Void> {
  private final Runnable task;

  public CustomSwingWorker(Runnable task) {
    this.task = Objects.requireNonNull(task);
  }

  @Override
  protected Void doInBackground() throws Exception {
    this.task.run();

    return null;
  }
}