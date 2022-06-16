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
package org.sing_group.seda.gui.sort;

import static org.sing_group.seda.gui.sort.SortTransformationChangeType.SEQUENCE_TARGET_CHANGED;
import static org.sing_group.seda.gui.sort.SortTransformationChangeType.SORT_CRITERIA_CHANGED;
import static org.sing_group.seda.gui.sort.SortTransformationChangeType.SORT_ORDER_CHANGED;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.comparator.SequenceComparator;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultTransformationValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupSortTransformation;

@XmlRootElement
public class SortTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private SequenceTarget sequenceTarget;
  @XmlElement
  private SequenceComparator sequenceComparator;
  @XmlElement
  private boolean descendingSort;

  @Override
  public Validation validate() {
    List<String> errorList = new ArrayList<>();
    if (this.sequenceTarget == null) {
      errorList.add("Sequence target is not defined");
    }
    if (this.sequenceComparator == null) {
      errorList.add("Sequence comparator is not defined");
    }

    if (errorList.isEmpty()) {
      return new DefaultTransformationValidation();
    } else {
      return new DefaultTransformationValidation(errorList);
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        factory,
        new SequencesGroupSortTransformation(
          this.sequenceComparator.getComparator(this.sequenceTarget),
          this.descendingSort,
          factory
        )
      )
    );
  }

  public void setSequenceTarget(SequenceTarget sequenceTarget) {
    if (this.sequenceTarget == null || !this.sequenceTarget.equals(sequenceTarget)) {
      this.sequenceTarget = sequenceTarget;
      this.fireTransformationsConfigurationModelEvent(SEQUENCE_TARGET_CHANGED, this.sequenceTarget);
    }
  }

  public SequenceTarget getSequenceTarget() {
    return sequenceTarget;
  }

  public void setSequenceComparator(SequenceComparator sequenceComparator) {
    if (this.sequenceComparator == null || !this.sequenceComparator.equals(sequenceComparator)) {
      this.sequenceComparator = sequenceComparator;
      this.fireTransformationsConfigurationModelEvent(SORT_CRITERIA_CHANGED, this.sequenceComparator);
    }
  }

  public SequenceComparator getSequenceComparator() {
    return sequenceComparator;
  }

  public void setDescendingSort(boolean descendingSort) {
    if (this.descendingSort != descendingSort) {
      this.descendingSort = descendingSort;
      this.fireTransformationsConfigurationModelEvent(SORT_ORDER_CHANGED, this.descendingSort);
    }
  }

  public boolean isDescendingSort() {
    return descendingSort;
  }
}
