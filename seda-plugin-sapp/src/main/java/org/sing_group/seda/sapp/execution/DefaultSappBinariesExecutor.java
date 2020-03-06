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

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.sapp.datatype.SappCodon;
import org.sing_group.seda.sapp.datatype.SappSpecies;

@XmlRootElement
public class DefaultSappBinariesExecutor extends AbstractSappBinariesExecutor {
  @XmlAnyElement(lax = true)
  private final SappCommands configuration;

  public DefaultSappBinariesExecutor() {
    this.configuration = null;
  }

  public DefaultSappBinariesExecutor(SappCommands configuration) {
    this.configuration = configuration;
  }

  @Override
  public void fasta2hdt(
    File input, File output, String sampleIdentifier, SappCodon sappCodon, SappSpecies sappSpecies,
    String additionalParameters
  ) throws IOException, InterruptedException {
    super.fasta2hdt(
      toList(this.getConversionCommand()),
      input, output, sampleIdentifier, sappCodon, sappSpecies, additionalParameters
    );
  }

  @Override
  public void augustus(File input, File output, SappCodon sappCodon, SappSpecies sappSpecies)
    throws IOException, InterruptedException {
    super.augustus(
      toList(this.getGeneCallerCommand()),
      input, output, sappCodon, sappSpecies
    );
  }

  protected List<String> toList(String command) {
    return asList(command.split(" "));
  }

  @Override
  protected String getConversionCommand() {
    return this.configuration.conversion();
  }

  @Override
  protected String getGeneCallerCommand() {
    return this.configuration.geneCaller();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }

  public SappCommands getSappCommands() {
    return this.configuration;
  }
}
