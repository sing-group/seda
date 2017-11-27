package org.sing_group.seda.transformation.dataset;

import static org.junit.Assert.assertThat;

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
    Sequence.of("Sequence 1", "Sequence 1 description", "", Collections.emptyMap()),
    Sequence.of("Sequence 2", "Sequence 2 description", "", Collections.emptyMap())
  };
  
  private static final SequencesGroup GROUP_1 =
    SequencesGroup.of("GCF_000001735.1_TAIR10_cds_from_genomic.fna", GROUP1_SEQUENCES);
  private static final SequencesGroup GROUP_2 =
    SequencesGroup.of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna");
  
  
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
            SequencesGroup.of("GCF_000001735.1_TAIR10_cds_from_genomic.fna", GROUP1_SEQUENCES), 
            SequencesGroup.of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna")
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.NONE),
          new SequenceHeaderRenameConfiguration(RenameMode.OVERRIDE, "", true, "_", new ReplaceCharacterConfiguration()),
          INPUT, 
          SequencesGroupDataset.of(  
            SequencesGroup.of("GCF_000001735.1_TAIR10_cds_from_genomic.fna", new Sequence[]{
              Sequence.of("Homo", "sapiens_1", "", Collections.emptyMap()),
              Sequence.of("Homo", "sapiens_2", "", Collections.emptyMap())
            }), 
            SequencesGroup.of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna")
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.OVERRIDE),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            SequencesGroup.of("Homo sapiens", GROUP1_SEQUENCES), 
            SequencesGroup.of("Lupinus angustifolius (narrow-leaved blue lupine)")
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.OVERRIDE),
          new SequenceHeaderRenameConfiguration(RenameMode.OVERRIDE, "", true, "_", new ReplaceCharacterConfiguration()),
          INPUT, 
          SequencesGroupDataset.of(  
            SequencesGroup.of("Homo sapiens", new Sequence[]{
              Sequence.of("Homo", "sapiens_1", "", Collections.emptyMap()),
              Sequence.of("Homo", "sapiens_2", "", Collections.emptyMap())
            }), 
            SequencesGroup.of("Lupinus angustifolius (narrow-leaved blue lupine)")
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.PREFIX, "_"),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            SequencesGroup.of("Homo sapiens_GCF_000001735.1_TAIR10_cds_from_genomic.fna", GROUP1_SEQUENCES), 
            SequencesGroup.of("Lupinus angustifolius (narrow-leaved blue lupine)_GCF_000002775.2_Poptr2_0_cds_from_genomic.fna")
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.SUFFIX, "_"),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            SequencesGroup.of("GCF_000001735.1_TAIR10_cds_from_genomic.fna_Homo sapiens", GROUP1_SEQUENCES), 
            SequencesGroup.of("GCF_000002775.2_Poptr2_0_cds_from_genomic.fna_Lupinus angustifolius (narrow-leaved blue lupine)")
          )
        },
        {  
          new FileRenameConfiguration(RenameMode.REPLACE, "_"),
          new SequenceHeaderRenameConfiguration(),
          INPUT, 
          SequencesGroupDataset.of(  
            SequencesGroup.of("Homo sapiens_TAIR10_cds_from_genomic.fna", GROUP1_SEQUENCES), 
            SequencesGroup.of("Lupinus angustifolius (narrow-leaved blue lupine)_Poptr2_0_cds_from_genomic.fna")
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
