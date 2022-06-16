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
package org.sing_group.seda.datatype.pattern;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.eclipse.persistence.oxm.annotations.XmlVirtualAccessMethods;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.util.StringUtils;

@XmlRootElement
@XmlVirtualAccessMethods
@XmlSeeAlso({
  SequencePatternGroup.class, SequencePattern.class
})
public interface EvaluableSequencePattern {

  public enum GroupMode {
    ANY, ALL;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  };

  public boolean eval(String sequence);

  public Validation validate();
}
