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
package org.sing_group.seda.transformation.dataset;

import java.util.function.Function;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public class SequenceCountFilterSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final int minSequences;
  private final int maxSequences;


  public SequenceCountFilterSequencesGroupDatasetTransformation(int minSequences, int maxSequences) {
    this(minSequences, maxSequences, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SequenceCountFilterSequencesGroupDatasetTransformation(
    int minSequences, int maxSequences, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroupDataset;
    this.minSequences = minSequences;
    this.maxSequences = maxSequences;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final SequencesGroup[] sequencesGroups = dataset.getSequencesGroups()
      .filter(
        sequenceGroup -> {

          if (minSequences > 0 && sequenceGroup.getSequenceCount() < minSequences) {
            return false;
          }

          if (maxSequences > 0 && sequenceGroup.getSequenceCount() > maxSequences) {
            return false;
          }

          return true;
        }

      )
      .toArray(SequencesGroup[]::new);

    if (sequencesGroups.length == 0)
      throw new TransformationException("No sequences after filtering");

    return this.builder.apply(sequencesGroups);
  }
}
