/*
 * #%L
 * SEquence DAtaset builder
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
package org.sing_group.seda.clustalomega.plugin.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationConfigurationPanel;
import org.sing_group.seda.clustalomega.gui.ClustalOmegaAlignmentTransformationProvider;
import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.plugin.core.gui.AbstractSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ClustalOmegaAlignmentSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private ClustalOmegaAlignmentTransformationConfigurationPanel clustalAlignment = 
    new ClustalOmegaAlignmentTransformationConfigurationPanel();

  @Override
  public String getName() {
    return "Clustal Omega Alignment";
  }
  
  @Override
  public String getGroupName() {
    return GROUP_ALIGNMENT;
  }
  
  @Override
  public String getDescription() {
    return "Use Clustal Omega to align the input FASTA files.";
  }

  @Override
  public Component getEditor() {
    return this.clustalAlignment;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.clustalAlignment.getTransformationProvider();
  }

  @Override
  public boolean canSaveTransformation() {
    return true;
  }

  @Override
  public void saveTransformation(File file) throws IOException {
    new JsonObjectWriter<ClustalOmegaAlignmentTransformationProvider>()
      .write(this.clustalAlignment.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.clustalAlignment.setTransformationProvider(
      new JsonObjectReader<ClustalOmegaAlignmentTransformationProvider>()
        .read(file, ClustalOmegaAlignmentTransformationProvider.class)
    );
  }
}
