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

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupBuilder;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.rename.FileRenameConfiguration;
import org.sing_group.seda.datatype.rename.ReplaceCharacterConfiguration;
import org.sing_group.seda.datatype.rename.SequenceHeaderRenameConfiguration;
import org.sing_group.seda.util.StringUtils;

public class MapRenameSequencesGroupDatasetTransformation implements SequencesGroupDatasetTransformation {
  public enum RenameMode {
    PREFIX, SUFFIX, REPLACE, OVERRIDE, NONE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  };

  private DatatypeFactory factory;
  private final SequencesGroupBuilder groupBuilder;
  private final Function<SequencesGroup[], SequencesGroupDataset> datasetBuilder;
  private FileRenameConfiguration fileRenameConfiguration;
  private SequenceHeaderRenameConfiguration headerConfiguration;
  private final Map<String, String> renamings;

  public MapRenameSequencesGroupDatasetTransformation(
    FileRenameConfiguration fileNameConfiguration, SequenceHeaderRenameConfiguration headerConfiguration,
    Map<String, String> renamings
  ) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), fileNameConfiguration, headerConfiguration, renamings);
  }

  public MapRenameSequencesGroupDatasetTransformation(
    DatatypeFactory factory, FileRenameConfiguration fileNameConfiguration,
    SequenceHeaderRenameConfiguration headerConfiguration
  ) {
    this(factory, fileNameConfiguration, headerConfiguration, Collections.emptyMap());
  }

  public MapRenameSequencesGroupDatasetTransformation(
    DatatypeFactory factory, FileRenameConfiguration fileNameConfiguration,
    SequenceHeaderRenameConfiguration headerConfiguration, Map<String, String> renamings
  ) {
    this.factory = factory;
    this.datasetBuilder = factory::newSequencesGroupDataset;
    this.groupBuilder = factory::newSequencesGroup;
    this.fileRenameConfiguration = fileNameConfiguration;
    this.headerConfiguration = headerConfiguration;
    this.renamings = renamings;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    return datasetBuilder.apply(
      dataset.getSequencesGroups().map(
        sequencesGroup -> {
          return rename(sequencesGroup);
        }
      ).toArray(size -> new SequencesGroup[size])
    );
  }

  protected SequencesGroup rename(SequencesGroup sequencesGroup) {
    String groupName = sequencesGroup.getName();

    SequencesGroup renamedSequencesGroup = sequencesGroup;

    String newName = "";
    for (String renameKey : getRenamings().keySet()) {
      if (groupName.contains(renameKey)) {
        String renameValue = getRenamings().get(renameKey);
        switch (this.fileRenameConfiguration.getMode()) {
          case NONE:
            newName = groupName;
            break;
          case OVERRIDE:
            newName = renameValue;
            break;
          case PREFIX:
            newName = renameValue + this.fileRenameConfiguration.getDelimiter() + groupName;
            break;
          case SUFFIX:
            newName = groupName + this.fileRenameConfiguration.getDelimiter() + renameValue;
            break;
          case REPLACE:
            newName = groupName.replace(renameKey, renameValue);
            break;
          default:
            break;
        }

        newName = replaceCharacters(newName, this.fileRenameConfiguration.getReplaceCharacterConfiguration());

        if (this.headerConfiguration.getPosition().isPresent()) {
          String newRenameValue =
            replaceCharacters(renameValue, this.fileRenameConfiguration.getReplaceCharacterConfiguration());

          AddStringHeaderRenamer headerRenamer =
            new AddStringHeaderRenamer(
              HeaderTarget.ALL, newRenameValue, this.headerConfiguration.getDelimiter(),
              this.headerConfiguration.getPosition().get(),
              this.headerConfiguration.isAddIndex(), this.headerConfiguration.getIndexDelimiter()
            );

          renamedSequencesGroup = groupBuilder.of(
            newName, sequencesGroup.getProperties(),
            headerRenamer.rename(sequencesGroup, this.factory).getSequences().collect(toList())
          );
        } else {
          renamedSequencesGroup = groupBuilder.of(
            newName, renamedSequencesGroup.getProperties(),
            renamedSequencesGroup.getSequences().collect(toList())
          );
        }

        if (this.fileRenameConfiguration.isStopAtFirstMatch()) {
          break;
        }
      }
    }

    return renamedSequencesGroup;
  }

  private static String replaceCharacters(String newName, ReplaceCharacterConfiguration configuration) {
    String toret = newName;

    if (configuration.isReplaceBlankSpaces()) {
      toret = toret.replace(" ", configuration.getReplacement());
    }

    if (configuration.isReplaceSpecialCharacters()) {
      for (String character : ReplaceCharacterConfiguration.getSpecialCharacters()) {
        toret = toret.replace(character, configuration.getReplacement());
      }
    }
    return toret;
  }

  protected Map<String, String> getRenamings() {
    return this.renamings;
  }
}
