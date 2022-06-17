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
package org.sing_group.seda.gui.isoforms;

import static org.sing_group.seda.gui.isoforms.RemoveIsoformsChangeType.HEADER_MATCHER_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveIsoformsChangeType.ISOFORM_SELECTOR_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveIsoformsChangeType.MINIMUM_ISOFORM_WORD_LENGTH_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveIsoformsChangeType.REMOVED_ISOFORMS_FILES_DIRECTORY_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveIsoformsChangeType.SEQUENCE_HEADERS_JOINER_CHANGED;

import java.io.File;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.operations.DefaultSequenceIsoformSelector;
import org.sing_group.seda.core.operations.SequenceIsoformSelector;
import org.sing_group.seda.core.rename.EmptySequenceHeadersJoiner;
import org.sing_group.seda.core.rename.SequenceHeadersJoiner;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.plugin.spi.DefaultValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveIsoformsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveIsoformsSequencesGroupTransformation.RemoveIsoformsTransformationConfiguration;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

@XmlRootElement
public class RemoveIsoformsTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private int minimumWordLengh = 250;
  @XmlAnyElement(lax = true)
  private SequenceIsoformSelector selector;
  @XmlElement
  private RegexHeaderMatcher regexHeaderMatcher;
  @XmlElement
  private SequenceHeadersJoiner sequenceHeadersJoiner;
  @XmlElement
  private File removedIsoformsFilesDirectory = null;

  public RemoveIsoformsTransformationProvider() {
    this(new EmptySequenceHeadersJoiner());
  }

  public RemoveIsoformsTransformationProvider(SequenceHeadersJoiner sequenceHeadersJoiner) {
    this.sequenceHeadersJoiner = sequenceHeadersJoiner;
  }

  @Override
  public Validation validate() {
    return this.selector != null ? new DefaultValidation()
      : new DefaultValidation("The isoform selector is not defined");
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation groupTransformation = null;

    RegexHeaderMatcher headerMatcher = getRegexHeaderMatcher();
    if (headerMatcher == null) {
      groupTransformation =
        new RemoveIsoformsSequencesGroupTransformation(
          factory, getRemoveIsoformsTransformationConfiguration(), getSelector(), getSequenceHeadersJoiner()
        );
    } else {
      groupTransformation =
        new RemoveIsoformsSequencesGroupTransformation(
          factory, headerMatcher, getRemoveIsoformsTransformationConfiguration(), getSelector(),
          getSequenceHeadersJoiner()
        );
    }

    return new ComposedSequencesGroupDatasetTransformation(groupTransformation);
  }

  private RemoveIsoformsTransformationConfiguration getRemoveIsoformsTransformationConfiguration() {
    if (this.removedIsoformsFilesDirectory == null) {
      return new RemoveIsoformsTransformationConfiguration(getMinimumWordLengh());
    } else {
      return new RemoveIsoformsTransformationConfiguration(getMinimumWordLengh(), removedIsoformsFilesDirectory);
    }
  }

  public int getMinimumWordLengh() {
    return minimumWordLengh;
  }

  public void setMinimumWordLength(int minimumWordLengh) {
    if (this.minimumWordLengh != minimumWordLengh) {
      this.minimumWordLengh = minimumWordLengh;
      fireTransformationsConfigurationModelEvent(MINIMUM_ISOFORM_WORD_LENGTH_CHANGED, this.minimumWordLengh);
    }
  }

  public SequenceIsoformSelector getSelector() {
    return selector;
  }

  public void setIsoformSelector(DefaultSequenceIsoformSelector selector) {
    if (this.selector == null || this.selector != selector) {
      this.selector = selector;
      fireTransformationsConfigurationModelEvent(ISOFORM_SELECTOR_CHANGED, this.selector);
    }
  }

  public void setHeaderMatcher(RegexHeaderMatcher newRegexHeaderMatcher) {
    if (this.regexHeaderMatcher == null || this.regexHeaderMatcher != newRegexHeaderMatcher) {
      this.regexHeaderMatcher = newRegexHeaderMatcher;
      fireTransformationsConfigurationModelEvent(HEADER_MATCHER_CHANGED, this.regexHeaderMatcher);
    }
  }

  public void removeHeaderMatcher() {
    this.regexHeaderMatcher = null;
    fireTransformationsConfigurationModelEvent(HEADER_MATCHER_CHANGED, this.regexHeaderMatcher);
  }

  public RegexHeaderMatcher getRegexHeaderMatcher() {
    return regexHeaderMatcher;
  }

  public void setRemovedIsoformFilesDirectory(File newRemovedIsoformsFilesDirectory) {
    if (
      (this.removedIsoformsFilesDirectory == null) || (this.removedIsoformsFilesDirectory != null
        && !this.removedIsoformsFilesDirectory.equals(newRemovedIsoformsFilesDirectory))
    ) {
      this.removedIsoformsFilesDirectory = newRemovedIsoformsFilesDirectory;
      fireTransformationsConfigurationModelEvent(
        REMOVED_ISOFORMS_FILES_DIRECTORY_CHANGED, this.removedIsoformsFilesDirectory
      );
    }
  }

  public File getRemovedIsoformsFilesDirectory() {
    return this.removedIsoformsFilesDirectory;
  }

  public void clearRemovedIsoformFilesDirectory() {
    this.removedIsoformsFilesDirectory = null;
    fireTransformationsConfigurationModelEvent(
      REMOVED_ISOFORMS_FILES_DIRECTORY_CHANGED, this.removedIsoformsFilesDirectory
    );
  }

  public SequenceHeadersJoiner getSequenceHeadersJoiner() {
    return this.sequenceHeadersJoiner;
  }

  public void setSequenceHeaderJoiner(SequenceHeadersJoiner sequenceHeaderSJoiner) {
    if (this.sequenceHeadersJoiner == null || this.sequenceHeadersJoiner != sequenceHeaderSJoiner) {
      this.sequenceHeadersJoiner = sequenceHeaderSJoiner;
      fireTransformationsConfigurationModelEvent(SEQUENCE_HEADERS_JOINER_CHANGED, this.sequenceHeadersJoiner);
    }
  }
}
