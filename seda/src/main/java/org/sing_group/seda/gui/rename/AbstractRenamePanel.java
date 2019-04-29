/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
