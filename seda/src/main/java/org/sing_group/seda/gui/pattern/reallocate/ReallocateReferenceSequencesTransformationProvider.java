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
package org.sing_group.seda.gui.pattern.reallocate;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.gui.pattern.PatternFilteringConfigurationPanel;
import org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ReallocateReferenceSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class ReallocateReferenceSequencesTransformationProvider extends PatternFilteringTransformationProvider {

  public ReallocateReferenceSequencesTransformationProvider(
    PatternFilteringConfigurationPanel patternFilteringConfigurationPanel
  ) {
    super(patternFilteringConfigurationPanel);
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation transformation;
    EvaluableSequencePattern pattern = getEvaluableSequencePattern();
    if (isTranslationSelected()) {
      SequenceTranslationConfiguration configuration = getSequenceTranslationConfiguration();
      transformation = new ReallocateReferenceSequencesTransformation(pattern, configuration, factory);
    } else {
      transformation = new ReallocateReferenceSequencesTransformation(pattern, getSelectedSequenceTarget(), factory);
    }

    SequencesGroupDatasetTransformation datasetTransformation =
      new ComposedSequencesGroupDatasetTransformation(factory, transformation);

    return datasetTransformation;
  }
}
