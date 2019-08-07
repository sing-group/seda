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
package org.sing_group.seda.transformation.dataset;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sing_group.seda.core.split.SequencesGroupSplitter;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;

public class SplitSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final SequencesGroupSplitter splitter;

  public SplitSequencesGroupDatasetTransformation(SequencesGroupSplitter splitter) {
    this(splitter, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public SplitSequencesGroupDatasetTransformation(SequencesGroupSplitter splitter, DatatypeFactory factory) {
    this.builder = factory::newSequencesGroupDataset;
    this.splitter = splitter;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final List<SequencesGroup> sequencesGroups = dataset.getSequencesGroups()
      .map(splitter::split)
      .flatMap(Collection::stream).collect(Collectors.toList());

    if (sequencesGroups.size() == 0)
      throw new TransformationException("No sequences after filtering");

    return this.builder.apply(sequencesGroups.toArray(new SequencesGroup[sequencesGroups.size()]));
  }
}
