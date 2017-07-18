package org.sing_group.seda.cli.command;

import static java.util.Arrays.asList;

import java.math.BigDecimal;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.sing_group.seda.cli.SedaCommand;

import es.uvigo.ei.sing.yaacli.command.option.BigDecimalOption;
import es.uvigo.ei.sing.yaacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yaacli.command.option.FileOption;
import es.uvigo.ei.sing.yaacli.command.option.IntegerOption;
import es.uvigo.ei.sing.yaacli.command.option.Option;
import es.uvigo.ei.sing.yaacli.command.option.StringOption;
import es.uvigo.ei.sing.yaacli.command.parameter.Parameters;

public class GenerateCommand extends SedaCommand {
	private static final String OPTION_STARTING_CODONS_NAME = "start_codons";
	private static final String OPTION_REMOVE_STOP_CODONS_NAME = "remove_stop_codons";
	private static final String OPTION_REMOVE_NON_MULTIPLE_OF_3_NAME = "remove_non_3";
	private static final String OPTION_REMOVE_IF_INFRAME_STOP_CODONS_NAME = "remove_in_frame_stop_codons";
	private static final String OPTION_REMOVE_BY_SIZE_NAME = "remove_by_size";
	private static final String OPTION_REFERENCE_SEQUENCE_NAME = "reference_sequence";
	private static final String OPTION_REMOVE_NUM_OF_SEQUENCES_NAME = "remove_by_num_of_sequences";
//	private static final String OPTION_INPUT_DIRECTORY_NAME = "input_dir";

	public static final StringOption OPTION_STARTING_CODONS = new StringOption(
		OPTION_STARTING_CODONS_NAME, "sc",
		"A comma-separated list of codons. Sequences that do not start with any of these codons will be removed.",
		true, true
	);
	
	public static final BooleanOption OPTION_REMOVE_STOP_CODONS = new BooleanOption(
		OPTION_REMOVE_STOP_CODONS_NAME, "rsc",
		"Removes the stop codon of sequences, if present.",
		true, false
	);
	
	public static final BooleanOption OPTION_REMOVE_NON_MULTIPLE_OF_3 = new BooleanOption(
		OPTION_REMOVE_NON_MULTIPLE_OF_3_NAME, "rn3",
		"Removes sequences with a length that is not multiple of three.",
		true, false
	);
	
	public static final BooleanOption OPTION_REMOVE_IF_INFRAME_STOP_CODONS = new BooleanOption(
		OPTION_REMOVE_IF_INFRAME_STOP_CODONS_NAME, "rifsc",
		"Removes sequences with in-frame stop codons",
		true, false
	);
	
	public static final BigDecimalOption OPTION_REMOVE_BY_SIZE = new BigDecimalOption(
		OPTION_REMOVE_BY_SIZE_NAME, "rbs",
		"A percentage value. Removes sequences with a sequence length that are the percentage "
		+ "provided bigger or smaller than a reference sequence. By default, first sequence will"
		+ "be used as reference, but a custom sequence can be used with the option: " + OPTION_REFERENCE_SEQUENCE_NAME,
		BigDecimal.valueOf(-1L)
	);
	
	public static final IntegerOption OPTION_REFERENCE_SEQUENCE = new IntegerOption(
		OPTION_REFERENCE_SEQUENCE_NAME, "rs",
		"The index of the reference sequence to be used in the " + OPTION_REMOVE_BY_SIZE_NAME + " option when removing by size. "
			+ "First sequence has index 0.",
		0
	);
	
	public static final IntegerOption OPTION_REMOVE_NUM_OF_SEQUENCES = new IntegerOption(
		OPTION_REMOVE_NUM_OF_SEQUENCES_NAME, "rns",
		"An integer value. Removes files with less sequences that the value provided.",
		-1
	);
	
	public static final FileOption OPTION_INPUT_DIRECTORY = new FileOption(
		OPTION_REMOVE_NUM_OF_SEQUENCES_NAME, "i",
		"Input directory.",
		false, true
	);
	

	@Override
	public String getName() {
		return "generate";
	}

	@Override
	public String getDescriptiveName() {
		return "Generate Dataset";
	}

	@Override
	public String getDescription() {
		return "Generate a new dataset from existing files";
	}

	@Override
	protected List<Option<?>> createSedaOptions() {
		return asList(
			OPTION_STARTING_CODONS,
			OPTION_REMOVE_STOP_CODONS,
			OPTION_REMOVE_NON_MULTIPLE_OF_3,
			OPTION_REMOVE_IF_INFRAME_STOP_CODONS,
			OPTION_REMOVE_BY_SIZE,
			OPTION_REFERENCE_SEQUENCE,
			OPTION_REMOVE_NUM_OF_SEQUENCES
		);
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
	  //TODO: Complete this
		throw new OperationNotSupportedException();
	}
}
