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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.sing_group.seda.datatype.IsEqualToSequence.containsSequencesInOrder;
import static org.sing_group.seda.io.IOUtils.extractIfNeeded;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.io.FastaFileInformation;
import org.sing_group.seda.io.TestFastaFileInformations;

@RunWith(Parameterized.class)
public class InDiskSequencesGroupTest {
  protected String expectedName;
  private final Sequence[] expectedSequences;
  private InDiskSequencesGroup sequenceGroup;
  
  public InDiskSequencesGroupTest(
    String testName, // Ignored
    String name,
    Sequence[] sequences,
    Supplier<InDiskSequencesGroup> sequenceGroupFactory
  ) {
    this.expectedName = name;
    this.expectedSequences = sequences;
    this.sequenceGroup = sequenceGroupFactory.get();
  }

  @Parameters(name = "{0}")
  public static Object[][] getParameters() {
    return TestFastaFileInformations.getFileInformations().entrySet().stream()
      .flatMap(entry -> buildParamsFor(entry.getKey(), entry.getValue()))
    .toArray(Object[][]::new);
  }
  
  private static Stream<Object[]> buildParamsFor(String name, FastaFileInformation info) {
    try {
      final List<Object[]> params = new ArrayList<>();
      
      final Path file = extractIfNeeded(info.getPath());
      final String customName = "Custon sequence group name";
      
      params.addAll(Arrays.asList(new Object[][] {
        {
          name + " [File]",
          file.toFile().getName(),
          info.getSequences(),
          (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
            file,
            info.getCharset()
          )
        },
        {
          name + " [File custom name]",
          customName,
          info.getSequences(),
          (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
            customName, file, info.getCharset()
          )
        },
        {
          name + " [Sequences]",
          info.getSequenceGroupName(),
          info.getSequences(),
          (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
            info.getSequenceGroupName(),
            info.getSequences()
          )
        },
        {
          name + " [Sequences Mixed]",
          info.getSequenceGroupName(),
          info.getSequences(),
          (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
            info.getSequenceGroupName(),
            info.getMixedSequences()
          )
        },
        {
          name + " [Sequences Lazy]",
          info.getSequenceGroupName(),
          info.getSequences(),
          (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
            info.getSequenceGroupName(),
            info.getLazySequences()
          )
        }
      }));
      
      if (!info.isCharsetRequired()) {
        params.addAll(Arrays.asList(new Object[][] {
          {
            name + " [File no charset]",
            file.toFile().getName(),
            info.getSequences(),
            (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
              file
            )
          },
          {
            name + " [File custon name no charset]",
            customName,
            info.getSequences(),
            (Supplier<InDiskSequencesGroup>) () -> new InDiskSequencesGroup(
              customName, file
            )
          }
        }));
      }
      
      return params.stream();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
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
    
    assertThat(sequences, everyItem(instanceOf(InDiskSequence.class)));
  }

}