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
package org.sing_group.seda.transformation.sequence;

import static org.sing_group.seda.datatype.DatatypeFactory.getDefaultDatatypeFactory;

import org.sing_group.seda.bio.SequenceUtils;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;
import org.sing_group.seda.transformation.TransformationException;

public class ReverseComplementSequenceTransformation implements SequenceTransformation {

  private final SequenceBuilder builder;
  private final boolean reverse;
  private final boolean complement;

  public ReverseComplementSequenceTransformation(boolean reverse, boolean complement) {
    this(reverse, complement, getDefaultDatatypeFactory());
  }

  public ReverseComplementSequenceTransformation(boolean reverse, boolean complement, DatatypeFactory factory) {
    this.reverse = reverse;
    this.complement = complement;
    this.builder = factory::newSequence;
  }

  @Override
  public Sequence transform(Sequence sequence) throws TransformationException {
    String chain = sequence.getChain();
    if (this.complement) {
      chain = SequenceUtils.complement(chain);
    }
    if (this.reverse) {
      chain = SequenceUtils.reverse(chain);
    }
    return builder.of(sequence.getName(), sequence.getDescription(), chain, sequence.getProperties());
  }
}
