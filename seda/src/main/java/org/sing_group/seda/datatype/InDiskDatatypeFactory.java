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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.sing_group.seda.io.IOUtils;

public class InDiskDatatypeFactory implements DatatypeFactory {
  private final DatatypeFactory defaultFactory = DatatypeFactory.getDefaultDatatypeFactory();
  
  private final boolean charsetSupport;
  
  public InDiskDatatypeFactory() {
    this(false);
  }

  public InDiskDatatypeFactory(boolean charsetSupport) {
    this.charsetSupport = charsetSupport;
  }

  @Override
  public Sequence newSequence(String name, String description, String sequence, Map<String, Object> properties) {
    return new InDiskSequence(name, description, sequence, properties);
  }

  @Override
  public SequencesGroup newSequencesGroup(Path file) {
    try {
      final Charset charset =
        this.charsetSupport
          ? IOUtils.detectCharset(file)
          : null;

      return new InDiskSequencesGroup(file, charset);
    } catch (IOException e) {
      throw new RuntimeException("Error detecting charset", e);
    }
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Map<String, Object> properties, Sequence... sequences) {
    return new InDiskSequencesGroup(name, properties, sequences);
  }

  @Override
  public SequencesGroup newSequencesGroup(String name, Map<String, Object> properties, List<Sequence> sequences) {
    return newSequencesGroup(name, properties, sequences.toArray(new Sequence[sequences.size()]));
  }

  @Override
  public SequencesGroupDataset newSequencesGroupDataset(SequencesGroup... sequencesGroups) {
    return this.defaultFactory.newSequencesGroupDataset(sequencesGroups);
  }
}
