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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.io.DatasetProcessor;
import org.sing_group.seda.io.DatasetProcessorConfiguration;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

import es.uvigo.ei.sing.yacli.command.AbstractCommand;
import es.uvigo.ei.sing.yacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yacli.command.option.FileOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public abstract class SedaCommand extends AbstractCommand {
	protected static final String OPTION_INPUT_DIRECTORY_NAME = "input_directory";
	protected static final String OPTION_OUTPUT_DIRECTORY_NAME = "output_directory";
	protected static final String OPTION_OUTPUT_GROUP_SIZE_NAME = "output_group_size";
	protected static final String OPTION_OUTPUT_GZIP_NAME = "output_gzip";

	public static final FileOption OPTION_INPUT_DIRECTORY = new FileOption(OPTION_INPUT_DIRECTORY_NAME, "input",
			"Path to the folder containing the files to process ", false, true);

	public static final FileOption OPTION_OUTPUT_DIRECTORY = new FileOption(OPTION_OUTPUT_DIRECTORY_NAME, "output",
			"Path to the folder containing the output files with the results", false, true);

	public static final IntegerOption OPTION_OUTPUT_GROUP_SIZE = new IntegerOption(OPTION_OUTPUT_GROUP_SIZE_NAME,
			"size", "Group size", 0);

	public static final BooleanOption OPTION_OUTPUT_GZIP = new BooleanOption(OPTION_OUTPUT_GZIP_NAME, "gzip", "gzip",
			false, false);

	@Override
	protected List<Option<?>> createOptions() {
		final List<Option<?>> options = new ArrayList<>();

		options.add(OPTION_INPUT_DIRECTORY);
		options.add(OPTION_OUTPUT_DIRECTORY);
		options.addAll(this.createSedaOptions());

		return options;
	}

	@Override
	public void execute(Parameters parameters) throws Exception {

		Path inputPath = Paths.get(parameters.getSingleValueString(OPTION_INPUT_DIRECTORY));

		Path outputPath = Paths.get(parameters.getSingleValueString(OPTION_OUTPUT_DIRECTORY));

		if (!Files.isDirectory(inputPath)) {
			throw new IllegalArgumentException("--input-directory have to be a directory");
		}

		Stream<Path> paths = Files.list(inputPath).filter(Files::isRegularFile);

		int groupSize = 0;

		if (parameters.hasOption(OPTION_OUTPUT_GROUP_SIZE)) {
			groupSize = parameters.getSingleValue(OPTION_OUTPUT_GROUP_SIZE);
		}

		boolean gzip = parameters.hasOption(OPTION_OUTPUT_GZIP);

		final DatasetProcessorConfiguration configuration = new DatasetProcessorConfiguration(groupSize, gzip);

		final SequencesGroupDatasetTransformation transformation = this.getTransformation(parameters);

		DatasetProcessor processor = new DatasetProcessor(DatatypeFactory.getDefaultDatatypeFactory());

		processor.process(paths, outputPath, transformation, configuration);
	}

	protected abstract List<Option<?>> createSedaOptions();

	protected abstract SequencesGroupDatasetTransformation getTransformation(Parameters parameters);
}
