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
package org.sing_group.seda.core.rename;

import java.util.LinkedList;
import java.util.List;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.util.StringUtils;

public class AddStringHeaderRenamer extends AbstractHeaderRenamer {

  public enum Position {
    PREFIX, SUFFIX, OVERRIDE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  private String string;
  private String delimiter;
  private Position position;
  private boolean addIndex;
  private String indexDelimiter;

  public AddStringHeaderRenamer(
    DatatypeFactory factory, HeaderTarget target, String string, String delimiter, Position position
  ) {
    this(factory, target, string, delimiter, position, false, "");
  }

  public AddStringHeaderRenamer(
    DatatypeFactory factory, HeaderTarget target, String string, String delimiter, Position position, boolean addIndex,
    String indexDelimiter
  ) {
    super(target, factory);

    this.string = string;
    this.delimiter = delimiter;
    this.position = position;
    this.addIndex = addIndex;
    this.indexDelimiter = indexDelimiter;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      String partToAdd = string;
      if (addIndex) {
        partToAdd = partToAdd + this.indexDelimiter + String.valueOf(i + 1);
      }

      String renamedPart;
      if (this.position.equals(Position.PREFIX)) {
        renamedPart = partToAdd + this.delimiter + partToRename;
      } else if (this.position.equals(Position.SUFFIX)) {
        renamedPart = partToRename + this.delimiter + partToAdd;
      } else {
        renamedPart = partToAdd;
      }

      renamedSequences.add(renameSequence(original, renamedPart));
    }

    return buildSequencesGroup(sequences.getName(), sequences.getProperties(), renamedSequences);
  }
}
