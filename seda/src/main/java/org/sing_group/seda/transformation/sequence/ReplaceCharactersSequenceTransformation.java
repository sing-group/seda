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
package org.sing_group.seda.transformation.sequence;

import java.util.Map;
import java.util.Map.Entry;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;

public class ReplaceCharactersSequenceTransformation implements SequenceTransformation {
  private final SequenceBuilder builder;
  private final Map<String, String> replacements;

  public ReplaceCharactersSequenceTransformation(Map<String, String> replacements) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), replacements);
  }

  public ReplaceCharactersSequenceTransformation(DatatypeFactory factory, Map<String, String> replacements) {
    this.builder = factory::newSequence;
    this.replacements = replacements;
  }

  @Override
  public Sequence transform(Sequence sequence) {
    String chain = sequence.getChain();

    for (Entry<String, String> entry : replacements.entrySet()) {
      chain = chain.replace(entry.getKey(), entry.getValue());
    }

    return this.builder.of(sequence.getName(), sequence.getDescription(), chain, sequence.getProperties());
  }
}
