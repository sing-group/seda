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

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.rename.FileRenameConfiguration;
import org.sing_group.seda.datatype.rename.ReplaceCharacterConfiguration;
import org.sing_group.seda.datatype.rename.SequenceHeaderRenameConfiguration;
import org.sing_group.seda.matcher.EqualSequencesGroupDatasetMatcher;
import org.sing_group.seda.transformation.dataset.MapRenameSequencesGroupDatasetTransformation.RenameMode;

@RunWith(Parameterized.class)
public class MapRenameSequencesGroupDatasetTransformationTest {
  
  private static final Map<String, String> RENAMINGS = new HashMap<>();
  
  static {
    RENAMINGS.put("GCF_000001735.1", "Homo sapiens");
    RENAMINGS.put("GCF_000002775.2", "Lupinus angustifolius (narrow-leaved blue lupine)");
  }
  
  private static final Sequence[] GROUP1_SEQUENCES = {
    of("Sequence 1", "Sequence 1 description", "", Collections.emptyMap()),
    of("Sequence 2", "Sequence 2 description", "", Collections.emptyMap())
  };
  
  private static final SequencesGroup GROUP_1 =
    of("GCF_000001735.1_TAIR10_cds_from_genomic.fna", emptyMap(), GROUP1_SEQUENCES);
  private static final SequencesGroup GROUP_2 =
    of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna", emptyMap());
  
  
  private static final SequencesGroupDataset INPUT = SequencesGroupDataset.of(GROUP_1, GROUP_2);
  
  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
        {  
          new FileRenameConfiguration(RenameMode.NONE),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            of("GCF_000001735.1_TAIR10_cds_from_genomic.fna", emptyMap(), GROUP1_SEQUENCES), 
            of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna", emptyMap())
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.NONE),
          new SequenceHeaderRenameConfiguration(RenameMode.OVERRIDE, "", true, "_", new ReplaceCharacterConfiguration()),
          INPUT, 
          SequencesGroupDataset.of(  
            of("GCF_000001735.1_TAIR10_cds_from_genomic.fna", emptyMap(), new Sequence[]{
              of("Homo", "sapiens_1", "", Collections.emptyMap()),
              of("Homo", "sapiens_2", "", Collections.emptyMap())
            }), 
            of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna", emptyMap())
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.OVERRIDE),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            of("Homo sapiens", emptyMap(), GROUP1_SEQUENCES), 
            of("Lupinus angustifolius (narrow-leaved blue lupine)", emptyMap())
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.OVERRIDE),
          new SequenceHeaderRenameConfiguration(RenameMode.OVERRIDE, "", true, "_", new ReplaceCharacterConfiguration()),
          INPUT, 
          SequencesGroupDataset.of(  
            of("Homo sapiens", emptyMap(), new Sequence[]{
              of("Homo", "sapiens_1", "", Collections.emptyMap()),
              of("Homo", "sapiens_2", "", Collections.emptyMap())
            }), 
            of("Lupinus angustifolius (narrow-leaved blue lupine)", emptyMap())
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.PREFIX, "_"),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            of("Homo sapiens_GCF_000001735.1_TAIR10_cds_from_genomic.fna", emptyMap(), GROUP1_SEQUENCES), 
            of("Lupinus angustifolius (narrow-leaved blue lupine)_GCF_000002775.2_Poptr2_0_cds_from_genomic.fna", emptyMap())
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.SUFFIX, "_"),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            of("GCF_000001735.1_TAIR10_cds_from_genomic.fna_Homo sapiens", emptyMap(), GROUP1_SEQUENCES), 
            of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna_Lupinus angustifolius (narrow-leaved blue lupine)", emptyMap())
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.REPLACE, "_"),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            of("Homo sapiens_TAIR10_cds_from_genomic.fna", emptyMap(), GROUP1_SEQUENCES), 
            of("Lupinus angustifolius (narrow-leaved blue lupine)_Poptr2_0_cds_from_genomic.fna", emptyMap())
          )
        }
      }
    );
  }

  private FileRenameConfiguration fileNameConfiguration;
  private SequenceHeaderRenameConfiguration headerRenameConfiguration;
  private SequencesGroupDataset input;
  private SequencesGroupDataset expected;

  public MapRenameSequencesGroupDatasetTransformationTest(
    FileRenameConfiguration fileNameConfiguration, SequenceHeaderRenameConfiguration headerRenameConfiguration,
    SequencesGroupDataset input,
    SequencesGroupDataset expected
  ) {
    this.fileNameConfiguration = fileNameConfiguration;
    this.headerRenameConfiguration = headerRenameConfiguration;
    this.input = input;
    this.expected = expected;
  }

  @Test
  public void test() {
    SequencesGroupDataset actual =
      new MapRenameSequencesGroupDatasetTransformation(fileNameConfiguration, headerRenameConfiguration, RENAMINGS)
        .transform(this.input);

    assertThat(
      actual,
      EqualSequencesGroupDatasetMatcher.hasEqualSequenceGroups(expected)
    );
  }
}
