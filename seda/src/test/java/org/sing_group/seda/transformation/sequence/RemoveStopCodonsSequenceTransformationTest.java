/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.transformation.sequence;

import static org.junit.Assert.assertEquals;
import static org.sing_group.seda.TestUtils.sequence;
import static org.sing_group.seda.bio.StopCodon.TAA;
import static org.sing_group.seda.bio.StopCodon.TAG;
import static org.sing_group.seda.bio.StopCodon.TGA;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class RemoveStopCodonsSequenceTransformationTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(
      new Object[][] {
            { sequence(TAA.getChain()), sequence("") },
            { sequence(TAG.getChain()), sequence("") },
            { sequence(TGA.getChain()), sequence("") },
            { sequence("ACT" + TGA.getChain()), sequence("ACT") },
            { sequence("ACT" + TGA.getChain() + "TTT"), sequence("ACT" + TGA.getChain() + "TTT") },
      }
    );
  }

  private Sequence sequence;
  private Sequence expectedSequence;

  public RemoveStopCodonsSequenceTransformationTest(Sequence sequence, Sequence expectedSequence) {
    this.sequence = sequence;
    this.expectedSequence = expectedSequence;
  }

  @Test
  public void removeStopCodonsTest() {
    Sequence transformed = new RemoveStopCodonsSequenceTransformation().transform(this.sequence);
    assertEquals(this.expectedSequence, transformed);
  }
}