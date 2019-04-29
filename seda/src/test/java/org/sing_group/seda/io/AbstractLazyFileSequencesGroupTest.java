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
package org.sing_group.seda.io;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.IsEqualToSequence.containsSequencesInOrder;
import static org.sing_group.seda.io.TestFnaFileInformation.getSequences;
import static org.sing_group.seda.io.TestFnaFileInformation.sequenceGroupName;
import static org.sing_group.seda.io.TestFnaFileInformation.sequencesCount;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sing_group.seda.datatype.Sequence;

public abstract class AbstractLazyFileSequencesGroupTest {
  protected LazyFileSequencesGroup sequenceGroup;

  protected abstract LazyFileSequencesGroup buildSequenceGroup();
  
  @Before
  public void beforeTest() {
    this.sequenceGroup = this.buildSequenceGroup();
  }
  
  @After
  public void afterTest() {
    this.sequenceGroup = null;
  }

  @Test
  public void testName() {
    assertThat(this.sequenceGroup.getName(), is(equalTo(sequenceGroupName())));
  }

  @Test
  public void testSequenceCount() {
    assertThat(this.sequenceGroup.getSequenceCount(), is(equalTo(sequencesCount())));
  }

  @Test
  public void testLoadFile() {
    final List<Sequence> sequences = sequenceGroup.getSequences()
    .collect(toList());
    
    assertThat(sequences, containsSequencesInOrder(getSequences()));
  }

}