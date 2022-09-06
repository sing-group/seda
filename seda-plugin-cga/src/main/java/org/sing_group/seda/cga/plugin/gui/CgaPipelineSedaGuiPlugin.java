/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.plugin.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.sing_group.seda.cga.gui.CgaPipelineTransformationConfigurationPanel;
import org.sing_group.seda.cga.gui.CgaPipelineTransformationProvider;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.plugin.core.Group;
import org.sing_group.seda.plugin.core.gui.AbstractSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class CgaPipelineSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private CgaPipelineTransformationConfigurationPanel panel = new CgaPipelineTransformationConfigurationPanel();

  @Override
  public String getName() {
    return "Conserved Genome Annotation (CGA) Pipeline";
  }

  @Override
  public String getGroupName() {
    return Group.GROUP_GENE_ANNOTATION.getName();
  }

  @Override
  public String getDescription() {
    return "Obtain CDS annotations with CGA, using selected files and a FASTA file with the reference sequence.";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getTransformationProvider();
  }

  @Override
  public boolean canSaveTransformation() {
    return true;
  }

  @Override
  public void saveTransformation(File file) throws IOException {
    new JsonObjectWriter<CgaPipelineTransformationProvider>().write(this.panel.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.panel.setTransformationProvider(
      new JsonObjectReader<CgaPipelineTransformationProvider>().read(file, CgaPipelineTransformationProvider.class)
    );
  }
}
