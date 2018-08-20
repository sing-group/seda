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
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.util.StringUtils;

public class FieldSplitRenamer extends AbstractHeaderRenamer {

  public enum Mode {
    KEEP, REMOVE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  private String fieldDelimiter;
  private String joinDelimiter;
  private Mode mode;
  private Set<Integer> fields;;

  public FieldSplitRenamer(
    DatatypeFactory factory, HeaderTarget target, String fieldDelimiter, String joinDelimiter, Mode mode,
    Set<Integer> fields
  ) {
    super(target, factory);

    this.fieldDelimiter = fieldDelimiter;
    this.joinDelimiter = joinDelimiter;
    this.mode = mode;
    this.fields = fields;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      String[] parts = partToRename.split(Pattern.quote(fieldDelimiter));

      List<String> newFields = new LinkedList<>();

      for (int field = 0; field < parts.length; field++) {
        if (this.mode.equals(Mode.KEEP)) {
          if (fields.contains(field)) {
            newFields.add(parts[field]);
          }
        } else if (this.mode.equals(Mode.REMOVE)) {
          if (!fields.contains(field)) {
            newFields.add(parts[field]);
          }
        }
      }

      String renamedPart = newFields.stream().collect(Collectors.joining(joinDelimiter));

      renamedSequences.add(renameSequence(original, renamedPart));
    }

    return buildSequencesGroup(sequences.getName(), sequences.getProperties(), renamedSequences);
  }
}
