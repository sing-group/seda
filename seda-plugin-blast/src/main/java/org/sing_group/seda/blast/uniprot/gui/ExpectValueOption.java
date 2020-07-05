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
package org.sing_group.seda.blast.uniprot.gui;

import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.ExpectationOption;

public class ExpectValueOption {

  private ExpectationOption expectationOption;

  public ExpectValueOption(ExpectationOption expectationOption) {
    this.expectationOption = expectationOption;
  }

  public ExpectationOption getExpectationOption() {
    return expectationOption;
  }

  @Override
  public String toString() {
    return this.expectationOption.getExpectationValue();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((expectationOption == null) ? 0 : expectationOption.hashCode());
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
    ExpectValueOption other = (ExpectValueOption) obj;
    if (expectationOption != other.expectationOption)
      return false;
    return true;
  }
}
