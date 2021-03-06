/*
 * #%L
 * SEquence DAtaset builder bedtools plugin
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
package org.sing_group.seda.bedtools.execution;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DefaultBedToolsBinariesExecutor extends AbstractBedToolsBinariesExecutor {
  @XmlElement
  private final File bedToolsBinary;

  public DefaultBedToolsBinariesExecutor() {
    this(Paths.get(BedToolsEnvironment.getInstance().getBedToolsCommand()).toFile());
  }

  public DefaultBedToolsBinariesExecutor(File bedToolsExecutable) {
    this.bedToolsBinary = bedToolsExecutable;
  }

  @Override
  public void getFasta(File inputFasta, File bedFile, File output, String additionalParameters)
    throws IOException, InterruptedException {
    super.getFasta(asList(getBedToolsCommand()), inputFasta, bedFile, output, additionalParameters);
  }

  @Override
  protected String getBedToolsCommand() {
    return this.bedToolsBinary.getPath();
  }

  @Override
  protected String toFilePath(File file) {
    return file.getAbsolutePath();
  }

  public File getBedToolsBinary() {
    return bedToolsBinary;
  }
}
