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
package org.sing_group.seda.gui.rename;

import static org.sing_group.seda.core.rename.HeaderRenameTransformationChangeType.CONFIGURATION_CHANGED;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderRenamerTransformation;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class RenameHeaderTransformationProvider extends AbstractTransformationProvider {

  @XmlAnyElement(lax = true)
  private HeaderRenamer headerRenamer;

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation
      .concat(
        new ComposedSequencesGroupDatasetTransformation(factory, getHeaderRenameTransformation())
      );
  }

  private SequencesGroupTransformation getHeaderRenameTransformation() {
    return new HeaderRenamerTransformation(this.headerRenamer);
  }

  @Override
  public boolean isValidTransformation() {
    return this.headerRenamer != null;
  }

  public void setHeaderRenamer(HeaderRenamer headerRenamer) {
    if (this.headerRenamer == null || !this.headerRenamer.equals(headerRenamer)) {
      this.headerRenamer = headerRenamer;
      this.fireTransformationsConfigurationModelEvent(CONFIGURATION_CHANGED, this.headerRenamer);
    }
  }

  public void clearHeaderRenamer() {
    this.headerRenamer = null;
    this.fireTransformationsConfigurationModelEvent(CONFIGURATION_CHANGED, this.headerRenamer);
  }

  public HeaderRenamer getHeaderRenamer() {
    return this.headerRenamer;
  }
}
