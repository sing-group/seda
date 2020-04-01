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
import static org.sing_group.seda.io.IOUtils.createNumberedLineReader;

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

public final class FastaReader {

  private FastaReader() {}

  @FunctionalInterface
  public static interface SequenceBuilder {
    public Sequence create(SequenceInfo info);
  }
  
  public static class SequenceInfo {
    private final Path file;
    private final Charset charset;
    
    private final String name;
    private final long nameLocation;
    private final int nameLength;
  
    private final String description;
    private final long descriptionLocation;
    private final int descriptionLength;
  
    private final String header;
    private final long headerLocation;
    private final int headerLength;
  
    private final String chain;
    private final long chainLocation;
    private final int chainLength;
  
    private final int columnWidth;

    private final Map<String, Object> properties;

    SequenceInfo(
      Path file, Charset charset,
      String name, long nameLocation, int nameLength,
      String description, long descriptionLocation, int descriptionLength,
      String header, long headerLocation, int headerLength,
      String chain, long chainLocation, int chainLength,
      int columnWidth, Map<String, Object> properties
    ) {
      this.file = file;
      this.charset = charset;
      this.name = name;
      this.nameLocation = nameLocation;
      this.nameLength = nameLength;
      this.description = description;
      this.descriptionLocation = descriptionLocation;
      this.descriptionLength = descriptionLength;
      this.header = header;
      this.headerLocation = headerLocation;
      this.headerLength = headerLength;
      this.chain = chain;
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

    public String getName() {
      return name;
    }

    public long getNameLocation() {
      return nameLocation;
    }

    public int getNameLength() {
      return nameLength;
    }

    public String getDescription() {
      return description;
    }

    public long getDescriptionLocation() {
      return descriptionLocation;
    }

    public int getDescriptionLength() {
      return descriptionLength;
    }

    public String getHeader() {
      return header;
    }

    public long getHeaderLocation() {
      return headerLocation;
    }

    public int getHeaderLength() {
      return headerLength;
    }

    public String getChain() {
      return chain;
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
        chain, chainLength, chainLocation, columnWidth, description, descriptionLength, descriptionLocation, file,
        header, headerLength, headerLocation, name, nameLength, nameLocation, properties
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
      SequenceInfo other = (SequenceInfo) obj;
      return Objects.equals(chain, other.chain) && chainLength == other.chainLength
        && chainLocation == other.chainLocation && columnWidth == other.columnWidth && Objects.equals(
          description, other.description
        ) && descriptionLength == other.descriptionLength && descriptionLocation == other.descriptionLocation && Objects
          .equals(file, other.file)
        && Objects.equals(header, other.header) && headerLength == other.headerLength && headerLocation == other.headerLocation && Objects.equals(name, other.name) && nameLength == other.nameLength && nameLocation == other.nameLocation && Objects.equals(properties, other.properties);
    }

    @Override
    public String toString() {
      return "SequenceInfo [file=" + file + ", name=" + name + ", nameLocation=" + nameLocation + ", nameLength="
        + nameLength + ", description=" + description + ", descriptionLocation=" + descriptionLocation
        + ", descriptionLength=" + descriptionLength + ", header=" + header + ", headerLocation=" + headerLocation
        + ", headerLength=" + headerLength + ", chain=" + chain + ", chainLocation=" + chainLocation + ", chainLength="
        + chainLength + ", columnWidth=" + columnWidth + ", properties=" + properties + "]";
    }
  }

  public static Stream<Sequence> readFasta(Path file, SequenceBuilder sequenceBuilder) {
    try (NumberedLineReader reader = createNumberedLineReader(file)) {
      final List<Sequence> sequencesList = new LinkedList<>();
  
      final SequenceInfoBuilder builder = new SequenceInfoBuilder(file, reader.getCharset());
  
      NumberedLineReader.Line nline;
      StringBuilder chain = new StringBuilder();
      while ((nline = reader.readLine()) != null) {
        final String line = nline.getLine().trim();
  
        final boolean isEmpty = line.isEmpty();
        final boolean isSequenceStart = line.startsWith(">");
  
        if (isEmpty || isSequenceStart) {
          if (builder.hasSequenceInfo()) {
            builder.setChain(chain.toString());
            sequencesList.add(sequenceBuilder.create(builder.buildSequenceInfo()));
  
            builder.clear();
          } else if (builder.hasName() || builder.hasChain()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          }
          chain = new StringBuilder();
        }
  
        if (isSequenceStart) {
          final int spaceIndex = line.indexOf(" ");
          final int nameLength;
          final String name, description;
          if (spaceIndex > 0) {
            name = line.substring(1, spaceIndex);
            nameLength = nline.getTextLengthTo(spaceIndex - 1);
            description = line.substring(spaceIndex + 1);
          } else {
            name = line.substring(1);
            nameLength = nline.getTextLengthFrom(1);
            description = "";
          }
  
          builder.setHeaderLocation(nline.getStart());
          builder.setHeaderLength(nline.getTextLength());
          builder.setHeader(nline.getLine());
  
          builder.setNameLocation(nline.getStart() + 1);
          builder.setNameLength(nameLength);
          builder.setName(name);
  
          if (description.length() > 0) {
            final int descriptionLength = nline.getTextLengthFrom(spaceIndex + 1);
            builder.setDescriptionLocation(nline.getTextEnd() - descriptionLength + 1);
            builder.setDescriptionLength(descriptionLength);
            builder.setDescription(description);
          }
  
          builder.setChainLocation(reader.getCurrentLocation());
        } else if (!isEmpty) {
          if (!builder.hasName()) {
            throw new IOException(String.format("Fasta syntax error. File: %s. Line: %s", file, line));
          } else {
            final String nextChainLine = line.trim();
  
            chain.append(nextChainLine);
            builder.updateChainColumns(nextChainLine.length());
            builder.setChainLength((int) (reader.getCurrentLocation() - builder.getChainLocation() - nline.getLineEndingLength()));
          }
        }
      }
  
      if (builder.hasSequenceInfo()) {
        builder.setChain(chain.toString());
        sequencesList.add(sequenceBuilder.create(builder.buildSequenceInfo()));
  
        builder.clear();
      } else if (builder.hasName() || builder.hasChain()) {
        throw new IOException("Incomplete file");
      }
  
      return sequencesList.stream();
    } catch (IOException ioe) {
      throw new RuntimeException("Error reading file: " + file.toString(), ioe);
    }
  }

  private static class SequenceInfoBuilder {
    private final Path file;
    private final Charset charset;
    
    private String name;
    private long nameLocation;
    private int nameLength;
  
    private String description;
    private long descriptionLocation;
    private int descriptionLength;
  
    private String header;
    private long headerLocation;
    private int headerLength;
  
    private String chain;
    private long chainLocation;
    private int chainLength;
  
    private int columnWidth;
  
    private Map<String, Object> properties;
  
    public SequenceInfoBuilder(Path file, Charset charset) {
      this.file = file;
      this.charset = charset;
  
      this.clear();
    }
  
    public boolean hasSequenceInfo() {
      return this.nameLength > 0 && this.chainLength > 0;
    }
  
    public boolean hasName() {
      return this.nameLength > 0;
    }
  
    public boolean hasChain() {
      return this.chainLength > 0;
    }
  
    public void setName(String name) {
      if (name == null)
        throw new IllegalArgumentException("Name can't be null");
      
      this.name = name;
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
  
    public void setDescription(String description) {
      if (description == null)
        throw new IllegalArgumentException("Description can't be null");
      
      this.description = description;
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
  
    public void setHeader(String header) {
      if (header == null)
        throw new IllegalArgumentException("Header can't be null");
      
      this.header = header;
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
  
    public void setChain(String chain) {
      if (chain == null)
        throw new IllegalArgumentException("Chain can't be null");
      
      this.chain = chain;
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
  
    public SequenceInfo buildSequenceInfo() {
      return new SequenceInfo(
        file, charset,
        name, nameLocation, nameLength,
        description, descriptionLocation, descriptionLength,
        header, headerLocation, headerLength,
        chain, chainLocation, chainLength,
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
