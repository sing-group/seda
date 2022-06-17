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
import static org.sing_group.seda.gui.concatenate.ConcatenateSequencesTransformationChangeType.MERGE_DESCRIPTIONS_CHANGED;
import static org.sing_group.seda.gui.concatenate.ConcatenateSequencesTransformationChangeType.MERGE_NAME_CHANGED;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.filtering.HeaderMatcher;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.TransformationChangeEvent;
import org.sing_group.seda.plugin.spi.TransformationChangeListener;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ConcatenateSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;

@XmlRootElement
public class ConcatenateSequencesTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private String mergeName;

  @XmlAnyElement(lax = true)
  private HeaderMatcher headerMatcher;

  @XmlElement
  private boolean mergeDescriptions;

  private ReformatFastaTransformationProvider reformatFastaTransformationProvider;

  private TransformationChangeListener reformatFastaTransformationChangeListener = new TransformationChangeListener() {
    @Override
    public void onTransformationChange(TransformationChangeEvent event) {
      fireTransformationsConfigurationModelEvent(event);
    }
  };

  @Override
  public Validation validate() {
    List<String> errorList = new ArrayList<>();
    errorList.addAll(this.reformatFastaTransformationProvider.validate().getValidationErrors());
    if (!this.isValidMergeName()) {
      errorList.add("The merge name is not valid.");
    }
    if (this.getHeaderMatcher() == null) {
      errorList.add("The regular expression header matcher is not defined.");
    }

    if (errorList.isEmpty()) {
      return new DefaultValidation();
    } else {
      return new DefaultValidation(errorList);
    }
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    return SequencesGroupDatasetTransformation.concat(
      new ConcatenateSequencesGroupDatasetTransformation(
        factory, getMergeName(), getHeaderMatcher(), isMergeDescriptions()
      ),
      this.reformatFastaTransformationProvider.getTransformation(factory)
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

  public HeaderMatcher getHeaderMatcher() {
    return this.headerMatcher;
  }

  public void setMergeName(String name) {
    String oldValue = this.mergeName;
    this.mergeName = name;
    this.fireTransformationsConfigurationModelEvent(MERGE_NAME_CHANGED, oldValue, this.mergeName);
  }

  public String getMergeName() {
    return this.mergeName;
  }

  public void setMergeDescriptions(boolean mergeDescriptions) {
    boolean oldValue = this.mergeDescriptions;
    this.mergeDescriptions = mergeDescriptions;
    this.fireTransformationsConfigurationModelEvent(MERGE_DESCRIPTIONS_CHANGED, oldValue, this.mergeDescriptions);
  }

  public boolean isMergeDescriptions() {
    return mergeDescriptions;
  }

  private boolean isValidMergeName() {
    return getMergeName() != null && !getMergeName().isEmpty();
  }

  public ReformatFastaTransformationProvider getReformatFastaTransformationProvider() {
    return reformatFastaTransformationProvider;
  }

  public void setReformatFastaTransformationProvider(
    ReformatFastaTransformationProvider reformatFastaTransformationProvider
  ) {
    this.reformatFastaTransformationProvider = reformatFastaTransformationProvider;
    this.reformatFastaTransformationProvider
      .addTransformationChangeListener(this.reformatFastaTransformationChangeListener);
  }
}
