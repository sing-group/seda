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
package org.sing_group.seda.datatype;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.sing_group.seda.gui.reformat.LineBreakType;

public interface SequencesGroup {
  public static final String PROPERTY_LINE_BREAK_OS = "sequence.group.linebreak";
  public static final String DEFAULT_LINE_BREAK_OS = LineBreakType.defaultType().getLineBreak();

  public static SequencesGroup of(String name, Map<String, Object> properties, Sequence... sequences) {
    return DatatypeFactory.getDefaultDatatypeFactory().newSequencesGroup(name, properties, sequences);
  }

  public String getName();

  public Stream<Sequence> getSequences();

  public int getSequenceCount();

  public Map<String, Object> getProperties();

  @SuppressWarnings("unchecked")
  public default <T> Optional<T> getProperty(String key) {
    return Optional.ofNullable((T) this.getProperties().get(key));
  }

  public default Sequence getSequence(int index) {
    return this.getSequences()
      .skip(index)
    .findFirst()
    .orElseThrow(() -> new IndexOutOfBoundsException("Invalid sequence index"));
  }

  public default Sequence getSequence(String name) {
    return this.getSequences()
      .filter(sequence -> sequence.getName().equals(name))
    .findFirst()
    .orElseThrow(() -> new IndexOutOfBoundsException("No sequence found with name: " + name));
  }
}
