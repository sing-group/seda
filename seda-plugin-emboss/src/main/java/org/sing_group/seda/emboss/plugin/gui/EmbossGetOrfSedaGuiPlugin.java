/*
 * #%L
 * SEquence DAtaset builder EMBOSS plugin
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
package org.sing_group.seda.emboss.plugin.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationConfigurationPanel;
import org.sing_group.seda.emboss.getorf.gui.GetOrfTransformationProvider;
import org.sing_group.seda.plugin.core.gui.AbstractSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class EmbossGetOrfSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private GetOrfTransformationConfigurationPanel embossConfigurationPanel =
    new GetOrfTransformationConfigurationPanel();

  @Override
  public String getName() {
    return "getorf (EMBOSS)";
  }

  @Override
  public String getGroupName() {
    return GROUP_GENE_ANNOTATION;
  }

  @Override
  public String getDescription() {
    return "Find and extract all open reading frames (ORFs) with length longer than that specified by user, using the getorf program from the EMBOSS suite.";
  }

  @Override
  public Component getEditor() {
    return this.embossConfigurationPanel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.embossConfigurationPanel.getTransformationProvider();
  }

  @Override
  public boolean canSaveTransformation() {
    return true;
  }

  @Override
  public void saveTransformation(File file) throws IOException {
    new JsonObjectWriter<GetOrfTransformationProvider>()
      .write(this.embossConfigurationPanel.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.embossConfigurationPanel.setTransformationProvider(
      new JsonObjectReader<GetOrfTransformationProvider>()
        .read(file, GetOrfTransformationProvider.class)
    );
  }
}