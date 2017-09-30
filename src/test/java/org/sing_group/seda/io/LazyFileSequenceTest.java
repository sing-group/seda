package org.sing_group.seda.io;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.IsEqualToSequence.equalToSequence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.Sequence;

@RunWith(Parameterized.class)
public class LazyFileSequenceTest {
  private Sequence sequence;
  private Path file;
  
  public LazyFileSequenceTest(Sequence sequence) {
    this.sequence = sequence;
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.stream(TestFnaFileInformation.getSequences())
      .map(sequence -> new Object[] { sequence })
    .collect(toList());
  }
  
  @After
  public void checkFileDeletion() {
    System.gc();
    System.runFinalization();
    
    assertThat(Files.exists(this.file), is(false));
    
    this.file = null;
  }
  
  @Test
  public void testCreatingTempFile() {
    final LazyFileSequence actual = new LazyFileSequence(
      sequence.getName(),
      sequence.getDescription(),
      sequence.getChain(),
      sequence.getProperties()
    );
    this.file = actual.getFile();
    
    assertThat(actual, is(equalToSequence(sequence)));
  }

}
