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
package org.sing_group.seda.core.rename;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.util.StringUtils;

@XmlRootElement
public class AddStringHeaderRenamer extends AbstractHeaderRenamer {

  public enum Position {
    PREFIX, SUFFIX, OVERRIDE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  @XmlElement
  private String string;
  @XmlElement
  private String delimiter;
  @XmlElement
  private Position position;
  @XmlElement
  private boolean addIndex;
  @XmlElement
  private String indexDelimiter;
  @XmlElement
  private int startIndex;

  public AddStringHeaderRenamer() {
    super(HeaderTarget.ALL);
  }

  public AddStringHeaderRenamer(HeaderTarget target, String string, String delimiter, Position position) {
    this(target, string, delimiter, position, false, "", 0);
  }

  public AddStringHeaderRenamer(
    HeaderTarget target, String string, String delimiter, Position position, boolean addIndex, String indexDelimiter
  ) {
    this(target, string, delimiter, position, addIndex, indexDelimiter, 1);
  }
  public AddStringHeaderRenamer(
    HeaderTarget target, String string, String delimiter, Position position, boolean addIndex, String indexDelimiter, int startIndex
  ) {
    super(target);

    this.string = string;
    this.delimiter = delimiter;
    this.position = position;
    this.addIndex = addIndex;
    this.indexDelimiter = indexDelimiter;
    this.startIndex = startIndex;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences, DatatypeFactory factory) {
    List<Sequence> renamedSequences = new LinkedList<>();

    int start = this.startIndex;
    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      String partToAdd = string;
      if (addIndex) {
        partToAdd = partToAdd + this.indexDelimiter + String.valueOf(start++);
      }

      String renamedPart;
      if (this.position.equals(Position.PREFIX)) {
        renamedPart = partToAdd + this.delimiter + partToRename;
      } else if (this.position.equals(Position.SUFFIX)) {
        renamedPart = partToRename + this.delimiter + partToAdd;
      } else {
        renamedPart = partToAdd;
      }

      renamedSequences.add(renameSequence(original, renamedPart, factory));
    }

    return buildSequencesGroup(sequences.getName(), sequences.getProperties(), renamedSequences, factory);
  }

  public String getString() {
    return string;
  }

  public String getDelimiter() {
    return delimiter;
  }

  public Position getPosition() {
    return position;
  }

  public boolean isAddIndex() {
    return addIndex;
  }

  public String getIndexDelimiter() {
    return indexDelimiter;
  }

  public int getStartIndex() {
    return startIndex;
  }
}
