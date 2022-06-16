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
package org.sing_group.seda.gui.grow;

import static org.sing_group.seda.gui.grow.GrowSequencesConfigurationChangeType.MINIMUM_OVERLAPPING_CHANGED;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultTransformationValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.GrowSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class GrowSequencesTransformationProvider extends AbstractTransformationProvider {

  @XmlElement
  private int minimumOverlapping = 500;

  public int getMinimumOverlapping() {
    return minimumOverlapping;
  }

  public void setMinimumOverlapping(int minimumOverlapping) {
    if (this.minimumOverlapping != minimumOverlapping) {
      this.minimumOverlapping = minimumOverlapping;
      fireTransformationsConfigurationModelEvent(MINIMUM_OVERLAPPING_CHANGED, this.minimumOverlapping);
    }
  }

  @Override
  public Validation validate() {
    return new DefaultTransformationValidation();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation groupTransformation =
      new GrowSequencesGroupTransformation(factory, this.minimumOverlapping);

    return new ComposedSequencesGroupDatasetTransformation(groupTransformation);
  }
}
