package org.sing_group.seda.transformation.dataset;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sing_group.seda.core.ncbi.NcbiAssemblyAccesionResolver;
import org.sing_group.seda.core.ncbi.NcbiAssemblyAccession;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyConfiguration;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyFields;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyInfo;
import org.sing_group.seda.core.ncbi.NcbiTaxonomyResolver;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.datatype.rename.FileRenameConfiguration;
import org.sing_group.seda.datatype.rename.SequenceHeaderRenameConfiguration;
import org.sing_group.seda.util.FileUtils;

public class NcbiRenameSequencesGroupDatasetTransformation extends MapRenameSequencesGroupDatasetTransformation {
  private Map<String, String> renamings;
  private Map<SequencesGroup, NcbiAssemblyAccession> accessions;
  private NcbiTaxonomyConfiguration taxonomyConfiguration;
  private File replacementsMapFile;

  public NcbiRenameSequencesGroupDatasetTransformation(
    FileRenameConfiguration fileNameConfiguration,
    SequenceHeaderRenameConfiguration headerConfiguration,
    NcbiTaxonomyConfiguration taxonomyConfiguration
  ) {
    this(
      DatatypeFactory.getDefaultDatatypeFactory(), fileNameConfiguration, headerConfiguration, taxonomyConfiguration
    );
  }

  public NcbiRenameSequencesGroupDatasetTransformation(
    DatatypeFactory factory,
    FileRenameConfiguration fileNameConfiguration,
    SequenceHeaderRenameConfiguration headerConfiguration,
    NcbiTaxonomyConfiguration taxonomyConfiguration
  ) {
    this(factory, fileNameConfiguration, headerConfiguration, taxonomyConfiguration, null);
  }

  public NcbiRenameSequencesGroupDatasetTransformation(
    DatatypeFactory factory,
    FileRenameConfiguration fileNameConfiguration,
    SequenceHeaderRenameConfiguration headerConfiguration,
    NcbiTaxonomyConfiguration taxonomyConfiguration,
    File replacementsMapFile
  ) {
    super(factory, fileNameConfiguration, headerConfiguration);

    this.taxonomyConfiguration = taxonomyConfiguration;
    this.replacementsMapFile = replacementsMapFile;
  }

  @Override
  public SequencesGroupDataset transform(SequencesGroupDataset dataset) {
    this.accessions = resolveAccessions(dataset);
    updateRenamings();

    return super.transform(dataset);
  }

  private void updateRenamings() {
    this.renamings = new HashMap<>();
    this.accessions.forEach(
      (group, accession) -> {
        this.renamings.put(accession.getAccession(), getAccessionReplacement(accession));
      }
    );
    this.saveAccessionsMap();
  }

  private void saveAccessionsMap() {
    if (this.replacementsMapFile != null) {
      try {
        FileUtils.writeMap(this.replacementsMapFile, this.renamings);
      } catch (IOException e) {
        e.printStackTrace();
      }
      ;
    }
  }

  private String getAccessionReplacement(NcbiAssemblyAccession accession) {
    if (this.taxonomyConfiguration.getFields().length == 0) {
      return accession.getOrganismName();
    } else {
      return resolveNcbiTaxonomyAccession(accession);
    }
  }

  private String resolveNcbiTaxonomyAccession(NcbiAssemblyAccession accession) {
    Optional<NcbiTaxonomyInfo> resolved = new NcbiTaxonomyResolver().resolve(accession.getTaxonomyUrl());
    List<String> values = new LinkedList<>();
    values.add(accession.getOrganismName());

    if (resolved.isPresent()) {
      NcbiTaxonomyInfo info = resolved.get();
      for (NcbiTaxonomyFields field : this.taxonomyConfiguration.getFields()) {
        Optional<String> fieldValue = info.getValue(field);
        if (fieldValue.isPresent()) {
          values.add(fieldValue.get());
        }
      }
    }

    return values.stream().collect(Collectors.joining(this.taxonomyConfiguration.getDelimiter()));
  }

  private Map<SequencesGroup, NcbiAssemblyAccession> resolveAccessions(SequencesGroupDataset dataset) {
    Map<SequencesGroup, NcbiAssemblyAccession> accessionsMap = new HashMap<>();
    dataset.getSequencesGroups().forEach(
      group -> {
        Optional<NcbiAssemblyAccession> accession = new NcbiAssemblyAccesionResolver().resolve(group.getName());
        if (accession.isPresent()) {
          accessionsMap.put(group, accession.get());
        }
      }
    );

    return accessionsMap;
  }

  @Override
  protected Map<String, String> getRenamings() {
    return this.renamings;
  }
}
