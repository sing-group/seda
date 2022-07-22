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

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.InDiskDatatypeFactory;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.DatasetProcessorConfiguration;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.TransformationException;

import es.uvigo.ei.sing.yacli.command.AbstractCommand;
import es.uvigo.ei.sing.yacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.FlagOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.OptionCategory;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

/**
 * An abstract class with common logic for SEDA commands. All this commands must
 * extend this class.
 */
public abstract class SedaCommand extends AbstractCommand {
  protected static final String OPTION_INPUT_DIRECTORY_NAME = "input-directory";
  protected static final String OPTION_OUTPUT_DIRECTORY_NAME = "output-directory";
  protected static final String OPTION_INPUT_FILE_NAME = "input-file";
  protected static final String OPTION_INPUT_LIST_NAME = "input-list";
  protected static final String OPTION_PARAMETERS_FILE_NAME = "parameters-file";
  protected static final String OPTION_SAVE_PARAMETERS_FILE_NAME = "save-parameters-file";
  protected static final String OPTION_OUTPUT_GROUP_SIZE_NAME = "output-group-size";
  protected static final String OPTION_OUTPUT_GZIP_NAME = "output-gzip";
  protected static final String OPTION_DISK_PROCESSING_NAME = "in-disk-processing";

  public static final List<OptionCategory> GROUP_INPUT = asList(new OptionCategory("Input options"));

  public static final FileOption OPTION_INPUT_DIRECTORY =
    new FileOption(
      GROUP_INPUT, OPTION_INPUT_DIRECTORY_NAME, "id", "Path to the folder containing the files to process.", true, true
    );

  public static final FileOption OPTION_INPUT_FILE =
    new FileOption(
      GROUP_INPUT, OPTION_INPUT_FILE_NAME, "if", "Path to the file to process.", true, true, true
    );

  public static final FileOption OPTION_INPUT_LIST =
    new FileOption(
      GROUP_INPUT, OPTION_INPUT_LIST_NAME, "il", "Plain-text file with the paths of the files to process. ", true, true
    );

  public static final List<OptionCategory> GROUP_OUTPUT = asList(new OptionCategory("Output options"));

  public static final FileOption OPTION_OUTPUT_DIRECTORY =
    new FileOption(
      GROUP_OUTPUT, OPTION_OUTPUT_DIRECTORY_NAME, "od", "Path to the folder to create the result files.", false, true
    );

  public static final BooleanOption OPTION_OUTPUT_GZIP =
    new BooleanOption(
      GROUP_OUTPUT, OPTION_OUTPUT_GZIP_NAME, "gz", "Whether the output files must be compressed using gzip.", true,
      false
    );

  public static final IntegerDefaultValuedStringConstructedOption OPTION_OUTPUT_GROUP_SIZE =
    new IntegerDefaultValuedStringConstructedOption(
      GROUP_OUTPUT, OPTION_OUTPUT_GROUP_SIZE_NAME, "sz",
      "Whether output files must be split into subdirectories of a defined size. By default (0), no split subdirectories are created.",
      0
    );

  public static final List<OptionCategory> GROUP_CONFIGURATION = asList(new OptionCategory("Configuration options"));

  public static final FlagOption OPTION_DISK_PROCESSING =
    new FlagOption(
      GROUP_CONFIGURATION, OPTION_DISK_PROCESSING_NAME, "dp",
      "Whether files must be procesed in hard disk. If not specified, files are processed in RAM memory. "
        + "This option is slower but allows processing big batches of files with thousands of sequences."
    );

  public static final List<OptionCategory> GROUP_COMMAND_OPTIONS =
    asList(new OptionCategory("Command configuration files"));

  public static final FileOption OPTION_PARAMETERS_FILE =
    new FileOption(
      GROUP_COMMAND_OPTIONS, OPTION_PARAMETERS_FILE_NAME, "pf",
      "File with the command configuration (created using --" + OPTION_SAVE_PARAMETERS_FILE_NAME
        + "/-spf or the GUI) to load the command options.",
      true, true
    );

  public static final FileOption OPTION_SAVE_PARAMETERS_FILE =
    new FileOption(
      GROUP_COMMAND_OPTIONS, OPTION_SAVE_PARAMETERS_FILE_NAME, "spf",
      "File to save the command configuration options for later reuse.", true, true
    );

  /**
   * Lists the available command-line options of the command.
   *
   * @return the available command-line options of the command.
   */
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
    options.add(OPTION_DISK_PROCESSING);
    options.addAll(this.createSedaOptions());

    return options;
  }

  /**
   * Executes the command with the given parameters.
   *
   * @param parameters
   *          the command parameters
   * @throws Exception
   *           if an error occurs while processing the input files
   * @throws Exception
   *           if an error occurs on the transformation process
   * @throws Exception
   *           if an error occurs while saving the transformation parameters
   *           file
   */
  @Override
  public void execute(Parameters parameters) throws Exception {
    this.checkInputOptions(parameters);

    Stream<Path> inputs = this.getInputPaths(parameters);
    Path output = this.getOutputPath(parameters);

    final DatasetProcessorConfiguration configuration = getConfiguration(parameters);

    final DatatypeFactory datatypeFactory = this.getDatatypeFactory(parameters);
    final TransformationProvider transformation = this.getTransformationProvider(parameters);

    final DatasetProcessor processor = new DatasetProcessor(datatypeFactory);

    this.checkSaveTransformation(parameters, transformation);

    try {
      processor.process(inputs, output, transformation.getTransformation(datatypeFactory), configuration);
    } catch (TransformationException e) {
      System.out.println("SedaCommand: ");
      e.printStackTrace();
    }
  }

  private void checkInputOptions(Parameters parameters) {
    if (
      (!parameters.hasOption(OPTION_INPUT_DIRECTORY) ^ !parameters.hasOption(OPTION_INPUT_FILE)
        ^ !parameters.hasOption(OPTION_INPUT_LIST))
        ^ (parameters.hasOption(OPTION_INPUT_DIRECTORY) && parameters.hasOption(OPTION_INPUT_FILE)
          && parameters.hasOption(OPTION_INPUT_LIST))
    ) {
      validationError("An Input (file, directory or list) is mandatory");
    }
  }

  private Stream<Path> getInputPaths(Parameters parameters) throws IOException {
    if (parameters.hasOption(OPTION_INPUT_DIRECTORY)) {
      return getInputDirectory(parameters);
    } else if (parameters.hasOption(OPTION_INPUT_FILE)) {
      return getInputFile(parameters);
    } else if (parameters.hasOption(OPTION_INPUT_LIST)) {
      return getInputList(parameters);
    } else {
      throw new IllegalStateException("An input mode must be specified.");
    }
  }

  private Stream<Path> getInputList(Parameters parameters) throws IOException {
    Path pathFile = parameters.getSingleValue(OPTION_INPUT_LIST).toPath();

    if (!Files.exists(pathFile)) {
      validationError("Invalid path. The path to the file must be valid and exist.");
    }

    return Files.lines(pathFile)
      .filter(s -> Files.isRegularFile(Paths.get(s)))
      .map(Paths::get);
  }

  private Stream<Path> getInputFile(Parameters parameters) throws IllegalArgumentException {

    List<File> fileList = parameters.getAllValues(OPTION_INPUT_FILE);

    if (fileList.stream().anyMatch(f -> !f.isFile())) {
      validationError("Invalid path. The path to the file must be valid and exist.");
    }

    return fileList.stream().map(File::toPath);
  }

  private Stream<Path> getInputDirectory(Parameters parameters) throws IOException {
    Path inputPath = Paths.get(parameters.getSingleValueString(OPTION_INPUT_DIRECTORY));
    if (!Files.isDirectory(inputPath)) {
      validationError("--input-directory have to be a directory");
    }

    List<Path> fileList = Files.list(inputPath).filter(Files::isRegularFile).collect(Collectors.toList());

    if (fileList.isEmpty()) {
      validationError("Invalid path. The directory cant be empty.");
    }

    return fileList.stream();
  }

  private Path getOutputPath(Parameters parameters) {
    Path outputPath = Paths.get(parameters.getSingleValueString(OPTION_OUTPUT_DIRECTORY));
    if (Files.notExists(outputPath)) {
      outputPath.toFile().mkdir();
    }

    return outputPath;
  }

  private DatasetProcessorConfiguration getConfiguration(Parameters parameters) {
    return new DatasetProcessorConfiguration(
      parameters.getSingleValue(OPTION_OUTPUT_GROUP_SIZE), parameters.hasOption(OPTION_OUTPUT_GZIP)
    );
  }

  private DatatypeFactory getDatatypeFactory(Parameters parameters) {
    return parameters.hasOption(OPTION_DISK_PROCESSING) ? new InDiskDatatypeFactory() : getDefaultDatatypeFactory();
  }

  private TransformationProvider getTransformationProvider(Parameters parameters) throws IOException {

    TransformationProvider transformation;

    if (parameters.hasOption(OPTION_PARAMETERS_FILE)) {
      File parameterFile = parameters.getSingleValue(OPTION_PARAMETERS_FILE);

      if (!parameterFile.isFile()) {
        validationError("Invalid path. The parameters file must be valid and exist.");
      }

      transformation = this.getTransformation(parameterFile);

    } else {

      checkMandatoryOptions(parameters);

      transformation = this.getTransformation(parameters);
    }

    Validation validation = transformation.validate();

    if (!validation.isValid()) {
      formattedValidationErrors(validation.getValidationErrors());
    }

    return transformation;
  }

  private void checkSaveTransformation(Parameters parameters, final TransformationProvider transformation)
    throws IOException {
    if (parameters.hasOption(OPTION_SAVE_PARAMETERS_FILE)) {
      File parametersFile = parameters.getSingleValue(OPTION_SAVE_PARAMETERS_FILE);

      this.saveTransformation(transformation, parametersFile);
    }
  }

  protected static String formatValidationErrors(String... errors) {
    return (formatValidationErrors(asList(errors)));
  }

  protected static String formatValidationErrors(List<String> validationErrors) {
    StringBuilder sb = new StringBuilder("The transformation is not valid: ");
    if (validationErrors.size() == 1) {
      sb.append(validationErrors.get(0));
    } else {
      sb.append("\n\t - ").append(validationErrors.stream().collect(joining("\n\t - ")));
    }

    return sb.toString();
  }

  public static <T> void invalidEnumValue(Option<T> option) {
    invalidOptionValue(option, "Invalid value for ");
  }

  public static <T> void invalidOptionValue(Option<T> option, String message) {
    formattedValidationError(message + formatParam(option) + ".\n\nOption description: " + option.getDescription());
  }

  public static void formattedValidationError(String error) {
    validationError(formatValidationErrors(error));
  }

  protected static void formattedValidationErrors(List<String> errors) {
    validationError(formatValidationErrors(errors));
  }

  private static void validationError(String message) {
    System.err.println(message);
    System.exit(1);
  }

  /**
   * Checks if there are any mandatory options in the command parameters. If
   * there are, it throws an exception.
   *
   * @param parameters
   *          the command parameters
   */
  protected void checkMandatoryOptions(Parameters parameters) {
    List<Option<?>> missingOptions =
      this.getMandatoryOptions().stream()
        .filter(option -> !parameters.hasOption(option))
        .collect(toList());

    if (!missingOptions.isEmpty()) {

      List<String> stringErrorList =
        missingOptions.stream()
          .map(SedaCommand::formatMissingMandatoryOptionMessage)
          .collect(toList());

      formattedValidationErrors(stringErrorList);
    }
  }

  /**
   * Recover the mandatory options for the command. The commands must override
   * this method to set their mandatory options.
   *
   * @return the mandatory options of a command
   */
  protected List<Option<?>> getMandatoryOptions() {
    return Collections.emptyList();
  }

  protected abstract List<Option<?>> createSedaOptions();

  /**
   * Returns the transformation provider for the command.
   *
   * @param parameters
   *          the command parameters
   * @return the transformation provider
   */
  protected abstract TransformationProvider getTransformation(Parameters parameters);

  /**
   * Loads the transformation provider from the given file. This file must be
   * generated by SEDA. This method should use this structure:
   *
   * <pre>
   * {@code
   * return new JsonObjectReader<CommandTransformationProvider>()
   *   .read(parametersFile, CommandTransformationProvider.class);
   *   }
   * </pre>
   *
   * @param parametersFile
   *          the file to load the transformation provider
   * @return the transformation provider
   * @throws IOException
   *           if the file is not valid
   */
  protected abstract TransformationProvider getTransformation(File parametersFile) throws IOException;

  /**
   * Saves the command configuration to the given file.
   *
   * @param provider
   *          the transformation provider with the configuration to save
   * @param file
   *          the file to save the configuration
   * @throws IOException
   *           if the file is not valid
   */
  protected void saveTransformation(TransformationProvider provider, File file) throws IOException {
    new JsonObjectWriter<TransformationProvider>().write(provider, file);
  }

  public static <T> void checkMandatoryOption(Parameters parameters, Option<T> option) {
    if (!parameters.hasOption(option)) {
      validationError(formatMissingMandatoryOptionMessage(option));
    }
  }

  protected static <T> String formatMissingMandatoryOptionMessage(Option<T> option) {
    StringBuilder sb = new StringBuilder("Missing parameter: ");
    sb.append(formatParam(option)).append(" is mandatory.");

    return sb.toString();
  }

  public static <T> String formatParam(Option<T> option) {
    StringBuilder sb = new StringBuilder();
    sb.append("--").append(option.getParamName()).append("/-").append(option.getShortName());

    return sb.toString();
  }

  /**
   * Returns the group category for the command.
   *
   * @return the group category
   */
  protected abstract String getSedaGroup();
}
