/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cli.parameters;

import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_ALL_FRAME_HELP;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_ALL_FRAME_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_ALL_FRAME_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CODON_TABLE_CUSTOM_HELP;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CODON_TABLE_CUSTOM_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CODON_TABLE_CUSTOM_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CODON_TABLE_HELP;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CODON_TABLE_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CODON_TABLE_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CONVERT_AMINO_ACID_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_CONVERT_AMINO_ACID_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_FRAME_HELP;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_FRAME_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_FRAME_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_JOIN_FRAME_HELP;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_JOIN_FRAME_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_JOIN_FRAME_SHORT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_REVERSE_COMPLEMENT_HELP;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_REVERSE_COMPLEMENT_NAME;
import static org.sing_group.seda.plugin.core.info.common.SequenceTranslationInfo.PARAM_REVERSE_COMPLEMENT_SHORT_NAME;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.core.ncbi.codes.NcbiCodonTables;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;

import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class SequenceTranslationCliParameters {
  public static final IntegerDefaultValuedStringConstructedOption OPTION_FRAME =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_FRAME_NAME, PARAM_FRAME_SHORT_NAME, PARAM_FRAME_HELP, 1
    );

  public static final FlagOption OPTION_ALL_FRAMES =
    new FlagOption(
      PARAM_ALL_FRAME_NAME, PARAM_ALL_FRAME_SHORT_NAME, PARAM_ALL_FRAME_HELP
    );

  public static final FlagOption OPTION_JOIN_FRAMES =
    new FlagOption(
      PARAM_JOIN_FRAME_NAME, PARAM_JOIN_FRAME_SHORT_NAME, PARAM_JOIN_FRAME_HELP
    );

  public static final FlagOption OPTION_REVERSE_COMPLEMENT =
    new FlagOption(
      PARAM_REVERSE_COMPLEMENT_NAME, PARAM_REVERSE_COMPLEMENT_SHORT_NAME, PARAM_REVERSE_COMPLEMENT_HELP
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_CODON_TABLE =
    new IntegerDefaultValuedStringConstructedOption(
      PARAM_CODON_TABLE_NAME, PARAM_CODON_TABLE_SHORT_NAME, PARAM_CODON_TABLE_HELP, 1
    );

  public static final FileOption OPTION_CODON_TABLE_CUSTOM =
    new FileOption(
      PARAM_CODON_TABLE_CUSTOM_NAME, PARAM_CODON_TABLE_CUSTOM_SHORT_NAME, PARAM_CODON_TABLE_CUSTOM_HELP, true, true
    );

  private final boolean checkAminoAcidOption;
  private final boolean checkJoinFramesOption;
  private String convertAminoAcidHelp;
  private FlagOption optionConvertAminoAcid;

  public SequenceTranslationCliParameters(
    boolean checkAminoAcidOption, boolean checkJoinFramesOption, String convertAminoAcidHelp
  ) {
    this.checkAminoAcidOption = checkAminoAcidOption;
    this.checkJoinFramesOption = checkJoinFramesOption;
    this.convertAminoAcidHelp = convertAminoAcidHelp;
  }

  public List<Option<?>> getOptionList() {
    final List<Option<?>> options = new ArrayList<>();
    if (this.checkAminoAcidOption) {
      this.optionConvertAminoAcid =
        new FlagOption(
          PARAM_CONVERT_AMINO_ACID_NAME, PARAM_CONVERT_AMINO_ACID_SHORT_NAME, this.convertAminoAcidHelp
        );
      options.add(this.optionConvertAminoAcid);
    }
    options.add(OPTION_FRAME);
    options.add(OPTION_ALL_FRAMES);
    if (this.checkJoinFramesOption) {
      options.add(OPTION_JOIN_FRAMES);
    }
    options.add(OPTION_REVERSE_COMPLEMENT);
    options.add(OPTION_CODON_TABLE);
    options.add(OPTION_CODON_TABLE_CUSTOM);

    return options;
  }

  public boolean hasConvertAminoAcid(Parameters parameters) {
    return parameters.hasOption(this.optionConvertAminoAcid);
  }

  public SequenceTranslationConfiguration getSequenceTranslationConfiguration(Parameters parameters)
    throws IllegalArgumentException {
    if (checkAminoAcidOption && !hasConvertAminoAcid(parameters)) {
      throw new IllegalArgumentException("Missing " + SedaCommand.formatParam(this.optionConvertAminoAcid) + " option");
    }
    Map<String, String> codonTable = Collections.emptyMap();
    if (parameters.hasOption(OPTION_CODON_TABLE_CUSTOM)) {
      codonTable = loadCustomMap(parameters.getSingleValue(OPTION_CODON_TABLE_CUSTOM));
    } else {
      NcbiCodonTables ncbiCodonTables = new NcbiCodonTables();
      codonTable = ncbiCodonTables.getCodonTable(parameters.getSingleValue(OPTION_CODON_TABLE));
    }
    boolean isReverseComplement = parameters.hasFlag(OPTION_REVERSE_COMPLEMENT);
    boolean isJoinFrames = false;

    int[] frames = new int[] {
      parameters.getSingleValue(OPTION_FRAME)
    };

    if (parameters.hasFlag(OPTION_ALL_FRAMES)) {
      frames = new int[] {
        1, 2, 3
      };
      if (checkJoinFramesOption) {
        isJoinFrames = parameters.hasFlag(OPTION_JOIN_FRAMES);
      }
    }

    return new SequenceTranslationConfiguration(codonTable, isReverseComplement, isJoinFrames, frames);
  }

  private Map<String, String> loadCustomMap(File file) throws IllegalArgumentException {
    if (!file.exists() || !file.canRead()) {
      throw new IllegalArgumentException("Error processing custom codon file, check if the file exists");
    }
    Properties properties = new Properties();
    try {
      properties.load(Files.newInputStream(file.toPath()));
    } catch (IOException e) {
      throw new IllegalArgumentException("Error reading custom codon file. Wrong format", e);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error processing custom codon file, check if the file exists", e);
    }
    Map<String, String> customCodonTable = new HashMap<>();
    for (final String name : properties.stringPropertyNames()) {
      customCodonTable.put(name, properties.getProperty(name));
    }
    return customCodonTable;
  }
}
