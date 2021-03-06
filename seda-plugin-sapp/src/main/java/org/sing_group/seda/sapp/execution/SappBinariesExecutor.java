/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.execution;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.sing_group.seda.core.execution.BinaryCheckException;
import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;

@XmlRootElement
@XmlSeeAlso({
  DefaultSappBinariesExecutor.class, DockerSappBinariesExecutor.class
})
public interface SappBinariesExecutor {
  void fasta2hdt(
    File input, File output, String sampleIdentifier, SappCodon sappCodon, SappSpecies sappSpecies,
    String additionalParameters
  ) throws IOException, InterruptedException;

  void augustus(File input, File output, SappCodon sappCodon, SappSpecies sappSpecies)
    throws IOException, InterruptedException;

  void checkBinary() throws BinaryCheckException;
}
