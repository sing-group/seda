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

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.StandardOpenOption.READ;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.sing_group.seda.io.TestFastaFileInformations.getFnaFileInformation;
import static org.sing_group.seda.io.TestFastaFileInformations.getFnaGZipFileInformation;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class GZipUtilsTest {
  @Test
  public void testIsGZip() throws IOException {
    final FastaFileInformation fastaInfo = getFnaGZipFileInformation();
    
    try (InputStream in = new BufferedInputStream(
      newInputStream(fastaInfo.getPath(), READ)
    )) {
      assertThat(IOUtils.isGZipped(in), is(true));
      
    }
  }
  
  @Test
  public void testIsNotGZip() throws IOException {
    final FastaFileInformation fastaInfo = getFnaFileInformation();
    
    try (InputStream in = new BufferedInputStream(
      newInputStream(fastaInfo.getPath(), READ)
    )) {
      assertThat(IOUtils.isGZipped(in), is(false));
    }
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMarkNotSupported() throws IOException {
    final FastaFileInformation fastaInfo = getFnaGZipFileInformation();
    
    try (InputStream in = newInputStream(fastaInfo.getPath(), StandardOpenOption.READ)) {
      IOUtils.isGZipped(in);
    }
  }
  
  @Test
  public void testCreateInputStreamOfGZipFile() throws IOException {
    final FastaFileInformation fastaInfo = getFnaGZipFileInformation();
    
    testCreateInputStreamOfFile(fastaInfo.getPath());
  }
  
  @Test
  public void testCreateInputStreamOfRegularFile() throws IOException {
    final FastaFileInformation fastaInfo = getFnaFileInformation();
    
    testCreateInputStreamOfFile(fastaInfo.getPath());
  }
  
  private static void testCreateInputStreamOfFile(Path file) throws IOException {
    final FastaFileInformation fastaInfo = getFnaFileInformation();
    
    final byte[] expectedData = readAllBytes(fastaInfo.getPath());
    
    try (InputStream in = IOUtils.createInputStream(file)) {
      final byte[] actualData = readFully(in);
      
      assertThat(actualData, is(equalTo(expectedData)));
    }
  }
  
  private static byte[] readFully(InputStream in) throws IOException {
    try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      final byte[] data = new byte[4096];
      int length;
      
      while ((length = in.read(data)) != -1) {
        output.write(data, 0, length);
      }
      
      return output.toByteArray();
    }
  }
}
