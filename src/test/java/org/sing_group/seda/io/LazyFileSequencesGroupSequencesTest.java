package org.sing_group.seda.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.io.TestFnaFileInformation.getSequences;
import static org.sing_group.seda.io.TestFnaFileInformation.sequenceGroupName;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;

public class LazyFileSequencesGroupSequencesTest extends AbstractLazyFileSequencesGroupTest {
  private Path file;
  
  @After
  @Override
  public void afterTest() {
    super.afterTest();
    
    this.checkFileDeletion();
  }
  
  private void checkFileDeletion() {
    System.gc();
    System.runFinalization();
    
    assertThat(Files.exists(this.file), is(false));
    
    this.file = null;
  }

  @Override
  protected LazyFileSequencesGroup buildSequenceGroup() {
    final LazyFileSequencesGroup sequenceGroup = new LazyFileSequencesGroup(sequenceGroupName(), getSequences());
    
    this.file = sequenceGroup.getFile();
    
    return sequenceGroup;
  }

}
