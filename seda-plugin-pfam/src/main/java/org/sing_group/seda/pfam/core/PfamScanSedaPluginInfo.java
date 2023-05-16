/*
 * #%L
 * SEquence DAtaset builder PfamScan plugin
 * %%
 * Copyright (C) 2017 - 2023 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.pfam.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.sing_group.seda.pfam.PfamScanRequestConfiguration;
import org.sing_group.seda.pfam.PfamScanSequenceErrorPolicy;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.info.AbstractInfo;

public class PfamScanSedaPluginInfo  extends AbstractInfo {
  public static final String NAME = "PfamScan";
  public static final String SHORT_NAME = "pfamscan";
  public static final String DESCRIPTION =
    "Search and annotate sequences against the Pfam-A HMM library using the EMBL-EBI web service (https://www.ebi.ac.uk/Tools/pfa/pfamscan/).";
  public static final String GROUP = Group.GROUP_PROTEIN_ANNOTATION.getName();
  
  public static final String PARAM_EMAIL_NAME = "email";
  public static final String PARAM_EMAIL_SHORT_NAME = "em";
  public static final String PARAM_EMAIL_DESCRIPTION = "E-mail";
  public static final List<String> PARAM_EMAIL_HELP_ITEMS =
    asList(
      "Problems with the service which affect your jobs.",
      "Scheduled maintenance which affects services you are using.",
      "Deprecation and retirement of a service you are using"
    );
  public static final String PARAM_EMAIL_HELP_BASE =
    "A valid e-mail address. This is required by EMBL-EBI so they can contact you in the event of: ";
  public static final String PARAM_EMAIL_HELP =
    itemsListToCliString(PARAM_EMAIL_HELP_ITEMS, PARAM_EMAIL_HELP_BASE);
  public static final String PARAM_EMAIL_HELP_GUI =
    toHtml(itemsListToGuiString(PARAM_EMAIL_HELP_ITEMS, PARAM_EMAIL_HELP_BASE));

  public static final String PARAM_ACTIVE_SITE_PREDICTION_NAME = "active-site-prediction";
  public static final String PARAM_ACTIVE_SITE_PREDICTION_SHORT_NAME = "asp";
  public static final String PARAM_ACTIVE_SITE_PREDICTION_DESCRIPTION = "Active site prediction";
  public static final String PARAM_ACTIVE_SITE_PREDICTION_HELP = "Whether to predict active site residues for Pfam-A matches or not.";
  public static final String PARAM_ACTIVE_SITE_PREDICTION_HELP_GUI = PARAM_ACTIVE_SITE_PREDICTION_HELP;
  
  public static final String PARAM_EVALUE_NAME = "evalue";
  public static final String PARAM_EVALUE_SHORT_NAME = "ev";
  public static final String PARAM_EVALUE_DESCRIPTION = "Expectation value";
  public static final String PARAM_EVALUE_HELP = "Optionally, the expectation value cut-off.";
  public static final String PARAM_EVALUE_HELP_GUI = PARAM_EVALUE_HELP;

  public static final String PARAM_ERROR_POLICY_NAME = "error-policy";
  public static final String PARAM_ERROR_POLICY_SHORT_NAME = "ep";
  public static final String PARAM_ERROR_POLICY_DESCRIPTION = "Sequence error policy";
  private static final List<String> PARAM_ERROR_POLICY_HELP_ITALIC = 
    asList("Annotate sequence as error", "Ignore sequences", "Produce an error (stop operation)");
  private static final String[] PARAM_ERROR_POLICY_HELP_ENUM = {
    "If a sequence analysis fails, this is annotated as an error in the output FASTA",
    "If a sequence analysis fails, it is ignored and not included in the output FASTA.",
    "If a sequence analysis fails an error is produced and the whole operation is stopped."
  };
  public static final String PARAM_ERROR_POLICY_HELP =
    longEnumStringForCli(
      "The policy to apply with sequences that fail when analyzed with PfamScan.",
      cliMap(PfamScanSequenceErrorPolicy.values(), PARAM_ERROR_POLICY_HELP_ENUM)
    );
  public static final String PARAM_ERROR_POLICY_HELP_GUI =
    toHtml(
      longEnumStringForGui(
        "The policy to apply with sequences that fail when analyzed with PfamScan.",
        guiMap(PfamScanSequenceErrorPolicy.values(), PARAM_ERROR_POLICY_HELP_ENUM)
      ),
      emptyList(), PARAM_ERROR_POLICY_HELP_ITALIC, false
    );
  
  public static final String PARAM_BATCH_DELAY_FACTOR_NAME = "batch-delay-factor";
  public static final String PARAM_BATCH_DELAY_FACTOR_SHORT_NAME = "bdf";
  public static final String PARAM_BATCH_DELAY_FACTOR_DESCRIPTION = "Batch delay factor";
  public static final String PARAM_BATCH_DELAY_FACTOR_HELP = 
    "The delay factor between batches. SEDA runs PfamScan queries in batches of 30 sequences to meet the EMBL-EBI "
      + "guidelines regarding the usage of resources. A delay factor of 1 means that SEDA waits a time between "
      + "batches equal to the time required to analyze the first batch.";
  public static final String PARAM_BATCH_DELAY_FACTOR_HELP_GUI = toHtml(PARAM_BATCH_DELAY_FACTOR_HELP);

  public static final int DEFAULT_BATCH_DELAY_FACTOR = PfamScanRequestConfiguration.DEFAULT_BATCH_DELAY_FACTOR;
  public static final double DEFAULT_EVALUE = PfamScanRequestConfiguration.DEFAULT_EVALUE;
  public static final PfamScanSequenceErrorPolicy DEFAULT_ERROR_POLICY =
    PfamScanRequestConfiguration.DEFAULT_ERROR_POLICY;
}
