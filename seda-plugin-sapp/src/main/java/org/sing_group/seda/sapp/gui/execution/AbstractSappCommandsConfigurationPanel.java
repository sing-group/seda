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
package org.sing_group.seda.sapp.gui.execution;

import java.util.Optional;

import javax.swing.JPanel;

import org.sing_group.seda.sapp.execution.DefaultSappCommands;
import org.sing_group.seda.sapp.execution.SappCommands;
import org.sing_group.seda.sapp.execution.SappEnvironment;

public abstract class AbstractSappCommandsConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String JAVA_PATH_PROPERTY = "commands.javapath";
  public static final String SAPP_JARS_PATH_PROPERTY = "commands.sappjarspath";

  private SappEnvironment environment;

  public AbstractSappCommandsConfigurationPanel() {
    this(SappEnvironment.getInstance());
  }

  public AbstractSappCommandsConfigurationPanel(SappEnvironment environment) {
    this.environment = environment;
  }

  public SappCommands sappCommands() {
    if (this.selectedJavaPath().isPresent()) {
      return new DefaultSappCommands(this.selectedJavaPath().get() + "/java", conversionJarPath(), geneCallerJarPath());
    } else {
      return new DefaultSappCommands(conversionJarPath(), geneCallerJarPath());
    }
  }

  public String conversionJarPath() {
    return this.selectedSappJarsPath().orElse("") + "/" + this.environment.getConversionJar();
  }

  public String geneCallerJarPath() {
    return this.selectedSappJarsPath().orElse("") + "/" + this.environment.getGeneCallerJar();
  }

  protected abstract Optional<String> selectedJavaPath();

  protected abstract Optional<String> selectedSappJarsPath();
}
