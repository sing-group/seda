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

import static java.nio.file.Files.createTempFile;
import static org.hamcrest.MatcherAssert.assertThat;
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
