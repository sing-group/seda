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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.sing_group.seda.datatype.SequencesGroup.PROPERTY_LINE_BREAK_OS;
import static org.sing_group.seda.gui.reformat.LineBreakType.WINDOWS;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.sing_group.seda.datatype.SequencesGroup;


public class LazyDatatypeFactoryTest {
  private Path linuxLineBreaksPath = Paths.get("src/test/resources/datatype-factory/linux-line-breaks.fasta");
  private Path windowsLineBreaksPath = Paths.get("src/test/resources/datatype-factory/windows-line-breaks.fasta");

  @Test
  public void loadFileWithLinuxLineBreaks() {
    SequencesGroup group = new LazyDatatypeFactory().newSequencesGroup(linuxLineBreaksPath);
    assertTrue(group.getProperties().isEmpty());
  }

  @Test
  public void loadFileWithWindowsLineBreaks() {
    SequencesGroup group = new LazyDatatypeFactory().newSequencesGroup(windowsLineBreaksPath);
    assertFalse(group.getProperties().isEmpty());
    assertEquals(WINDOWS.getLineBreak(), group.getProperties().get(PROPERTY_LINE_BREAK_OS));
  }
}
