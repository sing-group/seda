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
package org.sing_group.seda.transformation.dataset;

import static org.sing_group.seda.bio.FunctionUtil.wrapWithExceptionToNull;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class ComposedSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final SequencesGroupTransformation[] transformations;
  
  public ComposedSequencesGroupDatasetTransformation(SequencesGroupTransformation... transformations) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), transformations);
  }

  public ComposedSequencesGroupDatasetTransformation(DatatypeFactory factory, SequencesGroupTransformation ... transformations) {
    this.builder = factory::newSequencesGroupDataset;
    this.transformations = transformations;
  }
  
  public ComposedSequencesGroupDatasetTransformation(Collection<SequencesGroupTransformation> transformations) {
    this(transformations.stream().toArray(SequencesGroupTransformation[]::new));
  }
  
  public ComposedSequencesGroupDatasetTransformation(DatatypeFactory factory, Collection<SequencesGroupTransformation> transformations) {
    this(factory, transformations.stream().toArray(SequencesGroupTransformation[]::new));
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) throws TransformationException {
    Stream<SequencesGroup> sequencesGroupsStream = dataset.getSequencesGroups().parallel();
    
    for (SequencesGroupTransformation transformation : this.transformations) {
      sequencesGroupsStream = sequencesGroupsStream
        .map(
          wrapWithExceptionToNull(
            g -> transformation.transform(g), (g, e) -> {
              System.err.println("An exception occurred processing " + ((SequencesGroup) g).getName());
              System.err.println("Exception message: " + e.getMessage());
              if (e.getCause() != null) {
                System.err.println("Exception message: " + e.getCause().getMessage());
              }
              System.err.println();
            }
          )
        )
        .filter(Objects::nonNull);
    }

    final SequencesGroup[] sequencesGroups = sequencesGroupsStream.toArray(SequencesGroup[]::new);

    if (sequencesGroups.length == 0)
      throw new TransformationException("No files after filtering");
    
    return this.builder.apply(sequencesGroups);
  }
}
