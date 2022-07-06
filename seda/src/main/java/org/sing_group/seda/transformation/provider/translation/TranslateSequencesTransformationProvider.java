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
package org.sing_group.seda.transformation.provider.translation;

import static org.sing_group.seda.transformation.provider.translation.TranslateSequencesTransformationChangeType.TRANSLATION_CONFIGURATION_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.configuration.SequenceTranslationConfiguration;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.TranslateSequencesGroupDatasetTransformation;

@XmlRootElement
public class TranslateSequencesTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  protected SequenceTranslationConfiguration translationConfiguration;

  @Override
  public Validation validate() {
    if (this.translationConfiguration != null) {
      return new DefaultValidation();
    } else {
      return new DefaultValidation("The translation configuration is not set");
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new TranslateSequencesGroupDatasetTransformation(getTranslationConfiguration(), factory);
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

  public SequenceTranslationConfiguration getTranslationConfiguration() {
    return this.translationConfiguration;
  }
}
