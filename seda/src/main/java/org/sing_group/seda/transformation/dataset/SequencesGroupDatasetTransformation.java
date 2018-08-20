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

import static java.util.Collections.emptyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public interface SequencesGroupDatasetTransformation {
  public SequencesGroupDataset transform(SequencesGroupDataset dataset)
    throws TransformationException;
  
  public static SequencesGroupDatasetTransformation concat(Collection<SequencesGroupDatasetTransformation> transformations) {
    return concat(transformations.stream());
  }
  
  public static SequencesGroupDatasetTransformation concat(Stream<SequencesGroupDatasetTransformation> transformations) {
    return concat(transformations.toArray(SequencesGroupDatasetTransformation[]::new));
  }
  
  public static SequencesGroupDatasetTransformation concat(SequencesGroupDatasetTransformation ... transformations) {
    return dataset -> {
      SequencesGroupDataset transformedDataset = dataset;
      
      for (SequencesGroupDatasetTransformation transformation : transformations) {
        transformedDataset = transformation.transform(transformedDataset);
      }
      
      return transformedDataset;
    };
  }
  
  public static Collector<? super SequencesGroupDatasetTransformation, Collection<SequencesGroupDatasetTransformation>, SequencesGroupDatasetTransformation> toSequencesGroupDatasetTransformation() {
    return Collector.of(
      ArrayList::new,
      (accumulator, transformation) -> accumulator.add(transformation),
      (accumulator1, accumulator2) -> {
        accumulator1.addAll(accumulator2);
        return accumulator1;
      },
      SequencesGroupDatasetTransformation::concat
    );
  }
  
  default Map<String, Object> getDatasetPropertiesMap(SequencesGroupDataset dataset) {
    Map<String, Object> properties = emptyMap();
    Optional<SequencesGroup> firstSequencesGroup = dataset.getSequencesGroups().findFirst();
    if (firstSequencesGroup.isPresent()) {
      properties = firstSequencesGroup.get().getProperties();
    }
    return properties;
  }
}
