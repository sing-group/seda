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
package org.sing_group.seda.transformation.dataset;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.TestUtils.testSequences;
import static org.sing_group.seda.datatype.SequencesGroup.of;
import static org.sing_group.seda.datatype.SequencesGroupDataset.of;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.SequencesGroupDataset;
import org.sing_group.seda.transformation.TransformationException;


@RunWith(Parameterized.class)
public class SequenceCountFilterSequencesGroupDatasetTransformationTest {

  private static final SequencesGroupDataset DATASET = of(
      of("Size 1", emptyMap(), testSequences(1)),
      of("Size 2", emptyMap(), testSequences(2)),
      of("Size 3", emptyMap(), testSequences(3)),
      of("Size 4", emptyMap(), testSequences(4))
  );

  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(new Object[][] { 
      { DATASET, 0, 5, 4 },
      { DATASET, 1, 5, 4 },
      { DATASET, 1, 4, 4 },
      { DATASET, 2, 4, 3 },
      { DATASET, 1, 3, 3 },
      { DATASET, 3, 4, 2 },
      { DATASET, 1, 2, 2 },
      { DATASET, 2, 2, 1 },
      { DATASET, 1, 1, 1 },
      { DATASET, 3, 3, 1 },
      { DATASET, 4, 4, 1 },
      { DATASET, 5, 5, 0 },
    });
  }

  private SequencesGroupDataset dataset;
  private int min;
  private int max;
  private int expectedCount;

  public SequenceCountFilterSequencesGroupDatasetTransformationTest(SequencesGroupDataset dataset, int min, int max,
      int expectedCount
  ) {
    this.dataset = dataset;
    this.min = min;
    this.max = max;
    this.expectedCount = expectedCount;
  }

  @Test
  public void translateSequences() {
    SequencesGroupDatasetTransformation transformation = 
        new SequenceCountFilterSequencesGroupDatasetTransformation(this.min, this.max);

    try {
      SequencesGroupDataset transformedDataset = transformation.transform(this.dataset);
      assertEquals(transformedDataset.getSequencesGroupsCount(), this.expectedCount);
    } catch (TransformationException e) {
      assertEquals(0, this.expectedCount);
    }
  }
}
