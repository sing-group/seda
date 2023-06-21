/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.statistics;

import org.jdesktop.swingx.JXTextArea;
import org.sing_group.seda.datatype.SequencesGroup;

public class SequencesGroupFastaViewer extends JXTextArea {
  private static final long serialVersionUID = 1L;

  public SequencesGroupFastaViewer(SequencesGroup group) {
    super();
    this.setColumns(120);
    this.setRows(60);
    this.setText(toFasta(group));
  }

  private String toFasta(SequencesGroup group) {
    StringBuilder sb = new StringBuilder();
    group.getSequences()
      .forEach(s -> sb.append(s.getHeader()).append("\n").append(s.getChain()).append("\n"));

    return sb.toString();
  }
}
