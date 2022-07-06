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
package org.sing_group.seda.transformation.provider.filtering.base;

import static org.sing_group.seda.transformation.provider.filtering.base.FilterByBaseConfigurationEventType.BASES_CHANGED;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.operations.BasePresence;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation;

@XmlRootElement
public class FilterByBasePresenceTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private List<BasePresence> basePresences = new LinkedList<>();

  @Override
  public boolean isValidTransformation() {
    return !this.basePresences.isEmpty();
  }

  @Override
  public Validation validate() {
    if (this.basePresences.isEmpty()) {
      return new DefaultValidation("No base presence selected");
    } else {
      return new DefaultValidation();
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return new ComposedSequencesGroupDatasetTransformation(
      new FilterByBasePresenceTransformation(
        factory, this.basePresences
      )
    );
  }

  public void setBasePresences(List<BasePresence> basePresences) {
    if (basePresences != null && !this.basePresences.equals(basePresences)) {
      this.basePresences = basePresences;
      fireTransformationsConfigurationModelEvent(BASES_CHANGED, this.basePresences);
    }
  }

  public List<BasePresence> getBasePresences() {
    return basePresences;
  }
}
