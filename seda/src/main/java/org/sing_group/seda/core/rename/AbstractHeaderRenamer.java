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
package org.sing_group.seda.core.rename;

import java.util.List;
import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;

public abstract class AbstractHeaderRenamer implements HeaderRenamer {
  private HeaderTarget target;
  private SequencesGroupBuilder groupBuilder;
  private SequenceBuilder sequenceBuilder;

  public AbstractHeaderRenamer(HeaderTarget target, DatatypeFactory factory) {
    this.target = target;
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
  }

  protected SequencesGroup buildSequencesGroup(String name, Map<String, Object> properties,
      List<Sequence> renamedSequences
  ) {
    return this.groupBuilder.of(name, properties, renamedSequences);
  }

  protected Sequence renameSequence(Sequence sequence, String renamedPart) {
    String newHeader;
    switch (this.target) {
      case ALL:
        newHeader = renamedPart;
        break;
      case DESCRIPTION:
        newHeader = sequence.getName() + " " + renamedPart;
        break;
      case NAME:
        newHeader = renamedPart + " " + sequence.getDescription();
        break;
      default:
        throw new IllegalStateException("Unknown header target");
    }

    int firstBlankSpace = newHeader.indexOf(" ");
    if (firstBlankSpace == -1) {
      return this.sequenceBuilder.of(newHeader, "", sequence.getChain(), sequence.getProperties());
    } else {
      String newName = newHeader.substring(0, firstBlankSpace);
      String newDescription = newHeader.substring(firstBlankSpace + 1);

      return this.sequenceBuilder.of(newName, newDescription, sequence.getChain(), sequence.getProperties());
    }
  }

  protected String getRenamePart(Sequence sequence) {
    switch (this.target) {
      case ALL:
        return sequence.getName() + " " + sequence.getDescription();
      case DESCRIPTION:
        return sequence.getDescription();
      case NAME:
        return sequence.getName();
      default:
        break;
    }
    throw new IllegalStateException("Unknown header target");
  }
}
