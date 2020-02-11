/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.io;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;

public class JsonObjectReader<T> {

  @SuppressWarnings("unchecked")
  public T read(File input, Class<?> objectClass) throws IOException {
    
    JAXBContext jc;
    try {
      jc = JAXBContext.newInstance(objectClass);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
      
      unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
      
      return (T) unmarshaller.unmarshal(input);
    } catch (JAXBException e) {
      throw new IOException("Error reading the configuration from JSON", e);
    }
  }
}
