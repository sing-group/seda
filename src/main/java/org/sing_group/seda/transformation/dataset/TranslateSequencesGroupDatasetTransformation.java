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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.transformation.TransformationException;

public class TranslateSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  private final Function<SequencesGroup[], SequencesGroupDataset> builder;
  private final BiFunction<String, List<Sequence>, SequencesGroup> groupBuilder;
  private final SequenceBuilder sequenceBuilder;
  private final SequenceTranslationConfiguration configuration;

  public TranslateSequencesGroupDatasetTransformation(SequenceTranslationConfiguration configuration) {
    this(configuration, DatatypeFactory.getDefaultDatatypeFactory());
  }

  public TranslateSequencesGroupDatasetTransformation(
    SequenceTranslationConfiguration configuration, DatatypeFactory factory
  ) {
    this.builder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.sequenceBuilder = factory::newSequence;
    this.configuration = configuration;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    final List<SequencesGroup> sequencesGroups = dataset.getSequencesGroups()
      .map(this::translateSequencesGroup)
      .flatMap(Collection::stream).collect(Collectors.toList());

    if (sequencesGroups.size() == 0)
      throw new TransformationException("No sequences after filtering");

    return this.builder.apply(sequencesGroups.toArray(new SequencesGroup[sequencesGroups.size()]));
  }

  private List<SequencesGroup> translateSequencesGroup(SequencesGroup group) {
    boolean appendFrameName = this.configuration.getFrames().length > 1;
    List<SequencesGroup> translatedSequenceGroups = new LinkedList<>();

    for (int frame : this.configuration.getFrames()) {
      String newGroupName = group.getName();
      if (appendFrameName) {
        newGroupName += "_frame_" + frame;
      }
      translatedSequenceGroups.add(translateGroup(newGroupName, group, frame));
    }

    return translatedSequenceGroups;
  }

  private SequencesGroup translateGroup(String newGroupName, SequencesGroup group, int frame) {
    return this.groupBuilder.apply(
      newGroupName,
      group.getSequences().map(
        s -> {
          return sequenceBuilder.of(
            s.getName(), s.getDescription(),
            SequenceUtils.translate(
              s.getChain(), configuration.isReverseComplement(), frame, configuration.getCodonTable()
            ), 
            s.getProperties()
          );
        }
      )
        .collect(Collectors.toList())
    );
  }
}
