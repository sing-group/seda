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
package org.sing_group.seda.gui.concatenate;

import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaConfigurationModel;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ConcatenateSequencesTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaConfigurationModel reformatModel;
  private ConcatenateSequencesConfigurationPanel concatenateSequencesModel;

  public ConcatenateSequencesTransformationProvider(
    ConcatenateSequencesConfigurationPanel concatenateSequencesModel,
    ReformatFastaConfigurationModel reformatModel
  ) {
    this.concatenateSequencesModel = concatenateSequencesModel;
    this.reformatModel = reformatModel;
    this.reformatModel.addTransformationChangeListener(
      new TransformationChangeListener() {

        @Override
        public void onTransformationChange(TransformationChangeEvent event) {
          fireTransformationsConfigurationModelEvent(event.getType(), event.getNewValue());
        }
      }
    );
  }

  @Override
  public boolean isValidTransformation() {
    return reformatModel.isValidTransformation()
      && this.concatenateSequencesModel.isValidConfiguration();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ConcatenateSequencesGroupDatasetTransformation(factory, getMergeName(), getHeaderTarget()),
      this.reformatModel.getTransformation(factory)
    );
  }

  private HeaderTarget getHeaderTarget() {
    return this.concatenateSequencesModel.getHeaderTarget();
  }

  private String getMergeName() {
    return this.concatenateSequencesModel.getMergeName();
  }

  public void headerTargetChanged() {
    fireTransformationsConfigurationModelEvent(
      ConcatenateSequencesTransformationChangeType.HEADER_TARGET_CHANGED,
      concatenateSequencesModel.getHeaderTarget()
    );
  }

  public void nameChanged() {
    fireTransformationsConfigurationModelEvent(
      ConcatenateSequencesTransformationChangeType.MERGE_NAME_CHANGED,
      concatenateSequencesModel.getMergeName()
    );
  }
}
