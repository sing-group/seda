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

import java.util.HashMap;
import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;

public class UndoAlignmentSequenceTransformation extends ReplaceCharactersSequenceTransformation {
  private static final Map<String, String> REPLACEMENTS = new HashMap<>();
  {
    REPLACEMENTS.put("-", "");
  }

  public UndoAlignmentSequenceTransformation() {
    this(DatatypeFactory.getDefaultDatatypeFactory());
  }

  public UndoAlignmentSequenceTransformation(DatatypeFactory factory) {
    super(factory, REPLACEMENTS);
  }
}
