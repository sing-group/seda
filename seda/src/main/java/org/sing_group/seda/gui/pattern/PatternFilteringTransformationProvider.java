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
package org.sing_group.seda.gui.pattern;

import static org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider.PatternFilteringEventType.PATTERN_CLEARED;
import static org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider.PatternFilteringEventType.PATTERN_EDITED;
import static org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider.PatternFilteringEventType.SEQUENCE_TARGET_CHANGED;
import static org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider.PatternFilteringEventType.TRANSLATION_CONFIGURATION_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.SequenceTarget;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.datatype.pattern.EvaluableSequencePattern;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeType;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.PatternFilteringSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class PatternFilteringTransformationProvider extends AbstractTransformationProvider {
  public enum PatternFilteringEventType implements TransformationChangeType {
    PATTERN_EDITED, PATTERN_CLEARED, TRANSLATION_CONFIGURATION_CHANGED, SEQUENCE_TARGET_CHANGED;
  }

  @XmlElement
  protected SequenceTarget target;
  @XmlElement
  protected SequenceTranslationConfiguration translationConfiguration;
  @XmlElement
  protected EvaluableSequencePattern pattern;

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation patternTransformation;

    if (this.target.isSequence() && isTranslationSelected()) {
      patternTransformation =
        new PatternFilteringSequencesGroupTransformation(this.pattern, this.translationConfiguration, factory);
    } else {
      patternTransformation =
        new PatternFilteringSequencesGroupTransformation(this.pattern, this.target, factory);
    }

    SequencesGroupDatasetTransformation datasetTransformation =
      new ComposedSequencesGroupDatasetTransformation(factory, patternTransformation);

    return datasetTransformation;
  }

  protected boolean isTranslationSelected() {
    return this.translationConfiguration != null;
  }

  @Override
  public boolean isValidTransformation() {
    return this.pattern != null && this.target != null;
  }

  public void setPattern(EvaluableSequencePattern pattern) {
    if (pattern != null && (this.pattern == null || !this.pattern.equals(pattern))) {
      this.pattern = pattern;
      this.fireTransformationsConfigurationModelEvent(PATTERN_EDITED, this.pattern);
    }
  }

  public void clearPattern() {
    this.pattern = null;
    this.fireTransformationsConfigurationModelEvent(PATTERN_CLEARED, this.pattern);
  }

  public void setTranslationConfiguration(SequenceTranslationConfiguration translationConfiguration) {
    if (
      translationConfiguration != null
        && (this.translationConfiguration == null || !this.translationConfiguration.equals(translationConfiguration))
    ) {
      this.translationConfiguration = translationConfiguration;
      this.fireTransformationsConfigurationModelEvent(TRANSLATION_CONFIGURATION_CHANGED, this.translationConfiguration);
    }
  }

  public void clearTranslationConfiguration() {
    this.translationConfiguration = null;
    this.fireTransformationsConfigurationModelEvent(TRANSLATION_CONFIGURATION_CHANGED, this.translationConfiguration);
  }

  public void setTarget(SequenceTarget target) {
    if (this.target == null || !this.target.equals(target)) {
      this.target = target;
      this.fireTransformationsConfigurationModelEvent(SEQUENCE_TARGET_CHANGED, this.target);
    }
  }
}
