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
package org.sing_group.seda.gui.reverse_complement;

import static org.sing_group.seda.gui.reverse_complement.ReverseComplementTransformationChangeType.COMPLEMENT_CHANGED;
import static org.sing_group.seda.gui.reverse_complement.ReverseComplementTransformationChangeType.HEADER_RENAMED_CHANGED;
import static org.sing_group.seda.gui.reverse_complement.ReverseComplementTransformationChangeType.REVERSE_CHANGED;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.HeaderRenamerTransformation;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.ReverseComplementSequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class ReverseComplementTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private boolean reverse;
  @XmlElement
  private boolean complement;
  @XmlElement
  private AddStringHeaderRenamer headerRenamer;

  public ReverseComplementTransformationProvider() {
    this(true, true);
  }

  public ReverseComplementTransformationProvider(boolean reverse, boolean complement) {
    this.reverse = reverse;
    this.complement = complement;
  }

  @Override
  public Validation validate() {
    List<String> errors = new LinkedList<String>();
    if (this.reverse == false && this.complement == false) {
      errors.add("Reverse and complement cannot be both false.");
    }
    if (this.headerRenamer != null) {
      errors.addAll(this.headerRenamer.validate().getValidationErrors());
    }

    return errors.isEmpty() ? new DefaultValidation() : new DefaultValidation(errors);
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    List<SequencesGroupTransformation> sequenceGroupTransformations = new LinkedList<>();

    sequenceGroupTransformations.add(
      new ComposedSequencesGroupTransformation(
        factory, new ReverseComplementSequenceTransformation(this.reverse, this.complement, factory)
      )
    );

    if (this.headerRenamer != null) {
      sequenceGroupTransformations.add(new HeaderRenamerTransformation(this.headerRenamer, factory));
    }

    return SequencesGroupDatasetTransformation.concat(
      new ComposedSequencesGroupDatasetTransformation(
        factory,
        SequencesGroupTransformation.concat(
          sequenceGroupTransformations.toArray(new SequencesGroupTransformation[sequenceGroupTransformations.size()])
        )
      )
    );
  }

  public void setReverseSequences(boolean reverse) {
    if (this.reverse != reverse) {
      this.reverse = reverse;
      this.fireTransformationsConfigurationModelEvent(REVERSE_CHANGED, this.reverse);
    }
  }

  public boolean isReverseSequences() {
    return this.reverse;
  }

  public void setComplementSequences(boolean complement) {
    if (this.complement != complement) {
      this.complement = complement;
      this.fireTransformationsConfigurationModelEvent(COMPLEMENT_CHANGED, this.complement);
    }
  }

  public boolean isComplementSequences() {
    return this.complement;
  }

  public void setHeaderRenamer(AddStringHeaderRenamer headerRenamer) {
    if (this.headerRenamer == null || !this.headerRenamer.equals(headerRenamer)) {
      this.headerRenamer = headerRenamer;
      this.fireTransformationsConfigurationModelEvent(HEADER_RENAMED_CHANGED, this.headerRenamer);
    }
  }

  public void clearHeaderRenamer() {
    this.headerRenamer = null;
    this.fireTransformationsConfigurationModelEvent(HEADER_RENAMED_CHANGED, this.headerRenamer);
  }

  public AddStringHeaderRenamer getHeaderRenamer() {
    return this.headerRenamer;
  }
}
