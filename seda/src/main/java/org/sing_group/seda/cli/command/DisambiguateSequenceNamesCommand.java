package org.sing_group.seda.cli.command;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.gui.disambiguate.DisambiguateSequenceNamesTransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.DisambiguateSequenceNamesTransformation.Mode;

import es.uvigo.ei.sing.yacli.command.option.BooleanOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class DisambiguateSequenceNamesCommand extends SedaCommand {
	private static final String OPTION_RENAME_NAME = "rename";
	private static final String OPTION_REMOVE_NAME = "remove";

	public static final BooleanOption OPTION_RENAME = new BooleanOption(OPTION_RENAME_NAME, "rn",
			"[DEFAULT] Rename: add a numeric prefix to disambiguate duplicate identifiers.", true, false);

	public static final BooleanOption OPTION_REMOVE = new BooleanOption(OPTION_REMOVE_NAME, "rm",
			"Remove: remove sequences with duplicate identifiers, keeping the first occurrence.", true, false);

	@Override
	public String getName() {
		return "disambiguate";
	}

	@Override
	public String getDescriptiveName() {
		return "The method to disambiguate sequences.";
	}

	@Override
	public String getDescription() {
		return "The method to disambiguate sequences with duplicated identifiers.";
	}

	@Override
	public void execute(Parameters parameters) throws Exception {
//		DisambiguateSequenceNamesTransformation transformation;
		DisambiguateSequenceNamesTransformationProvider provider = new DisambiguateSequenceNamesTransformationProvider();

		if (parameters.hasOption(OPTION_REMOVE)) {
//			transformation = new DisambiguateSequenceNamesTransformation(Mode.REMOVE);
			provider.setMode(Mode.REMOVE);

		} else {
//			transformation = new DisambiguateSequenceNamesTransformation(Mode.RENAME);
			provider.setMode(Mode.RENAME);
		}

		// Data to test the command
		Sequence s1 = Sequence.of("Test", "", "ACCTTGG");
		Sequence s2 = Sequence.of("Test", "", "ACCTTGG");

		SequencesGroup group = SequencesGroup.of("TestGroup", Collections.emptyMap(), s1, s2);

//		SequencesGroup groupResult = transformation.transform(group);
		SequencesGroupDataset dataset = provider.getTransformation(DatatypeFactory.getDefaultDatatypeFactory())
				.transform(SequencesGroupDataset.of(group));
		SequencesGroup groupResult = dataset.getSequencesGroups().findFirst().get();

		System.out.println("Input");
		System.out.println(s1.getName() + ":" + s1.getChain());
		System.out.println(s2.getName() + ":" + s2.getChain());
		System.out.println("---------------------------------------------");
		System.out.println("Output");
		groupResult.getSequences().forEach(s -> System.out.println(s.getName() + ":" + s.getChain()));

	}

	@Override
	protected List<Option<?>> createSedaOptions() {
		return asList(OPTION_RENAME, OPTION_REMOVE);
	}

}
