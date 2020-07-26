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
import org.sing_group.seda.gui.pattern.reallocate.ReallocateReferenceSequencesPluginPanel;
import org.sing_group.seda.gui.pattern.reallocate.ReallocateReferenceSequencesTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class ReallocateReferenceSequencesSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final ReallocateReferenceSequencesPluginPanel panel;

  public ReallocateReferenceSequencesSedaGuiPlugin() {
    this.panel = new ReallocateReferenceSequencesPluginPanel();
  }

  @Override
  public String getName() {
    return "Reallocate reference sequences";
  }

  @Override
  public String getGroupName() {
    return GROUP_REFORMATTING;
  }

  @Override
  public String getDescription() {
    return "Find one or more sequences (i.e. the reference sequences) using a pattern filtering option and reallocate them at the beginning of the file.";
  }

  @Override
  public Component getEditor() {
    return this.panel;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.panel.getReallocateSequencesTransformationProvider();
  }

  @Override
  public boolean canSaveTransformation() {
    return true;
  }

  @Override
  public void saveTransformation(File file) throws IOException {
    new JsonObjectWriter<ReallocateReferenceSequencesTransformationProvider>()
      .write(this.panel.getReallocateSequencesTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.panel.setTransformationProvider(
      new JsonObjectReader<ReallocateReferenceSequencesTransformationProvider>()
        .read(file, ReallocateReferenceSequencesTransformationProvider.class)
    );
  }
}
