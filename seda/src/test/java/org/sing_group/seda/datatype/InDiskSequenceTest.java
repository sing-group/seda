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
package org.sing_group.seda.datatype;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.IsEqualToSequence.equalToSequence;
import static org.sing_group.seda.io.TestFastaFileInformations.getFnaFileInformation;

import java.nio.file.Path;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.InDiskSequence;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class InDiskSequenceTest {
  private Sequence sequence;
  private Path file;
  
  public InDiskSequenceTest(Sequence sequence) {
    this.sequence = sequence;
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return stream(getFnaFileInformation().getSequences())
      .map(sequence -> new Object[] { sequence })
    .collect(toList());
  }
  
  @After
  public void checkFileDeletion() {
    System.gc();
    System.runFinalization();
    
    assertThat(this.file.toFile(), not(anExistingFile()));
    
    this.file = null;
  }
  
  @Test
  public void testCreatingTempFile() {
    final InDiskSequence actual = new InDiskSequence(
      sequence.getName(),
      sequence.getDescription(),
      sequence.getChain(),
      sequence.getProperties()
    );
    this.file = actual.getFile();
    
    assertThat(actual, is(equalToSequence(sequence)));
  }

}
