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

import static java.nio.file.StandardOpenOption.WRITE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.sing_group.seda.datatype.Sequence;

public class LazyFileSequence implements Sequence {
  private final Path file;
  private final boolean isTempFile;
  
  private final long nameLocation;
  private final int nameLength;
  
  private final long descriptionLocation;
  private final int descriptionLength;
  
  private final long headerLocation;
  private final int headerLength;
  
  private final long chainLocation;
  private final int chainLength;
  
  private final Map<String, Object> properties;
  
  public LazyFileSequence(
    Path file,
    long nameLocation, int nameLength,
    long descriptionLocation, int descriptionLength,
    long headerLocation, int headerLength,
    long chainLocation, int chainLength,
    Map<String, Object> properties
  ) {
    this.file = file;
    this.isTempFile = false;
    this.nameLocation = nameLocation;
    this.nameLength = nameLength;
    this.descriptionLocation = descriptionLocation;
    this.descriptionLength = descriptionLength;
    this.headerLocation = headerLocation;
    this.headerLength = headerLength;
    this.chainLocation = chainLocation;
    this.chainLength = chainLength;
    this.properties = new HashMap<>(properties);
  }
  
  public LazyFileSequence(
    String name,
    String description,
    String sequence,
    Map<String, Object> properties
  ) {
    try {
      this.file = Files.createTempFile("seda_", "seq.fasta");
      this.file.toFile().deleteOnExit();
      this.isTempFile = true;
      writeFasta(this.file, name, description, sequence);

      this.headerLocation = 0;
      this.nameLocation = 1;
      this.nameLength = name.getBytes().length;
      
      if (description != null && !description.isEmpty()) {
        this.descriptionLocation = this.nameLocation + this.nameLength + 1;
        this.descriptionLength = description.getBytes().length;
        
        this.headerLength = (int) this.descriptionLocation + this.descriptionLength;
      } else {
        this.descriptionLocation = -1;
        this.descriptionLength = -1;
        
        this.headerLength = (int) this.nameLocation + this.nameLength;
      }

      this.chainLocation = this.headerLocation + this.headerLength + 1;
      this.chainLength = sequence.getBytes().length;
      
      this.properties = new HashMap<>(properties);
    } catch (IOException e) {
      throw new RuntimeException("Unexpected error creating temporary file.", e);
    }
  }
  
  private final static void writeFasta(
    Path file,
    String name,
    String description,
    String sequence
  ) throws IOException {
    try (BufferedWriter output = Files.newBufferedWriter(file, WRITE)) {
      output.write(">" + name);
      
      if (description != null && !description.isEmpty()) {
        output.write(" " + description);
      }
      
      output.write('\n');
      output.write(sequence);
      output.flush();
    }
  }

  private String readFromFile(long location, int length) {
    try (RandomAccessFile rac = new RandomAccessFile(this.file.toFile(), "r")) {
      rac.seek(location);
      
      final byte[] bytes = new byte[length];
      
      rac.readFully(bytes, 0, bytes.length);
      
      return new String(bytes);
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading sequence file", ioe);
    }
  }

  public Path getFile() {
    return file;
  }
  
  @Override
  public String getName() {
    return this.readFromFile(this.nameLocation, this.nameLength).trim();
  }

  @Override
  public String getDescription() {
    if (this.descriptionLocation > 0) {
      return this.readFromFile(this.descriptionLocation, this.descriptionLength).trim();
    } else {
      return "";
    }
  }

  @Override
  public String getHeader() {
//    final long start = this.nameLocation;
//    final int length;
//    
//    if (this.descriptionLocation > 0) {
//      length = (int) (this.descriptionLocation + this.descriptionLength - start);
//    } else {
//      length = this.nameLength;
//    }
//    
//    return ">" + this.readFromFile(start, length).trim();
    return this.readFromFile(this.headerLocation, this.headerLength);
  }

  @Override
  public String getChain() {
    return this.readFromFile(this.chainLocation, this.chainLength).replaceAll("\\s+", "");
  }

  @Override
  public Map<String, Object> getProperties() {
    return this.properties;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getProperty(String key) {
    return Optional.ofNullable((T) this.properties.get(key));
  }

  @Override
  protected void finalize() throws Throwable {
    if (this.isTempFile)
      Files.deleteIfExists(this.file);
  }
}