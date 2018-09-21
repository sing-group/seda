/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.gui.compare;

import static org.sing_group.seda.gui.compare.CompareSequencesGroupDatasetTransformationChangeType.SEQUENCE_TARGET_CHANGED;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationModel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.CompareSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class CompareSequencesGroupDatasetTransformationProvider extends AbstractTransformationProvider {
  public static final SequenceTarget DEFAULT_SEQUENCE_TARGET = SequenceTarget.SEQUENCE;

  private ReformatFastaConfigurationModel reformatModel;
  private SequenceTarget sequenceTarget = DEFAULT_SEQUENCE_TARGET;

  public CompareSequencesGroupDatasetTransformationProvider(ReformatFastaConfigurationModel reformatModel) {
    this.reformatModel = reformatModel;
    this.reformatModel.addTransformationChangeListener(new TransformationChangeListener() {

      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        fireTransformationsConfigurationModelEvent(event.getType(), event.getNewValue());
      }
    });
  }

  @Override
  public boolean isValidTransformation() {
    return reformatModel.isValidTransformation();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new CompareSequencesGroupDatasetTransformation(this.sequenceTarget, factory),
      this.reformatModel.getTransformation(factory)
    );
  }

  public void setSequenceTarget(SequenceTarget newSequenceTarget) {
    if (!this.sequenceTarget.equals(newSequenceTarget)) {
      this.sequenceTarget = newSequenceTarget;
      this.fireTransformationsConfigurationModelEvent(SEQUENCE_TARGET_CHANGED, this.sequenceTarget);
    }
  }
}
