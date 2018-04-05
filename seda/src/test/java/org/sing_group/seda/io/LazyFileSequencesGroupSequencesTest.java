/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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