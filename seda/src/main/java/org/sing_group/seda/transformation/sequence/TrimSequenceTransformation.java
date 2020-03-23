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

import org.sing_group.seda.core.trim.StringTrimmer;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceBuilder;

public class TrimSequenceTransformation implements SequenceTransformation {
  private final SequenceBuilder builder;
  private final int leading;
  private final int trailing;

  public TrimSequenceTransformation(int leading, int trailing) {
    this(DatatypeFactory.getDefaultDatatypeFactory(), leading, trailing);
  }

  public TrimSequenceTransformation(DatatypeFactory factory, int leading, int trailing) {
    this.builder = factory::newSequence;
    this.leading = leading;
    this.trailing = trailing;
  }

  @Override
  public Sequence transform(Sequence sequence) {
    String chain = new StringTrimmer(this.leading, this.trailing).trim(sequence.getChain());

    return this.builder.of(sequence.getName(), sequence.getDescription(), chain, sequence.getProperties());
  }
}
