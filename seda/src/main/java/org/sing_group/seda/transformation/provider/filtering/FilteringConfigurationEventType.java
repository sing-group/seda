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
package org.sing_group.seda.transformation.provider.filtering;

import org.sing_group.seda.plugin.spi.TransformationChangeType;

public enum FilteringConfigurationEventType implements TransformationChangeType {
  STARTING_CODON_ADDED, STARTING_CODON_REMOVED, REMOVE_NON_MULTIPLE_OF_THREE_CHANGED, REMOVE_IF_IN_FRAME_STOP_CODON_CHANGED, REMOVE_BY_SIZE_DIFFERENCE_CHANGED, SIZE_DIFFERENCE_CHANGED, REFERENCE_INDEX_CHANGED, REFERENCE_FILE_CHANGED, MIN_NUM_OF_SEQUENCES_CHANGED, MAX_NUM_OF_SEQUENCES_CHANGED, MIN_SEQUENCE_LENGTH_CHANGED, MAX_SEQUENCE_LENGTH_CHANGED, HEADER_FILTERING_CONFIGURATION_CHANGED;
}
