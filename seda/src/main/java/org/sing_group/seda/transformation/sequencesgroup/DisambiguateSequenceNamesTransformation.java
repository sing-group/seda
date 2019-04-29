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
package org.sing_group.seda.transformation.sequencesgroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.util.StringUtils;

public class DisambiguateSequenceNamesTransformation implements SequencesGroupTransformation {
  private final SequenceBuilder seqBuilder;
  private final SequencesGroupBuilder builder;
  private Mode mode;
  
  public enum Mode {
    RENAME, REMOVE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }

  };

  public DisambiguateSequenceNamesTransformation(Mode mode) {
    this(mode, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public DisambiguateSequenceNamesTransformation(Mode mode, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroup;
    this.seqBuilder = factory::newSequence;
    this.mode = mode;
  }
  
  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    List<Sequence> newSequences = new ArrayList<>(sequencesGroup.getSequenceCount());

    Map<String, List<Sequence>> nameToSequences = new HashMap<>();

    sequencesGroup.getSequences().forEach(
      sequence -> {
        nameToSequences.putIfAbsent(sequence.getName(), new LinkedList<>());
        nameToSequences.get(sequence.getName()).add(sequence);
      }
    );

    boolean removeMode = mode.equals(Mode.REMOVE);

    for (Entry<String, List<Sequence>> entry : nameToSequences.entrySet()) {
      int i = 1;
      boolean addIndex = entry.getValue().size() > 1 && !removeMode;
      for (Sequence s : entry.getValue()) {
        String newSequenceName = s.getName();
        if (addIndex) {
          newSequenceName += "_" + (i++);
        }

        newSequences.add(seqBuilder.of(newSequenceName, s.getDescription(), s.getChain(), s.getProperties()));

        if (removeMode) {
          break;
        }
      }
    }

    return this.builder.of(sequencesGroup.getName(), sequencesGroup.getProperties(), newSequences);
  }
}
