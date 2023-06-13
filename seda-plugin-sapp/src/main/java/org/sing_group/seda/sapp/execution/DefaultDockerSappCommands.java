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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DefaultDockerSappCommands extends DefaultSappCommands implements DockerSappCommands {
  public static final String DEFAULT_DOCKER_IMAGE = "singgroup/seda-sapp";

  @XmlElement
  private final String dockerImage;

  public DefaultDockerSappCommands() {
    this("java", "/opt/sapp/Conversion.jar", "/opt/sapp/genecaller.jar", DEFAULT_DOCKER_IMAGE);
  }

  public DefaultDockerSappCommands(String conversionJarPath, String geneCallerJarPath, String dockerImage) {
    this("java", conversionJarPath, geneCallerJarPath, dockerImage);
  }

  public DefaultDockerSappCommands(
    String javaExecutablePath, String conversionJarPath, String geneCallerJarPath, String dockerImage
  ) {
    super(javaExecutablePath, conversionJarPath, geneCallerJarPath);

    this.dockerImage = dockerImage;
  }

  @Override
  public String dockerImage() {
    return dockerImage;
  }
}
