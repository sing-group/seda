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
import org.sing_group.seda.gui.pattern.PatternFilteringPluginPanel;
import org.sing_group.seda.gui.pattern.PatternFilteringTransformationProvider;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class PatternFilteringSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private final PatternFilteringPluginPanel panel;

  public PatternFilteringSedaGuiPlugin() {
    this.panel = new PatternFilteringPluginPanel();
  }

  @Override
  public String getName() {
    return "Pattern filtering";
  }

  @Override
  public String getGroupName() {
    return GROUP_FILTERING;
  }

  @Override
  public String getDescription() {
    return "Filter sequences based on a text pattern that can be interpreted as either regular expression or plain text.";
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
    new JsonObjectWriter<PatternFilteringTransformationProvider>()
      .write(this.panel.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.panel.setTransformationProvider(
      new JsonObjectReader<PatternFilteringTransformationProvider>()
        .read(file, PatternFilteringTransformationProvider.class)
    );
  }
}
