package org.sing_group.seda.io;

import static java.nio.file.Files.createTempFile;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.matcher.HasEqualFileContentMatcher.hasEqualFileContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.sing_group.seda.datatype.Sequence;

public class FastaWriterTest {
  public static final File TEST_FILE = new File(
    "src/test/resources/fasta/write-test.fa"
  );

  private static final Map<String, Object> PROPERTIES = new HashMap<>();
  static {
    PROPERTIES.put(Sequence.PROPERTY_CHAIN_COLUMNS, 3);
  }

  private static final List<Sequence> SEQUENCES = Arrays.asList(
    Sequence.of("A", "Sequence A", "AAA", PROPERTIES),
    Sequence.of("B", "Sequence B", "AAACCC", PROPERTIES),
    Sequence.of("C", "Sequence C", "ACTACTACTG", PROPERTIES)
  );

  @Test
  public void writeFastaTest() throws IOException {
    Path tmpFastaFile = createTempFile("write-fasta-test-", "fa");
    tmpFastaFile.toFile().deleteOnExit();

    FastaWriter.writeFasta(tmpFastaFile, SEQUENCES.stream());

    assertThat(
      tmpFastaFile.toFile(),
      hasEqualFileContent(TEST_FILE)
    );
  }
}
