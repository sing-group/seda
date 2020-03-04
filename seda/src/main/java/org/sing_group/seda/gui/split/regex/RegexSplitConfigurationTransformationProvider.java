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
package org.sing_group.seda.gui.split.regex;

import static org.sing_group.seda.gui.split.regex.RegexSplitConfigurationChangeType.HEADER_MATCHER_CHANGED;
import static org.sing_group.seda.gui.split.regex.RegexSplitConfigurationChangeType.SAVE_GROUP_NAMES_FILES_DIRECTORY;

import java.io.File;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.split.HeaderMatcherSplitter;
import org.sing_group.seda.core.split.SequencesGroupSplitter;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SplitSequencesGroupDatasetTransformation;

@XmlRootElement
public class RegexSplitConfigurationTransformationProvider extends AbstractTransformationProvider {
  @XmlElement
  private RegexHeaderMatcher regexHeaderMatcher;
  @XmlElement
  private File saveGroupNamesDirectory;

  @Override
  public boolean isValidTransformation() {
    return this.regexHeaderMatcher != null;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    RegexHeaderMatcher headerMatcher = getRegexHeaderMatcher();
    SequencesGroupSplitter splitter =
      this.saveGroupNamesDirectory == null ? new HeaderMatcherSplitter(headerMatcher) : new HeaderMatcherSplitter(headerMatcher, this.saveGroupNamesDirectory);

    return new SplitSequencesGroupDatasetTransformation(splitter, factory);
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

  public File getSaveGroupNamesDirectory() {
    return saveGroupNamesDirectory;
  }

  public void setSaveGroupNamesDirectory(File newSaveGroupNamesDirectory) {
    if (saveGroupNamesDirectory == null || !this.saveGroupNamesDirectory.equals(newSaveGroupNamesDirectory)) {
      this.saveGroupNamesDirectory = newSaveGroupNamesDirectory;
      fireTransformationsConfigurationModelEvent(SAVE_GROUP_NAMES_FILES_DIRECTORY, this.saveGroupNamesDirectory);
    }
  }

  public void removeSaveGroupNamesDirectory() {
    this.saveGroupNamesDirectory = null;
    fireTransformationsConfigurationModelEvent(SAVE_GROUP_NAMES_FILES_DIRECTORY, this.saveGroupNamesDirectory);
  }
}
