/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.ncbi.parameters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FilterParameter implements NcbiBlastParameter {
  
  @XmlElement
  private boolean useFilter;

  @XmlElement
  private boolean maskAtLookup;

  public FilterParameter() {
    this(false);
  }

  public FilterParameter(boolean useFilter) {
    this(useFilter, false);
  }
  
  public FilterParameter(boolean useFilter, boolean maskAtLookup) {
    this.useFilter = useFilter;
    this.maskAtLookup = maskAtLookup;
  }

  @Override
  public String paramName() {
    return "FILTER";
  }

  @Override
  public String value() {
    if(!this.useFilter) {
      return "F";
    } else {
      return this.maskAtLookup ? "mL" : "T";
    }
  }
  
  public boolean isUseFilter() {
    return useFilter;
  }
  
  public boolean isMaskAtLookup() {
    return maskAtLookup;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (maskAtLookup ? 1231 : 1237);
    result = prime * result + (useFilter ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FilterParameter other = (FilterParameter) obj;
    if (maskAtLookup != other.maskAtLookup)
      return false;
    if (useFilter != other.useFilter)
      return false;
    return true;
  }
}
