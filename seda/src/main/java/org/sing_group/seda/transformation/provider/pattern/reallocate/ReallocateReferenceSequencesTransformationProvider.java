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
package org.sing_group.seda.transformation.provider.pattern.reallocate;

import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.provider.pattern.PatternFilteringTransformationProvider;
import org.sing_group.seda.transformation.sequencesgroup.ReallocateReferenceSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class ReallocateReferenceSequencesTransformationProvider extends PatternFilteringTransformationProvider {

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation transformation;
    if (isTranslationSelected()) {
      transformation = new ReallocateReferenceSequencesTransformation(pattern, translationConfiguration, factory);
    } else {
      transformation = new ReallocateReferenceSequencesTransformation(pattern, target, factory);
    }

    SequencesGroupDatasetTransformation datasetTransformation =
      new ComposedSequencesGroupDatasetTransformation(factory, transformation);

    return datasetTransformation;
  }
}
