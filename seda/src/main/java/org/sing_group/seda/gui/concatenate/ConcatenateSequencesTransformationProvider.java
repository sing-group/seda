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
package org.sing_group.seda.gui.concatenate;

import static org.sing_group.seda.gui.concatenate.ConcatenateSequencesTransformationChangeType.HEADER_MATCHER;
import static org.sing_group.seda.gui.concatenate.ConcatenateSequencesTransformationChangeType.MERGE_NAME_CHANGED;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

public class ConcatenateSequencesTransformationProvider extends AbstractTransformationProvider {
  private ReformatFastaTransformationProvider reformatModel;
  private String mergeName;
  private HeaderMatcher headerMatcher;

  public ConcatenateSequencesTransformationProvider(ReformatFastaTransformationProvider reformatModel) {
    this.reformatModel = reformatModel;
    this.reformatModel.addTransformationChangeListener(new TransformationChangeListener() {

      @Override
      public void onTransformationChange(TransformationChangeEvent event) {
        fireTransformationsConfigurationModelEvent(event.getType(), event.getOldValue(), event.getNewValue());
      }
    });
  }

  @Override
  public boolean isValidTransformation() {
    return reformatModel.isValidTransformation() && this.isValidConfiguration();
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
        new ConcatenateSequencesGroupDatasetTransformation(factory, getMergeName(), getHeaderMatcher()),
        this.reformatModel.getTransformation(factory)
    );
  }

  public void setHeaderMatcher(HeaderMatcher headerMatcher) {
    HeaderMatcher oldValue = this.headerMatcher;
    this.headerMatcher = headerMatcher;
    this.fireTransformationsConfigurationModelEvent(HEADER_MATCHER, oldValue, this.headerMatcher);
  }

  public void removeHeaderMatcher() {
    this.setHeaderMatcher(null);
  }

  private HeaderMatcher getHeaderMatcher() {
    return this.headerMatcher;
  }

  public void setMergeName(String name) {
    String oldValue = this.mergeName;
    this.mergeName = name;
    this.fireTransformationsConfigurationModelEvent(MERGE_NAME_CHANGED, oldValue, this.mergeName);
  }

  private String getMergeName() {
    return this.mergeName;
  }

  public boolean isValidConfiguration() {
    return this.isValidMergeName() && this.getHeaderMatcher() != null;
  }

  private boolean isValidMergeName() {
    return getMergeName() != null && !getMergeName().isEmpty();
  }
}
