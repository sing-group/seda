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
package org.sing_group.seda.cli.command;

import static java.util.Arrays.asList;

import java.util.List;

import org.sing_group.seda.cli.SedaCommand;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.disambiguate.DisambiguateSequenceNamesTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
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
	public SequencesGroupDatasetTransformation getTransformation(Parameters parameters) {
		DisambiguateSequenceNamesTransformationProvider provider = new DisambiguateSequenceNamesTransformationProvider();

		if (parameters.hasOption(OPTION_REMOVE)) {
			provider.setMode(Mode.REMOVE);

		} else {
			provider.setMode(Mode.RENAME);
		}

		return provider.getTransformation(DatatypeFactory.getDefaultDatatypeFactory());

	}

	@Override
	protected List<Option<?>> createSedaOptions() {
		return asList(OPTION_RENAME, OPTION_REMOVE);
	}

}
