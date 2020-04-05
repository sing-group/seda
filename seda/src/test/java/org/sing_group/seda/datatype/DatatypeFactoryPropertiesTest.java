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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.collection.IsMapWithSize.anEmptyMap;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.datatype.SequencesGroup.PROPERTY_LINE_BREAK_OS;
import static org.sing_group.seda.io.LineBreakType.WINDOWS;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.InMemoryDatatypeFactory;
import org.sing_group.seda.datatype.InDiskDatatypeFactory;
import org.sing_group.seda.datatype.SequencesGroup;

@RunWith(Parameterized.class)
public class DatatypeFactoryPropertiesTest {
  private Path linuxRegularLineBreaksPath = Paths.get("src/test/resources/datatype-factory/linux-line-breaks.fasta");
  private Path linuxGzipLineBreaksPath = Paths.get("src/test/resources/datatype-factory/linux-line-breaks.fasta.gz");
  private Path windowsRegularLineBreaksPath = Paths.get("src/test/resources/datatype-factory/windows-line-breaks.fasta");
  private Path windowsGzipLineBreaksPath = Paths.get("src/test/resources/datatype-factory/windows-line-breaks.fasta.gz");

  private final DatatypeFactory factory;
  
  public DatatypeFactoryPropertiesTest(String name, DatatypeFactory factory) {
    this.factory = factory;
  }
  
  @Parameters(name = "{0}")
  public static Object[][] parameters() {
    return new Object[][] {
      { InMemoryDatatypeFactory.class.getSimpleName(), new InMemoryDatatypeFactory() },
      { InDiskDatatypeFactory.class.getSimpleName(), new InDiskDatatypeFactory() }
    };
  }
  
  @Test
  public void testLoadRegularFileWithLinuxLineBreaks() {
    testLoadFileWithLinuxLineBreaks(linuxRegularLineBreaksPath);
  }
  
  @Test
  public void testLoadGZipFileWithLinuxLineBreaks() {
    testLoadFileWithLinuxLineBreaks(linuxGzipLineBreaksPath);
  }

  private void testLoadFileWithLinuxLineBreaks(Path path) {
    SequencesGroup group = this.factory.newSequencesGroup(path);
    
    assertThat(group.getProperties(), is(anEmptyMap()));
  }

  @Test
  public void testLoadRegularFileWithWindowsLineBreaks() {
    testFileWithWindowsLineBreaks(windowsRegularLineBreaksPath);
  }
  
  @Test
  public void testLoadGZipFileWithWindowsLineBreaks() {
    testFileWithWindowsLineBreaks(windowsGzipLineBreaksPath);
  }

  private void testFileWithWindowsLineBreaks(final Path path) {
    SequencesGroup group = this.factory.newSequencesGroup(path);
    
    assertThat(group.getProperties(), is(not(anEmptyMap())));
    assertThat(group.getProperties(), is(hasEntry(PROPERTY_LINE_BREAK_OS, WINDOWS.getLineBreak())));
  }
}
