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
package org.sing_group.seda.io;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.IsEqualToSequence.containsSequencesInOrder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class LazyFileSequencesGroupTest {
  protected String expectedName;
  private final Sequence[] expectedSequences;
  private LazyFileSequencesGroup sequenceGroup;
  
  public LazyFileSequencesGroupTest(
    String testName, // Ignored
    String name,
    Sequence[] sequences,
    Supplier<LazyFileSequencesGroup> sequenceGroup
  ) {
    this.expectedName = name;
    this.expectedSequences = sequences;
    this.sequenceGroup = sequenceGroup.get();
  }

  @Parameters(name = "{0}")
  public static Object[][] getParameters() {
    return TestFastaFileInformations.getFileInformations().entrySet().stream()
      .flatMap(entry -> {
        final String name = entry.getKey();
        final FastaFileInformation info  = entry.getValue();
        
        return Arrays.stream(new Object[][] {
          {
            name + " [File]",
            info.getSequenceGroupName(),
            info.getSequences(),
            (Supplier<LazyFileSequencesGroup>) () -> new LazyFileSequencesGroup(info.getPath())
          },
          {
            name + " [Sequences]",
            info.getSequenceGroupName(),
            info.getSequences(),
            (Supplier<LazyFileSequencesGroup>) () -> new LazyFileSequencesGroup(
              info.getSequenceGroupName(),
              info.getSequences()
            )
          },
          {
            name + " [Sequences Mixed]",
            info.getSequenceGroupName(),
            info.getSequences(),
            (Supplier<LazyFileSequencesGroup>) () -> new LazyFileSequencesGroup(
              info.getSequenceGroupName(),
              info.getMixedSequences()
            )
          },
          {
            name + " [Sequences Lazy]",
            info.getSequenceGroupName(),
            info.getSequences(),
            (Supplier<LazyFileSequencesGroup>) () -> new LazyFileSequencesGroup(
              info.getSequenceGroupName(),
              info.getLazySequences()
            )
          }
        });
      })
    .toArray(Object[][]::new);
  }
  
  @After
  public void afterTest() {
    final File file = this.sequenceGroup.getFile().toFile();
    final boolean isTempFile = this.sequenceGroup.isTempFile();
    
    this.sequenceGroup = null;
    
    this.checkFileDeletion(file, isTempFile);
  }

  private void checkFileDeletion(File file, boolean isTempFile) {
    System.gc();
    System.runFinalization();

    if (isTempFile) {
      assertThat(file, is(not(anExistingFile())));
    } else {
      assertThat(file, is(anExistingFile()));
    }
  }

  @Test
  public void testName() {
    assertThat(this.sequenceGroup.getName(), is(equalTo(expectedName)));
  }

  @Test
  public void testSequenceCount() {
    assertThat(this.sequenceGroup.getSequenceCount(), is(equalTo(expectedSequences.length)));
  }

  @Test
  public void testLoadFile() {
    final List<Sequence> sequences = sequenceGroup.getSequences()
      .collect(toList());
    
    assertThat(sequences, containsSequencesInOrder(expectedSequences));
  }
  
  @Test
  public void testAllSequencesAreLazy() {
    final List<Sequence> sequences = sequenceGroup.getSequences()
      .collect(toList());
    
    assertThat(sequences, everyItem(instanceOf(LazyFileSequence.class)));
  }

}