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