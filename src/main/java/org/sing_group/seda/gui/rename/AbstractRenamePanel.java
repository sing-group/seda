package org.sing_group.seda.gui.rename;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.sing_group.gc4s.event.DocumentAdapter;

public abstract class AbstractRenamePanel extends JPanel implements RenameConfigurationPanel {
  private static final long serialVersionUID = 1L;

  protected DocumentListener documentListener = new DocumentAdapter() {

    @Override
    public void removeUpdate(DocumentEvent e) {
      renameConfigurationChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      renameConfigurationChanged();
    }
  };

  protected void renameConfigurationChanged() {
    this.fireRenameConfigurationChanged();
  }

  protected void fireRenameConfigurationChanged() {
    for (RenamePanelEventListener l : this.getRenamePanelEventListeners()) {
      l.onRenameConfigurationChanged(this);
    }
  }

  public void addRenamePanelEventListener(RenamePanelEventListener listener) {
    this.listenerList.add(RenamePanelEventListener.class, listener);
  }

  public void removeRenamePanelEventListener(RenamePanelEventListener listener) {
    this.listenerList.remove(RenamePanelEventListener.class, listener);
  }

  public RenamePanelEventListener[] getRenamePanelEventListeners() {
    return this.listenerList.getListeners(RenamePanelEventListener.class);
  }
}
