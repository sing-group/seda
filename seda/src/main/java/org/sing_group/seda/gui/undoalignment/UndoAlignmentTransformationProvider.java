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
package org.sing_group.seda.gui.undoalignment;

import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.UndoAlignmentSequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;

@XmlRootElement
public class UndoAlignmentTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaTransformationProvider reformatFastaTransformationProvider;

  private TransformationChangeListener reformatFastaTransformationChangeListener = new TransformationChangeListener() {
    @Override
    public void onTransformationChange(TransformationChangeEvent event) {
      fireTransformationsConfigurationModelEvent(event);
    }
  };

  public UndoAlignmentTransformationProvider() {}

  @Override
  public boolean isValidTransformation() {
    return reformatFastaTransformationProvider.isValidTransformation();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        new ComposedSequencesGroupTransformation(new UndoAlignmentSequenceTransformation(factory))
      ), this.reformatFastaTransformationProvider.getTransformation(factory)
    );
  }

  public ReformatFastaTransformationProvider getReformatFastaTransformationProvider() {
    return reformatFastaTransformationProvider;
  }

  public void setReformatFastaTransformationProvider(
    ReformatFastaTransformationProvider reformatFastaTransformationProvider
  ) {
    this.reformatFastaTransformationProvider = reformatFastaTransformationProvider;
    this.reformatFastaTransformationProvider
      .addTransformationChangeListener(this.reformatFastaTransformationChangeListener);
  }
}