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
package org.sing_group.seda.blast.gui;

import org.sing_group.seda.plugin.spi.TransformationChangeType;

public enum BlastTransformationConfigurationChangeType implements TransformationChangeType {
  BLAST_PATH_CHANGED,
  STORE_DATABASES_CHANGED,
  DATABASES_DIRECTORY_CHANGED,
  STORE_ALIAS_CHANGED,
  ALIAS_FILE_CHANGED,
  BLAST_TYPE_CHANGED,
  DATABASE_QUERY_MODE_CHANGED,
  QUERY_FILE_CHANGED,
  E_VALUE_CHANGED,
  MAX_TARGET_SEQS_CHANGED,
  BLAST_ADDITONAL_PARAMETERS_CHANGED,
  EXTRACT_ONLY_HIT_REGIONS_CHANGED,
  HIT_REGIONS_WINDOW_SIZE_CHANGED
}
