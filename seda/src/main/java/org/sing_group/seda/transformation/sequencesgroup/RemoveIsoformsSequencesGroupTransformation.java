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
package org.sing_group.seda.transformation.sequencesgroup;

import static es.uvigo.ei.sing.commons.csv.entities.CsvData.CsvDataBuilder.newCsvDataBuilder;
import static java.lang.System.lineSeparator;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.operations.SequenceIsoformSelector;
import org.sing_group.seda.core.operations.SequencesGroupIsoformTester;
import org.sing_group.seda.core.operations.SequencesGroupIsoformTesterResult;
import org.sing_group.seda.core.operations.SequencesGroupSeparator;
import org.sing_group.seda.core.rename.SequenceHeadersJoiner;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

import es.uvigo.ei.sing.commons.csv.entities.CsvEntry;
import es.uvigo.ei.sing.commons.csv.entities.CsvFormat;
import es.uvigo.ei.sing.commons.csv.io.CsvWriter;

public class RemoveIsoformsSequencesGroupTransformation implements SequencesGroupTransformation {
  private static final CsvEntry CSV_HEADER = new CsvEntry(asList("Selected isoform", "Removed isoforms"));
  private static final CsvFormat CSV_FORMAT = new CsvFormat("\t", '.', true, lineSeparator());

  private DatatypeFactory factory;
  private HeaderMatcher matcher;
  private RemoveIsoformsTransformationConfiguration configuration;
  private SequenceIsoformSelector isoformSelector;
  private SequenceHeadersJoiner sequenceHeadersJoiner;

  public RemoveIsoformsSequencesGroupTransformation(RemoveIsoformsTransformationConfiguration configuration,
      SequenceIsoformSelector isoformSelector, SequenceHeadersJoiner sequenceHeadersJoiner
  ) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), configuration, isoformSelector, sequenceHeadersJoiner);
  }

  public RemoveIsoformsSequencesGroupTransformation(HeaderMatcher matcher,
      RemoveIsoformsTransformationConfiguration configuration, SequenceIsoformSelector isoformSelector,
      SequenceHeadersJoiner sequenceHeadersJoiner
  ) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), matcher, configuration, isoformSelector, sequenceHeadersJoiner);
  }

  public RemoveIsoformsSequencesGroupTransformation(DatatypeFactory factory,
      RemoveIsoformsTransformationConfiguration configuration, SequenceIsoformSelector isoformSelector,
      SequenceHeadersJoiner sequenceHeadersJoiner
  ) {
    this(factory, null, configuration, isoformSelector, sequenceHeadersJoiner);
  }

  public RemoveIsoformsSequencesGroupTransformation(DatatypeFactory factory, HeaderMatcher matcher,
      RemoveIsoformsTransformationConfiguration configuration, SequenceIsoformSelector isoformSelector,
      SequenceHeadersJoiner sequenceHeadersJoiner
  ) {
    this.factory = factory;
    this.matcher = matcher;
    this.configuration = configuration;
    this.isoformSelector = isoformSelector;
    this.sequenceHeadersJoiner = sequenceHeadersJoiner;
  }

  @Override
  public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
    SequencesGroupIsoformTesterResult isoformsResult = new SequencesGroupIsoformTesterResult();
    int minimumIsoformWordLength = this.configuration.getMinimumIsoformWordLength();
    if (matcher == null) {
      new SequencesGroupIsoformTester(sequencesGroup).test(minimumIsoformWordLength).getIsoformsLists().forEach(l -> {
        isoformsResult.addIsoformsList(l);
      });
    } else {
      Map<String, List<Sequence>> groups = new SequencesGroupSeparator(this.matcher).separate(sequencesGroup);
      groups.forEach((k, v) -> {
        SequencesGroup current = factory.newSequencesGroup(k, sequencesGroup.getProperties(), v);
        new SequencesGroupIsoformTester(current).test(minimumIsoformWordLength).getIsoformsLists().forEach(l -> {
          isoformsResult.addIsoformsList(l);
        });
      });
    }

    List<CsvEntry> csvEntries = new LinkedList<>();

    List<Sequence> sequences = new LinkedList<>();
    isoformsResult.getIsoformsLists().forEach(l -> {
      Sequence selectedSequence = isoformSelector.selectSequence(l);

      if (this.configuration.isSaveRemovedIsoformsFile() && l.size() > 1) {
        csvEntries.add(csvEntry(l, selectedSequence));
      }
      String newDescripion = selectedSequence.getDescription() + " " + 
        (l.size() > 1 ? sequenceHeadersJoiner.join(l.stream().filter(f -> !f.equals(selectedSequence)).collect(Collectors.toList())) : "");
      
      sequences.add(
        this.factory.newSequence(
          selectedSequence.getName(), 
          newDescripion.trim(), 
          selectedSequence.getChain(), 
          selectedSequence.getProperties()
        )
      );
    });

    if (this.configuration.isSaveRemovedIsoformsFile() && !csvEntries.isEmpty()) {
      try {
        CsvWriter.of(CSV_FORMAT).write(
            newCsvDataBuilder(CSV_FORMAT).withHeader(CSV_HEADER).withEntries(csvEntries).build(),
            new File(this.configuration.getRemovedIsoformsFileDirectory(), sequencesGroup.getName() + ".csv")
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return this.factory.newSequencesGroup(sequencesGroup.getName(), sequencesGroup.getProperties(), sequences);
  }

  private CsvEntry csvEntry(List<Sequence> l, Sequence selectedSequence) {
    return new CsvEntry(
        asList(selectedSequence.getName(),
        l.stream().filter(f -> !f.equals(selectedSequence)).map(Sequence::getName).collect(joining(", ")))
    );
  }

  public static class RemoveIsoformsTransformationConfiguration {
    private int minimumIsoformWordLength;
    private File removedIsoformsFileDirectory;

    public RemoveIsoformsTransformationConfiguration(int minimumIsoformWordLength) {
      this(minimumIsoformWordLength, null);
    }

    public RemoveIsoformsTransformationConfiguration(int minimumIsoformWordLength, File removedIsoformsFileDirectory) {
      this.minimumIsoformWordLength = minimumIsoformWordLength;
      this.removedIsoformsFileDirectory = removedIsoformsFileDirectory;
    }

    public int getMinimumIsoformWordLength() {
      return minimumIsoformWordLength;
    }

    public boolean isSaveRemovedIsoformsFile() {
      return removedIsoformsFileDirectory != null;
    }

    public File getRemovedIsoformsFileDirectory() {
      return this.removedIsoformsFileDirectory;
    }
  }
}
