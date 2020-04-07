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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.IsEqualToSequence.equalToSequence;
import static org.sing_group.seda.io.IOUtils.extractIfNeeded;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.io.FastaFileInformation;
import org.sing_group.seda.io.FastaReader.SequenceLocationsInfo;
import org.sing_group.seda.io.FastaReader.SequenceTextInfo;
import org.sing_group.seda.io.TestFastaFileInformations;

@RunWith(Parameterized.class)
public class InDiskSequenceTest {
  private final FastaFileInformation info;
  
  private Path[] files;
  private boolean filesAreTemporal;
  
  public InDiskSequenceTest(String notUsedName, FastaFileInformation info) {
    this.info = info;
  }

  @Parameters(name = "{0}")
  public static Collection<Object[]> parameters() {
    return TestFastaFileInformations.getFileInformations().entrySet().stream()
      .map(entry -> new Object[] { entry.getKey(), entry.getValue() })
    .collect(toList());
  }
  
  @After
  public void checkFileDeletion() {
    System.gc();
    System.runFinalization();
    
    for (Path file : this.files) {
      if (this.filesAreTemporal) {
        assertThat(file.toFile(), not(anExistingFile()));
      } else {
        assertThat(file.toFile(), anExistingFile());
      }
    }
    
    this.files = null;
  }
  
  @Test
  public void testConstructorValues() {
    final Sequence[] sequences = this.info.getSequences();
    final SequenceTextInfo[] infos = this.info.getSequenceTextInfos();
    this.files = new Path[this.info.getSequencesCount()];
    this.filesAreTemporal = true;
    
    for (int i = 0; i < this.info.getSequencesCount(); i++) {
      final Sequence sequence = sequences[i];
      final SequenceTextInfo info = infos[i];
      
      final InDiskSequence actual = new InDiskSequence(
        info.getName(),
        info.getDescription(),
        info.getChain(),
        info.getProperties()
      );

      this.files[i] = actual.getFile();
      
      assertThat(actual, is(equalToSequence(sequence)));
    }
  }
  
  @Test
  public void testConstructorSequence() {
    final Sequence[] sequences = this.info.getSequences();
    this.files = new Path[this.info.getSequencesCount()];
    this.filesAreTemporal = true;
    
    for (int i = 0; i < this.info.getSequencesCount(); i++) {
      final Sequence sequence = sequences[i];
      
      final InDiskSequence actual = new InDiskSequence(sequence);

      this.files[i] = actual.getFile();
      
      assertThat(actual, is(equalToSequence(sequence)));
    }
  }
  
  @Test
  public void testConstructorPositions() throws IOException {
    final Sequence[] sequences = this.info.getSequences();
    final SequenceLocationsInfo[] infos = this.info.getSequenceLocationInfos();
    this.files = new Path[this.info.getSequencesCount()];
    this.filesAreTemporal = false;
    
    for (int i = 0; i < this.info.getSequencesCount(); i++) {
      final Sequence sequence = sequences[i];
      final SequenceLocationsInfo info = infos[i];
      
      final Path file = extractIfNeeded(info.getFile());
      final InDiskSequence actual = new InDiskSequence(
        file, info.getCharsetSubtype(),
        info.getNameLocation(), info.getNameLength(),
        info.getDescriptionLocation(), info.getDescriptionLength(),
        info.getHeaderLocation(), info.getHeaderLength(),
        info.getChainLocation(), info.getChainLength(),
        info.getProperties()
      );

      this.files[i] = actual.getFile();
      
      assertThat(actual, is(equalToSequence(sequence)));
    }
  }
  
  @Test
  public void testConstructorPositionsNoCharset() throws IOException {
    if (this.info.isCharsetRequired()) {
      this.files = new Path[0];
    } else {
      final Sequence[] sequences = this.info.getSequences();
      final SequenceLocationsInfo[] infos = this.info.getSequenceLocationInfos();
      this.files = new Path[this.info.getSequencesCount()];
      this.filesAreTemporal = false;
      
      for (int i = 0; i < this.info.getSequencesCount(); i++) {
        final Sequence sequence = sequences[i];
        final SequenceLocationsInfo info = infos[i];
        
        final Path file = extractIfNeeded(info.getFile());
        final InDiskSequence actual = new InDiskSequence(
          file, null,
          info.getNameLocation(), info.getNameLength(),
          info.getDescriptionLocation(), info.getDescriptionLength(),
          info.getHeaderLocation(), info.getHeaderLength(),
          info.getChainLocation(), info.getChainLength(),
          info.getProperties()
        );

        this.files[i] = actual.getFile();
        
        assertThat(actual, is(equalToSequence(sequence)));
      }
    }
  }

}
