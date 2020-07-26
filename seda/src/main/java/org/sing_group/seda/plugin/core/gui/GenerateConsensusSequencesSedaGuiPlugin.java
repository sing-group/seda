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
package org.sing_group.seda.plugin.core.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.gui.consensus.GenerateConsensusSequenceConfigurationPanel;
import org.sing_group.seda.gui.consensus.GenerateConsensusSequenceTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class GenerateConsensusSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final GenerateConsensusSequenceConfigurationPanel panel;

  public GenerateConsensusSequencesSedaGuiPlugin() {
    this.panel = new GenerateConsensusSequenceConfigurationPanel();
  }

  @Override
  public String getName() {
    return "Consensus sequence";
  }
  
  @Override
  public String getGroupName() {
    return GROUP_ALIGNMENT;
  }
  
  @Override
  public String getDescription() {
    return "Create a consensus sequence using the set of aligned sequences present in the selected FASTA file(s).";
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
    new JsonObjectWriter<GenerateConsensusSequenceTransformationProvider>()
      .write(this.panel.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.panel.setTransformationProvider(
      new JsonObjectReader<GenerateConsensusSequenceTransformationProvider>()
        .read(file, GenerateConsensusSequenceTransformationProvider.class)
    );
  }
}
