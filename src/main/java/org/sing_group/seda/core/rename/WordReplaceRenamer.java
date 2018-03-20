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

public class WordReplaceRenamer extends AbstractHeaderRenamer {

  private String replacement;
  private List<String> targets;
  private boolean regex;

  public WordReplaceRenamer(
    DatatypeFactory factory, HeaderTarget target, String replacement, boolean regex, List<String> targets
  ) {
    super(target, factory);

    this.replacement = replacement;
    this.targets = targets;
    this.regex = regex;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      for (String target : targets) {
        if (regex) {
          partToRename = partToRename.replaceAll(target, replacement);
        } else {
          partToRename = partToRename.replace(target, replacement);
        }
      }

      renamedSequences.add(renameSequence(original, partToRename));
    }

    return buildSequencesGroup(sequences.getName(), renamedSequences);
  }
}