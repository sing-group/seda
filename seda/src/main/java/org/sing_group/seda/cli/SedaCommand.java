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
package org.sing_group.seda.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.DatasetProcessorConfiguration;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.AbstractCommand;
import es.uvigo.ei.sing.yacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public abstract class SedaCommand extends AbstractCommand {
  protected static final String OPTION_INPUT_DIRECTORY_NAME = "input-directory";
  protected static final String OPTION_OUTPUT_DIRECTORY_NAME = "output-directory";
  protected static final String OPTION_INPUT_FILE_NAME = "input-file";
  protected static final String OPTION_INPUT_LIST_NAME = "input-list";
  protected static final String OPTION_PARAMETERS_FILE_NAME = "parameters-file";
  protected static final String OPTION_SAVE_PARAMETERS_FILE_NAME = "save-parameters-file";
  protected static final String OPTION_OUTPUT_GROUP_SIZE_NAME = "output-group-size";
  protected static final String OPTION_OUTPUT_GZIP_NAME = "output-gzip";

  public static final FileOption OPTION_INPUT_DIRECTORY =
    new FileOption(
      OPTION_INPUT_DIRECTORY_NAME, "id", "Path to the folder containing the files to process ", true, true
    );

  public static final FileOption OPTION_OUTPUT_DIRECTORY =
    new FileOption(
      OPTION_OUTPUT_DIRECTORY_NAME, "od", "Path to the folder containing the output files with the results", false, true
    );

  public static final FileOption OPTION_INPUT_FILE =
    new FileOption(OPTION_INPUT_FILE_NAME, "if", "Path to the file to process", true, true, true);

  public static final FileOption OPTION_INPUT_LIST =
    new FileOption(OPTION_INPUT_LIST_NAME, "il", "File with paths to the files to process", true, true);

  public static final FileOption OPTION_PARAMETERS_FILE =
    new FileOption(
      OPTION_PARAMETERS_FILE_NAME, "pf", "File with the parameters configuration for a command", true, true
    );

  public static final FileOption OPTION_SAVE_PARAMETERS_FILE =
    new FileOption(
      OPTION_SAVE_PARAMETERS_FILE_NAME, "spf", "File path to save parameters configuration for the command", true, true
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_OUTPUT_GROUP_SIZE =
    new IntegerDefaultValuedStringConstructedOption(
      OPTION_OUTPUT_GROUP_SIZE_NAME, "sz", "Group size", 0
    );

  public static final BooleanOption OPTION_OUTPUT_GZIP =
    new BooleanOption(
      OPTION_OUTPUT_GZIP_NAME, "gz", "gzip", true, false
    );

  @Override
  protected List<Option<?>> createOptions() {
    final List<Option<?>> options = new ArrayList<>();

    options.add(OPTION_INPUT_DIRECTORY);
    options.add(OPTION_OUTPUT_DIRECTORY);
    options.add(OPTION_INPUT_FILE);
    options.add(OPTION_INPUT_LIST);
    options.add(OPTION_PARAMETERS_FILE);
    options.add(OPTION_SAVE_PARAMETERS_FILE);
    options.add(OPTION_OUTPUT_GROUP_SIZE);
    options.add(OPTION_OUTPUT_GZIP);
    options.addAll(this.createSedaOptions());

    return options;
  }

  @Override
  public void execute(Parameters parameters) throws Exception {

    if (
      (!parameters.hasOption(OPTION_INPUT_DIRECTORY) ^ !parameters.hasOption(OPTION_INPUT_FILE)
        ^ !parameters.hasOption(OPTION_INPUT_LIST)) ^
        (parameters.hasOption(OPTION_INPUT_DIRECTORY) && parameters.hasOption(OPTION_INPUT_FILE)
          && parameters.hasOption(OPTION_INPUT_LIST))
    ) {
      throw new IllegalArgumentException("An Input (file, directory or list) is mandatory");
    }

    Stream<Path> paths = Stream.empty();

    if (parameters.hasOption(OPTION_INPUT_DIRECTORY)) {
      Path inputPath = Paths.get(parameters.getSingleValueString(OPTION_INPUT_DIRECTORY));
      paths = Files.list(inputPath).filter(Files::isRegularFile);

      if (!Files.isDirectory(inputPath)) {
        throw new IllegalArgumentException("--input-directory have to be a directory");
      }
    }

    if (parameters.hasOption(OPTION_INPUT_FILE)) {

      paths = parameters.getAllValues(OPTION_INPUT_FILE).stream().map(File::toPath);

    }

    if (parameters.hasOption(OPTION_INPUT_LIST)) {
      paths =
        Files.lines(parameters.getSingleValue(OPTION_INPUT_LIST).toPath())
          .filter(s -> Files.isRegularFile(Paths.get(s)))
          .map(Paths::get);
    }

    int groupSize = parameters.getSingleValue(OPTION_OUTPUT_GROUP_SIZE);

    boolean gzip = parameters.hasOption(OPTION_OUTPUT_GZIP);

    final DatasetProcessorConfiguration configuration = new DatasetProcessorConfiguration(groupSize, gzip);

    final TransformationProvider transformation;

    if (parameters.hasOption(OPTION_PARAMETERS_FILE)) {
      transformation = this.getTransformation(parameters.getSingleValue(OPTION_PARAMETERS_FILE));
    } else {
      transformation = this.getTransformation(parameters);
    }

    DatasetProcessor processor = new DatasetProcessor(DatatypeFactory.getDefaultDatatypeFactory());

    Path outputPath = Paths.get(parameters.getSingleValueString(OPTION_OUTPUT_DIRECTORY));

    if (Files.notExists(outputPath)) {
      outputPath.toFile().mkdir();
    }

    if (parameters.hasOption(OPTION_SAVE_PARAMETERS_FILE)) {
      this.saveTransformation(transformation, parameters.getSingleValue(OPTION_SAVE_PARAMETERS_FILE));
    }

    processor.process(
      paths, outputPath, transformation.getTransformation(DatatypeFactory.getDefaultDatatypeFactory()), configuration
    );
  }

  protected abstract List<Option<?>> createSedaOptions();

  protected abstract TransformationProvider getTransformation(Parameters parameters);

  protected abstract TransformationProvider getTransformation(File parametersFile) throws IOException;

  protected abstract TransformationProvider loadTransformation(File file) throws IOException;

  protected abstract void saveTransformation(TransformationProvider provider, File file) throws IOException;
}
