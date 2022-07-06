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
package org.sing_group.seda.transformation.provider.redundant;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveRedundantSequencesTransformation.RemoveRedundantSequencesTransformationConfiguration;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class RemoveRedundantSequencesTransformationProvider extends AbstractTransformationProvider {

  @XmlElement
  private RemoveRedundantSequencesTransformationConfiguration configuration;

  @Override
  public Validation validate() {
    if (this.configuration != null) {
      return new DefaultValidation();
    } else {
      return new DefaultValidation("Configuration is null");
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation
      .concat(
        new ComposedSequencesGroupDatasetTransformation(
          factory, getRemoveRedundantSequencesTransformation(factory)
        )
      );
  }

  private SequencesGroupTransformation getRemoveRedundantSequencesTransformation(DatatypeFactory factory) {
    return new RemoveRedundantSequencesTransformation(
      this.configuration,
      factory
    );
  }

  public void setConfiguration(RemoveRedundantSequencesTransformationConfiguration configuration) {
    if (configuration != null && (this.configuration == null || !this.configuration.equals(configuration))) {
      this.configuration = configuration;
      this.fireTransformationsConfigurationModelEvent(
        RemoveRedundantSequencesConfiguratioEventType.CONFIGURATION_CHANGED, this.configuration
      );
    }
  }

  public RemoveRedundantSequencesTransformationConfiguration getConfiguration() {
    return configuration;
  }
}
