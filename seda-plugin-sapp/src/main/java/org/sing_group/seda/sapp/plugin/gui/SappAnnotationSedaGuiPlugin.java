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
package org.sing_group.seda.sapp.plugin.gui;

import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.GROUP;
import static org.sing_group.seda.sapp.plugin.core.SappAnnotationSedaPluginInfo.NAME;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.plugin.core.gui.AbstractSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;
import org.sing_group.seda.sapp.gui.SappAnnotationTransformationConfigurationPanel;
import org.sing_group.seda.sapp.transformation.provider.SappAnnotationTransformationProvider;

public class SappAnnotationSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private SappAnnotationTransformationConfigurationPanel configurationPanel =
    new SappAnnotationTransformationConfigurationPanel();

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getGroupName() {
    return GROUP;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public Component getEditor() {
    return this.configurationPanel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.configurationPanel.getTransformationProvider();
  }

  @Override
  public boolean canSaveTransformation() {
    return true;
  }

  @Override
  public void saveTransformation(File file) throws IOException {
    new JsonObjectWriter<SappAnnotationTransformationProvider>()
      .write(this.configurationPanel.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.configurationPanel.setTransformationProvider(
      new JsonObjectReader<SappAnnotationTransformationProvider>()
        .read(file, SappAnnotationTransformationProvider.class)
    );
  }
}