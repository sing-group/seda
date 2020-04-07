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

import static java.util.Collections.unmodifiableMap;
import static org.sing_group.seda.io.IOUtils.createInputStream;
import static org.sing_group.seda.io.IOUtils.createReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.io.NumberedLineReader.Line;

public final class FastaReader {

  private FastaReader() {}

  @FunctionalInterface
  public static interface SequenceFromTextBuilder {
    public Sequence create(SequenceTextInfo info);
  }

  @FunctionalInterface
  public static interface SequenceFromLocationsBuilder {
    public Sequence create(SequenceLocationsInfo info);
  }
  
  public static class SequenceLocationsInfo {
    private final Path file;
    private final Charset charset;
    private final Charset charsetSubtype;
    
    private final long nameLocation;
    private final int nameLength;
  
    private final long descriptionLocation;
    private final int descriptionLength;
  
    private final long headerLocation;
    private final int headerLength;
  
    private final long chainLocation;
    private final int chainLength;
  
    private final int columnWidth;

    private final Map<String, Object> properties;

    SequenceLocationsInfo(
      Path file, Charset charset, Charset charsetSubtype,
      long nameLocation, int nameLength,
      long descriptionLocation, int descriptionLength,
      long headerLocation, int headerLength,
      long chainLocation, int chainLength,
      int columnWidth, Map<String, Object> properties
    ) {
      this.file = file;
      this.charset = charset;
      this.charsetSubtype = charsetSubtype;
      this.nameLocation = nameLocation;
      this.nameLength = nameLength;
      this.descriptionLocation = descriptionLocation;
      this.descriptionLength = descriptionLength;
      this.headerLocation = headerLocation;
      this.headerLength = headerLength;
      this.chainLocation = chainLocation;
      this.chainLength = chainLength;
      this.columnWidth = columnWidth;
      this.properties = properties;
    }

    public Path getFile() {
      return file;
    }
    
    public Charset getCharset() {
      return charset;
    }
    
    public Charset getCharsetSubtype() {
      return charsetSubtype;
    }

    public long getNameLocation() {
      return nameLocation;
    }

    public int getNameLength() {
      return nameLength;
    }

    public long getDescriptionLocation() {
      return descriptionLocation;
    }

    public int getDescriptionLength() {
      return descriptionLength;
    }

    public long getHeaderLocation() {
      return headerLocation;
    }

    public int getHeaderLength() {
      return headerLength;
    }

    public long getChainLocation() {
      return chainLocation;
    }

    public int getChainLength() {
      return chainLength;
    }

    public int getColumnWidth() {
      return columnWidth;
    }
    
    public Map<String, Object> getProperties() {
      return unmodifiableMap(properties);
    }

    @Override
    public int hashCode() {
      return Objects.hash(
        chainLength, chainLocation, charset, charsetSubtype, columnWidth, descriptionLength, descriptionLocation, file,
        headerLength, headerLocation, nameLength, nameLocation, properties
      );
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      SequenceLocationsInfo other = (SequenceLocationsInfo) obj;
      return chainLength == other.chainLength && chainLocation == other.chainLocation && Objects.equals(
        charset, other.charset
      ) && Objects.equals(charsetSubtype, other.charsetSubtype) && columnWidth == other.columnWidth
        && descriptionLength == other.descriptionLength && descriptionLocation == other.descriptionLocation && Objects
          .equals(file, other.file)
        && headerLength == other.headerLength && headerLocation == other.headerLocation
        && nameLength == other.nameLength && nameLocation == other.nameLocation
        && Objects.equals(properties, other.properties);
    }

    @Override
    public String toString() {
      return "SequenceLocationsInfo [file=" + file + ", charset=" + charset + ", charsetSubtype=" + charsetSubtype
        + ", nameLocation=" + nameLocation + ", nameLength=" + nameLength + ", descriptionLocation="
        + descriptionLocation + ", descriptionLength=" + descriptionLength + ", headerLocation=" + headerLocation
        + ", headerLength=" + headerLength + ", chainLocation=" + chainLocation + ", chainLength=" + chainLength
        + ", columnWidth=" + columnWidth + ", properties=" + properties + "]";
    }
  }
  
  public static class SequenceTextInfo {
    private final String name;
    private final String description;
    private final String header;
    private final String chain;
    private final int columnWidth;

    private final Map<String, Object> properties;

    SequenceTextInfo(
      String name, String description, String header, String chain,
      int columnWidth, Map<String, Object> properties
    ) {
      this.name = name;
      this.description = description;
      this.header = header;
      this.chain = chain;
      this.columnWidth = columnWidth;
      this.properties = properties;
    }

    public String getName() {
      return name;
    }

    public String getDescription() {
      return description;
    }

    public String getHeader() {
      return header;
    }

    public String getChain() {
      return chain;
    }

    public int getColumnWidth() {
      return columnWidth;
    }
    
    public Map<String, Object> getProperties() {
      return unmodifiableMap(properties);
    }

    @Override
    public int hashCode() {
      return Objects.hash(chain, columnWidth, description, header, name, properties);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      SequenceTextInfo other = (SequenceTextInfo) obj;
      return Objects.equals(chain, other.chain) && columnWidth == other.columnWidth && Objects
        .equals(description, other.description) && Objects.equals(header, other.header)
        && Objects.equals(name, other.name) && Objects.equals(properties, other.properties);
    }
  }

  public static Stream<Sequence> readFasta(Path file, Charset charset, SequenceFromTextBuilder sequenceBuilder) {
    try (BufferedReader reader = new BufferedReader(createReader(file, charset))) {
      final List<Sequence> sequencesList = new LinkedList<>();
  
      final SequenceTextInfoBuilder builder = new SequenceTextInfoBuilder();
  
      String line;
      StringBuilder chain = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        line = line.trim();
  
        final boolean isEmpty = line.isEmpty();
        final boolean isSequenceStart = line.startsWith(">");
  
        if (isEmpty || isSequenceStart) {
          if (builder.hasName() && chain.length() > 0) {
            builder.setChain(chain.toString());
            sequencesList.add(sequenceBuilder.create(builder.buildSequenceTextInfo()));
  
            builder.clear();
          } else if (builder.hasName() || chain.length() > 0) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          }
          chain = new StringBuilder();
        }
  
        if (isSequenceStart) {
          builder.setHeader(line);
          
          final int spaceIndex = line.indexOf(" ");
          if (spaceIndex > 0) {
            builder.setName(line.substring(1, spaceIndex));
            builder.setDescription(line.substring(spaceIndex + 1));
          } else {
            builder.setName(line.substring(1));
          }
        } else if (!isEmpty) {
          if (!builder.hasName()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          } else {
            final String nextChainLine = line.trim();
  
            chain.append(nextChainLine);
            builder.updateChainColumns(nextChainLine.length());
          }
        }
      }
  
      if (builder.hasName() && chain.length() > 0) {
        builder.setChain(chain.toString());
        sequencesList.add(sequenceBuilder.create(builder.buildSequenceTextInfo()));
  
        builder.clear();
      } else if (builder.hasName() || chain.length() > 0) {
        throw new IOException("Incomplete file");
      }
  
      return sequencesList.stream();
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading file: " + file.toString(), ioe);
    }
  }

  public static Stream<Sequence> readFasta(Path file, Charset charset, SequenceFromLocationsBuilder sequenceBuilder) {
    try (NumberedLineReader reader = new NumberedLineReader(createInputStream(file), charset)) {
      final List<Sequence> sequencesList = new LinkedList<>();
  
      final SequenceLocationsInfoBuilder builder = new SequenceLocationsInfoBuilder(
        file, charset, reader.getCharset()
      );
  
      Line nline;
      while ((nline = reader.readLine()) != null) {
        final String line = nline.getLine().trim();
  
        final boolean isEmpty = line.isEmpty();
        final boolean isSequenceStart = line.startsWith(">");
  
        if (isEmpty || isSequenceStart) {
          if (builder.hasSequenceTextInfo()) {
            sequencesList.add(sequenceBuilder.create(builder.buildSequenceTextInfo()));
  
            builder.clear();
          } else if (builder.hasName() || builder.hasChain()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          }
        }
  
        if (isSequenceStart) {
          final int spaceIndex = line.indexOf(" ");
          final int nameLength;
          final boolean hasDescription;
          if (spaceIndex > 0) {
            nameLength = nline.countTextBytesBetween(1, spaceIndex - 1);
            hasDescription = spaceIndex < nline.countTextChars() - 1;
          } else {
            nameLength = nline.countTextBytesFrom(1);
            hasDescription = false;
          }
  
          builder.setHeaderLocation(nline.getStart());
          builder.setHeaderLength(nline.countTextBytes());
  
          builder.setNameLocation(nline.getCharPosition(1));
          builder.setNameLength(nameLength);
  
          if (hasDescription) {
            final int descriptionStart = spaceIndex + 1;
            builder.setDescriptionLocation(nline.getCharPosition(descriptionStart));
            builder.setDescriptionLength(nline.countTextBytesFrom(descriptionStart));
          }
        } else if (!isEmpty) {
          if (!builder.hasName()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          } else {
            final String nextChainLine = line.trim();
  
            if (!builder.hasChain()) {
              builder.setChainLocation(nline.getStart());
            }
            builder.updateChainColumns(nextChainLine.length());
            builder.setChainLength((int) (nline.getTextEnd() - builder.getChainLocation() + 1));
          }
        }
      }
  
      if (builder.hasSequenceTextInfo()) {
        sequencesList.add(sequenceBuilder.create(builder.buildSequenceTextInfo()));
  
        builder.clear();
      } else if (builder.hasName() || builder.hasChain()) {
        throw new IOException("Incomplete file");
      }
  
      return sequencesList.stream();
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading file: " + file.toString(), ioe);
    }
  }

  private static class SequenceTextInfoBuilder {
    private String name;
    private String description;
    private String header;
    private String chain;
    private int columnWidth;
    private Map<String, Object> properties;
  
    public SequenceTextInfoBuilder() {
      this.clear();
    }
  
    public boolean hasName() {
      return this.name != null;
    }
  
    public void setName(String name) {
      if (name == null)
        throw new IllegalArgumentException("Name can't be null");
      
      this.name = name;
    }
    
    public void setDescription(String description) {
      if (description == null)
        throw new IllegalArgumentException("Description can't be null");
      
      this.description = description;
    }

    public void setHeader(String header) {
      if (header == null)
        throw new IllegalArgumentException("Header can't be null");
      
      this.header = header;
    }
  
    public void setChain(String chain) {
      if (chain == null)
        throw new IllegalArgumentException("Chain can't be null");
      
      this.chain = chain;
    }
  
    public void updateChainColumns(int columns) {
      if (columns < 0)
        throw new IllegalArgumentException("Chain columns should be greater than 0");
  
      this.columnWidth = Math.max(this.columnWidth, columns);
  
      this.properties.put(Sequence.PROPERTY_CHAIN_COLUMNS, this.columnWidth);
    }
  
    public SequenceTextInfo buildSequenceTextInfo() {
      return new SequenceTextInfo(name, description, header, chain, columnWidth, properties);
    }
  
    public void clear() {
      this.name = null;
      this.description = null;
      this.header = null;
      this.chain = null;
      this.columnWidth = 0;
      this.properties = new HashMap<>();
    }
  }

  private static class SequenceLocationsInfoBuilder {
    private final Path file;
    private final Charset charset;
    private final Charset charsetSubtype;
    
    private long nameLocation;
    private int nameLength;
  
    private long descriptionLocation;
    private int descriptionLength;
  
    private long headerLocation;
    private int headerLength;
  
    private long chainLocation;
    private int chainLength;
  
    private int columnWidth;
  
    private Map<String, Object> properties;
  
    public SequenceLocationsInfoBuilder(Path file, Charset charset, Charset charsetSubtype) {
      this.file = file;
      this.charset = charset;
      this.charsetSubtype = charsetSubtype;
  
      this.clear();
    }
  
    public boolean hasSequenceTextInfo() {
      return this.nameLength > 0 && this.chainLength > 0;
    }
  
    public boolean hasName() {
      return this.nameLength > 0;
    }
  
    public boolean hasChain() {
      return this.chainLength > 0;
    }
    
    public void setNameLocation(long nameLocation) {
      if (nameLocation < 0)
        throw new IllegalArgumentException("Name location should be greater than 0");
  
      this.nameLocation = nameLocation;
    }
  
    public void setNameLength(int nameLength) {
      if (this.nameLocation < 0)
        throw new IllegalStateException("Name location must be set before name length");
      if (nameLength < 0)
        throw new IllegalArgumentException("Name length should be greater than 0");
  
      this.nameLength = nameLength;
    }
  
    public void setDescriptionLocation(long descriptionLocation) {
      if (descriptionLocation < 0)
        throw new IllegalArgumentException("Description location should be greater than 0");
  
      this.descriptionLocation = descriptionLocation;
    }
  
    public void setDescriptionLength(int descriptionLength) {
      if (this.descriptionLocation < 0)
        throw new IllegalStateException("Description location must be set before description length");
      if (descriptionLength < 0)
        throw new IllegalArgumentException("Description length should be greater than 0");
  
      this.descriptionLength = descriptionLength;
    }
  
    public void setHeaderLocation(long headerLocation) {
      if (headerLocation < 0)
        throw new IllegalArgumentException("Header location should be greater than 0");
  
      this.headerLocation = headerLocation;
    }
  
    public void setHeaderLength(int headerLength) {
      if (this.headerLocation < 0)
        throw new IllegalStateException("Header location must be set before header length");
      if (headerLength < 0)
        throw new IllegalArgumentException("Header length should be greater than 0");
  
      this.headerLength = headerLength;
    }
  
    public long getChainLocation() {
      return this.chainLocation;
    }
  
    public void setChainLocation(long chainLocation) {
      if (chainLocation < 0)
        throw new IllegalArgumentException("Chain location should be greater than 0");
  
      this.chainLocation = chainLocation;
    }
  
    public void setChainLength(int chainLength) {
      if (this.chainLocation < 0)
        throw new IllegalStateException("Chain location must be set before chain length");
      if (chainLength < 0)
        throw new IllegalArgumentException("Chain length should be greater than 0");
  
      this.chainLength = chainLength;
    }
  
    public void updateChainColumns(int columns) {
      if (columns < 0)
        throw new IllegalArgumentException("Chain columns should be greater than 0");
  
      this.columnWidth = Math.max(this.columnWidth, columns);
  
      this.properties.put(Sequence.PROPERTY_CHAIN_COLUMNS, this.columnWidth);
    }
  
    public SequenceLocationsInfo buildSequenceTextInfo() {
      return new SequenceLocationsInfo(
        file, charset, charsetSubtype,
        nameLocation, nameLength,
        descriptionLocation, descriptionLength,
        headerLocation, headerLength,
        chainLocation, chainLength,
        columnWidth, properties
      );
    }
  
    public void clear() {
      this.nameLocation = -1;
      this.nameLength = -1;
      this.descriptionLocation = -1;
      this.descriptionLength = -1;
      this.headerLocation = -1;
      this.headerLength = -1;
      this.chainLocation = -1;
      this.chainLength = -1;
      this.columnWidth = 0;
      this.properties = new HashMap<>();
    }
  }
}
