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

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.Sequence.of;
import static org.sing_group.seda.datatype.SequencesGroup.of;

import org.junit.Test;
import org.sing_group.seda.core.filtering.RegexConfiguration;
import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector.TieBreakOption;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.matcher.ContainsSameSequencesMatcher;
import org.sing_group.seda.transformation.sequencesgroup.RemoveIsoformsSequencesGroupTransformation.RemoveIsoformsTransformationConfiguration;

public class RemoveIsoformsSequencesGroupTransformationTest {

	@Test
	public void selectIsoformsWithoutSeparationTest() {
		Sequence S1 = of("S1", "", "AAAATTTT", emptyMap());
		Sequence S2 = of("S2", "", "CGTACGTA", emptyMap());
		Sequence S3 = of("S3", "", "AAATTT", emptyMap());
		Sequence S4 = of("S4", "", "GGGGGGT", emptyMap());
		Sequence S5 = of("S5", "", "GGGGGT", emptyMap());
		Sequence S6 = of("S6", "", "CCCCCCCCCC", emptyMap());
		Sequence S7 = of("S7", "", "CCCCCCCC", emptyMap());
		Sequence S8 = of("S8", "", "CCCCCCC", emptyMap());
		SequencesGroup group = of("G", S1, S2, S3, S4, S5, S6, S7, S8);

		RemoveIsoformsSequencesGroupTransformation transformation = new RemoveIsoformsSequencesGroupTransformation(
				new RemoveIsoformsTransformationConfiguration(6, false),
		    new DefaultSequenceIsoformSelector(10, TieBreakOption.SHORTEST)
	  );

		SequencesGroup result = transformation.transform(group);

		SequencesGroup expectedSequencesGroup = SequencesGroup.of("Expected", S1, S2, S4, S6);

		assertThat(
        result,
        ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
      );
	}

	@Test
	public void selectIsoformsWithSeparationTest() {
		Sequence S1 = of("Homo_Sapiens_1", "", "AAAATTTT", 	emptyMap());
		Sequence S2 = of("Homo_Sapiens_2", "",  "AAATTT", 	emptyMap());
		Sequence S3 = of("Homo_Sapiens_3", "", "CCCCGGGG", 	emptyMap());
		Sequence S4 = of("Homo_Sapiens_4", "",  "CCCGGG", 	emptyMap());
		Sequence S5 = of("Mus_Musculus_1", "", "AAAATTTT", 	emptyMap());
		Sequence S6 = of("Mus_Musculus_2", "",  "AAATTT", 	emptyMap());
		Sequence S7 = of("Mus_Musculus_3", "", "CCCCGGGG", 	emptyMap());
		Sequence S8 = of("Mus_Musculus_4", "",  "CCCGGG", 	emptyMap());
		SequencesGroup group = of("G", S1, S2, S3, S4, S5, S6, S7, S8);

		RemoveIsoformsSequencesGroupTransformation transformation = new RemoveIsoformsSequencesGroupTransformation(
				new RegexHeaderMatcher("(.*)_[0-9]", HeaderTarget.NAME, new RegexConfiguration(false, 1, false)),
				new RemoveIsoformsTransformationConfiguration(6, false),
				new DefaultSequenceIsoformSelector(10, TieBreakOption.SHORTEST)
			);

		SequencesGroup result = transformation.transform(group);

		SequencesGroup expectedSequencesGroup = SequencesGroup.of("Expected", S1, S3, S5, S7);

    assertThat(
        result,
        ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
      );
	}

	@Test
	public void selectIsoformsWithSeparationAndAddRemovedIsoformsToHeadersTest() {
		Sequence S1 = of("Homo_Sapiens_1", "", "AAAATTTT", 	emptyMap());
		Sequence S2 = of("Homo_Sapiens_2", "",  "AAATTT", 	emptyMap());
		Sequence S3 = of("Homo_Sapiens_3", "", "CCCCGGGG", 	emptyMap());
		Sequence S4 = of("Homo_Sapiens_4", "",  "CCCGGG", 	emptyMap());
		Sequence S5 = of("Mus_Musculus_1", "", "AAAATTTT", 	emptyMap());
		Sequence S6 = of("Mus_Musculus_2", "",  "AAATTT", 	emptyMap());
		Sequence S7 = of("Mus_Musculus_3", "", "CCCCGGGG", 	emptyMap());
		Sequence S8 = of("Mus_Musculus_4", "",  "CCCGGG", 	emptyMap());
		SequencesGroup group = of("G", S1, S2, S3, S4, S5, S6, S7, S8);

		RemoveIsoformsSequencesGroupTransformation transformation = new RemoveIsoformsSequencesGroupTransformation(
				new RegexHeaderMatcher("(.*)_[0-9]", HeaderTarget.NAME, new RegexConfiguration(false, 1, false)),
				new RemoveIsoformsTransformationConfiguration(6, true),
				new DefaultSequenceIsoformSelector(10, TieBreakOption.SHORTEST)
		);

		SequencesGroup result = transformation.transform(group);

		SequencesGroup expectedSequencesGroup = SequencesGroup.of("Expected",
				Sequence.of(S5.getName(), "[Isoforms: " + S6.getName() + "]", S5.getChain(), S5.getProperties()),
				Sequence.of(S7.getName(), "[Isoforms: " + S8.getName() + "]", S7.getChain(), S7.getProperties()),
				Sequence.of(S1.getName(), "[Isoforms: " + S2.getName() + "]", S1.getChain(), S1.getProperties()),
				Sequence.of(S3.getName(), "[Isoforms: " + S4.getName() + "]", S3.getChain(), S3.getProperties())
		);

		assertThat(
				result,
				ContainsSameSequencesMatcher.containsSameSequencesThat(expectedSequencesGroup)
			);
	}
}
