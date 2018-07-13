/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.transformation.sequencesgroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.core.operations.SequenceIsoformSelector;
import org.sing_group.seda.core.operations.SequencesGroupIsoformTester;
import org.sing_group.seda.core.operations.SequencesGroupIsoformTesterResult;
import org.sing_group.seda.core.operations.SequencesGroupSeparator;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.transformation.TransformationException;

public class RemoveIsoformsSequencesGroupTransformation implements SequencesGroupTransformation {
	private DatatypeFactory factory;
	private HeaderMatcher matcher;
	private RemoveIsoformsTransformationConfiguration configuration;
	private SequenceIsoformSelector isoformSelector;

	public RemoveIsoformsSequencesGroupTransformation(RemoveIsoformsTransformationConfiguration configuration,
	    SequenceIsoformSelector isoformSelector
	) {
		this(DatatypeFactory.getDefaultDatatypeFactory(), configuration, isoformSelector);
	}

	public RemoveIsoformsSequencesGroupTransformation(HeaderMatcher matcher,
	    RemoveIsoformsTransformationConfiguration configuration, SequenceIsoformSelector isoformSelector
	) {
		this(DatatypeFactory.getDefaultDatatypeFactory(), matcher, configuration, isoformSelector);
	}

	public RemoveIsoformsSequencesGroupTransformation(DatatypeFactory factory,
	    RemoveIsoformsTransformationConfiguration configuration, SequenceIsoformSelector isoformSelector
	) {
		this(factory, null, configuration, isoformSelector);
	}

	public RemoveIsoformsSequencesGroupTransformation(DatatypeFactory factory, HeaderMatcher matcher,
	    RemoveIsoformsTransformationConfiguration configuration, SequenceIsoformSelector isoformSelector
	) {
		this.factory = factory;
		this.matcher = matcher;
		this.configuration = configuration;
		this.isoformSelector = isoformSelector;
	}

	@Override
	public SequencesGroup transform(SequencesGroup sequencesGroup) throws TransformationException {
		SequencesGroupIsoformTesterResult isoformsResult = new SequencesGroupIsoformTesterResult();
		int minimumIsoformWordLength = this.configuration.getMinimumIsoformWordLength();

		if (matcher == null) {
			new SequencesGroupIsoformTester(sequencesGroup)
				.test(minimumIsoformWordLength)
				.getIsoformsLists().forEach(l -> {
					isoformsResult.addIsoformsList(l);
				});
		} else {
			Map<String, List<Sequence>> groups = new SequencesGroupSeparator(this.matcher).separate(sequencesGroup);
			groups.forEach((k, v) -> {
				SequencesGroup current = factory.newSequencesGroup(k, v);
				new SequencesGroupIsoformTester(current)
				.test(minimumIsoformWordLength)
				.getIsoformsLists().forEach(l -> {
					isoformsResult.addIsoformsList(l);
				});
			});
		}

		List<Sequence> sequences = new LinkedList<>();
		isoformsResult.getIsoformsLists().forEach(l -> {
			Sequence selectedSequence = isoformSelector.selectSequence(l);

			if(this.configuration.isAddRemovedIsoformNamesToHeaders() && l.size() > 1) {
				StringBuilder newDescriptionBuilder = new StringBuilder();

				newDescriptionBuilder
					.append(selectedSequence.getDescription())
					.append(selectedSequence.getDescription().isEmpty() ? "" : " ")
					.append("[Isoforms: ");

				List<String> isoformsNames = new LinkedList<>();

				for(Sequence s : l) {
					if(!s.equals(selectedSequence)) {
						isoformsNames.add(s.getName());
					}
				}
				newDescriptionBuilder.append(isoformsNames.stream().collect(Collectors.joining("; ")));
				newDescriptionBuilder.append("]");

				selectedSequence = factory.newSequence(
						selectedSequence.getName(),
						newDescriptionBuilder.toString(),
				    selectedSequence.getChain(),
				    selectedSequence.getProperties()
				);
			}

			sequences.add(selectedSequence);
		});

		return this.factory.newSequencesGroup(sequencesGroup.getName(), sequences);
	}

	public static class RemoveIsoformsTransformationConfiguration {
		private int minimumIsoformWordLength;
		private boolean addRemovedIsoformNamesToHeaders;

		public RemoveIsoformsTransformationConfiguration(int minimumIsoformWordLength,
		    boolean addRemovedIsoformNamesToHeaders
		) {
			this.minimumIsoformWordLength = minimumIsoformWordLength;
			this.addRemovedIsoformNamesToHeaders = addRemovedIsoformNamesToHeaders;
		}

		public int getMinimumIsoformWordLength() {
			return minimumIsoformWordLength;
		}

		public boolean isAddRemovedIsoformNamesToHeaders() {
			return addRemovedIsoformNamesToHeaders;
		}
	}
}
