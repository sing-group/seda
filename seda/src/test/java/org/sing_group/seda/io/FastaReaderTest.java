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

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.io.FastaReader.readFasta;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.io.FastaReader.SequenceBuilder;
import org.sing_group.seda.io.FastaReader.SequenceInfo;

@RunWith(Parameterized.class)
public class FastaReaderTest {
  private final Path file;
  private final SequenceInfo[] sequenceInfos;
  
  public FastaReaderTest(
    String testName, // Not used
    Path file,
    SequenceInfo[] sequenceInfos
  ) {
    this.file = file;
    this.sequenceInfos = sequenceInfos;
  }

  @Parameters(name = "{0}")
  public static Object[][] parameters() {
    return TestFastaFileInformations.getFileInformations()
      .entrySet().stream()
      .map(entry -> new Object[] {
        entry.getKey(),
        entry.getValue().getPath(),
        entry.getValue().getSequenceInfos()
      })
    .toArray(Object[][]::new);
  }
  
  @Test
  public void testReadFna() {
    final SequenceBuilderStub sequenceBuilder =
      new SequenceBuilderStub(this.sequenceInfos);
    
    readFasta(this.file, sequenceBuilder);
    
    sequenceBuilder.assertNoInfosLeft();
  }
  
  private static class SequenceBuilderStub implements SequenceBuilder {
    private final Queue<SequenceInfo> expectedInfos;
    
    public SequenceBuilderStub(SequenceInfo[] expectedInfos) {
      this.expectedInfos = new LinkedList<>(asList(expectedInfos));
    }
    
    public void assertNoInfosLeft() {
      assertThat(this.expectedInfos, is(empty()));
    }
    
    @Override
    public Sequence create(SequenceInfo info) {
      final SequenceInfo expectedInfo = this.expectedInfos.poll();
      
      assertThat("More infos than expected", expectedInfo, is(notNullValue()));
      assertThat(info, is(equalTo(expectedInfo)));
      
      return Sequence.of(
        info.getName(),
        info.getDescription(),
        info.getChain(),
        info.getProperties()
      );
    }
  }
}
