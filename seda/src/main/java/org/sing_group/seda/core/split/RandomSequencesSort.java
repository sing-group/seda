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
package org.sing_group.seda.core.split;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.sing_group.seda.datatype.Sequence;

public class RandomSequencesSort implements SequencesSort {

  private Random random;

  public RandomSequencesSort(long randomSeed) {
    this(new Random(randomSeed));
  }

  public RandomSequencesSort(Random random) {
    this.random = random;
  }

  @Override
  public List<Sequence> sort(List<Sequence> sequences) {
    List<Sequence> sorted = new LinkedList<>(sequences);
    Collections.shuffle(sorted, this.random);
    return sorted;
  }
}
