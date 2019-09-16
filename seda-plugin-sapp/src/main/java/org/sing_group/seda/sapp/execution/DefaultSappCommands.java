/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.execution;

import java.io.File;

public class DefaultSappCommands implements SappCommands {

  private String javaPath;
  private String conversionJarPath;
  private String geneCallerJarPath;

  public DefaultSappCommands(String conversionJarPath, String geneCallerJarPath) {
    this("java", conversionJarPath, geneCallerJarPath);
  }

  public DefaultSappCommands(String javaPath, String conversionJarPath, String geneCallerJarPath) {
    this.javaPath = javaPath;
    this.conversionJarPath = conversionJarPath;
    this.geneCallerJarPath = geneCallerJarPath;
  }

  @Override
  public String conversion() {
    return this.javaPath + " -jar " + this.conversionJarPath;
  }

  @Override
  public String geneCaller() {
    return this.javaPath + " -jar " + this.geneCallerJarPath;
  }

  @Override
  public String jarsPath() {
    return new File(this.geneCallerJarPath).getParent();
  }
}
