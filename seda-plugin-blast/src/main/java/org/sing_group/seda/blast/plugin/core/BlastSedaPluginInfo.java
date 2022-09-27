package org.sing_group.seda.blast.plugin.core;

import static java.util.Arrays.asList;

import org.sing_group.seda.blast.datatype.DatabaseQueryMode;
import org.sing_group.seda.blast.datatype.SequenceType;
import org.sing_group.seda.blast.datatype.blast.BlastType;
import org.sing_group.seda.blast.transformation.dataset.BlastTransformation;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;
import org.sing_group.seda.util.SedaProperties;

public class BlastSedaPluginInfo extends AbstractInfo {
  public static final String NAME = "BLAST";
  public static final String SHORT_NAME = "blast";
  public static final String DESCRIPTION =
    "Perform BLAST queries using the selected FASTA files as a single or mutiple independent database(s).";
  public static final String GROUP = Group.GROUP_BLAST.getName();

  public static final String PROPERTY_ENABLE_LOCAL_EXECUTION_BLAST =
    SedaProperties.PROPERTY_ENABLE_LOCAL_EXECUTION + ".blast";
  
  public static final String PARAM_DOCKER_MODE_HELP =
    "The BLAST docker image. By default, the official SEDA image for BLAST is used. "
      + "If you provide a custom image, it should have the BLAST commands available in the path.";
  public static final String PARAM_DOCKER_MODE_HELP_GUI = toHtml(PARAM_DOCKER_MODE_HELP);

  public static final String PARAM_LOCAL_MODE_HELP =
    "The directory that contains the BLAST binaries. Leave it empty if they are in the path.";
  public static final String PARAM_LOCAL_MODE_HELP_GUI =
    toHtml(PARAM_LOCAL_MODE_HELP, asList("directory", "blast binaries"), asList(), false);
  
  /*
   * Query file is only used in the CLI and thus _HELP_GUI and _DESCRIPTION are not defined. The GUI provides a combobox
   * for selecting a file from the current SEDA selection as well as a file chooser.
   */
  public static final String PARAM_QUERY_FILE_NAME = "query-file";
  public static final String PARAM_QUERY_FILE_SHORT_NAME = "qf";
  public static final String PARAM_QUERY_FILE_HELP = "The file that contains the sequences that must be used for the BLAST queries.";

  public static final String PARAM_QUERY_SOURCE_DESCRIPTION = "Query source";
  public static final String PARAM_QUERY_SOURCE_HELP_GUI = "The source of the query sequences.";
  
  public static final String PARAM_DATABASE_QUERY_MODE_NAME = "query-mode";
  public static final String PARAM_DATABASE_QUERY_MODE_SHORT_NAME = "qm";
  public static final String PARAM_DATABASE_QUERY_MODE_DESCRIPTION = "Query against";
  private static final String[] PARAM_DATABASE_QUERY_MODE_HELP_ENUM = {
    "Firstly, one BLAST database is created for each selected FASTA file. Then, one alias referencing to all the databases created before is created. Finally, each sequence in the FASTA file used as query source is executed against the alias. As a result, this mode creates as many output files as sequences in the FASTA file. To create these output files, the sequences where hits were found are retrieved from the database.",
    "Firstly, one BLAST database is created for each selected FASTA file. Then, each sequence in the FASTA file used as query source is executed against each of the databases. As a result, this mode creates as many output files as sequences in the FASTA file multiplied by the number of selected FASTA files. To create these output files, the sequences where hits were found are retrieved from the corresponding database." };
  public static final String PARAM_DATABASE_QUERY_MODE_HELP =
    longEnumStringForCli(
      "The mode in which the query should be performed.",
      cliMap(DatabaseQueryMode.values(), PARAM_DATABASE_QUERY_MODE_HELP_ENUM)
    );
  public static final String PARAM_DATABASE_QUERY_MODE_HELP_GUI =
    toHtml(
      longEnumStringForGui(
        "The mode in which the query should be performed.",
        guiMap(DatabaseQueryMode.values(), PARAM_DATABASE_QUERY_MODE_HELP_ENUM)
      )
    );
  
  public static final String PARAM_QUERY_BLAST_TYPE_NAME = "query-blast-type";
  public static final String PARAM_QUERY_BLAST_TYPE_SHORT_NAME = "qbt";
  public static final String PARAM_QUERY_BLAST_TYPE_DESCRIPTION = "BLAST type";
  public static final String PARAM_QUERY_BLAST_TYPE_HELP =
    shortEnumString(
      "The BLAST command to execute.", BlastType.class
    );
  public static final String PARAM_QUERY_BLAST_TYPE_HELP_GUI = toHtml(PARAM_QUERY_BLAST_TYPE_HELP);

  /**
   * The store databases parameter is not needed at all in the CLI. If the user sets the databases directory means that
   * means that it should be set to true in the transformation provider. Same occurs with the store alias parameter.
   * 
   * We could get rid off these parameters as well in the GUI: if file choosers are empty, then the parameters are not
   * used. And the file chooser selection can be removed by doing right-click. 
   * 
   * We decided to keep them for backwards compatibility with parameter files.
   */
  public static final String PARAM_STORE_DATABASES_NAME = "store-databases";
  public static final String PARAM_STORE_DATABASES_SHORT_NAME = "sdbs";
  public static final String PARAM_STORE_DATABASES_DESCRIPTION = "Store databases";
  public static final String PARAM_STORE_DATABASES_HELP = "Whether BLAST databases must be stored or not. By choosing to store them, they can be reused for future analysis.";
  public static final String PARAM_STORE_DATABASES_HELP_GUI = toHtml(PARAM_STORE_DATABASES_HELP, true);
  
  public static final String PARAM_STORE_DATABASES_DIRECTORY_NAME = "store-databases-directory";
  public static final String PARAM_STORE_DATABASES_DIRECTORY_SHORT_NAME = "sdd";
  public static final String PARAM_STORE_DATABASES_DIRECTORY_DESCRIPTION = "Databases directory";
  public static final String PARAM_STORE_DATABASES_DIRECTORY_HELP = "The directory where databases must be stored.";
  public static final String PARAM_STORE_DATABASES_DIRECTORY_HELP_GUI = toHtml(PARAM_STORE_DATABASES_DIRECTORY_HELP, true);
  
  public static final String PARAM_STORE_ALIAS_NAME = "store-alias";
  public static final String PARAM_STORE_ALIAS_SHORT_NAME = "sa";
  public static final String PARAM_STORE_ALIAS_DESCRIPTION = "Store alias";
  public static final String PARAM_STORE_ALIAS_HELP = "Whether the database alias must be stored or not.";
  public static final String PARAM_STORE_ALIAS_HELP_GUI = PARAM_STORE_ALIAS_HELP;
  
  public static final String PARAM_STORE_ALIAS_FILE_NAME = "store-alias-file";
  public static final String PARAM_STORE_ALIAS_FILE_SHORT_NAME = "saf";
  public static final String PARAM_STORE_ALIAS_FILE_DESCRIPTION = "Alias file";
  public static final String PARAM_STORE_ALIAS_FILE_HELP = "The file where the alias must be stored.";
  public static final String PARAM_STORE_ALIAS_FILE_HELP_GUI = PARAM_STORE_ALIAS_FILE_HELP;
  
  public static final String PARAM_EVALUE_NAME = "evalue";
  public static final String PARAM_EVALUE_SHORT_NAME = "ev";
  public static final String PARAM_EVALUE_DESCRIPTION = "Expectation value";
  public static final String PARAM_EVALUE_HELP = "The expectation value (E) threshold for saving hits.";
  public static final String PARAM_EVALUE_HELP_GUI = PARAM_EVALUE_HELP;
  
  public static final String PARAM_MAX_TARGET_SEQS_NAME = "max-target-seqs";
  public static final String PARAM_MAX_TARGET_SEQS_SHORT_NAME = "mts";
  public static final String PARAM_MAX_TARGET_SEQS_DESCRIPTION = "Max. target seqs.";
  public static final String PARAM_MAX_TARGET_SEQS_HELP = "The maximum number of aligned sequences to keep.";
  public static final String PARAM_MAX_TARGET_SEQS_HELP_GUI = PARAM_MAX_TARGET_SEQS_HELP;
  
  public static final String PARAM_HIT_REGION_WINDOW_SIZE_NAME = "hit-regions-window";
  public static final String PARAM_HIT_REGION_WINDOW_SIZE_SHORT_NAME = "hrg";
  public static final String PARAM_HIT_REGION_WINDOW_SIZE_DESCRIPTION = "Hit regions window";
  public static final String PARAM_HIT_REGION_WINDOW_SIZE_HELP = "The window size to retrieve only hit regions.";
  public static final String PARAM_HIT_REGION_WINDOW_SIZE_HELP_GUI = PARAM_HIT_REGION_WINDOW_SIZE_HELP;

  public static final String PARAM_EXTRACT_HIT_REGIONS_SIZE_DESCRIPTION = "Extract only hit regions";
  public static final String PARAM_EXTRACT_HIT_REGIONS_HELP =
    "Use this option to extract only the part of the sequences where hits are produced instead of the entire subject sequences.";
  
  public static final String PARAM_ADDITIONAL_PARAMS_NAME = "additional-params";
  public static final String PARAM_ADDITIONAL_PARAMS_SHORT_NAME = "ad";
  public static final String PARAM_ADDITIONAL_PARAMS_DESCRIPTION = "Additional parameters";
  public static final String PARAM_ADDITIONAL_PARAMS_HELP = "Additional parameters for the BLAST command.";
  public static final String PARAM_ADDITIONAL_PARAMS_HELP_GUI = PARAM_ADDITIONAL_PARAMS_HELP;

  public static final DatabaseQueryMode DEFAULT_DATABAE_QUERY_MODE = DatabaseQueryMode.ALL;
  public static final SequenceType DEFAULT_SEQUENCE_TYPE = BlastTransformation.DEFAULT_SEQUENCE_TYPE;
  public static final BlastType DEFAULT_BLAST_TYPE = BlastTransformation.DEFAULT_BLAST_TYPE;
  public static final double DEFAULT_EVALUE = BlastTransformation.DEFAULT_EVALUE;
  public static final int DEFAULT_MAX_TARGET_SEQS = BlastTransformation.DEFAULT_MAX_TARGET_SEQS;
  public static final boolean DEFAULT_EXTRACT_ONLY_HIT_REGIONS = BlastTransformation.DEFAULT_EXTRACT_ONLY_HIT_REGIONS;
  public static final int DEFAULT_HIT_REGIONS_WINDOW_SIZE = BlastTransformation.DEFAULT_HIT_REGIONS_WINDOW_SIZE;
}
