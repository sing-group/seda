/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2018 Jorge Vieira, Miguel Reboiro-Jato and Hugo López-Fernández
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

import static org.sing_group.seda.gui.isoforms.RemoveSIsoformsChangeType.ADD_REMOVED_ISOFORM_NAMES_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveSIsoformsChangeType.HEADER_MATCHER_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveSIsoformsChangeType.ISOFORM_SELECTOR_CHANGED;
import static org.sing_group.seda.gui.isoforms.RemoveSIsoformsChangeType.MINIMUM_ISOFORM_WORD_LENGTH_CHANGED;

import org.sing_group.seda.core.filtering.RegexHeaderMatcher;
import org.sing_group.seda.core.operations.SequenceIsoformSelector;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveIsoformsSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.RemoveIsoformsSequencesGroupTransformation.RemoveIsoformsTransformationConfiguration;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class RemoveIsoformsTransformationProvider extends AbstractTransformationProvider {
	private int minimumWordLengh = 250;
	private boolean addRemovedIsoformNamesToHeaders = false;
	private SequenceIsoformSelector selector;
	private RegexHeaderMatcher regexHeaderMatcher;

	@Override
	public boolean isValidTransformation() {
		return this.selector != null;
	}

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequencesGroupTransformation groupTransformation = null;

    RegexHeaderMatcher headerMatcher = getRegexHeaderMatcher();
    if(headerMatcher == null) {
    	groupTransformation = new RemoveIsoformsSequencesGroupTransformation(
    			factory,
    			new RemoveIsoformsTransformationConfiguration(getMinimumWordLengh(), isAddRemovedIsoformNamesToHeaders()),
    			getSelector()
      );
    } else {
    	groupTransformation = new RemoveIsoformsSequencesGroupTransformation(
    			factory, headerMatcher,
    			new RemoveIsoformsTransformationConfiguration(getMinimumWordLengh(), isAddRemovedIsoformNamesToHeaders()),
    			getSelector()
      );
    }

    return new ComposedSequencesGroupDatasetTransformation(groupTransformation);
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

	public void setIsoformSelector(SequenceIsoformSelector selector) {
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

	public RegexHeaderMatcher getRegexHeaderMatcher() {
		return regexHeaderMatcher;
	}

	public void setAddRemovedIsoformNames(boolean newAddRemovedIsoformNamesToHeaders) {
		if (this.addRemovedIsoformNamesToHeaders != newAddRemovedIsoformNamesToHeaders) {
			this.addRemovedIsoformNamesToHeaders = newAddRemovedIsoformNamesToHeaders;
			fireTransformationsConfigurationModelEvent(ADD_REMOVED_ISOFORM_NAMES_CHANGED,
			    this.addRemovedIsoformNamesToHeaders);
		}
	}

	public boolean isAddRemovedIsoformNamesToHeaders() {
		return addRemovedIsoformNamesToHeaders;
	}
}
